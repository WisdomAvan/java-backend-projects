package africa.datingApp.utils;

import africa.datingApp.data.models.Seeker;
import africa.datingApp.dtos.responseDtos.SeekerLoginResponseDto;

public class LoginMapper {
    public static void map(SeekerLoginResponseDto response, Seeker seeker) {
        response.setSeekerId(seeker.getSeekerId());
        response.setFirstName(seeker.getFirstName());
        response.setLastName(seeker.getLastName());
        response.setMessage("Login Successful");
    }

}
