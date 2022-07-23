package com.xapaya.xapaporra.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("matches")
@Builder
@Data
public class Match {
    @Id
    private String id;

    @Indexed(unique = true, background = true)
    private String competitionCode;
    @Indexed(unique = true, background = true)
    private int season;

    private String homeTeam;
    private String awayTeam;
    private int matchday;

    private int scoreHome;
    private int scoreAway;
}
