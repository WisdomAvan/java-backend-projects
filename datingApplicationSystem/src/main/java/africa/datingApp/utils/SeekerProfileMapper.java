package africa.datingApp.utils;

import africa.datingApp.data.models.SeekerProfile;
import africa.datingApp.dtos.requestDtos.SeekerProfileRequestDto;

public class SeekerProfileMapper {
    public SeekerProfile SeekerProfileEntry(SeekerProfileRequestDto seekerProfileDto) {
        SeekerProfile profile = new SeekerProfile();
        profile. setFirstName(seekerProfileDto.getFirstName());
        profile.setLastName(seekerProfileDto.getLastName());
        profile.setBio(seekerProfileDto.getBio());
        profile.setGender(seekerProfileDto.getGender());
        profile.setDateOfBirth(seekerProfileDto.getDateOfBirth());
        profile.setHeightInCm(seekerProfileDto.getHeightInCm());
        profile.setLocation(seekerProfileDto.getLocation());
        profile.setSeekerPhotoUrl(seekerProfileDto.getSeekerPhotoUrl());
        return profile;
    }

    public void updateSeekerProfile(SeekerProfileRequestDto seekerProfileRequestDto, SeekerProfile seekerProfile) {
        seekerProfile. setFirstName(seekerProfileRequestDto.getFirstName());
        seekerProfile.setLastName(seekerProfileRequestDto.getLastName());
        seekerProfile.setBio(seekerProfileRequestDto.getBio());
        seekerProfile.setGender(seekerProfileRequestDto.getGender());
        seekerProfile.setDateOfBirth(seekerProfileRequestDto.getDateOfBirth());
        seekerProfile.setHeightInCm(seekerProfileRequestDto.getHeightInCm());
        seekerProfile.setLocation(seekerProfileRequestDto.getLocation());
        seekerProfile.setSeekerPhotoUrl(seekerProfileRequestDto.getSeekerPhotoUrl());

    }

}
