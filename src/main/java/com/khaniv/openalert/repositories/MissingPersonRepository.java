package com.khaniv.openalert.repositories;

import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MissingPersonRepository extends MongoRepository<MissingPerson, UUID> {
    @Query("{ 'type': ?0, 'active': ?1 }")
    List<MissingPerson> findByTypeAndActive(MissingPersonType type, Boolean active);

    @Query("{ 'id': ?0, 'type': ?1 }")
    Optional<MissingPerson> findByIdAndType(UUID id, MissingPersonType missingPersonType);
}
