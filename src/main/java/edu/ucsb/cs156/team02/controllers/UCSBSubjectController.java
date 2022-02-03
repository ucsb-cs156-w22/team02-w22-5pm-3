package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.UCSBSubject;
import edu.ucsb.cs156.team02.models.CurrentUser;
import edu.ucsb.cs156.team02.repositories.UCSBSubjectRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(description = "UCSBSubjects")
@RequestMapping("/api/UCSBSubjects/")
@RestController
@Slf4j
public class UCSBSubjectController extends ApiController {

//    /**
//     * This inner class helps us factor out some code for checking
//     * whether todos exist, and whether they belong to the current user,
//     * along with the error messages pertaining to those situations. It
//     * bundles together the state needed for those checks.
//     */
//    public class TodoOrError {
//        Long id;
//        Todo todo;
//        ResponseEntity<String> error;
//
//        public TodoOrError(Long id) {
//            this.id = id;
//        }
//    }

    @Autowired
    UCSBSubjectRepository ucsbSubjectRepository;

//    @Autowired
//    ObjectMapper mapper;

    @ApiOperation(value = "List all UCSB Subjects")
    @GetMapping("/all")
    public Iterable<UCSBSubject> allUCSBSubjects() {
        loggingService.logMethod();
        return ucsbSubjectRepository.findAll();
    }

//    @ApiOperation(value = "Get a single todo (if it belongs to current user)")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @GetMapping("")
//    public ResponseEntity<String> getTodoById(
//            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
//        loggingService.logMethod();
//        TodoOrError toe = new TodoOrError(id);
//
//        toe = doesTodoExist(toe);
//        if (toe.error != null) {
//            return toe.error;
//        }
//        toe = doesTodoBelongToCurrentUser(toe);
//        if (toe.error != null) {
//            return toe.error;
//        }
//        String body = mapper.writeValueAsString(toe.todo);
//        return ResponseEntity.ok().body(body);
//    }
//
//    @ApiOperation(value = "Get a single todo (no matter who it belongs to, admin only)")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/admin")
//    public ResponseEntity<String> getTodoById_admin(
//            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
//        loggingService.logMethod();
//
//        TodoOrError toe = new TodoOrError(id);
//
//        toe = doesTodoExist(toe);
//        if (toe.error != null) {
//            return toe.error;
//        }
//
//        String body = mapper.writeValueAsString(toe.todo);
//        return ResponseEntity.ok().body(body);
//    }

    @ApiOperation(value = "Create a new UCSBSubject JSON object")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/post")
    public UCSBSubject postUCSBSubject(
            @ApiParam("subjectCode") @RequestParam String subjectCode,
            @ApiParam("subjectTranslation") @RequestParam String subjectTranslation,
            @ApiParam("deptCode") @RequestParam String deptCode,
            @ApiParam("collegeCode") @RequestParam String collegeCode,
            @ApiParam("relatedDeptCode") @RequestParam String relatedDeptCode,
            @ApiParam("inactive") @RequestParam boolean inactive) {
        loggingService.logMethod();
        CurrentUser currentUser = getCurrentUser();
        log.info("UCSB subject /post called: subjectCode={}, subjectTranslation={}, " +
                        "deptCode={}, collegeCode={}, relatedDeptCode={}, inactive={}",
                subjectCode, subjectTranslation, deptCode, collegeCode, relatedDeptCode, inactive);
        final var savedUCSBSubject = new UCSBSubject();
        savedUCSBSubject.setSubjectCode(subjectCode);
        savedUCSBSubject.setSubjectTranslation(subjectTranslation);
        savedUCSBSubject.setDeptCode(deptCode);
        savedUCSBSubject.setCollegeCode(collegeCode);
        savedUCSBSubject.setRelatedDeptCode(relatedDeptCode);
        savedUCSBSubject.setInactive(inactive);
        return savedUCSBSubject;
    }

//    @ApiOperation(value = "Delete a Todo owned by this user")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @DeleteMapping("")
//    public ResponseEntity<String> deleteTodo(
//            @ApiParam("id") @RequestParam Long id) {
//        loggingService.logMethod();
//
//        TodoOrError toe = new TodoOrError(id);
//
//        toe = doesTodoExist(toe);
//        if (toe.error != null) {
//            return toe.error;
//        }
//
//        toe = doesTodoBelongToCurrentUser(toe);
//        if (toe.error != null) {
//            return toe.error;
//        }
//        ucsbSubjectRepository.deleteById(id);
//        return ResponseEntity.ok().body(String.format("todo with id %d deleted", id));
//
//    }
//
//    @ApiOperation(value = "Delete another user's todo")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/admin")
//    public ResponseEntity<String> deleteTodo_Admin(
//            @ApiParam("id") @RequestParam Long id) {
//        loggingService.logMethod();
//
//        TodoOrError toe = new TodoOrError(id);
//
//        toe = doesTodoExist(toe);
//        if (toe.error != null) {
//            return toe.error;
//        }
//
//        ucsbSubjectRepository.deleteById(id);
//
//        return ResponseEntity.ok().body(String.format("todo with id %d deleted", id));
//
//    }
//
//    @ApiOperation(value = "Update a single todo (if it belongs to current user)")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PutMapping("")
//    public ResponseEntity<String> putTodoById(
//            @ApiParam("id") @RequestParam Long id,
//            @RequestBody @Valid Todo incomingTodo) throws JsonProcessingException {
//        loggingService.logMethod();
//
//        CurrentUser currentUser = getCurrentUser();
//        User user = currentUser.getUser();
//
//        TodoOrError toe = new TodoOrError(id);
//
//        toe = doesTodoExist(toe);
//        if (toe.error != null) {
//            return toe.error;
//        }
//        toe = doesTodoBelongToCurrentUser(toe);
//        if (toe.error != null) {
//            return toe.error;
//        }
//
//        incomingTodo.setUser(user);
//        ucsbSubjectRepository.save(incomingTodo);
//
//        String body = mapper.writeValueAsString(incomingTodo);
//        return ResponseEntity.ok().body(body);
//    }
//
//    @ApiOperation(value = "Update a single todo (regardless of ownership, admin only, can't change ownership)")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/admin")
//    public ResponseEntity<String> putTodoById_admin(
//            @ApiParam("id") @RequestParam Long id,
//            @RequestBody @Valid Todo incomingTodo) throws JsonProcessingException {
//        loggingService.logMethod();
//
//        TodoOrError toe = new TodoOrError(id);
//
//        toe = doesTodoExist(toe);
//        if (toe.error != null) {
//            return toe.error;
//        }
//
//        // Even the admin can't change the user; they can change other details
//        // but not that.
//
//        User previousUser = toe.todo.getUser();
//        incomingTodo.setUser(previousUser);
//        ucsbSubjectRepository.save(incomingTodo);
//
//        String body = mapper.writeValueAsString(incomingTodo);
//        return ResponseEntity.ok().body(body);
//    }
//
//    /**
//     * Pre-conditions: toe.id is value to look up, toe.todo and toe.error are null
//     * <p>
//     * Post-condition: if todo with id toe.id exists, toe.todo now refers to it, and
//     * error is null.
//     * Otherwise, todo with id toe.id does not exist, and error is a suitable return
//     * value to
//     * report this error condition.
//     */
//    public TodoOrError doesTodoExist(TodoOrError toe) {
//
//        Optional<Todo> optionalTodo = ucsbSubjectRepository.findById(toe.id);
//
//        if (optionalTodo.isEmpty()) {
//            toe.error = ResponseEntity
//                    .badRequest()
//                    .body(String.format("todo with id %d not found", toe.id));
//        } else {
//            toe.todo = optionalTodo.get();
//        }
//        return toe;
//    }
//
//    /**
//     * Pre-conditions: toe.todo is non-null and refers to the todo with id toe.id,
//     * and toe.error is null
//     * <p>
//     * Post-condition: if todo belongs to current user, then error is still null.
//     * Otherwise error is a suitable
//     * return value.
//     */
//    public TodoOrError doesTodoBelongToCurrentUser(TodoOrError toe) {
//        CurrentUser currentUser = getCurrentUser();
//        log.info("currentUser={}", currentUser);
//
//        Long currentUserId = currentUser.getUser().getId();
//        Long todoUserId = toe.todo.getUser().getId();
//        log.info("currentUserId={} todoUserId={}", currentUserId, todoUserId);
//
//        if (todoUserId != currentUserId) {
//            toe.error = ResponseEntity
//                    .badRequest()
//                    .body(String.format("todo with id %d not found", toe.id));
//        }
//        return toe;
//    }

}
