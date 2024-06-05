package com.kfc.app.controller;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.WorkshopDto;
import com.kfc.app.service.WorkshopService;
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

@RequestMapping("v1/app/workshop")
@RestController
public class WorkshopController extends AbstractController {

    private final WorkshopService workshopService;
    private static final Logger log = Logger.getLogger(WorkshopController.class.getName());

    @Autowired
    public WorkshopController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkshopDto> getById(@PathVariable(value = "id") Integer id) { // Changed id type to Long (assuming Workshop uses Long)
        WorkshopDto workshopDto = workshopService.getById(id);
        return new ResponseEntity<>(workshopDto, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkshopDto> save(@Valid @RequestBody WorkshopDto workshopDto) {
        workshopDto = workshopService.save(workshopDto);
        return new ResponseEntity<>(workshopDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkshopDto> update(@PathVariable(value = "id") Integer id, @Valid @RequestBody WorkshopDto workshopDto) {
        workshopDto = workshopService.update(id, workshopDto);
        return new ResponseEntity<>(workshopDto, HttpStatus.OK);
    }

    @GetMapping(value = "/organization/{organizationId}")
    public ResponseEntity<Map<String, Object>> getAllByOrgId(@PathVariable(value = "organizationId") int organizationId,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        ResultPageWrapper<WorkshopDto> resultPageWrapper = workshopService.findByOrganizationId(organizationId, paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected String getResource() {
        return "workshops"; // Updated resource name
    }
}