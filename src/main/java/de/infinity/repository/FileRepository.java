package de.infinity.repository;

import de.infinity.domain.SoftwareFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<SoftwareFile, String> {

    @Query("{'name' : ?0}")
    SoftwareFile findByName(String name);
}
