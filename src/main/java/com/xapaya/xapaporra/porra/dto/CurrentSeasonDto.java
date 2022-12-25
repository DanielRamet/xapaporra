package com.xapaya.xapaporra.porra.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CurrentSeasonDto {
    private int id;
    private Date startDate;
    private Date endDate;
    private int currentMatchDay;
    private String competitionCode;
}
