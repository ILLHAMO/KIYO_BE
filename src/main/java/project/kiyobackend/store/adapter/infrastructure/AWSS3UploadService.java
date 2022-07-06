package project.kiyobackend.store.adapter.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class AWSS3UploadService {

private final AmazonS3 amazonS3;
private final S3Component s3Component;

public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata,String filename)
{
    amazonS3.putObject(new PutObjectRequest(s3Component.getBucket(), filename,inputStream,objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
}
public String getFileUrl(String filename)
{
    return amazonS3.getUrl(s3Component.getBucket(),filename).toString();
}
}
