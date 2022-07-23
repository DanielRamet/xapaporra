package com.xapaya.xapaporra.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamDto {
    private String name;
    private String tla;
    private String shortName;
}
