package africa.a1foodhub.userservice.service.Interfaces;

import africa.a1foodhub.userservice.dtos.requestDto.ProfileRequest;
import africa.a1foodhub.userservice.dtos.responseDto.ProfileResponse;

import java.util.UUID;

public interface  ProfileService {

     ProfileResponse createProfile(UUID userId, ProfileRequest request);

     ProfileResponse updateProfile(UUID userId, ProfileRequest request);

     ProfileResponse getProfile(UUID userId);
}
