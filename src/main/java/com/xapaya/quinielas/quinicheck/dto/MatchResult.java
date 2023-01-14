package com.xapaya.quinielas.quinicheck.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResult {
    Long posicion;
    String signo;
}
