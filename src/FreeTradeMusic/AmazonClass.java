package FreeTradeMusic;

import java.io.File;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AmazonClass {

    private static AmazonClass instance = null;
    private AWSCredentials creds;
    private AmazonS3Client s3Client;
    private String bucketName = "freetrademusic";
    private String key;
    private AmazonClass(){
        creds = new BasicAWSCredentials("bossman","cs480ftm");
        s3Client = (AmazonS3Client) AmazonS3ClientBuilder.standard().
                withCredentials(new AWSStaticCredentialsProvider(creds)).build();
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

    public void upload(String path, String fileName){
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,path));
    }

}
