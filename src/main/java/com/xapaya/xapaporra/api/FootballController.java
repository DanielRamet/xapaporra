package com.xapaya.xapaporra.api;

import com.xapaya.xapaporra.config.AppProperties;
import com.xapaya.xapaporra.service.FootballService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/football")
@RequiredArgsConstructor
@Slf4j
public class FootballController {

    private final FootballService service;
    private final AppProperties properties;

    @PutMapping("/laliga")
    public ResponseEntity<?> updateLaLiga() {
        service.updateSeason(properties.getFootballData().getLeagues().getLaliga());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/laliga/teams/{season}")
    public ResponseEntity<?> insertTeamsLaLiga(@PathVariable int season) {
        return ResponseEntity.ok(service.insertTeams(properties.getFootballData().getLeagues().getLaliga(), season));
    }

    @DeleteMapping("/laliga/teams/{season}")
    public ResponseEntity<?> deleteTeamsLaLiga(@PathVariable int season) {
        service.deleteTeams(properties.getFootballData().getLeagues().getLaliga(), season);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/laliga/matches/{season}")
    public ResponseEntity<?> updateMatches(@PathVariable int season) {
        // TODO:
        //service.insertMatches(properties.getFootballData().getLeagues().getLaliga(), season);
        return ResponseEntity.ok().build();
    }
}
