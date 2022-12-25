package com.xapaya.xapaporra.porra.api;

import com.xapaya.config.AppProperties;
import com.xapaya.xapaporra.porra.service.FootballService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

//@RestController
//@RequestMapping("/api/football")
@RequiredArgsConstructor
@Slf4j
public class FootballController {

    private final FootballService service;
    private final AppProperties properties;

    //@PutMapping("/laliga")
    public ResponseEntity<?> updateLaLiga() {
        service.updateSeason(properties.getFootballData().getLeagues().getLaliga());
        return ResponseEntity.ok().build();
    }

    //@PostMapping("/laliga/teams/{season}")
    public ResponseEntity<?> insertTeamsLaLiga(@PathVariable int season) {
        return ResponseEntity.ok(service.insertTeams(properties.getFootballData().getLeagues().getLaliga(), season));
    }

    //@DeleteMapping("/laliga/teams/{season}")
    public ResponseEntity<?> deleteTeamsLaLiga(@PathVariable int season) {
        service.deleteTeams(properties.getFootballData().getLeagues().getLaliga(), season);
        return ResponseEntity.ok().build();
    }

    //@PostMapping("/laliga/matches/{season}")
    public ResponseEntity<?> insertMatches(@PathVariable int season) {
        service.insertMatches(properties.getFootballData().getLeagues().getLaliga(), season);
        return ResponseEntity.ok().build();
    }

    //@DeleteMapping("/laliga/matches/{season}")
    public ResponseEntity<?> deleteMatches(@PathVariable int season) {
        service.deleteMatches(properties.getFootballData().getLeagues().getLaliga(), season);
        return ResponseEntity.ok().build();
    }
}
