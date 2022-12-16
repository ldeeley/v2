package com.example.user.service;

import com.example.user.exception.UserGroupNotFoundException;
import com.example.user.exception.UserNotFoundException;
import com.example.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class BannedUsersClientTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;


    @DisplayName("Testing to see if the User is banned")
    @Test
    @Tag("user_service")
    void isBannedUser() {

        BannedUsersClient bannedUsersClient = new BannedUsersClient();

        assertTrue(bannedUsersClient.isBannedUser("Lester"));
        assertTrue(bannedUsersClient.isBannedUser("LESTER"));
        assertTrue(bannedUsersClient.isBannedUser("lESTER"));
        assertTrue(bannedUsersClient.isBannedUser("lester"));
        assertFalse(bannedUsersClient.isBannedUser(" lester"));
         assertFalse(bannedUsersClient.isBannedUser(" lester "));
         assertFalse(bannedUsersClient.isBannedUser("lester "));

    }

    @Test
    void testUserExceptionError() throws UserNotFoundException, UserGroupNotFoundException {
        when(userRepository.findById(99)).thenThrow(UserNotFoundException.class);
        userService.addUserToUserGroup(99,1);
        assertThrows(UserNotFoundException.class,()-> System.out.println("Did it work ?"));

    }

}