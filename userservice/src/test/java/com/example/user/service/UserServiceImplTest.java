package com.example.user.service;

import com.example.user.exception.UserNotFoundException;
import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.user.utils.Utils;
import com.example.usergroup.repository.UserGroupRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private BannedUsersClient bannedUsersClient;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor ArgumentCaptor<List<User>> listUserArgumentCaptor;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllUsers() {

        Mockito.when(userRepository.findAll()).thenReturn(Utils.createUserList());

        userService.findAllUsers();
        verify(userRepository,times(1)).findAll();
        assertEquals(3,userService.findAllUsers().size());

    }

    @Test
    @DisplayName("Check that a User object with specified Id is returned")
    void findUserById_UserFound() throws UserNotFoundException {
        Mockito.when(userRepository.findById(99)).thenReturn(Optional.of(Utils.CreateUser(99)));
        Mockito.when(modelMapper.map(any(), any())).thenReturn(Utils.CreateAPIUserResponseDTO(99));

        assertEquals(99,userService.findUserById(99).getUserId());
    }

    @Test
    @DisplayName("Check that a UserNotFound exception is thrown if User not found in database")
    void findUserById_UserNotFound(){
        Mockito.when(userRepository.findById(98)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()-> userService.findUserById(99));
    }

    @Test
    @Disabled("later")
    void findAllUserWithSorting() {
    }

    @Test
    @Disabled("later")
    void findUserWithPagination() {
    }

    @Test
    @Disabled("later")
    void findUserWithPaginationAndSort() {
    }

    @Test
    @DisplayName("Check that if trying to create a Banned User returns a BAD_REQUEST code")
    void createUser_ForBannedUser() {
        Mockito.when(modelMapper.map(any(), any())).thenReturn(Utils.CreateUser(99,"Lester"));
        Mockito.when(bannedUsersClient.isBannedUser(any())).thenCallRealMethod();

        userService.createUser(Utils.CreateAPIUserRequestDTO("Lester"));
        verify(bannedUsersClient,times(1)).isBannedUser("Lester");

    }

    @Test
    @DisplayName("Check that creating a User updates User Repository")
    void createUser_ForNotBannedUser() {
        Mockito.when(modelMapper.map(any(),ArgumentMatchers.eq(User.class))).thenReturn(Utils.CreateUser(99,"Coco"));
        Mockito.when(bannedUsersClient.isBannedUser(any())).thenCallRealMethod();

        userService.createUser(Utils.CreateAPIUserRequestDTO("Coco"));
        verify(userRepository,times(1)).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getUserId()).isEqualTo(99);
        assertThat(userArgumentCaptor.getValue().getName()).isEqualTo("Coco");
    }

    @Test
    @DisplayName("Check if banned User then no save to UserRepository")
    void updateUserById_ForBannedUser() {
        Mockito.when(modelMapper.map(any(),ArgumentMatchers.eq(User.class))).thenReturn(Utils.CreateUser(99,"Lester"));
        Mockito.when(bannedUsersClient.isBannedUser(any())).thenCallRealMethod();

        userService.updateUserById(Utils.CreateAPIUserRequestDTO("Lester"),1);
        verify(userRepository,times(0)).save(userArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Check if updating a User returns OK status code and URL of updated User")
    void updateUserById_ForNotBannedUser() {
        Mockito.when(bannedUsersClient.isBannedUser(any())).thenCallRealMethod();
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock -> {
            User user = invocationOnMock.getArgument(0);
            user.setUserId(99);
            return user;
        });

        userService.updateUserById(Utils.CreateAPIUserRequestDTO("Coco"),1);
        verify(userRepository,times(1)).save(userArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Check if Deleting a User by Id returns an OK status if delete ok")
    void deleteUserById_UserExists() {
        Mockito.when(userRepository.existsById(99)).thenReturn(Boolean.TRUE);

        assertEquals(HttpStatus.OK,userService.deleteUserById(99).getStatusCode());
        verify(userRepository,times(1)).deleteById(ArgumentMatchers.any(Integer.class));
    }

    @Test
    @DisplayName("Check if trying to delete non existent user returns BAD_REQUEST")
    void deleteUserById_UserDoesNotExists() {
        Mockito.when(userRepository.existsById(99)).thenReturn(Boolean.FALSE);

        assertEquals(HttpStatus.BAD_REQUEST,userService.deleteUserById(99).getStatusCode());
    }

    @Test
    @Disabled
    void findPaginated() {
    }

    @Test
    void saveUserList() {
    }

    @Test
    @DisplayName("Add a User to a UserGroup. Check User and Group object updates")
    void addUserToUserGroup() {
    }
}