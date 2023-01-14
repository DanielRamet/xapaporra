package com.xapaya.quinielas.quinicheck.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FootballDataResults {
    Long matchday;
    List<MatchResult> results;
}
