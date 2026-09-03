package africa.a1foodhub.userservice.data.repositories;

import africa.a1foodhub.userservice.data.entities.Profile;
import africa.a1foodhub.userservice.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Optional<Profile> findByUser(User User);

}