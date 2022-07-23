package com.xapaya.xapaporra.dto;

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
