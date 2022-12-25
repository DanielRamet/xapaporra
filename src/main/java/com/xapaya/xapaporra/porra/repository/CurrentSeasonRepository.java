package com.xapaya.xapaporra.porra.repository;

import com.xapaya.xapaporra.porra.model.CurrentSeason;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentSeasonRepository
        extends MongoRepository<CurrentSeason, String>, CurrentSeasonRepositoryCustom {
    CurrentSeason findCurrentSeasonByCompetitionCodeAndSeason(String competitionCode, int season);
}
