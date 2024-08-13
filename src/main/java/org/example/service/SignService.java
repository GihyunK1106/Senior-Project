package org.example.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.domain.UserSession;
import org.example.object.SessionRequest;
import org.example.object.exceptions.BusinessException;
import org.example.object.response.SessionResponse;
import org.example.repository.UserRepository;
import org.example.repository.UserSessionRepository;
import org.example.service.internal.Workspace;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignService extends Workspace {
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    public User checkIfUserByEmail(String email){
        User byEmail = userRepository.findByEmail(email);
//        if(bePresent(byEmail)) withException("100-001");
        return byEmail;
    }


    public void logout(User session){
        userSessionRepository.deleteAllByUserNo(session.getUserNo());
    }

    public SessionRequest checkSession(SessionRequest sessionRequest, Boolean throwError){
        try {
            String sessAuthKey = sessionRequest.getSessionKey();
            Claims body = parseClaimsFromSessionKey(sessAuthKey);
            String email = body.get("email", String.class);
            UserSession userSession = userSessionRepository.findByUser_Email(email);
            if(bePresent(userSession)){
                User user = userSession.getUser();
                String _sessionKey = userSession.getSessionKey();
                if(!sessAuthKey.equals(_sessionKey) && throwError) throwErrorSession();
                sessionRequest.getParam().put("sess_user_no", user.getUserNo());
                sessionRequest.setSession(user);
            }else if(!bePresent(userSession) && throwError) {
                throwErrorSession();
            }
        }catch (Exception e){
            if(throwError) throwErrorSession();
        }
        return sessionRequest;
    }

    private void throwErrorSession() {
//        withException("000-002", HttpStatus.UNAUTHORIZED);
        throw new BusinessException("unauthorized user", HttpStatus.UNAUTHORIZED);
    }

    public SessionResponse setResponseData(User user, String sessionKey){
        SessionResponse sessionResponseDto = new SessionResponse();
//        sessionResponseDto.setUserId(user.getUserId());
        sessionResponseDto.setUserName(user.getName());
        sessionResponseDto.setEmail(user.getEmail());
//        sessionResponseDto.setUserStatusType(user.getUserStatusType());
        sessionResponseDto.setSessionKey(sessionKey);
        return sessionResponseDto;
    }

    protected Claims parseClaimsFromSessionKey(String sessAuthKey){
        int i = sessAuthKey.lastIndexOf('.');
        String withoutSignature = sessAuthKey.substring(0, i+1);
        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        return untrusted.getBody();
    }

    public UserSession setSession(User session/*, UserSessionTypes userSessionTypes*/){
        userSessionRepository.deleteAllByUserNo(session.getUserNo()/*, userSessionTypes*/);
        UserSession userSession = new UserSession();
        userSession.setUser(session);
        userSession.makeSessionKey();
//        userSession.setSessionTypes(userSessionTypes);
        userSessionRepository.save(userSession);
        return userSession;
    }
}
