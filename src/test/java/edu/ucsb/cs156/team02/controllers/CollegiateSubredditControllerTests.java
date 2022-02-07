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

    // Authorization tests

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_collegiateSubreddits_all__user_logged_in__returns_200() throws Exception {
        mockMvc.perform(get("/api/collegiateSubreddits/all"))
                .andExpect(status().isOk());
    }


    // Tests with mocks for database actions on user
    // @WithMockUser(roles = { "USER" })
    @Test
    public void api_collegiateSubreddit_user_get_all() throws Exception {
        // arrange

        // User u = currentUserService.getCurrentUser().getUser();

        CollegiateSubreddit expectedSubreddit1 = CollegiateSubreddit.builder()
                .name("Name1")
                .location("Location1")
                .subreddit("Subreddit1")
                .id(0L)
                .build();
        CollegiateSubreddit expectedSubreddit2 = CollegiateSubreddit.builder()
                .name("Name2")
                .location("Location2")
                .subreddit("Subreddit2")
                .id(1L)
                .build();
        CollegiateSubreddit expectedSubreddit3 = CollegiateSubreddit.builder()
                .name("Name3")
                .location("Location3")
                .subreddit("Subreddit3")
                .id(2L)
                .build();

        ArrayList<CollegiateSubreddit> expectedSubreddits = new ArrayList<>();
        expectedSubreddits.addAll(Arrays.asList(expectedSubreddit1,expectedSubreddit2, expectedSubreddit3));

        when(collegiateSubredditRepository.findAll()).thenReturn(expectedSubreddits);

        // act
        MvcResult response = mockMvc.perform(
                get("/api/collegiateSubreddits/all"))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedSubreddits);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_collegiateSubreddit_user_post__user_logged_in() throws Exception {
        // arrange

        User u = currentUserService.getCurrentUser().getUser();

        CollegiateSubreddit expectedSubreddit = CollegiateSubreddit.builder()
                .name("Name1")
                .location("Location1")
                .subreddit("Subreddit1")
                .id(0L)
                .build();

        when(collegiateSubredditRepository.save(eq(expectedSubreddit))).thenReturn(expectedSubreddit);

        // act
        MvcResult response = mockMvc.perform(
                post("/api/collegiateSubreddits/post?name=Name1&location=Location1&subreddit=Subreddit1")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).save(expectedSubreddit);
        String expectedJson = mapper.writeValueAsString(expectedSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }
}
