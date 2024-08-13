package org.example.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BeansMappers {

    private static final int FIXED_THREAD = 30;

    @Bean
    public AmazonS3 amazonS3(@Value("${amazon.credential.accessKey}") String credentialAccessKye, @Value("${amazon.credential.secretKey}") String credentialSecretKey){
        AWSCredentials awsCredentials = new BasicAWSCredentials(credentialAccessKye, credentialSecretKey);
        AmazonS3 amazonS3 = AmazonS3Client
                .builder()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        return amazonS3;
    }

    @Bean("withFixedExecutor")
    public ExecutorService withFixedExecutor(){
        return Executors.newFixedThreadPool(FIXED_THREAD);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("inner-thread-");
        executor.initialize();
        return executor;
    }
}
