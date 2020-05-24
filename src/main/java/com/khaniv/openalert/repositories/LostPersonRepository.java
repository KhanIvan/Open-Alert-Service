package com.khaniv.openalert.repositories;

import com.khaniv.openalert.documents.LostPerson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LostPersonRepository extends MongoRepository<LostPerson, UUID> {
}
