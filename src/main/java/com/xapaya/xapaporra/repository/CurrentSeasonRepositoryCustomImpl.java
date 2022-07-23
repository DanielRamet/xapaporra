package com.xapaya.xapaporra.repository;

import com.mongodb.client.result.UpdateResult;
import com.xapaya.xapaporra.dto.CurrentSeasonDto;
import com.xapaya.xapaporra.model.CurrentSeason;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Calendar;

@RequiredArgsConstructor
@Slf4j
public class CurrentSeasonRepositoryCustomImpl implements CurrentSeasonRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void upsert(CurrentSeasonDto dto) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dto.getStartDate());

        Query query = new Query(Criteria.where("competitionCode")
                .is(dto.getCompetitionCode())
                .andOperator(Criteria.where("season")
                        .is(calendar.get(Calendar.YEAR))));
        UpdateResult updateResult = mongoTemplate.upsert(query,
                new Update().set("startDate", dto.getStartDate())
                        .set("endDate", dto.getEndDate())
                        .set("currentMatchday", dto.getCurrentMatchDay()),
                CurrentSeason.class);
        log.info("Upsert current season {}. Update results: {}", dto, updateResult);
    }
}
