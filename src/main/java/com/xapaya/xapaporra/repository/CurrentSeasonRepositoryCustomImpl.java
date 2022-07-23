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

@RequiredArgsConstructor
@Slf4j
public class CurrentRepositoryCustomImpl implements CurrentSeasonRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void upsert(CurrentSeasonDto dto) {
        Query query = new Query(Criteria.where("competitionCode").is(dto.getCompetitionCode()));
        UpdateResult updateResult = mongoTemplate.upsert(query,
                new Update().addToSet("startDate", dto.getStartDate())
                        .addToSet("endDate", dto.getEndDate())
                        .addToSet("currentMatchday", dto.getCurrentMatchDay()),
                CurrentSeason.class);
        log.info("update results: {}", updateResult);
    }
}
