package com.khaniv.openalert.repositories;

import com.khaniv.openalert.documents.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository extends MongoRepository<Match, UUID> {

    @Query("{'lostPersonId': ?0, 'seenPersonId': ?1}")
    Optional<Match> findByLostPersonIdAndSeenPersonId(UUID lostPersonId, UUID seenPersonId);

    @Query("{'lostPersonId': ?0, 'seenPersonId': ?1}")
    boolean existsByLostPersonIdAndSeenPersonId(UUID lostPersonId, UUID seenPersonId);

    @Query("{'lostPersonId': ?0}")
    List<Match> findByLostPersonId(UUID lostPersonId);

    @Query("{'seenPersonId': ?0}")
    List<Match> findBySeenPersonId(UUID seenPersonId);
}
