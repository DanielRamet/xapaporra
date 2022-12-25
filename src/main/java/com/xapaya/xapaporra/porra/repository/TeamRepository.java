package com.xapaya.xapaporra.porra.repository;

import com.xapaya.xapaporra.porra.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    List<Team> findTeamsByCurrentSeasonId(String currentSeasonId);
}
