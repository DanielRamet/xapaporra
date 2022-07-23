package com.xapaya.xapaporra.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xapaya.xapaporra.dto.CurrentSeasonDto;
import com.xapaya.xapaporra.dto.TeamDto;
import com.xapaya.xapaporra.model.Match;
import com.xapaya.xapaporra.model.Team;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FootballDataOrgMapper {

    private final ObjectMapper objectMapper;

    public CurrentSeasonDto mapCurrentSeason(String payload, String competitionCode) {
        CurrentSeasonDto dto = null;
        try {
            JsonNode node = objectMapper.readTree(payload);
            JsonNode currentSeason = node.get("currentSeason");
            if (currentSeason != null) {
                dto = new CurrentSeasonDto();
                dto.setId(currentSeason.get("id").asInt());
                dto.setCurrentMatchDay(currentSeason.get("currentMatchday").asInt());
                dto.setStartDate(Date.valueOf(currentSeason.get("startDate").asText()));
                dto.setEndDate(Date.valueOf(currentSeason.get("endDate").asText()));
                dto.setCompetitionCode(competitionCode);
            }
        } catch (Exception e) {
            log.warn("Error processing current season: " + e);
        }

        return dto;
    }

    public List<TeamDto> mapTeams(String payload) {
        List<TeamDto> list = new ArrayList<>();
        try {
            JsonNode node = objectMapper.readTree(payload);
            JsonNode teams = node.get("teams");
            if (teams != null && teams.isArray()) {
                teams.forEach(team ->
                        list.add(TeamDto.builder()
                                        .name(team.get("name").asText())
                                        .shortName(team.get("shortName").asText())
                                        .tla(team.get("tla").asText())
                                .build()));
            }
        } catch (Exception e) {
            log.warn("Error processing teams: " + e);
        }

        return list;
    }

    public List<Match> mapMatches(String payload, String competitionCode, int season) {
        List<Match> list = new ArrayList<>();
        try {
            JsonNode node = objectMapper.readTree(payload);
            JsonNode matches = node.get("matches");
            if (matches != null && matches.isArray()) {
                matches.forEach(match ->
                        list.add(Match.builder()
                                .matchday(match.get("matchday").asInt())
                                .season(season)
                                .competitionCode(competitionCode)
                                .homeTeam(match.get("homeTeam").get("tla").asText())
                                .awayTeam(match.get("awayTeam").get("tla").asText())
                                .scoreHome(this.getScore(match.get("score"), "home"))
                                .scoreAway(this.getScore(match.get("score"), "away"))
                                .build()));
            }
        } catch (Exception e) {
            log.warn("Error processing matches: " + e);
        }
        return list;
    }

    private int getScore(JsonNode score, String side) {
        int res = 0;
        JsonNode node = score.get("fullTime").get(side);
        if(node != null) {
            res = node.asInt();
        }
        return res;
    }
}
