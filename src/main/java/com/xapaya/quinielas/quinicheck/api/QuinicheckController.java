package com.xapaya.quinielas.quinicheck.api;

import com.xapaya.quinielas.quinicheck.api.dto.MatchDayDto;
import com.xapaya.quinielas.quinicheck.service.QuinicheckService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@RestController
@RequestMapping("/api/quinicheck")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuinicheckController {

    private final WebClient webClientQuinicheck;
    private final QuinicheckService quinicheckService;

    @GetMapping
    public String proxyData() {
        return Objects.requireNonNull(webClientQuinicheck.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("game_id", "LAQU")
                        .queryParam("fechaInicioInclusiva", "20220814")
                        .queryParam("fechaFinInclusiva", "20991231")
                        .build())
                .retrieve()
                .toEntity(String.class).block()).getBody();
    }

    @PutMapping("/matchday")
    public ResponseEntity<?> addBet(@RequestBody MatchDayDto request) {
        String message = quinicheckService.validate(request);
        if(Strings.isEmpty(message)) {
            return ResponseEntity.ok(quinicheckService.setMatchday(request));
        }

        return ResponseEntity.badRequest().body(message);
    }
}
