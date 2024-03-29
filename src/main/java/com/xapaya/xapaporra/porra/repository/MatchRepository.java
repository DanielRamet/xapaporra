package com.xapaya.xapaporra.porra.repository;

import com.xapaya.xapaporra.porra.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends MongoRepository<Match, String> {
    boolean existsByCompetitionCodeAndSeason(String competitionCode, int season);
    List<Match> findMatchesByCompetitionCodeAndSeason(String competitionCode, int season);
}
