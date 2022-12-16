package com.example.user.controller;

import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.exception.UserNotFoundException;
import com.example.user.service.UserServiceImpl;
import com.example.usergroup.service.UserGroupServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserWebController.class)
class UserWebControllerTest {

    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private UserGroupServiceImpl userGroupService;

    private static final String MOBILE_ERR = "invalid mobile number.";
    private static final String FORM_OBJECT_ALIAS = "userdto";
    private static final String FORM_FIELD_NAME_TITLE = "mobile";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void newUserForm() throws Exception {

            this.mockMvc
                    .perform(get("/user/newUserForm"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("new_user"))
                    .andExpect(model().attributeExists("userreq"));
    }

    @Test
    void newUserFormPost() throws Exception {

        this.mockMvc
                .perform(post("/user/newUserForm")
                        .param("name", "duke")
                        .param("mobile", "8888888888")
                        .param("email", "duke@java.io"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/"));
    }

    @Test
    void checkShowUserFormForUpdate() throws Exception {

        when(userService.findUserById(99))
                .thenThrow(new UserNotFoundException("user not found with id : 99"));

        this.mockMvc
                .perform(get("/user/showFormForUpdate/99"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User not found in database : 99",result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("When validation fails on mobile number")
    void checkInvalidUserFormDataRejected() throws Exception {



        this.mockMvc
                .perform(post("/user/saveUser")
                        .param("name","lester")
                        .param("mobile","abc")
                        .param("email","lester@google.com"))
                .andExpect(model().attributeHasFieldErrors("userdto"))
                .andExpect(MockMvcResultMatchers.view().name("update_user"));
    }

    @Test
    @DisplayName("When validation fails on mobile number")
    void checkValidUserFormDataRejected() throws Exception {

        APIUserRequestDTO testAPIUserRequestDTO = APIUserRequestDTO.builder()
                .name("lester")
                .email("lester@google.com")
                .mobile("9999999999")
                .build();

        this.mockMvc
                .perform(post("/user/saveUser")
                        .param("name","lester")
                        .param("mobile","9999999999")
                        .param("email","lester@google.com"))
                .andExpect(flash().attribute("userreq",testAPIUserRequestDTO))
                .andExpect(redirectedUrl("/user/"));
    }

    @Test
    @DisplayName("Check the Thymeleaf view")
    void checkCallToUserUpdateForm() throws Exception {
        when(userService.findUserById(99))
                .thenReturn(
                APIUserResponseDTO.builder()
                        .name("Lester")
                        .email("lester.deeley@yahoo.com")
                        .mobile("7809886934")
                        .build());
        this.mockMvc
                .perform(get("/user/showFormForUpdate/99"))
                .andExpect(MockMvcResultMatchers.view().name("update_user"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userdto"));
    }

    @Test
    @DisplayName("Check the page of Users")
    void checkPageOfUsers() throws Exception {

        List<APIUserResponseDTO> apiUserResponseDTOList = List.of(
                APIUserResponseDTO.builder()
                        .userId(1)
                        .name("lester1")
                        .email("lester1@google.com")
                        .mobile("9999999999")
                        .build(),
                APIUserResponseDTO.builder()
                        .userId(2)
                        .name("lester2")
                        .email("lester2@google.com")
                        .mobile("9999999999")
                        .build(),
                APIUserResponseDTO.builder()
                        .userId(3)
                        .name("lester3")
                        .email("lester3@google.com")
                        .mobile("9999999999")
                        .build(),
                APIUserResponseDTO.builder()
                        .userId(4)
                        .name("lester4")
                        .email("lester4@google.com")
                        .mobile("9999999999")
                        .build(),
                APIUserResponseDTO.builder()
                        .userId(5)
                        .name("lester5")
                        .email("lester5@google.com")
                        .mobile("9999999999")
                        .build()

        );
        Page<APIUserResponseDTO> apiUserResponseDTOPage = new PageImpl<>(apiUserResponseDTOList);

        when(userService.findUserWithPagination(1,10,"asc","userId"))
                .thenReturn(apiUserResponseDTOPage);

        this.mockMvc
                .perform(get("/user/page/1"))
                .andExpect(MockMvcResultMatchers.model().attribute("currentPage",1))
                .andExpect(MockMvcResultMatchers.model().attribute("pageSize",10))
                .andExpect(MockMvcResultMatchers.view().name("user_list"));

    }

}