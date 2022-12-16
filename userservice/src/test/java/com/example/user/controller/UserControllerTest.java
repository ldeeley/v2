package com.example.user.controller;

import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.exception.UserNotFoundException;
import com.example.user.service.BannedUsersClient;
import com.example.user.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)          //1. Spring Mocks a Controller - should define the controller under test (not necessary)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private BannedUsersClient bannedUsersClient;

    @Test
    void shouldCreateMockMvc(){
        assertNotNull(mockMvc);
    }

    @Test
    void shouldReturnSpecificUser() throws Exception{
        // test userController endpoint GET:/users/{userId}
        when(userService.findUserById(1)).thenReturn(
                APIUserResponseDTO.builder()
                        .name("Lester")
                        .email("lester.deeley@yahoo.com")
                        .mobile("7809886934")
                        .build());
        // get the Mock to test the endpoint and check the results
        this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",Matchers.is("Lester")))
                .andExpect(jsonPath("$.email",Matchers.is("lester.deeley@yahoo.com")));
    }
    @Test
    void givenUserNotFoundException_thenNotFoundCode() throws Exception {
        // test userController endpoint GET:/users/{userId} - for non-existent user throws UserNotFoundException
        when(userService.findUserById(2)).thenThrow(new UserNotFoundException("User not found"));
        // get the Mock to test the endpoint and check the results
        this.mockMvc.perform(get("/users/2"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User not found", result.getResolvedException().getMessage()));
    }

    @Test
    void updateUserRecord() throws Exception {
        // test userController endpoint PUT:/users/{userId} - to update a User
        this.mockMvc
                .perform(MockMvcRequestBuilders
                    .put("/users/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getUserBody())
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
        // check the userService was called
        verify(userService).updateUserById(any(APIUserRequestDTO.class),any(Integer.class));
    }

    private String getUserBody() throws JsonProcessingException {
        APIUserRequestDTO myAPIUserRequestDTO = APIUserRequestDTO.builder().
                name("Gillianca").email("gill@yahoo.com").mobile("9999999999").build();
        return objectMapper.writeValueAsString(myAPIUserRequestDTO);

    }
}