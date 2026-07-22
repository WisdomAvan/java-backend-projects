package africa.datingApp.data.repositories;

import africa.datingApp.data.models.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {

}
