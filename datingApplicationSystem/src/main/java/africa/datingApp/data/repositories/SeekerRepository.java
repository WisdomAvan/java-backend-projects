package africa.datingApp.data.repositories;

import africa.datingApp.data.models.Seeker;
import africa.datingApp.data.models.SeekerProfile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SeekerRepository extends MongoRepository <Seeker, String> {

    boolean existsByEmail(@NotBlank @Email String email);

    Optional<Seeker> findByEmail(String email);

    boolean existsByPhoneNumber(@NotBlank @Pattern(regexp = "^[0-9]{11}$", message = "Phone must be 11 digits") String phoneNumber);
}
