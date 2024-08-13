package org.example.object;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.example.utils.keys.ENV;

@Getter @Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PreSignedURLVo extends OriginObject {

//    private MediaTypes mediaType;
    private String bucket;
    private String fileKey;
    private String url;
    private String filename;

//    public void checkValidation(){
//        if (!bePresent(this.bucket)) withException("500-005");
//        if (!bePresent(this.fileKey)) withException("500-005");
//        if (!bePresent(this.url)) withException("500-005");
//    }

    public PreSignedURLVo returnOriginBucket(){
        this.bucket = ENV.AWS_S3_ORIGIN_BUCKET;
        this.url = ENV.AWS_S3_ORIGIN_DOMAIN + "/" + fileKey;
        return this;
    }
}
