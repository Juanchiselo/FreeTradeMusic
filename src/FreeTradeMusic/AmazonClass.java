package FreeTradeMusic;

import java.io.File;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;

import static com.amazonaws.services.s3.model.Region.US_West_2;

public class AmazonClass {

    private static AmazonClass instance = null;
    private AWSCredentials creds;
    private AmazonS3 s3Client;
    private String bucketName = "freetrademusic";
    private String key;
    private String accessKey = "AKIAJY4ZIQ2AGUBWE45Q";
    private String secretKey= "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";

    private AmazonClass(){
        creds = new BasicAWSCredentials(accessKey,secretKey);
        s3Client = AmazonS3ClientBuilder.standard().
                withCredentials(new AWSStaticCredentialsProvider(creds)).
                withRegion(String.valueOf(Region.US_West)).build();
    }

    public static AmazonClass getInstance(){
        if (instance == null){
            instance = new AmazonClass();
        }
        return instance;
    }

    public void download(String path, String fileName){
        key = fileName;
        s3Client.getObject(new GetObjectRequest(bucketName, key), new File(path));
    }

    public void upload(File file){
        s3Client.putObject(new PutObjectRequest(bucketName,file.getName(),file));
    }

}
