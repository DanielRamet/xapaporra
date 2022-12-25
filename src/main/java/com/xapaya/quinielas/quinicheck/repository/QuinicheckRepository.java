package com.xapaya.quinielas.quinicheck.repository;

import com.xapaya.quinielas.quinicheck.model.Matchday;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuinicheckRepository extends MongoRepository<Matchday, String> {

    Matchday findBySeasonAndMatchdayAndPlayer(Long season, Long matchday, String name);
}
