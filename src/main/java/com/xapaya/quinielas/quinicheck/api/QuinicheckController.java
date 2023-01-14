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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quinicheck")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuinicheckController {

    private final QuinicheckService quinicheckService;

    @GetMapping("/matchday")
    public ResponseEntity<?> getBets(@RequestParam long season, @RequestParam long matchday) {
        return ResponseEntity.ok(quinicheckService.getBets(season, matchday));
    }

    @PutMapping("/matchday")
    public ResponseEntity<?> addBet(@RequestBody MatchDayDto request) {
        String message = quinicheckService.validate(request);
        if(Strings.isEmpty(message)) {
            return ResponseEntity.ok(quinicheckService.setMatchday(request));
        }

        return ResponseEntity.badRequest().body(message);
    }

    @PutMapping("/latest-hints")
    public ResponseEntity<?> updateLatestHints() {
        return ResponseEntity.ok(quinicheckService.updateLatestHints());
    }
}
