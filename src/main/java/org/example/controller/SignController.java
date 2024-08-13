package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.config.flows.SessionMapper;
import org.example.domain.User;
import org.example.domain.UserPw;
import org.example.domain.UserSession;
import org.example.object.OriginObject;
import org.example.object.PreSignedURLVo;
import org.example.object.SessionRequest;
import org.example.object.request.*;
import org.example.object.response.FetchProfileDto;
import org.example.object.response.SessionResponse;
import org.example.repository.UserPwRepository;
import org.example.repository.UserRepository;
import org.example.service.SignService;
import org.example.utils.AmazonUtils;
import org.example.utils.AutoKey;
import org.example.utils.EmailSender;
import org.example.utils.Utils;
import org.example.utils.keys.ENV;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@RestController
@RequiredArgsConstructor
public class SignController extends OriginObject {

    private final UserRepository usersRepository;
    private final UserPwRepository userPasswordsRepository;
    private final SignService signService;
    private final AutoKey autoKey;
    private final ExecutorService executorService;
    private final EmailSender emailSender;


    @SessionMapper(checkSession = false)
    @PostMapping("/joinus")
    @Transactional
    public SessionResponse joinUs(SessionRequest request){
        JoinRequest joinEmailRequest = map(request.getParam(), JoinRequest.class);
        joinEmailRequest.checkValidation();

        User checkDup = signService.checkIfUserByEmail(joinEmailRequest.getEmail());
        if(bePresent(checkDup.getName())){
            withException("100-001");
        }

//        String userId = autoKey.makeGetKey("users");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        User users = usersRepository.findByEmail(joinEmailRequest.getEmail());
        users.setName(joinEmailRequest.getName());
        users.setEmail(joinEmailRequest.getEmail());
        users.setGender(joinEmailRequest.getGender().equals("male") ? true : false);
        users.setBirth(LocalDateTime.parse(joinEmailRequest.getBirth(), dtf));
        usersRepository.save(users);

        UserPw userPassword = new UserPw();
        userPassword.setUserNo(users.getUserNo());
        userPassword.setUser(users);
        userPassword.makePassword(joinEmailRequest.getPassword());
        userPasswordsRepository.save(userPassword);
//        sendValidationCode(users, users.getEmail(), users.getName());
        UserSession userSession = signService.setSession(users);
        SessionResponse sessionResponse = signService.setResponseData(users, userSession.getSessionKey());
        return sessionResponse;
    }

//    private void sendValidationCode(User users, String email, String name) {
//        executorService.submit(() -> {
//            ValidationEmailScriptter loginValidationEmailScriptter = new ValidationEmailScriptter(email, "[" + users.getJoinCode() + "] 회원가입 이메일 인증번호가 발송되었습니다.");
//            loginValidationEmailScriptter.sender(name, users.getJoinCode());
//        });
//    }

    private void sendValidationCode(User users, String email) {
//        executorService.submit(() -> {
//            ValidationEmailScriptter loginValidationEmailScriptter = new ValidationEmailScriptter(email, "[" + users.getJoinCode() + "] 회원가입 이메일 인증번호가 발송되었습니다.");
//            loginValidationEmailScriptter.sender(name, users.getJoinCode());
//        });
        emailSender.sendEmail(email, getSubject(users.getJoinCode()), users.getName(), users.getJoinCode());

    }

    private String getSubject(String joinCode){
        return "[인증번호: ${joincode}] 회사 이메일 인증을 진행해 주세요.".replace("${joincode}", joinCode);
    }


    @Transactional
    @SessionMapper(checkSession = false)
    @PutMapping("/request/email-validation")
    public void requestEmailValidationCode(SessionRequest request){
        EmailRequest emailRequest = map(request.getParam(), EmailRequest.class);
        emailRequest.checkValidation();
        User session = usersRepository.findByEmail(emailRequest.getEmail());
        if(!bePresent(session)){
            session = new User();
            session.setEmail(emailRequest.getEmail());
        }else{
            if(bePresent(session.getName()))
                withException("100-001");
        }

        String code = Utils.numberGen(6, 1);
        session.setJoinCode(code);
        session.setJoinCodeAt(LocalDateTime.now());
        usersRepository.save(session);
        sendValidationCode(session, session.getEmail());
    }

    @Transactional
    @SessionMapper(checkSession = false)
    @PutMapping("/confirm/email-validation")
    public void confirmUserEmailByCode(SessionRequest request){
        EmailCodeRequest emailRequest = map(request.getParam(), EmailCodeRequest.class);
        emailRequest.checkValidation();
        User session = usersRepository.findByEmail(emailRequest.getEmail());
        LocalDateTime joinCodeAt = session.getJoinCodeAt();
        String joinCode = session.getJoinCode();
        long between = ChronoUnit.MINUTES.between(joinCodeAt, LocalDateTime.now());
        if (between > 3)
            withException("100-002");
        if(!bePresent(joinCode) || !joinCode.equals(emailRequest.getCode()))
            withException("100-003");
        usersRepository.save(session);
    }

    @SessionMapper
    @GetMapping("/hello-world")
    public SessionResponse helloWorld(SessionRequest request){
        User users = request.getSession();
        SessionResponse sessionResponse = signService.setResponseData(users, request.getSessionKey());
        return sessionResponse;
    }

    @Transactional
    @SessionMapper(checkSession = false)
    @PostMapping("/login")
    public SessionResponse loginWithEmail(SessionRequest request){
        LoginEmailRequest loginEmailRequest = map(request.getParam(), LoginEmailRequest.class);
        UserPw userPasswordByUserEmail = userPasswordsRepository.findByUser_Email(loginEmailRequest.getEmail());
        if(!bePresent(userPasswordByUserEmail))
            withException("");
        userPasswordByUserEmail.loginWithPassword(loginEmailRequest.getPassword());
        User users = userPasswordByUserEmail.getUser();
//        User users = null;
        UserSession userSession = signService.setSession(users);
        SessionResponse sessionResponse = signService.setResponseData(users, userSession.getSessionKey());
        return sessionResponse;
    }

    @SessionMapper
    @GetMapping("/profile")
    public FetchProfileDto getProfile(SessionRequest request){
        FetchProfileDto response = new FetchProfileDto();
        User session = request.getSession();
        response.setName(session.getName());
        response.setEmail(session.getEmail());
        response.setGender(session.getGender() ? "MALE" : "FEMALE");
        response.setBirth(session.getBirth().format(DateTimeFormatter.ofPattern("dd, MM, yyyy")));
        return response;
    }

    @Transactional
    @SessionMapper
    @PutMapping("/profile")
    public void editProfile(SessionRequest request){
        JoinRequest joinEmailRequest = map(request.getParam(), JoinRequest.class);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        User session = request.getSession();
        session.setBirth(LocalDateTime.parse(joinEmailRequest.getBirth(), dtf));
        session.setGender(joinEmailRequest.getGender().equals("male") ? true : false);
        usersRepository.save(session);
    }


    @Transactional
    @SessionMapper
    @PutMapping("/change-profile")
    public void changeProfile(SessionRequest request){
        EditProfileReq editProfileReq = map(request.getParam(), EditProfileReq.class);
        User user = request.getSession();
        user.setProfile(editProfileReq.getProfile());
        usersRepository.save(user);
    }

    @SessionMapper(checkSession = true)
    @PostMapping("/generate-presigned")
    public PreSignedURLVo generatePresignedUrl(SessionRequest request){
        PreSignedURLVo presigned = map(request.getParam(), PreSignedURLVo.class);
        String uuid = UUID.randomUUID().toString();
        PreSignedURLVo presignedURLVo = new PreSignedURLVo();
        presignedURLVo.setBucket(ENV.AWS_S3_QUEUE_BUCKET);
        presignedURLVo.setFileKey(request.getSession().getEmail() + "/" + uuid + presigned.getFilename());
        presignedURLVo.setFilename(presigned.getFilename());
        presignedURLVo.setUrl(AmazonUtils.AWSGeneratePresignedURL(presignedURLVo));
        return presignedURLVo;
    }

}
