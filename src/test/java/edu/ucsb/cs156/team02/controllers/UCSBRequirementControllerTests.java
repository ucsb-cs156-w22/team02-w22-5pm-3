package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UCSBRequirementRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.UCSBRequirement;
import edu.ucsb.cs156.team02.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBRequirementController.class)
@Import(TestConfig.class)
public class UCSBRequirementControllerTests extends ControllerTestCase {

    @MockBean
    UCSBRequirementRepository ucsbRequirementRepository;

    @MockBean
    UserRepository userRepository;

    // Tests with mocks for database actions

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_reqs__user_logged_in__returns_a_req_that_exists() throws Exception {

        // arrange

        UCSBRequirement req1 = UCSBRequirement.builder()
                .requirementCode("X")
                .requirementTranslation("X")
                .collegeCode("X")
                .objCode("X")
                .courseCount(0)
                .units(0)
                .inactive(false)
                .id(7L).build();

        when(ucsbRequirementRepository.findById(eq(7L))).thenReturn(Optional.of(req1));

        // act

        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements?id=7"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(ucsbRequirementRepository, times(1)).findById(eq(7L));
        String expectedJson = mapper.writeValueAsString(req1);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_reqs__user_logged_in__search_for_req_that_does_not_exist() throws Exception {

        // arrange

        User u = currentUserService.getCurrentUser().getUser();

        when(ucsbRequirementRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(ucsbRequirementRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("requirement with id 7 not found", responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_reqs__admin_logged_in__returns_a_req_that_exists() throws Exception {

        // arrange

        UCSBRequirement req1 = UCSBRequirement.builder()
                .requirementCode("X")
                .requirementTranslation("X")
                .collegeCode("X")
                .objCode("X")
                .courseCount(0)
                .units(0)
                .inactive(false)
                .id(7L).build();

        when(ucsbRequirementRepository.findById(eq(7L))).thenReturn(Optional.of(req1));

        // act

        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements?id=7"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(ucsbRequirementRepository, times(1)).findById(eq(7L));
        String expectedJson = mapper.writeValueAsString(req1);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_req__admin_logged_in__search_for_req_that_does_not_exist() throws Exception {

        // arrange

        when(ucsbRequirementRepository.findById(eq(29L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements?id=29"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(ucsbRequirementRepository, times(1)).findById(eq(29L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("requirement with id 29 not found", responseString);
    }

}
