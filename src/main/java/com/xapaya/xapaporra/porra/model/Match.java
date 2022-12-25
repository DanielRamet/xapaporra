package com.xapaya.xapaporra.porra.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("matches")
@Builder
@Data
public class Match {
    @Id
    private String id;

    private String competitionCode;
    private int season;

    private String homeTeam;
    private String awayTeam;
    private int matchday;

    private int scoreHome;
    private int scoreAway;

    private boolean finished;
    private boolean started;
}
