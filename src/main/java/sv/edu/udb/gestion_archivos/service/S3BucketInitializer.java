package sv.edu.udb.gestion_archivos.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@Service
public class S3BucketInitializer {

    private final S3Client s3Client;

    @Value("${aws.bucket}")
    private String bucketName;

    @Value("${aws.init.bucket:true}")
    private boolean initBucket;

    public S3BucketInitializer(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostConstruct
    public void init() {

        if (!initBucket) return;

        try {
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build());

            System.out.println("Bucket creado: " + bucketName);

        } catch (Exception e) {
            System.out.println("El bucket ya existe o error: " + e.getMessage());
        }
    }
}