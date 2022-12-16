package com.example.user.home;

import com.example.home.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getDashboardView() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/home/index"))
                .andExpect(MockMvcResultMatchers.view().name("index"));

    }


}