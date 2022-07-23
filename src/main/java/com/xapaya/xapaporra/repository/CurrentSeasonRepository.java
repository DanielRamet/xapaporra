package com.xapaya.xapaporra.repository;

import com.xapaya.xapaporra.model.CurrentSeason;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentSeasonRepository
        extends MongoRepository<CurrentSeason, String>, CurrentSeasonRepositoryCustom {
    CurrentSeason findCurrentSeasonByCompetitionCodeAndSeason(String competitionCode, int season);
}
