package africa.datingApp.data.repositories;

import africa.datingApp.data.models.SeekerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SeekerProfileRepository extends MongoRepository<SeekerProfile, String> {
    Optional<SeekerProfile> findBySeekerId(String seekerId);

}
