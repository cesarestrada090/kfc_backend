package com.kfc.app.controller;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.OrganizationDto;
import com.kfc.app.service.OrgService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@RequestMapping("v1/app/organization")
@RestController
public class OrgController extends AbstractController {
    private final OrgService orgService;
    private static final Logger log = Logger.getLogger(OrgController.class.getName());
    @Autowired
    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }
    
    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<OrganizationDto> getById(@PathVariable(value = "id") Integer id){
        OrganizationDto orgDto = orgService.getById(id);
        return new ResponseEntity<>(orgDto, HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<OrganizationDto> save(@Valid @RequestBody OrganizationDto orgDto){
        orgDto = orgService.save(orgDto);
        return new ResponseEntity<>(orgDto, HttpStatus.CREATED);
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody OrganizationDto orgDto){
        orgDto = orgService.update(id, orgDto);
        return new ResponseEntity<>(orgDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Map<String,Object>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<OrganizationDto> resultPageWrapper = orgService.getAll(paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Override
    protected String getResource() {
        return "organizations";
    }
}
