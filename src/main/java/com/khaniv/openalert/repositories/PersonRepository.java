package com.khaniv.openalert.repositories;

import com.khaniv.openalert.documents.Person;
import com.khaniv.openalert.enums.PersonType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersonRepository extends MongoRepository<Person, UUID> {
    @Query("{ 'type': ?0, 'active': ?1}")
    List<Person> findByTypeAndActive(PersonType type, Boolean active);
}
