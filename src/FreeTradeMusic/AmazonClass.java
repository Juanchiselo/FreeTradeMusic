package FreeTradeMusic;

import java.io.File;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AmazonClass {

    private static AmazonClass instance = null;
    private AmazonS3 s3Client;
    private String bucketName = "freetrademusic";
    private String key;

    private AmazonClass(){
        s3Client = AmazonS3ClientBuilder.standard().withRegion(String.valueOf(Region.US_West)).build();
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

    public void upload(File file){s3Client.putObject(new PutObjectRequest(bucketName,file.getName(),file));
    }

}
