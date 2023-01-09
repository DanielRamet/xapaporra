package com.xapaya.quinielas.quinicheck.service;

import com.xapaya.quinielas.quinicheck.api.dto.MatchDayDto;
import com.xapaya.quinielas.quinicheck.model.Matchday;
import com.xapaya.quinielas.quinicheck.repository.QuinicheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuinicheckService {

    private final QuinicheckRepository quinicheckRepository;
    private final WebClient webClientQuinicheck;

    public List<MatchDayDto> getBets(long season, long matchday) {
        return quinicheckRepository.findBySeasonAndMatchday(season, matchday)
                .stream().map(MatchDayDto::from)
                .collect(Collectors.toList());
    }

    public MatchDayDto setMatchday(MatchDayDto request) {
        Matchday matchday = quinicheckRepository.findBySeasonAndMatchdayAndPlayer(request.getSeason(),
                request.getMatchday(), request.getPlayer());
        matchday = this.parse(matchday, request);
        matchday = quinicheckRepository.save(matchday);
        log.info("Saved: {}", matchday);
        return MatchDayDto.from(matchday);
    }

    public String validate(MatchDayDto request) {
        String message = "Bad request ";
        if(request == null || Strings.isBlank(request.getBet()) || request.getMatchday() == null || Strings.isBlank(request.getPlayer())) {
            log.warn("bad request " + request);
            return message + request;
        }

        return null;
    }

    private Matchday parse(Matchday matchday, MatchDayDto request) {
        Matchday returned = matchday;
        if(returned == null) {
            returned = Matchday.builder().build();
            returned.setMatchday(request.getMatchday());
            returned.setSeason(request.getSeason());
            returned.setPlayer(request.getPlayer());
        }

        returned.setBet(request.getBet());
        returned.setHits(request.getHits() == null ? 0L : request.getHits());
        return returned;
    }

    public List<MatchDayDto> updateLatestHints(long season) {
        LocalDate init = LocalDate.now().minusDays(2);
        LocalDate end = LocalDate.now().plusDays(5);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String initStr = dateTimeFormatter.format(init);
        String endStr = dateTimeFormatter.format(end);
        String data = webClientQuinicheck.get()
                .uri(uriBuilder ->{
                    URI uri = uriBuilder
                        .queryParam("game_id", "LAQU")
                        .queryParam("fechaInicioInclusiva", initStr)
                        .queryParam("fechaFinInclusiva", endStr)
                        .queryParam("celebrados", false)
                        .build();
                    log.info("{}", uri.getQuery());
                    return uri;
                })
                .retrieve()
                .toEntity(String.class)
                .doOnError(t -> log.error("Failing getting data", t))
                .blockOptional()
                .get().getBody();

        if(data != null) {
            final JSONArray arr = new JSONArray(data);
            if(!arr.isEmpty()) {
                final JSONObject obj = arr.getJSONObject(0);
                long matchday = obj.getLong("jornada");
                List<Matchday> matchdays = quinicheckRepository.findBySeasonAndMatchday(season, matchday);
                if(!matchdays.isEmpty()) {
                    final JSONArray matches = obj.getJSONArray("partidos");
                    SortedMap<Long, String> betResults = new TreeMap<>();
                    for (int i = 0; i < matches.length(); i++) {
                        JSONObject match = matches.getJSONObject(i);
                        Long betIndex = match.getLong("posicion");
                        Object result = match.get("signo");
                        betResults.put(betIndex, result != null ? result.toString() : "");
                    }
                    return updateBets(betResults, matchdays).stream()
                            .map(MatchDayDto::from)
                            .collect(Collectors.toList());
                }
            }
        }

        return null;
    }

    private List<Matchday> updateBets(SortedMap<Long, String> betResults, List<Matchday> players) {
        for (Matchday player : players) {
            long hints = 0;
            char[] bets = player.getBet().toCharArray();
            for(int i = 0; i < bets.length; i++) {
                if (String.valueOf(bets[i]).equals(betResults.get((long) (i + 1)))) {
                    hints++;
                }
            }
            player.setHits(hints);
        }

        return quinicheckRepository.saveAll(players);
    }
}
