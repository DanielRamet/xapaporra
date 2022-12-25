package com.xapaya.quinielas.quinicheck.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("matchdays")
@Builder
@Data
public class Matchday {
    @Id
    private String id;

    private Long season;
    private Long matchday;
    private String player;
    private String bet;
    private Long hits;
}
