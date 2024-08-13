package org.example.utils;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.example.object.PreSignedURLVo;
import org.example.utils.keys.ENV;


import java.net.URL;
import java.util.Date;

public class AmazonUtils {

    public static AWSStaticCredentialsProvider awsCredentialsProvider(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(ENV.AMAZON_CREDENTIAL_ACCESS_KEY, ENV.AMAZON_CREDENTIAL_SECRET_KEY);
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    public static AmazonS3 amazonS3(){
        return AmazonS3Client.builder()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(awsCredentialsProvider())
                .build();
    }

    public static String AWSGeneratePresignedURL(PreSignedURLVo presignedURLVo){
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 5; // 1시간
        expiration.setTime(expTimeMillis);

        AWSStaticCredentialsProvider awsStaticCredentialsProvider = awsCredentialsProvider();

        AmazonS3 amazonS3 = AmazonS3Client
                .builder()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(awsStaticCredentialsProvider)
                .build();

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(presignedURLVo.getBucket(), presignedURLVo.getFileKey())
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toExternalForm();
    }

}
