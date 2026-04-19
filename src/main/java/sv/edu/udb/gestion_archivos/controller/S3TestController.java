package sv.edu.udb.gestion_archivos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;

@RestController
public class S3TestController {

    private final S3Client s3Client;

    public S3TestController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @GetMapping("/test-s3")
    public String testConnection() {
        try {
            s3Client.listBuckets();
            return "Conexión exitosa con S3 🚀";
        } catch (Exception e) {
            return "Error conectando a S3: " + e.getMessage();
        }
    }
}