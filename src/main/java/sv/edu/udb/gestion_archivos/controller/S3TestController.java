package sv.edu.udb.gestion_archivos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import sv.edu.udb.gestion_archivos.service.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@RestController
@RequestMapping("/api/files")
public class S3TestController {

    private final S3Client s3Client;
    private final S3Service s3Service;

    public S3TestController(S3Client s3Client, S3Service s3Service) {
        this.s3Client = s3Client;
        this.s3Service = s3Service;
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = s3Service.uploadFile(file);
            return ResponseEntity.ok("Archivo subido: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir archivo: " + e.getMessage());
        }
    }


    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        try {
            byte[] fileBytes = s3Service.downloadFile(fileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);

        } catch (NoSuchKeyException e) {
            return ResponseEntity.status(404).body("Error: El archivo no existe en la nube.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

}