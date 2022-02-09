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

  


    // Tests with mocks for database actions on user on GET(Index) and Post(Create)



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



    // Tests for Get(Show) Method

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
        assertEquals("subreddit with id 7 not found", responseString);
    }



    // Tests for Get(show) on admin


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
        assertEquals("subreddit with id 7 not found", responseString);
    }



    // Tests for Put(edit) method

    @Test
    public void api_collegiateSubreddit__put_exist() throws Exception {
        
        // arrange

        // User u = currentUserService.getCurrentUser().getUser();
        // User otherUser = User.builder().id(999).build();
        
        // We deliberately set the user information to another user
        // This shoudl get ignored and overwritten with currrent user when CollegiateSubreddit is saved

        CollegiateSubreddit initialCollegiateSubreddit = CollegiateSubreddit.builder()
                                                                            .name("Name1")
                                                                            .location("Location1")
                                                                            .subreddit("Subreddit1")
                                                                            .id(10L)
                                                                            .build();

        CollegiateSubreddit updatedCollegiateSubreddit = CollegiateSubreddit.builder()
                                                                            .name("changed")
                                                                            .location("changed")
                                                                            .subreddit("changed")
                                                                            .id(10L)
                                                                            .build();
        

        String requestBody = mapper.writeValueAsString(updatedCollegiateSubreddit);
        String expectedReturn = mapper.writeValueAsString(updatedCollegiateSubreddit);

        when(collegiateSubredditRepository.findById(eq(10L))).thenReturn(Optional.of(initialCollegiateSubreddit));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/collegiateSubreddits?id=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findById(10L);
        verify(collegiateSubredditRepository, times(1)).save(updatedCollegiateSubreddit); 
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }


    @Test
    public void api_collegiateSubreddit__put_not_exist() throws Exception {
        
        // arrange

        CollegiateSubreddit updatedCollegiateSubreddit = CollegiateSubreddit.builder()
                                                                            .name("Name1")
                                                                            .location("Location1")
                                                                            .subreddit("Subreddit1")
                                                                            .id(10L)
                                                                            .build();

        

        String requestBody = mapper.writeValueAsString(updatedCollegiateSubreddit);

        when(collegiateSubredditRepository.findById(eq(10L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/collegiateSubreddits?id=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findById(10L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("subreddit with id 10 not found", responseString);
    }



    @Test
    public void api_CollegiateSubreddit__delete_exist() throws Exception {

        // arrange
        CollegiateSubreddit collegiateSubreddit1 = CollegiateSubreddit.builder().name("CollegiateSubreddit1").location("CollegiateSubreddit1").subreddit("CollegiateSubreddit1").id(123L).build();
        when(collegiateSubredditRepository.findById(eq(123L))).thenReturn(Optional.of(collegiateSubreddit1));


        // act
        MvcResult response = mockMvc.perform(
                delete("/api/collegiateSubreddits?id=123")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findById(123L);
        verify(collegiateSubredditRepository, times(1)).deleteById(123L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("subreddit with id 123 deleted", responseString);
    }


    @Test
    public void api_collegiateSubreddit__delete_does_not_exist() throws Exception {
        // arrange

        User otherUser = User.builder().id(98L).build();

        CollegiateSubreddit collegiateSubreddit1 = CollegiateSubreddit.builder().name("CollegiateSubreddit1").location("CollegiateSubreddit1").subreddit("CollegiateSubreddit1").id(123L).build();
        when(collegiateSubredditRepository.findById(eq(123L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/collegiateSubreddits?id=123")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findById(123L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("subreddit with id 123 not found", responseString);
    }



}
