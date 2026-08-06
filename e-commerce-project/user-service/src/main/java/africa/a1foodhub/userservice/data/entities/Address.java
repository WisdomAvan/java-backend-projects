package africa.a1foodhub.userservice.data.entities;

import jakarta.persistence.*;
import lombok.*;
import africa.a1foodhub.userservice.data.enums.AddressType;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    private Boolean isDefault;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
