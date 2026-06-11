package africa.datingApp.services;


import africa.datingApp.data.enums.FriendRequestStatus;
import africa.datingApp.dtos.requestDtos.AcceptRequestRequestDto;
import africa.datingApp.dtos.responseDtos.AcceptFriendResponseDto;
import africa.datingApp.exceptions.UserNotLoggedInException;
import africa.datingApp.data.repositories.FriendRequestRepository;
import africa.datingApp.data.repositories.SeekerRepository;
import africa.datingApp.dtos.requestDtos.SeekerLoginRequestDto;
import africa.datingApp.dtos.requestDtos.SeekerRegistrationRequestDto;
import africa.datingApp.dtos.requestDtos.SendFriendRequestRequestDto;
import africa.datingApp.dtos.responseDtos.SeekerLoginResponseDto;
import africa.datingApp.dtos.responseDtos.SeekerRegistrationResponseDto;
import africa.datingApp.dtos.responseDtos.SendFriendRequestResponseDto;
import africa.datingApp.exceptions.CannotSendRequestToSelfException;
import africa.datingApp.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FriendRequestServiceImplTest {

    @Autowired
    private FriendRequestService friendsRequestService;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private OnboardingService onboardingService;
    @Autowired
    private SeekerRepository seekerRepository;

    @BeforeEach
    void setUp() {
        friendRequestRepository.deleteAll();
        seekerRepository.deleteAll();
    }

    @Test
    void givenThatARegisterSeekerIsLoggedIn_WhenTheSeekerSendRequest_ItShouldDisplayRequestSuccessful() {

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Comfort");
        seekerRegister.setLastName("Blessed");
        seekerRegister.setPhoneNumber("08016161213");
        seekerRegister.setEmail("chichi@gmail.com");
        seekerRegister.setPassword("12345678");

        SeekerRegistrationResponseDto registrationResponseDto = onboardingService.register(seekerRegister);

        SeekerRegistrationRequestDto friendRegister = new SeekerRegistrationRequestDto();
        friendRegister.setFirstName("Timothy");
        friendRegister.setLastName("Goodness");
        friendRegister.setPhoneNumber("08016161214");
        friendRegister.setEmail("timothy@gmail.com");
        friendRegister.setPassword("12345008");

        SeekerRegistrationResponseDto friendRegistrationResponseDto = onboardingService.register(friendRegister);


        SeekerLoginRequestDto loginRequestDto = new SeekerLoginRequestDto("chichi@gmail.com", "12345678");

        SeekerLoginResponseDto responseDto = onboardingService.login(loginRequestDto);
        assertEquals("Login Successful", responseDto.getMessage());


        SendFriendRequestRequestDto sendFriendRequestRequestDto = new SendFriendRequestRequestDto();
        sendFriendRequestRequestDto.setLoggedIn(true);
        sendFriendRequestRequestDto.setSeekerId(registrationResponseDto.getSeekerId());
        sendFriendRequestRequestDto.setFriendId(friendRegistrationResponseDto.getSeekerId());
        sendFriendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto sendResponse = friendsRequestService.sendFriendRequest(sendFriendRequestRequestDto);
        assertNotNull(sendResponse);
        assertEquals("Request Successful", sendResponse.getMessage());
        assertEquals(registrationResponseDto.getSeekerId(), sendResponse.getSeekerId());
        assertEquals(1, friendRequestRepository.count());
    }

    @Test
    void givenThatASeekerIsRegisterAndLoggedIn_WhenSeekerSendRequestToSelf_ItShouldThrowException() {

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPassword("12345008");

        SeekerRegistrationResponseDto registrationResponseDto = onboardingService.register(seekerRegister);


        SeekerLoginRequestDto loginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "12345008");

        SeekerLoginResponseDto responseDto = onboardingService.login(loginRequestDto);
        assertEquals("Login Successful", responseDto.getMessage());


        SendFriendRequestRequestDto sendFriendRequestRequestDto = new SendFriendRequestRequestDto();
        sendFriendRequestRequestDto.setLoggedIn(true);
        sendFriendRequestRequestDto.setSeekerId(registrationResponseDto.getSeekerId());
        sendFriendRequestRequestDto.setFriendId(registrationResponseDto.getSeekerId());

        sendFriendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        assertThrows(CannotSendRequestToSelfException.class, () -> friendsRequestService.sendFriendRequest(sendFriendRequestRequestDto));

    }

    @Test
    void sendFriendRequest_whenReceiverDoesNotExist_throwsException() {

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPassword("12345008");

        SeekerRegistrationResponseDto registrationResponseDto = onboardingService.register(seekerRegister);


        SeekerLoginRequestDto loginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "12345008");

        SeekerLoginResponseDto responseDto = onboardingService.login(loginRequestDto);
        assertEquals("Login Successful", responseDto.getMessage());


        SendFriendRequestRequestDto sendFriendRequestRequestDto = new SendFriendRequestRequestDto();

        sendFriendRequestRequestDto.setSeekerId(registrationResponseDto.getSeekerId());
        sendFriendRequestRequestDto.setLoggedIn(true);
        sendFriendRequestRequestDto.setFriendId("Does not exit");
        sendFriendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        assertThrows(UserNotFoundException.class, () -> friendsRequestService.sendFriendRequest(sendFriendRequestRequestDto));

    }
    @Test
    void  sendFriendRequest_whenSenderDoesNotExist_throwsException(){

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPassword("12345008");

        SeekerRegistrationResponseDto friendResponseDto = onboardingService.register(seekerRegister);

       SendFriendRequestRequestDto requestDto = new SendFriendRequestRequestDto();

       requestDto.setLoggedIn(true);
       requestDto.setSeekerId("Seeker does not exits");
       requestDto.setFriendId(friendResponseDto.getSeekerId());
       requestDto.setMessage("Hello Chi, Let's connect");

       assertThrows(UserNotFoundException.class, () -> friendsRequestService.sendFriendRequest(requestDto));

    }

    @Test
    void givenRegisteredSeekerWhoIsNotLoggedIn_WhenSendingFriendRequest_ItShouldThrowException(){
        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Comfort");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setEmail("comfort@gmail.com");
        seekerRegister.setPassword("12345008");

        SeekerRegistrationResponseDto seekerResponseDto = onboardingService.register(seekerRegister);

        SeekerRegistrationRequestDto friendRegistrationDto = new SeekerRegistrationRequestDto();

        friendRegistrationDto.setFirstName("Chioma");
        friendRegistrationDto.setLastName("Collins");
        friendRegistrationDto.setPhoneNumber("08023232350");
        friendRegistrationDto.setEmail("chioma@gmail.com");
        friendRegistrationDto.setPassword("12345008");

        SeekerRegistrationResponseDto friendResponseDto = onboardingService.register(friendRegistrationDto);

        SendFriendRequestRequestDto requestDto = new SendFriendRequestRequestDto();

        requestDto.setSeekerId(seekerResponseDto.getSeekerId());
        requestDto.setFriendId(friendResponseDto.getSeekerId());
        requestDto.setMessage("Hello Chi, Let's connect");

        assertThrows(UserNotLoggedInException.class, () -> friendsRequestService.sendFriendRequest(requestDto));

    }

    @Test
    void givenValidFriendRequest_WhenRequestIsProcessed_ResponseShouldNotBeNull() {


        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setPassword("12345008");
        SeekerRegistrationResponseDto seekerResponseDto = onboardingService.register(seekerRegister);


        SeekerRegistrationRequestDto friendRequestDto = new SeekerRegistrationRequestDto();
        friendRequestDto.setFirstName("Chioma");
        friendRequestDto.setLastName("Collins");
        friendRequestDto.setEmail("chioma@gmail.com");
        friendRequestDto.setPhoneNumber("08023232350");
        friendRequestDto.setPassword("12345008");
        SeekerRegistrationResponseDto friendResponseDto = onboardingService.register(friendRequestDto);


        SeekerLoginRequestDto loginRequest = new SeekerLoginRequestDto("timothy@gmail.com", "12345008");
        SeekerLoginResponseDto loginResponse = onboardingService.login(loginRequest);
        assertEquals("Login Successful", loginResponse.getMessage());


        SendFriendRequestRequestDto requestDto = new SendFriendRequestRequestDto();
        requestDto.setLoggedIn(true);
        requestDto.setSeekerId(seekerResponseDto.getSeekerId());
        requestDto.setFriendId(friendResponseDto.getSeekerId());
        requestDto.setMessage("Hello Chi, Let's connect");


        SendFriendRequestResponseDto response = friendsRequestService.sendFriendRequest(requestDto);
        assertNotNull(response);
    }

    @Test
    void givenValidFriendRequest_WhenProcessed_ResponseShouldContainCorrectSeekerAndFriendId(){

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setPassword("12345008");
        SeekerRegistrationResponseDto seekerResponseDto = onboardingService.register(seekerRegister);


        SeekerRegistrationRequestDto friendRequestDto = new SeekerRegistrationRequestDto();
        friendRequestDto.setFirstName("Chioma");
        friendRequestDto.setLastName("Collins");
        friendRequestDto.setEmail("chioma@gmail.com");
        friendRequestDto.setPhoneNumber("08023232350");
        friendRequestDto.setPassword("12345008");
        SeekerRegistrationResponseDto friendResponseDto = onboardingService.register(friendRequestDto);


        SeekerLoginRequestDto loginRequest = new SeekerLoginRequestDto("timothy@gmail.com", "12345008");
        SeekerLoginResponseDto loginResponse = onboardingService.login(loginRequest);
        assertEquals("Login Successful", loginResponse.getMessage());


        SendFriendRequestRequestDto requestDto = new SendFriendRequestRequestDto();
        requestDto.setLoggedIn(true);
        requestDto.setSeekerId(seekerResponseDto.getSeekerId());
        requestDto.setFriendId(friendResponseDto.getSeekerId());
        requestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto responseDto=friendsRequestService.sendFriendRequest(requestDto);

        assertNotNull(responseDto);
        assertThat(responseDto.getSeekerId()).isEqualTo(seekerResponseDto.getSeekerId());
        assertThat(responseDto.getFriendId()).isEqualTo(friendResponseDto.getSeekerId());

    }

    @Test
    void givenValidFriendRequest_WhenProcessed_ResponseShouldContainPendingStatus(){

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setPassword("12345008");
        SeekerRegistrationResponseDto seekerResponseDto = onboardingService.register(seekerRegister);

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com","12345008");
        SeekerLoginResponseDto seekerLoginResponseDto = onboardingService.login(seekerLoginRequestDto);

        SeekerRegistrationRequestDto friendRequestDto = new SeekerRegistrationRequestDto();
        friendRequestDto.setFirstName("Chioma");
        friendRequestDto.setLastName("Collins");
        friendRequestDto.setPhoneNumber("08023232350");
        friendRequestDto.setEmail("chioma@gmail.com");
        friendRequestDto.setPassword("12345008");

        SeekerRegistrationResponseDto friendResponseDto = onboardingService.register(friendRequestDto);

        SendFriendRequestRequestDto requestDto = new SendFriendRequestRequestDto();
        requestDto.setLoggedIn(true);
        requestDto.setSeekerId(seekerResponseDto.getSeekerId());
        requestDto.setFriendId(friendResponseDto.getSeekerId());
        requestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto responseDto = friendsRequestService.sendFriendRequest(requestDto);

        assertThat(responseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);
    }

    @Test
    void givenValidFriendRequest_WhenProcessed_ResponseShouldContainSuccessfulMessage(){

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setPassword("12345008");
        SeekerRegistrationResponseDto seekerResponseDto = onboardingService.register(seekerRegister);

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com","12345008");
        SeekerLoginResponseDto seekerLoginResponseDto = onboardingService.login(seekerLoginRequestDto);

        SeekerRegistrationRequestDto friendRequestDto = new SeekerRegistrationRequestDto();
        friendRequestDto.setFirstName("Chioma");
        friendRequestDto.setLastName("Collins");
        friendRequestDto.setPhoneNumber("08023232350");
        friendRequestDto.setEmail("chioma@gmail.com");
        friendRequestDto.setPassword("12345008");

        SeekerRegistrationResponseDto friendResponseDto = onboardingService.register(friendRequestDto);
        SendFriendRequestRequestDto requestDto = new SendFriendRequestRequestDto();
        requestDto.setLoggedIn(true);
        requestDto.setSeekerId(seekerResponseDto.getSeekerId());
        requestDto.setFriendId(friendResponseDto.getSeekerId());
        requestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto responseDto = friendsRequestService.sendFriendRequest(requestDto);

        assertThat(responseDto.getMessage()).isEqualTo("Request Successful");
    }

    @Test
    void givenPendingFriendRequest_WhenReceiverAcceptsRequest_ItShouldAcceptSuccessfully(){

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPassword("00000000");

        SeekerRegistrationResponseDto seekerResponseDto = onboardingService.register(seekerRegister);

        SeekerRegistrationRequestDto friendRequestDto = new SeekerRegistrationRequestDto();
        friendRequestDto.setFirstName("Chioma");
        friendRequestDto.setLastName("Collins");
        friendRequestDto.setPhoneNumber("08023232350");
        friendRequestDto.setEmail("chioma@gmail.com");
        friendRequestDto.setPassword("12345008");

        SeekerRegistrationResponseDto friendResponseDto = onboardingService.register(friendRequestDto);

        SeekerLoginRequestDto seekerLoginRequestDto =new SeekerLoginRequestDto("timothy@gmail.com","00000000");
        SeekerLoginResponseDto seekerLoginResponseDto = onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus().compareTo(FriendRequestStatus.PENDING));

        SeekerLoginRequestDto friendLogin = new SeekerLoginRequestDto("chioma@gmail.com", "12345008");
        SeekerLoginResponseDto friendResponse = onboardingService.login(friendLogin);

        AcceptRequestRequestDto acceptRequestRequestDto = new AcceptRequestRequestDto();

        acceptRequestRequestDto.setLoggedIn(true);
        acceptRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        acceptRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        acceptRequestRequestDto.setRequestId(friendRequestRequestDto.getRequestId());
        acceptRequestRequestDto.setMessage("Hello Chi, Let's connect");


        AcceptFriendResponseDto responseDto = friendsRequestService.acceptRequest(acceptRequestRequestDto);
        assertThat(responseDto.getStatus().compareTo(FriendRequestStatus.ACCEPTED));

    }

    @Test
    void acceptFriendRequest_whenFriendRequestDoesNotExist_throwsException(){

    }


}




