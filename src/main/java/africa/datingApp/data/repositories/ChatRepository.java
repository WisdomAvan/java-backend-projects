package africa.datingApp.data.repositories;

import africa.datingApp.data.models.Chat;
import com.mongodb.MongoCredential;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
}
