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

    // Authorization tests for /api/todos/admin/all

//     @Test
    
//     public void api_ucsbSubjects_admin_all__logged_out__returns_403() throws Exception {
//         mockMvc.perform(get("/api/UCSBSubjects/admin/all"))
//                 .andExpect(status().is(403));
//     }

//     @WithMockUser(roles = { "USER" })
//     @Test
//     public void api_ucsbSubjects_admin_all__user_logged_in__returns_403() throws Exception {
//         mockMvc.perform(get("/api/UCSBSubjects/admin/all"))
//                 .andExpect(status().is(403));
//     }

//     @WithMockUser(roles = { "USER" })
//     @Test
//     public void api_ucsbSubjects_admin__user_logged_in__returns_403() throws Exception {
//         mockMvc.perform(get("/api/UCSBSubjects/admin?id=7"))
//                 .andExpect(status().is(403));
//     }   


//     @Test
//     public void api_ucsbSubjects_admin_all__admin_logged_in__returns_200() throws Exception {
//         mockMvc.perform(get("/api/ucsbSubjects/admin/all"))
//                 .andExpect(status().isOk());
//     }

    // Authorization tests for /api/todos/all

//     @Test
//     public void api_UCSBSubjects_all__logged_out__returns_403() throws Exception {
//         mockMvc.perform(get("/api/UCSBSubjects/all"))
//                 .andExpect(status().is(403));
//     }

//     @WithMockUser(roles = { "USER" })
//     @Test
//     public void api_ucsbSubjects_all__user_logged_in__returns_200() throws Exception {
//         mockMvc.perform(get("/api/UCSBSubjects/all"))
//                 .andExpect(status().isOk());
//     }

    // Authorization tests for /api/todos/post

    @Test
    public void api_ucsbSubjects_post__logged_out__returns_403() throws Exception {
        mockMvc.perform(post("/api/UCSBSubjects/post"))
                .andExpect(status().is(403));
    }


    // Tests with mocks for database actions

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsbSubjects__user_logged_in__returns_a_ucsbSubject_that_exists() throws Exception {



        User u = currentUserService.getCurrentUser().getUser();
        UCSBSubject ucsbSubject1 = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(2L)
                .build();


        when(ucsbSubjectRepository.findById(eq(2L))).thenReturn(Optional.of(ucsbSubject1));

    
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/?id=2"))
                .andExpect(status().isOk()).andReturn();



        verify(ucsbSubjectRepository, times(1)).findById(eq(2L));
        String expectedJson = mapper.writeValueAsString(ucsbSubject1);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsbSubjects__user_logged_in__search_for_ucsbSubject_that_does_not_exist() throws Exception {

        User u = currentUserService.getCurrentUser().getUser();

        when(ucsbSubjectRepository.findById(eq(0L))).thenReturn(Optional.empty());


        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=0"))
                .andExpect(status().isBadRequest()).andReturn();


        verify(ucsbSubjectRepository, times(1)).findById(eq(0L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSB Subject with id 0 not found", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsbSubjects__user_logged_in__search_for_ucsbSubject_that_belongs_to_another_user() throws Exception {

        User u = currentUserService.getCurrentUser().getUser();
        User otherUser = User.builder().id(999L).build();
        UCSBSubject otherUsersUCSBSubject = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(0L)
                .build();

        when(ucsbSubjectRepository.findById(eq(0L))).thenReturn(Optional.of(otherUsersUCSBSubject));

        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=0"))
                .andExpect(status().isBadRequest()).andReturn();



        verify(ucsbSubjectRepository, times(1)).findById(eq(0L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSB Subject with id 0 not found", responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsbSubject__admin_logged_in__search_for_ucsbSubject_that_belongs_to_another_user() throws Exception {

        User u = currentUserService.getCurrentUser().getUser();
        User otherUser = User.builder().id(999L).build();
        UCSBSubject otherUsersUCSBSubject = UCSBSubject.builder()
                .subjectCode("A")
                .subjectTranslation("B")
                .collegeCode("C")
                .deptCode("D")
                .relatedDeptCode("E")
                .inactive(true)
                .id(0L)
                .build();

        when(ucsbSubjectRepository.findById(eq(0L))).thenReturn(Optional.of(otherUsersUCSBSubject));

        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=0"))
                .andExpect(status().isOk()).andReturn();

 

        verify(ucsbSubjectRepository, times(1)).findById(eq(0L));
        String expectedJson = mapper.writeValueAsString(otherUsersUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsbSubjects__admin_logged_in__search_for_ucsbSubject_that_does_not_exist() throws Exception {

        

        when(ucsbSubjectRepository.findById(eq(0L))).thenReturn(Optional.empty());

        
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=0"))
                .andExpect(status().isBadRequest()).andReturn();

        

        verify(ucsbSubjectRepository, times(1)).findById(eq(0L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 0 not found", responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_UCSBSubjects_admin_all__admin_logged_in__returns_all_UCSBSubjects() throws Exception {

    

        User u1 = User.builder().id(1L).build();
        User u2 = User.builder().id(2L).build();
        User u = currentUserService.getCurrentUser().getUser();

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

        ArrayList<UCSBSubject> expectedUCSBSubjects = new ArrayList<>();
        expectedUCSBSubjects.addAll(Arrays.asList(UCSBSubject1, UCSBSubject2, UCSBSubject3));

        when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubjects);

        
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/admin/all"))
                .andExpect(status().isOk()).andReturn();

        

        verify(ucsbSubjectRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubjects);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

//     @WithMockUser(roles = { "USER" })
//     @Test
//     public void api_todos_all__user_logged_in__returns_only_todos_for_user() throws Exception {

        

//         User thisUser = currentUserService.getCurrentUser().getUser();

//         UCSBSubject UCSBSubject1 = UCSBSubject.builder()
//                 .subjectCode("A")
//                 .subjectTranslation("B")
//                 .collegeCode("C")
//                 .deptCode("D")
//                 .relatedDeptCode("E")
//                 .inactive(true)
//                 .id(1L)
//                 .build();

//         UCSBSubject UCSBSubject2 = UCSBSubject.builder()
//                 .subjectCode("A")
//                 .subjectTranslation("B")
//                 .collegeCode("C")
//                 .deptCode("D")
//                 .relatedDeptCode("E")
//                 .inactive(true)
//                 .id(2L)
//                 .build();

//         UCSBSubject UCSBSubject3 = UCSBSubject.builder()
//                 .subjectCode("A")
//                 .subjectTranslation("B")
//                 .collegeCode("C")
//                 .deptCode("D")
//                 .relatedDeptCode("E")
//                 .inactive(true)
//                 .id(3L)
//                 .build();

//         ArrayList<UCSBSubject> expectedUCSBSubjects = new ArrayList<>();
//         expectedUCSBSubjects.addAll(Arrays.asList(UCSBSubject1, UCSBSubject2, UCSBSubject3));

        
//         when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubjects);

        
//         MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/all"))
//                 .andExpect(status().isOk()).andReturn();

        

//         verify(ucsbSubjectRepository, times(1)).findAllByUserId(eq(thisUser.getId()));
//         String expectedJson = mapper.writeValueAsString(expectedUCSBSubjects);
//         String responseString = response.getResponse().getContentAsString();
//         assertEquals(expectedJson, responseString);
//     }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBSubjects_post__user_logged_in() throws Exception {
        // arrange

        User u = currentUserService.getCurrentUser().getUser();

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
}
        /*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__delete_todo() throws Exception {
        // arrange

        User u = currentUserService.getCurrentUser().getUser();
        Todo todo1 = Todo.builder().title("Todo 1").details("Todo 1").done(false).user(u).id(15L).build();
        when(todoRepository.findById(eq(15L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos?id=15")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(15L);
        verify(todoRepository, times(1)).deleteById(15L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 15 deleted", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__delete_todo_that_does_not_exist() throws Exception {
        // arrange

        User otherUser = User.builder().id(98L).build();
        Todo todo1 = Todo.builder().title("Todo 1").details("Todo 1").done(false).user(otherUser).id(15L).build();
        when(todoRepository.findById(eq(15L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos?id=15")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(15L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 15 not found", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__cannot_delete_todo_belonging_to_another_user() throws Exception {
        // arrange

        User otherUser = User.builder().id(98L).build();
        Todo todo1 = Todo.builder().title("Todo 1").details("Todo 1").done(false).user(otherUser).id(31L).build();
        when(todoRepository.findById(eq(31L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos?id=31")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(31L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 31 not found", responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos__admin_logged_in__delete_todo() throws Exception {
        // arrange

        User otherUser = User.builder().id(98L).build();
        Todo todo1 = Todo.builder().title("Todo 1").details("Todo 1").done(false).user(otherUser).id(16L).build();
        when(todoRepository.findById(eq(16L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos/admin?id=16")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(16L);
        verify(todoRepository, times(1)).deleteById(16L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 16 deleted", responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos__admin_logged_in__cannot_delete_todo_that_does_not_exist() throws Exception {
        // arrange

        when(todoRepository.findById(eq(17L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos/admin?id=17")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(17L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 17 not found", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__put_todo() throws Exception {
        // arrange

        User u = currentUserService.getCurrentUser().getUser();
        User otherUser = User.builder().id(999).build();
        Todo todo1 = Todo.builder().title("Todo 1").details("Todo 1").done(false).user(u).id(67L).build();
        // We deliberately set the user information to another user
        // This shoudl get ignored and overwritten with currrent user when todo is saved

        Todo updatedTodo = Todo.builder().title("New Title").details("New Details").done(true).user(otherUser).id(67L).build();
        Todo correctTodo = Todo.builder().title("New Title").details("New Details").done(true).user(u).id(67L).build();

        String requestBody = mapper.writeValueAsString(updatedTodo);
        String expectedReturn = mapper.writeValueAsString(correctTodo);

        when(todoRepository.findById(eq(67L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/todos?id=67")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(67L);
        verify(todoRepository, times(1)).save(correctTodo); // should be saved with correct user
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__cannot_put_todo_that_does_not_exist() throws Exception {
        // arrange

        Todo updatedTodo = Todo.builder().title("New Title").details("New Details").done(true).id(67L).build();

        String requestBody = mapper.writeValueAsString(updatedTodo);

        when(todoRepository.findById(eq(67L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/todos?id=67")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(67L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 67 not found", responseString);
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__cannot_put_todo_for_another_user() throws Exception {
        // arrange

        User otherUser = User.builder().id(98L).build();
        Todo todo1 = Todo.builder().title("Todo 1").details("Todo 1").done(false).user(otherUser).id(31L).build();
        Todo updatedTodo = Todo.builder().title("New Title").details("New Details").done(true).id(31L).build();

        when(todoRepository.findById(eq(31L))).thenReturn(Optional.of(todo1));

        String requestBody = mapper.writeValueAsString(updatedTodo);

        when(todoRepository.findById(eq(67L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/todos?id=31")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(31L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 31 not found", responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos__admin_logged_in__put_todo() throws Exception {
        // arrange

        User otherUser = User.builder().id(255L).build();
        Todo todo1 = Todo.builder().title("Todo 1").details("Todo 1").done(false).user(otherUser).id(77L).build();
        User yetAnotherUser = User.builder().id(512L).build();
        // We deliberately put the wrong user on the updated todo
        // We expect the controller to ignore this and keep the user the same
        Todo updatedTodo = Todo.builder().title("New Title").details("New Details").done(true).user(yetAnotherUser).id(77L)
                .build();
        Todo correctTodo = Todo.builder().title("New Title").details("New Details").done(true).user(otherUser).id(77L)
                .build();

        String requestBody = mapper.writeValueAsString(updatedTodo);
        String expectedJson = mapper.writeValueAsString(correctTodo);

        when(todoRepository.findById(eq(77L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/todos/admin?id=77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(77L);
        verify(todoRepository, times(1)).save(correctTodo);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos__admin_logged_in__cannot_put_todo_that_does_not_exist() throws Exception {
        // arrange

        User otherUser = User.builder().id(345L).build();
        Todo updatedTodo = Todo.builder().title("New Title").details("New Details").done(true).user(otherUser).id(77L)
                .build();

        String requestBody = mapper.writeValueAsString(updatedTodo);

        when(todoRepository.findById(eq(77L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/todos/admin?id=77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(todoRepository, times(1)).findById(77L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 77 not found", responseString);
    }

}
*/