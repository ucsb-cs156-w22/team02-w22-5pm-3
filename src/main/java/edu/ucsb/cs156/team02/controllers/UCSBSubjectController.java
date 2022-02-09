package edu.ucsb.cs156.team02.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ucsb.cs156.team02.entities.UCSBSubject;
import edu.ucsb.cs156.team02.repositories.UCSBSubjectRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

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
    public class UCSBSubjectOrError {
        Long id;
        UCSBSubject ucsbSubject;
        ResponseEntity<String> error;

        public UCSBSubjectOrError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    UCSBSubjectRepository ucsbSubjectRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all UCSB Subjects")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBSubject> allUCSBSubjects() {
        loggingService.logMethod();
        return ucsbSubjectRepository.findAll();
    }

    @ApiOperation(value = "Get a single UCSBSubject")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> getUCSBSubjectById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        loggingService.logMethod();

        UCSBSubjectOrError usoe = new UCSBSubjectOrError(id);

        usoe = doesUCSBSubjectExist(usoe);
        if (usoe.error != null) {
            return usoe.error;
        }

        String body = mapper.writeValueAsString(usoe.ucsbSubject);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Create a new UCSBSubject JSON object")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public UCSBSubject postUCSBSubject(
            @ApiParam("subjectCode") @RequestParam String subjectCode,
            @ApiParam("subjectTranslation") @RequestParam String subjectTranslation,
            @ApiParam("deptCode") @RequestParam String deptCode,
            @ApiParam("collegeCode") @RequestParam String collegeCode,
            @ApiParam("relatedDeptCode") @RequestParam String relatedDeptCode,
            @ApiParam("inactive") @RequestParam boolean inactive) {
        loggingService.logMethod();
        log.info("UCSB subject /post called: subjectCode={}, subjectTranslation={}, " +
                        "deptCode={}, collegeCode={}, relatedDeptCode={}, inactive={}",
                subjectCode, subjectTranslation, deptCode, collegeCode, relatedDeptCode, inactive);
        final var ucsbSubject = new UCSBSubject();
        ucsbSubject.setSubjectCode(subjectCode);
        ucsbSubject.setSubjectTranslation(subjectTranslation);
        ucsbSubject.setDeptCode(deptCode);
        ucsbSubject.setCollegeCode(collegeCode);
        ucsbSubject.setRelatedDeptCode(relatedDeptCode);
        ucsbSubject.setInactive(inactive);

        return ucsbSubjectRepository.save(ucsbSubject);
    }

    @ApiOperation(value = "Delete a UCSBSubject")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteUCSBSubject(
            @ApiParam("id") @RequestParam Long id) {
        loggingService.logMethod();

        UCSBSubjectOrError usoe = new UCSBSubjectOrError(id);

        usoe = doesUCSBSubjectExist(usoe);
        if (usoe.error != null) {
            return usoe.error;
        }

        ucsbSubjectRepository.deleteById(id);
        return ResponseEntity.ok().body(String.format("UCSBSubject with id %d deleted", id));

    }

    @ApiOperation(value = "Update a single UCSBSubject")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public ResponseEntity<String> putUCSBSubjectById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid UCSBSubject incomingUCSBSubject) throws JsonProcessingException {
        loggingService.logMethod();

        UCSBSubjectOrError usoe = new UCSBSubjectOrError(id);

        usoe = doesUCSBSubjectExist(usoe);
        if (usoe.error != null) {
            return usoe.error;
        }

        ucsbSubjectRepository.save(incomingUCSBSubject);

        String body = mapper.writeValueAsString(incomingUCSBSubject);
        return ResponseEntity.ok().body(body);
    }

    /**
     * Pre-conditions: usoe.id is value to look up, usoe.todo and usoe.error are null
     * <p>
     * Post-condition: if todo with id usoe.id exists, usoe.todo now refers to it, and
     * error is null.
     * Otherwise, todo with id usoe.id does not exist, and error is a suitable return
     * value to
     * report this error condition.
     */
    public UCSBSubjectOrError doesUCSBSubjectExist(UCSBSubjectOrError usoe) {

        Optional<UCSBSubject> optionalUCSBSubject = ucsbSubjectRepository.findById(usoe.id);

        if (optionalUCSBSubject.isEmpty()) {
            usoe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("UCSB Subject with id %d not found", usoe.id));
        } else {
            usoe.ucsbSubject = optionalUCSBSubject.get();
        }
        return usoe;
    }
}
