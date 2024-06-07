package com.kfc.app.controller;
import com.kfc.app.dto.MaintenanceDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/app/maintenance")
public class MaintenanceController extends AbstractController {

    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MaintenanceDto> getById(@PathVariable(value = "id") Integer id) {
        MaintenanceDto maintenanceDTO = maintenanceService.getById(id);
        return new ResponseEntity<>(maintenanceDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MaintenanceDto> save(@Valid @RequestBody MaintenanceDto maintenanceDTO) {
        maintenanceDTO = maintenanceService.save(maintenanceDTO);
        return new ResponseEntity<>(maintenanceDTO, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MaintenanceDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody MaintenanceDto maintenanceDTO) {
        maintenanceDTO = maintenanceService.update(id, maintenanceDTO);
        return new ResponseEntity<>(maintenanceDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/organization/{organizationId}")
    public ResponseEntity<ResultPageWrapper<MaintenanceDto>> getAll(
            @PathVariable(value = "organizationId") int organizationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        ResultPageWrapper<MaintenanceDto> resultPageWrapper = maintenanceService.findByOrganizationId(organizationId,paging);
        return new ResponseEntity<>(resultPageWrapper, HttpStatus.OK);
    }

    @Override
    protected String getResource() {
        return "maintenances";
    }
}