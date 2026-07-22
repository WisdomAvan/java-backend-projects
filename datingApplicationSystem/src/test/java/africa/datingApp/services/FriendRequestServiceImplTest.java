package africa.datingApp.services;


import africa.datingApp.data.enums.FriendRequestStatus;

import africa.datingApp.dtos.responseDtos.*;
import africa.datingApp.data.models.Seeker;
import africa.datingApp.dtos.requestDtos.*;
import africa.datingApp.dtos.responseDtos.*;
import africa.datingApp.exceptions.*;
import africa.datingApp.data.repositories.FriendRequestRepository;
import africa.datingApp.data.repositories.SeekerRepository;
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
    void sendFriendRequest_whenSenderDoesNotExist_throwsException() {

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
    void givenRegisteredSeekerWhoIsNotLoggedIn_WhenSendingFriendRequest_ItShouldThrowException() {
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
    void givenValidFriendRequest_WhenProcessed_ResponseShouldContainCorrectSeekerAndFriendId() {

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

        SendFriendRequestResponseDto responseDto = friendsRequestService.sendFriendRequest(requestDto);

        assertNotNull(responseDto);
        assertThat(responseDto.getSeekerId()).isEqualTo(seekerResponseDto.getSeekerId());
        assertThat(responseDto.getFriendId()).isEqualTo(friendResponseDto.getSeekerId());

    }

    @Test
    void givenValidFriendRequest_WhenProcessed_ResponseShouldContainPendingStatus() {

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setPassword("12345008");
        SeekerRegistrationResponseDto seekerResponseDto = onboardingService.register(seekerRegister);

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "12345008");
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
    void givenValidFriendRequest_WhenProcessed_ResponseShouldContainSuccessfulMessage() {

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setPassword("12345008");
        SeekerRegistrationResponseDto seekerResponseDto = onboardingService.register(seekerRegister);

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "12345008");
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
    void givenPendingFriendRequest_WhenReceiverAcceptsRequest_ItShouldAcceptSuccessfully() {

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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus().compareTo(FriendRequestStatus.PENDING));


        AcceptRequestRequestDto acceptRequestRequestDto = new AcceptRequestRequestDto();

        acceptRequestRequestDto.setFriendId(friendRequestResponseDto.getFriendId());
        acceptRequestRequestDto.setRequestId(friendRequestResponseDto.getRequestId());
        acceptRequestRequestDto.setMessage("Accepted");


        AcceptFriendResponseDto responseDto = friendsRequestService.acceptRequest(acceptRequestRequestDto);
        assertThat(responseDto.getStatus()).isEqualTo(FriendRequestStatus.ACCEPTED);

    }

    @Test
    void acceptFriendRequest_whenFriendRequestDoesNotExist_throwsException() {

        SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
        seekerRegister.setFirstName("Timothy");
        seekerRegister.setLastName("Goodness");
        seekerRegister.setPhoneNumber("08016161214");
        seekerRegister.setEmail("timothy@gmail.com");
        seekerRegister.setPassword("00000000");

        SeekerRegistrationResponseDto seekerResponseDto = onboardingService.register(seekerRegister);

        SeekerRegistrationRequestDto friendRegister = new SeekerRegistrationRequestDto();
        friendRegister.setFirstName("Chioma");
        friendRegister.setLastName("Collins");
        friendRegister.setPhoneNumber("08023232350");
        friendRegister.setEmail("chioma@gmail.com");
        friendRegister.setPassword("11111111");

        SeekerRegistrationResponseDto friendResponseDto = onboardingService.register(friendRegister);


        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        SeekerLoginResponseDto seekerLoginResponseDto = onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        friendsRequestService.sendFriendRequest(friendRequestRequestDto);

        AcceptRequestRequestDto acceptRequestRequestDto = new AcceptRequestRequestDto();
        acceptRequestRequestDto.setRequestId("Does not exit requestId");
//        acceptRequestRequestDto.setFriendId(friendResponseDto.getFriendId());

        assertThrows(FriendRequestNotFoundException.class, () -> friendsRequestService.acceptRequest(acceptRequestRequestDto));

    }

    @Test
    void givenPendingFriendRequest_WhenAccepted_FriendshipShouldBeEstablished() {

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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);


        AcceptRequestRequestDto acceptRequestRequestDto = new AcceptRequestRequestDto();

        acceptRequestRequestDto.setFriendId(friendRequestResponseDto.getFriendId());
        acceptRequestRequestDto.setRequestId(friendRequestResponseDto.getRequestId());
        acceptRequestRequestDto.setMessage("Accepted");

        friendsRequestService.acceptRequest(acceptRequestRequestDto);

        Seeker seeker = seekerRepository.findById(seekerResponseDto.getSeekerId())
                .orElseThrow();

        Seeker friend = seekerRepository.findById(friendResponseDto.getSeekerId())
                .orElseThrow();

        assertThat(seeker.getFriends().contains(friendResponseDto.getSeekerId()))
                .isTrue();

        assertThat(friend.getFriends().contains(seekerResponseDto.getSeekerId()))
                .isTrue();
    }

    @Test
    void givenPendingFriendRequest_WhenReceiverDeclinesRequest_ItShouldDeclineSuccessfully() {

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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);


        DeclineFriendRequestDto declineFriendRequestDto = new DeclineFriendRequestDto();

        declineFriendRequestDto.setFriendId(friendRequestResponseDto.getFriendId());
        declineFriendRequestDto.setRequestId(friendRequestResponseDto.getRequestId());

        DeclineFriendResponseDto declineResponse = friendsRequestService.declineRequest(declineFriendRequestDto);

        assertNotNull(declineResponse);
        assertThat(declineResponse.getStatus()).isEqualTo(FriendRequestStatus.DECLINED);
    }

    @Test
    void declineFriendRequest_whenRequestDoesNotExist_throwsException(){

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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);


        DeclineFriendRequestDto declineFriendRequestDto = new DeclineFriendRequestDto();

            declineFriendRequestDto.setRequestId("Request doesn't exist");
            declineFriendRequestDto.setFriendId(friendResponseDto.getSeekerId());

            assertThrows(FriendRequestNotFoundException.class,() -> friendsRequestService.declineRequest(declineFriendRequestDto));
    }

    @Test
    void declineFriendRequest_whenReceiverDoesNotExist_throwsException(){
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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);


        DeclineFriendRequestDto declineFriendRequestDto = new DeclineFriendRequestDto();

        declineFriendRequestDto.setRequestId(friendRequestResponseDto.getRequestId());
        declineFriendRequestDto.setFriendId("Friend doesn't exist");

        assertThrows(UserNotFoundException.class,() -> friendsRequestService.declineRequest(declineFriendRequestDto));
    }

    @Test
    void declineFriendRequest_whenRequestAlreadyDeclined_throwsException(){

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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);


        DeclineFriendRequestDto declineFriendRequestDto = new DeclineFriendRequestDto();

        declineFriendRequestDto.setFriendId(friendRequestResponseDto.getFriendId());
        declineFriendRequestDto.setRequestId(friendRequestResponseDto.getRequestId());

        DeclineFriendResponseDto declineResponse = friendsRequestService.declineRequest(declineFriendRequestDto);

        assertThat(declineResponse.getStatus()).isEqualTo(FriendRequestStatus.DECLINED);


        DeclineFriendRequestDto secondAttemptDeclineFriend = new DeclineFriendRequestDto();

        secondAttemptDeclineFriend.setFriendId(friendRequestResponseDto.getFriendId());
        secondAttemptDeclineFriend.setRequestId(friendRequestResponseDto.getRequestId());

        assertThrows(FriendRequestAlreadyDeclinedException.class,()->friendsRequestService.declineRequest(secondAttemptDeclineFriend));


    }
    @Test
    void declineFriendRequest_whenRequestAlreadyAccepted_throwsException(){

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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);

        AcceptRequestRequestDto acceptRequestRequestDto = new AcceptRequestRequestDto();

        acceptRequestRequestDto.setFriendId(friendRequestResponseDto.getFriendId());
        acceptRequestRequestDto.setRequestId(friendRequestResponseDto.getRequestId());
        acceptRequestRequestDto.setMessage("Accepted");

        AcceptFriendResponseDto acceptFriendResponse =friendsRequestService.acceptRequest(acceptRequestRequestDto);
        assertThat(acceptFriendResponse.getStatus()).isEqualTo(FriendRequestStatus.ACCEPTED);

        DeclineFriendRequestDto declineFriendRequestDto = new DeclineFriendRequestDto();

        declineFriendRequestDto.setFriendId(friendRequestResponseDto.getFriendId());
        declineFriendRequestDto.setRequestId(friendRequestResponseDto.getRequestId());

        assertThrows(InvalidFriendRequestStateException.class,()->friendsRequestService.declineRequest(declineFriendRequestDto));
    }

    @Test
    void givenValidDeclineRequest_WhenProcessed_ResponseShouldNotBeNull(){

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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);

        DeclineFriendRequestDto declineFriendRequestDto = new DeclineFriendRequestDto();

        declineFriendRequestDto.setFriendId(friendRequestResponseDto.getFriendId());
        declineFriendRequestDto.setRequestId(friendRequestResponseDto.getRequestId());

        assertNotNull(friendsRequestService.declineRequest(declineFriendRequestDto));
    }

    @Test
    void givenValidDeclineRequest_WhenProcessed_ResponseShouldContainDeclinedStatus(){

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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);

        DeclineFriendRequestDto declineFriendRequestDto = new DeclineFriendRequestDto();

        declineFriendRequestDto.setFriendId(friendRequestResponseDto.getFriendId());
        declineFriendRequestDto.setRequestId(friendRequestResponseDto.getRequestId());

        DeclineFriendResponseDto declineResponseDto = friendsRequestService.declineRequest(declineFriendRequestDto);

        assertThat(declineResponseDto.getStatus()).isEqualTo(FriendRequestStatus.DECLINED);
    }

    @Test
    void givenValidDeclineRequest_WhenProcessed_ResponseShouldContainSuccessMessage(){

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

        SeekerLoginRequestDto seekerLoginRequestDto = new SeekerLoginRequestDto("timothy@gmail.com", "00000000");
        onboardingService.login(seekerLoginRequestDto);

        SendFriendRequestRequestDto friendRequestRequestDto = new SendFriendRequestRequestDto();
        friendRequestRequestDto.setLoggedIn(true);
        friendRequestRequestDto.setSeekerId(seekerResponseDto.getSeekerId());
        friendRequestRequestDto.setFriendId(friendResponseDto.getSeekerId());
        friendRequestRequestDto.setMessage("Hello Chi, Let's connect");

        SendFriendRequestResponseDto friendRequestResponseDto = friendsRequestService.sendFriendRequest(friendRequestRequestDto);
        assertThat(friendRequestResponseDto.getStatus()).isEqualTo(FriendRequestStatus.PENDING);

        DeclineFriendRequestDto declineFriendRequestDto = new DeclineFriendRequestDto();

        declineFriendRequestDto.setFriendId(friendRequestResponseDto.getFriendId());
        declineFriendRequestDto.setRequestId(friendRequestResponseDto.getRequestId());

        DeclineFriendResponseDto declineResponseDto = friendsRequestService.declineRequest(declineFriendRequestDto);

        assertThat(declineResponseDto.getMessage()).isEqualTo("Request Declined");

    }

    @Test
    void givenSeekerWithSentRequests_WhenViewingAllSentRequests_ItShouldReturnRequests(){
        
    }

}




