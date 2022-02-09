package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.UCSBSubject;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.repositories.UCSBSubjectRepository;

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

@WebMvcTest(controllers = UCSBSubjectController.class)
@Import(TestConfig.class)
public class UCSBSubjectsControllerTests extends ControllerTestCase {

    @MockBean
    UCSBSubjectRepository ucsbSubjectRepository;

    @MockBean
    UserRepository userRepository;

    // tests for /api/ucsbSubjects/all

    @Test
    public void api_ucsbSubjects_all__logged_out__returns_403() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void api_ucsbSubjects_all__user_logged_in__returns_200() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().is(200));
    }


    @WithMockUser(roles = {"USER"})
    @Test
    public void api_UCSBSubjects_all__user_logged_in__returns_all_UCSBSubjects() throws Exception {
        UCSBSubject UCSBSubject1 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(1L)
                .build();

        UCSBSubject UCSBSubject2 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(2L)
                .build();

        UCSBSubject UCSBSubject3 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(3L)
                .build();

        ArrayList<UCSBSubject> expectedUCSBSubjects = new ArrayList<>(
                Arrays.asList(UCSBSubject1, UCSBSubject2, UCSBSubject3));

        when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubjects);

        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk()).andReturn();

        verify(ucsbSubjectRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubjects);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void api_ucsbSubjects_all__admin_logged_in__returns_200() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().is(403));
    }


    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void api_UCSBSubjects_all__admin_logged_in__returns_all_UCSBSubjects() throws Exception {

        UCSBSubject UCSBSubject1 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(1L)
                .build();

        UCSBSubject UCSBSubject2 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(2L)
                .build();

        UCSBSubject UCSBSubject3 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(3L)
                .build();

        ArrayList<UCSBSubject> expectedUCSBSubjects = new ArrayList<>(
                Arrays.asList(UCSBSubject1, UCSBSubject2, UCSBSubject3));

        when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubjects);

        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk()).andReturn();

        verify(ucsbSubjectRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubjects);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // tests for /api/ucsbSubjects/post

    @Test
    public void api_ucsbSubjects_post__logged_out__returns_403() throws Exception {
        mockMvc.perform(post("/api/UCSBSubjects/post"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void api_ucsbSubjects_post__user_logged_in__returns_403() throws Exception {
        mockMvc.perform(post("/api/UCSBSubjects/post"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void api_UCSBSubjects_post__admin_logged_in() throws Exception {
        // arrange

        UCSBSubject expectedUCSBSubject = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(0L)
                .build();

        when(ucsbSubjectRepository.save(eq(expectedUCSBSubject))).thenReturn(expectedUCSBSubject);

        // act
        MvcResult response = mockMvc.perform(
                        post("/api/UCSBSubjects/post?id=0&subjectCode=A&subjectTranslation=B&collegeCode=C&deptCode=D&relatedDeptCode=E&inactive=true")
                                .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).save(expectedUCSBSubject);
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    // Tests with mocks for database actions

    @WithMockUser(roles = {"USER"})
    @Test
    public void api_ucsbSubjects__user_logged_in__returns_a_ucsbSubject_that_exists() throws Exception {

        UCSBSubject ucsbSubject1 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(1L)
                .build();


        when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.of(ucsbSubject1));


        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/?id=1"))
                .andExpect(status().isOk()).andReturn();


        verify(ucsbSubjectRepository, times(1)).findById(eq(1L));
        String expectedJson = mapper.writeValueAsString(ucsbSubject1);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    public void api_ucsbSubjects__user_logged_in__search_for_ucsbSubject_that_does_not_exist() throws Exception {

        User u = currentUserService.getCurrentUser().getUser();

        when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.empty());


        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/?id=1"))
                .andExpect(status().isBadRequest()).andReturn();


        verify(ucsbSubjectRepository, times(1)).findById(eq(1L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSB Subject with id 1 not found", responseString);
    }


//     @WithMockUser(roles = { "ADMIN" })
//     @Test
//     public void api_ucsbSubject__admin_logged_in__search_for_ucsbSubject_that_belongs_to_another_user() throws Exception {

//         User u = currentUserService.getCurrentUser().getUser();
//         User otherUser = User.builder().id(999L).build();
//         UCSBSubject otherUsersUCSBSubject = UCSBSubject.builder()
//                 .subjectCode("A")
//                 .subjectTranslation("B")
//                 .collegeCode("C")
//                 .deptCode("D")
//                 .relatedDeptCode("E")
//                 .inactive(true)
//                 .id(999L)
//                 .build();

//         when(ucsbSubjectRepository.findById(eq(999L))).thenReturn(Optional.of(otherUsersUCSBSubject));

//         MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=999"))
//                 .andExpect(status().isOk()).andReturn();


//         verify(ucsbSubjectRepository, times(1)).findById(eq(0L));
//         String expectedJson = mapper.writeValueAsString(otherUsersUCSBSubject);
//         String responseString = response.getResponse().getContentAsString();
//         assertEquals(expectedJson, responseString);
//     }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void api_ucsbSubjects__admin_logged_in__search_for_ucsbSubject_that_does_not_exist() throws Exception {


        when(ucsbSubjectRepository.findById(eq(0L))).thenReturn(Optional.empty());


        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/?id=0"))
                .andExpect(status().isBadRequest()).andReturn();


        verify(ucsbSubjectRepository, times(1)).findById(eq(0L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 0 not found", responseString);
    }


    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void api_UCSBSubject__admin_logged_in__delete_UCSBSubject() throws Exception {
        // arrange

        UCSBSubject ucsbSubject1 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(1L)
                .build();

        when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.of(ucsbSubject1));

        // act
        MvcResult response = mockMvc.perform(
                        delete("/api/UCSBSubjects/?id=1")
                                .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(1L);
        verify(ucsbSubjectRepository, times(1)).deleteById(1L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 1 deleted", responseString);
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void api_UCSBSubjects__admin_logged_in__cannot_delete_UCSBSubject_that_does_not_exist() throws Exception {
        // arrange

        when(ucsbSubjectRepository.findById(eq(17L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                        delete("/api/UCSBSubjects/?id=17")
                                .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(17L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSB Subject with id 17 not found", responseString);
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void api_ucsbSubjects__admin_logged_in__put_ucsbSubject() throws Exception {
        // arrange

        UCSBSubject ucsbSubject1 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(1L)
                .build();
        // We deliberately put the wrong user on the updated todo
        // We expect the controller to ignore this and keep the user the same
        UCSBSubject updatedUCSBSubject = UCSBSubject.builder()
                .subjectCode("F")
                .subjectTranslation("G")
                .collegeCode("H")
                .deptCode("I")
                .relatedDeptCode("J")
                .inactive(true)
                .id(1L)
                .build();

        String expectedJson = mapper.writeValueAsString(updatedUCSBSubject);
        String requestBody = mapper.writeValueAsString(updatedUCSBSubject);
        when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.of(ucsbSubject1));

        // act
        MvcResult response = mockMvc.perform(
                        put("/api/UCSBSubject/?id=1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(requestBody)
                                .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(1L);
        verify(ucsbSubjectRepository, times(1)).save(updatedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void api_ucsbSubjects__admin_logged_in__cannot_put_ucsbSubject_that_does_not_exist() throws Exception {
        // arrange

        UCSBSubject updatedUCSBSubject = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(1L)
                .build();

        String requestBody = mapper.writeValueAsString(updatedUCSBSubject);

        when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                        put("/api/UCSBSubject/?id=1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(requestBody)
                                .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(1L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 77 not found", responseString);
    }

}
