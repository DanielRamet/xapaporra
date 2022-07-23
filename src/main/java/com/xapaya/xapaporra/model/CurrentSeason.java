package com.xapaya.xapaporra.model;

import com.xapaya.xapaporra.dto.CurrentSeasonDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("currentSeason")
@Data
@Builder
public class CurrentSeason {
    @Id
    private String id;
    @Indexed(unique = true, background = true)
    private String competitionCode;
    @Indexed(unique = true, background = true)
    private int season;
    private Date startDate;
    private Date endDate;
    private int currentMatchday;

    public static CurrentSeason from(CurrentSeasonDto dto, String competitionCode) {
        return CurrentSeason.builder()
                .competitionCode(competitionCode)
                .currentMatchday(dto.getCurrentMatchDay())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }
}
