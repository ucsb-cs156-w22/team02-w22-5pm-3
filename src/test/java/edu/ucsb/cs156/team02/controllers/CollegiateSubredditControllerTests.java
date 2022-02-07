package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.CollegiateSubreddit;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.repositories.CollegiateSubredditRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CollegiateSubredditController.class)
@Import(TestConfig.class)
public class CollegiateSubredditControllerTests extends ControllerTestCase {

    @MockBean
    CollegiateSubredditRepository collegiateSubredditRepository;

    @MockBean
    UserRepository userRepository;

    // Authorization tests for /api/collegiateSubreddits/all

    
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_collegiateSubreddits_all__user_logged_in__returns_200() throws Exception {
        mockMvc.perform(get("/api/collegiateSubreddits/all"))
                .andExpect(status().isOk());
    }

    // Tests with mocks for database actions on user

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_collegiateSubreddits_user_returns_a_collegiateSubreddit_that_exists() throws Exception {

        User u = currentUserService.getCurrentUser().getUser();
        // arrange
        CollegiateSubreddit collegiateSubreddit1 = CollegiateSubreddit.builder()
                                                                        .name("UCSB")
                                                                        .location("Santa Barbara")
                                                                        .subreddit("UCSantaBarbara")
                                                                        .id(7L)
                                                                        .build();
        when(collegiateSubredditRepository.findById(eq(7L))).thenReturn(Optional.of(collegiateSubreddit1));

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits?id=7"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(collegiateSubredditRepository, times(1)).findById(eq(7L));
        String expectedJson = mapper.writeValueAsString(collegiateSubreddit1);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_collegiateSubreddits_user_search_for_collegiateSubreddit_that_does_not_exist() throws Exception {

        User u = currentUserService.getCurrentUser().getUser();
        // arrange
        when(collegiateSubredditRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("collegiateSubreddit with id 7 not found", responseString);
    }


    // Tests with mocks for database actions on admin

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_collegiateSubreddits_admin_returns_a_collegiateSubreddit_that_exists() throws Exception {

        // arrange
        CollegiateSubreddit collegiateSubreddit1 = CollegiateSubreddit.builder()
                                                                        .name("UCSB")
                                                                        .location("Santa Barbara")
                                                                        .subreddit("UCSantaBarbara")
                                                                        .id(7L)
                                                                        .build();
        when(collegiateSubredditRepository.findById(eq(7L))).thenReturn(Optional.of(collegiateSubreddit1));

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits?id=7"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(collegiateSubredditRepository, times(1)).findById(eq(7L));
        String expectedJson = mapper.writeValueAsString(collegiateSubreddit1);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_collegiateSubreddits_admin_search_for_collegiateSubreddit_that_does_not_exist() throws Exception {

        // arrange
        when(collegiateSubredditRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("collegiateSubreddit with id 7 not found", responseString);
    }
}
