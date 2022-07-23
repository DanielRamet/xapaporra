package com.xapaya.xapaporra.service;

import com.xapaya.xapaporra.api.exception.MessageException;
import com.xapaya.xapaporra.model.CurrentSeason;
import com.xapaya.xapaporra.model.Match;
import com.xapaya.xapaporra.model.Team;
import com.xapaya.xapaporra.repository.CurrentSeasonRepository;
import com.xapaya.xapaporra.repository.MatchRepository;
import com.xapaya.xapaporra.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FootballService {

    private final WebClient webClient;
    private final FootballDataOrgMapper footballDataOrgMapper;
    private final CurrentSeasonRepository currentSeasonRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public void updateSeason(String competitionCode) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + competitionCode).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> footballDataOrgMapper.mapCurrentSeason(s, competitionCode))
                .doOnNext(c -> log.info("Current Season: {}", c))
                .doOnNext(currentSeasonRepository::upsert)
                .subscribe();
    }

    public List<Team> insertTeams(String competitionCode, int season) {
        CurrentSeason c = currentSeasonRepository.findCurrentSeasonByCompetitionCodeAndSeason(competitionCode, season);
        if (c != null) {
            List<Team> prev = teamRepository.findTeamsByCurrentSeasonId(c.getId());
            if (prev != null && prev.size() > 0) {
                throw new MessageException("Already inserted teams for season " + season);
            }

            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/competitions/" + competitionCode + "/teams")
                            .queryParam("season", season)
                            .build())
                    .retrieve().bodyToMono(String.class)
                    .flatMapIterable(footballDataOrgMapper::mapTeams)
                    .map(dto -> Team.from(dto, c.getId()))
                    .collectList()
                    .log()
                    .map(teamRepository::saveAll)
                    .blockOptional().orElseGet(null);
        } else {
            throw new MessageException("Not exists season " + season);
        }
    }

    public void deleteTeams(String competitionCode, int season) {
        CurrentSeason c = currentSeasonRepository.findCurrentSeasonByCompetitionCodeAndSeason(competitionCode, season);
        if (c != null) {
            List<Team> list = teamRepository.findTeamsByCurrentSeasonId(c.getId());
            teamRepository.deleteAll(list);
        }
    }

    public void insertMatches(String competitionCode, int season) {
        if(matchRepository.existsByCompetitionCodeAndSeason(competitionCode, season)) {
            throw new MessageException("Already exists matches for " + competitionCode  + " " + season);
        }

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + competitionCode + "/matches")
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> footballDataOrgMapper.mapMatches(s, competitionCode, season))
                .map(matchRepository::saveAll)
                .doOnSuccess(list -> log.info("saved {} matches", list.size()))
                .doOnError(e -> log.error(e.getMessage()))
                .subscribe();
    }

    public void deleteMatches(String competitionCode, int season) {
        List<Match> matches = matchRepository.findMatchesByCompetitionCodeAndSeason(competitionCode, season);
        if (matches != null && matches.size() > 0) {
            log.info("removing {} matches for {} {} ... ", matches.size(), competitionCode, season);
            matchRepository.deleteAll(matches);
            log.info("removed {} matches for {} {} ... ", matches.size(), competitionCode, season);
        }
    }
}
