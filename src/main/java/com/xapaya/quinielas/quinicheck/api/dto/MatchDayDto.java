package com.xapaya.quinielas.quinicheck.api.dto;

import com.xapaya.quinielas.quinicheck.model.Matchday;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchDayDto {
    private Long season;
    private Long matchday;
    private String player;
    private String bet;
    private Long hits;

    public static MatchDayDto from(Matchday matchday) {
        return MatchDayDto.builder()
                .matchday(matchday.getMatchday())
                .season(matchday.getSeason())
                .hits(matchday.getHits())
                .bet(matchday.getBet())
                .player(matchday.getPlayer())
                .build();
    }
}
