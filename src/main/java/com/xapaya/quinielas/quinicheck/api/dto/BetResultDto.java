package com.xapaya.quinielas.quinicheck.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BetResultDto {
    Long matchday;
    List<MatchDayDto> bets;
}
