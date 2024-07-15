package com.kfc.app.controller;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.SupplierDto;
import com.kfc.app.service.SupplierService;
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

@RequestMapping("v1/app/supplier")
@RestController
public class SupplierController extends AbstractController {

    private final SupplierService supplierService;
    private static final Logger log = Logger.getLogger(SupplierController.class.getName());

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupplierDto> save(@Valid @RequestBody SupplierDto supplierDto){
        supplierDto = supplierService.save(supplierDto);
        return new ResponseEntity<>(supplierDto, HttpStatus.CREATED);
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<SupplierDto> getById(@PathVariable(value = "id") Integer id){
        SupplierDto supplierDto = supplierService.getById(id);
        return new ResponseEntity<>(supplierDto, HttpStatus.OK);
    }

    @GetMapping(value="/organization/{organizationId}")
    public ResponseEntity<Map<String,Object>> getAllByOrgId(@PathVariable(value = "organizationId") int organizationId,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<SupplierDto> resultPageWrapper = supplierService.findByOrganizationId(organizationId, paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupplierDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody SupplierDto supplierDto){
        supplierDto = supplierService.update(id, supplierDto);
        return new ResponseEntity<>(supplierDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Map<String,Object>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<SupplierDto> resultPageWrapper = supplierService.getAll(paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public String getResource () {
        return "suppliers";
    }
}
