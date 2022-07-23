package com.xapaya.xapaporra.model;

import com.xapaya.xapaporra.dto.TeamDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("teams")
@Builder
@Data
public class Team {
    @Id
    private String id;
    @Indexed(unique = true)
    private String tla;
    private String name;
    private String shortName;
    private String currentSeasonId;

    public static Team from(TeamDto dto, String currentSeasonId) {
        return Team.builder()
                .name(dto.getName())
                .shortName(dto.getShortName())
                .tla(dto.getTla())
                .currentSeasonId(currentSeasonId)
                .build();
    }
}
