package com.crio.starter.repository;


import com.crio.starter.data.MemeEntity;
import com.crio.starter.exchange.MemeDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MemeRepository extends MongoRepository<MemeEntity, String> {
  Optional<MemeEntity> findById(String extId);

  List<MemeEntity> findByname(String name);

  MemeEntity save(MemeDto memeDto);

  boolean existsByName(String name);

  boolean existsByUrl(String url);

  boolean existsByCaption(String caption);
}
