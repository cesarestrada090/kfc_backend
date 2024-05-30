package com.kfc.app.controller;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.service.WarehouseService;
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

@RequestMapping("v1/app/warehouse")
@RestController
public class WarehouseController extends AbstractController {
    private final WarehouseService warehouseService;
    private static final Logger log = Logger.getLogger(WarehouseController.class.getName());
    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }
    
    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<WarehouseDto> getById(@PathVariable(value = "id") Integer id){
        WarehouseDto warehouseDto = warehouseService.getById(id);
        return new ResponseEntity<>(warehouseDto, HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<WarehouseDto> save(@Valid @RequestBody WarehouseDto warehouseDto){
        warehouseDto = warehouseService.save(warehouseDto);
        return new ResponseEntity<>(warehouseDto, HttpStatus.CREATED);
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WarehouseDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody WarehouseDto warehouseDto){
        warehouseDto = warehouseService.update(id, warehouseDto);
        return new ResponseEntity<>(warehouseDto, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Map<String,Object>> getAllByUserId(@PathVariable(value = "id") int id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<WarehouseDto> resultPageWrapper = warehouseService.findByUserId(id,paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Override
    protected String getResource() {
        return "warehouses";
    }
}
