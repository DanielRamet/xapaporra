package com.xapaya.quinielas.quinicheck.service;

import com.xapaya.quinielas.quinicheck.api.dto.BetResultDto;
import com.xapaya.quinielas.quinicheck.api.dto.MatchDayDto;
import com.xapaya.quinielas.quinicheck.dto.FootballDataResults;
import com.xapaya.quinielas.quinicheck.dto.MatchResult;
import com.xapaya.quinielas.quinicheck.model.Matchday;
import com.xapaya.quinielas.quinicheck.repository.QuinicheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuinicheckService {

    private final QuinicheckRepository quinicheckRepository;
    private final LoteriasYApuestasService loteriasYApuestasService;

    public List<MatchDayDto> getBets(long season, long matchday) {
        return quinicheckRepository.findBySeasonAndMatchday(season, matchday)
                .stream().map(MatchDayDto::from)
                .collect(Collectors.toList());
    }

    public List<BetResultDto> getAll(Long season) {
        List<MatchDayDto> dto = quinicheckRepository.findBySeason(season)
                .stream().map(MatchDayDto::from).collect(Collectors.toList());

        SortedMap<Long, List<MatchDayDto>> map = dto.stream().collect(Collectors.groupingBy(MatchDayDto::getMatchday,
                TreeMap::new,
                Collectors.toCollection(ArrayList::new)));

        return map.keySet().stream()
                .map(matchday -> BetResultDto.builder().matchday(matchday).bets(map.get(matchday)).build())
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

    public List<MatchDayDto> updateLatestHints() {
        FootballDataResults latestResults = loteriasYApuestasService.getLatestMatchResults();
        if(latestResults != null) {
            long season = this.getCurrentSeason();
            List<Matchday> matchdays = quinicheckRepository.findBySeasonAndMatchday(season, latestResults.getMatchday());
            if(matchdays != null && !matchdays.isEmpty()) {
                return updateBets(latestResults.getResults(), matchdays).stream()
                        .map(MatchDayDto::from)
                        .collect(Collectors.toList());
            }
        }

        return null;
    }

    private List<Matchday> updateBets(List<MatchResult> results, List<Matchday> players) {
        Map<Long, String> mapResults = results.stream()
                .collect(Collectors.toMap(MatchResult::getPosicion, MatchResult::getSigno));
        for (Matchday player : players) {
            long hints = 0;
            char[] bets = player.getBet().toCharArray();
            for(int i = 0; i < bets.length; i++) {
                if (String.valueOf(bets[i]).equalsIgnoreCase(mapResults.get((long) (i + 1)))) {
                    hints++;
                }
            }
            player.setHits(hints);
        }

        return quinicheckRepository.saveAll(players);
    }

    private long getCurrentSeason() {
        LocalDate now = LocalDate.now();
        long season = now.getYear();
        if (now.getMonth().compareTo(Month.JUNE) > 0) {
            season = now.getYear() + 1;
        }
        return season;
    }
}
