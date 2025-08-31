package com.xapaya.quinielas.quinicheck.api;

import com.xapaya.quinielas.quinicheck.service.LoteriasYApuestasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lae")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoteriasApuestasEstadoController {

    private final LoteriasYApuestasService loteriasYApuestasService;

    @GetMapping("/test")
    public ResponseEntity<?> getData() {
        return ResponseEntity.ok(loteriasYApuestasService.getLatestMatchResults());
    }
}
