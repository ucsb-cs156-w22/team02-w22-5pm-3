package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.UCSBRequirement;
import edu.ucsb.cs156.team02.models.CurrentUser;
import edu.ucsb.cs156.team02.repositories.UCSBRequirementRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Api(description = "UCSB Requirements")
@RequestMapping("/api/UCSBRequirements")
@RestController
@Slf4j
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