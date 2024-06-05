package com.kfc.app.controller;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UnitDto;
import com.kfc.app.dto.WorkshopDto;
import com.kfc.app.service.UnitService;
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

@RequestMapping("v1/app/unit")
@RestController
public class UnitController extends AbstractController {

    private final UnitService unitService;
    private static final Logger log = Logger.getLogger(UnitController.class.getName());

    @Autowired
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnitDto> getById(@PathVariable(value = "id") Integer id) { // Assuming Unit uses Long for ID
        UnitDto unitDto = unitService.getById(id);
        return new ResponseEntity<>(unitDto, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnitDto> save(@Valid @RequestBody UnitDto unitDto) {
        unitDto = unitService.save(unitDto);
        return new ResponseEntity<>(unitDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnitDto> update(@PathVariable(value = "id") Integer id, @Valid @RequestBody UnitDto unitDto) {
        unitDto = unitService.update(id, unitDto);
        return new ResponseEntity<>(unitDto, HttpStatus.OK);
    }

    @GetMapping(value = "/organization/{organizationId}")
    public ResponseEntity<Map<String, Object>> getAllByOrgId(@PathVariable(value = "organizationId") int organizationId,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        ResultPageWrapper<UnitDto> resultPageWrapper = unitService.findByOrganizationId(organizationId, paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected String getResource() {
        return "units"; // Updated resource name
    }
}