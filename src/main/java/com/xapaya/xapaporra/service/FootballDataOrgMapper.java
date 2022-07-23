package com.xapaya.xapaporra.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xapaya.xapaporra.dto.CurrentSeasonDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@AllArgsConstructor
@Slf4j
public class SeasonMapper {

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
}
