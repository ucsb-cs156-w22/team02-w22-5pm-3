package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.UCSBRequirement;
import edu.ucsb.cs156.team02.repositories.UCSBRequirementRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Api(description = "UCSB Requirements")
@RequestMapping("/api/UCSBRequirements")
@RestController
public class UCSBRequirementController extends ApiController {

    public class UCSBRequirementOrError {
        Long id;
        UCSBRequirement req;
        ResponseEntity<String> error;

        public UCSBRequirementOrError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    UCSBRequirementRepository ucsbRequirementRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Get a single requirement by ID")
    // @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> getUCSBRequirementById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        loggingService.logMethod();
        UCSBRequirementOrError roe = new UCSBRequirementOrError(id);

        roe = doesUCSBRequirementExist(roe);
        if (roe.error != null) {
            return roe.error;
        }
        
        String body = mapper.writeValueAsString(roe.req);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Delete a requirement by ID")
    @DeleteMapping("")
    public ResponseEntity<String> deleteUCSBRequirement(
            @ApiParam("id") @RequestParam Long id) {
        loggingService.logMethod();

        UCSBRequirementOrError roe = new UCSBRequirementOrError(id);

        roe = doesUCSBRequirementExist(roe);
        if (roe.error != null) {
            return roe.error;
        }

        ucsbRequirementRepository.deleteById(id);
        return ResponseEntity.ok().body(String.format("requirement with id %d deleted", id));

    }

    @ApiOperation(value = "Update a single requirement by ID")
    @PutMapping("")
    public ResponseEntity<String> putTodoById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid UCSBRequirement incomingUCSBRequirement) throws JsonProcessingException {
        loggingService.logMethod();

        UCSBRequirementOrError roe = new UCSBRequirementOrError(id);

        roe = doesUCSBRequirementExist(roe);
        if (roe.error != null) {
            return roe.error;
        }

        ucsbRequirementRepository.save(incomingUCSBRequirement);

        String body = mapper.writeValueAsString(incomingUCSBRequirement);
        return ResponseEntity.ok().body(body);
    }

    

    public UCSBRequirementOrError doesUCSBRequirementExist(UCSBRequirementOrError roe) {

        Optional<UCSBRequirement> optionalReq = ucsbRequirementRepository.findById(roe.id);

        if (optionalReq.isEmpty()) {
            roe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("requirement with id %d not found", roe.id));
        } else {
            roe.req = optionalReq.get();
        }
        return roe;
    }

}