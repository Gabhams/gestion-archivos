package sv.edu.udb.gestion_archivos.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sv.edu.udb.gestion_archivos.service.S3Service;

@RestController
@RequestMapping("/api/archivos")
public class S3TestController {

    private final S3Service s3Service;

    public S3TestController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/subir")
    public ResponseEntity<String> subir(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(s3Service.uploadFile(file));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al subir: " + e.getMessage());
        }
    }

    @GetMapping("/descargar/{nombre}")
    public ResponseEntity<byte[]> descargar(@PathVariable String nombre) {
        try {
            byte[] data = s3Service.downloadFile(nombre);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombre + "\"")
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminar/{nombre}")
    public ResponseEntity<String> eliminar(@PathVariable String nombre) {
        String resultado = s3Service.deleteFile(nombre);
        if (resultado.contains("Error")) {
            return ResponseEntity.status(404).body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }
}