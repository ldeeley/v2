package com.example.user.controller;

import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.service.BannedUsersClient;
import com.example.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
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
    public void shouldReturnAllUsers() throws Exception{
        // this is what the Mock should do when called
        when(userService.findAllUsers()).thenReturn(
                List.of(new APIUserResponseDTO(1,
                        "Lester",
                        "lester.deeley@yahoo.com",
                        "07809886934")));
        // get the Mock to test the endpoint and check the results
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name",Matchers.is("Lester")))
                .andExpect(jsonPath("$[0].email",Matchers.is("lester.deeley@yahoo.com")))
                .andExpect(jsonPath("$.size()",Matchers.is(1)));
    }

//    @Test
//    void shouldCreateUser() throws Exception {
//        // this is what the Mock should do when called
//        UserRequest userRequest = getUserRequest();
//        String userRequestString = objectMapper.writeValueAsString(userRequest);
//        when(userService.createUser(userRequest).thenReturn(
//                ResponseEntity<String>);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/postUser")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userRequestString)
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("name").value("Gillianca"))
//                .andExpect(MockMvcResultMatchers.jsonPath("mobile").value("9999999999"));
//
//        verify(userService).createUser(ArgumentMatchers.any(UserRequest.class));
//    }

    private APIUserRequestDTO getUserRequest(){
        return APIUserRequestDTO.builder().
                name("Gillianca").email("gill@yahoo.com").mobile("9999999999").build();
    }
}