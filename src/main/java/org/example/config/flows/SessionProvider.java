package org.example.config.flows;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.object.SessionRequest;
import org.example.service.SignService;
import org.example.service.internal.Workspace;
import org.example.utils.keys.SESSION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class SessionProvider extends Workspace implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SessionProvider.class);
    private final SignService signService;

    @Around("@annotation(org.example.config.flows.SessionMapper)")
    public Object flow(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        SessionMapper sessionMapping = getSessionMapping(proceedingJoinPoint);
        SessionRequest sessionRequest = getRequestParams();
        if(sessionMapping.checkSession()){
            signService.checkSession(sessionRequest, sessionMapping.throwError());
        }
        Object proceed = method(proceedingJoinPoint, sessionRequest);
        return returnMap(sessionRequest, proceed);
    }

    private Object method(ProceedingJoinPoint proceedingJoinPoint, SessionRequest sessionRequest) throws Throwable {
        Object[] objects = new Object[1];
        objects[0] = sessionRequest;
        Object proceed = proceedingJoinPoint.proceed(objects);
        return proceed;
    }

    private SessionMapper getSessionMapping(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        SessionMapper sessionMapping = methodSignature.getMethod().getAnnotation(SessionMapper.class);
        return sessionMapping;
    }

    private Object returnMap(SessionRequest sessionRequest, Object o){
        // todo: log
        StopWatch stopWatch = sessionRequest.getStopWatch();
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
//        logger.info(String.valueOf(totalTimeMillis));
        return o == null ? new HashMap() : o;
    }

    private SessionRequest getRequestParams() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes(); // 3
        if (requestAttributes != null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response = attributes.getResponse();
            Map<String, String[]> paramMap = request.getParameterMap();
            Map body = bodyWrapper(request);
            for(String key : paramMap.keySet()){
                body.put(key, paramMap.get(key)[0]);
            }
            String sessionKey = request.getHeader(SESSION.TOKEN_NAME);
            try{
                body.put(SESSION.SESS_AUTH_KEY, sessionKey);
            }catch (Exception e){

            }
            SessionRequest sessionRequest = SessionRequest.makeSessionRequest(request, response, body);
            return sessionRequest;
        }
        return new SessionRequest();

    }

    private Map bodyWrapper(HttpServletRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] rawData;
            InputStream inputStream = request.getInputStream();
            rawData = IOUtils.toByteArray(inputStream);
            String line = null;
            InputStream is = null;
            StringBuffer json = new StringBuffer();
            try {
                is = new ByteArrayInputStream(rawData);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while((line = reader.readLine()) != null) {
                    json.append(line);
                }
            }catch(Exception e) {
//                e.printStackTrace();
            }
            Map<String, String> map = objectMapper.readValue(json.toString(), Map.class);
            return map;
        } catch (IOException e) {
            return new HashMap();
        }
    }


}
