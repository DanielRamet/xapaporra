package com.xapaya.quinielas.quinicheck.service;

import com.xapaya.quinielas.quinicheck.api.dto.MatchDayDto;
import com.xapaya.quinielas.quinicheck.model.Matchday;
import com.xapaya.quinielas.quinicheck.repository.QuinicheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuinicheckService {

    private final QuinicheckRepository quinicheckRepository;

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
}
