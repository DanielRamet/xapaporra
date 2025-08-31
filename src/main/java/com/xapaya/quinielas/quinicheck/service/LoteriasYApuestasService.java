package com.xapaya.quinielas.quinicheck.service;

import com.xapaya.quinielas.quinicheck.dto.FootballDataResults;
import com.xapaya.quinielas.quinicheck.dto.MatchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoteriasYApuestasService {

    private final String FIELD_JORNADA = "jornada";
    private final String FIELD_POSICION = "posicion";
    private final String FIELD_SIGNO = "signo";
    private final String FIELD_PARTIDOS = "partidos";

    private final WebClient webClientQuinicheck;

    public FootballDataResults getLatestMatchResults() {
        String data = this.getCurrentResults();
        log.info("DATA: {}", data);
        return this.parseData(data);
    }

    public Set<MatchResult> getResultsBySeasonAndMatchday(long season, long matchday) {
        return null;
    }

    private String getData(String initStr, String endStr, boolean celebrados) {
        return webClientQuinicheck.get()
                .uri(uriBuilder -> {
                    URI uri = uriBuilder
                            .queryParam("game_id", "LAQU")
                            .queryParam("fechaInicioInclusiva", initStr)
                            .queryParam("fechaFinInclusiva", endStr)
                            .queryParam("celebrados", celebrados)
                            .build();
                    log.info("{}", uri.getQuery());
                    return uri;
                })
                .retrieve()
                .toEntity(String.class)
                .doOnError(t -> log.error("Failing getting data", t))
                .blockOptional()
                .get().getBody();
    }

    private String getCurrentResults() {
        LocalDate init = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now().plusDays(7);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String initStr = dateTimeFormatter.format(init);
        String endStr = dateTimeFormatter.format(end);

        return this.getData(initStr, endStr, true);
    }

    private FootballDataResults parseData(String data) {
        if(data != null) {
            final JSONArray arr = new JSONArray(data);
            if(!arr.isEmpty()) {
                // maximum jornada
                JSONObject maxJornada = null;
                for (int index = 0; index < arr.length(); index++) {
                    if (maxJornada == null ||
                            maxJornada.getLong(FIELD_JORNADA) < arr.getJSONObject(index).getLong(FIELD_JORNADA)) {
                        maxJornada = arr.getJSONObject(index);
                    }
                }

                // Mapping partidos
                assert maxJornada != null;
                final JSONArray matches = maxJornada.getJSONArray(FIELD_PARTIDOS);
                List<MatchResult> results = new ArrayList<>();
                for (int i = 0; i < matches.length(); i++) {
                    JSONObject match = matches.getJSONObject(i);
                    Object signalResult = match.get(FIELD_SIGNO);
                    results.add(MatchResult.builder()
                            .posicion(match.getLong(FIELD_POSICION))
                            .signo(signalResult != null ? signalResult.toString().trim() : "")
                            .build());
                }

                return FootballDataResults.builder()
                        .matchday(maxJornada.getLong(FIELD_JORNADA))
                        .results(results)
                        .build();
            } else {
                log.warn("Empty Data arr");
            }
        } else {
            log.warn("Empty Data");
        }

        return null;
    }
}
