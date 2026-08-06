package africa.a1foodhub.userservice.data.repositories;

import africa.a1foodhub.userservice.data.entities.Address;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository <Address, UUID> {

    List<Address> findByUser(User user);
    List<Address> findByUserUserId(UUID userId);

}
