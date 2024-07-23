package com.crio.starter.repository;

import com.crio.starter.data.GreetingsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GreetingsRepository extends MongoRepository<GreetingsEntity, String> {
  GreetingsEntity findByExtId(String extId);
}
