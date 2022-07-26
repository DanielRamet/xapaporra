package com.xapaya.xapaporra.repository;

import com.xapaya.xapaporra.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    List<Team> findTeamsByCurrentSeasonId(String currentSeasonId);
}
