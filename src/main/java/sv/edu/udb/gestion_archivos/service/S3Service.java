package sv.edu.udb.gestion_archivos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.core.sync.ResponseTransformer;


@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public String uploadFile(MultipartFile file) throws Exception {

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(
                request,
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        return fileName;
    }


    public byte[] downloadFile(String fileName) {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.getObject(request, ResponseTransformer.toBytes()).asByteArray();
    }

}