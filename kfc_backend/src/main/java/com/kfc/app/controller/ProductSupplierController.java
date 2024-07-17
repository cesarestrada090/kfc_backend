package com.kfc.app.controller;

import com.kfc.app.dto.ProductDto;
import com.kfc.app.dto.ProductSupplierDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.service.ProductSupplierService;
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

@RequestMapping("v1/app/product_supplier")
@RestController
public class ProductSupplierController extends AbstractController {

    private final ProductSupplierService productSupplierService;
    private static final Logger log = Logger.getLogger(ProductSupplierController.class.getName());

    @Autowired
    public ProductSupplierController(ProductSupplierService productSupplierService) {
        this.productSupplierService = productSupplierService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductSupplierDto> save(@Valid @RequestBody ProductSupplierDto productSupplierDto){
        productSupplierDto = productSupplierService.save(productSupplierDto);
        return new ResponseEntity<>(productSupplierDto, HttpStatus.CREATED);
    }

    @GetMapping(value="/organization/{organizationId}")
    public ResponseEntity<Map<String,Object>> getAllByOrgId(@PathVariable(value = "organizationId") int organizationId,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<ProductSupplierDto> resultPageWrapper = productSupplierService.findByOrganizationId(organizationId, paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<ProductSupplierDto> getById(@PathVariable(value = "id") Integer id){
        ProductSupplierDto productSupplierDto = productSupplierService.getById(id);
        return new ResponseEntity<>(productSupplierDto, HttpStatus.OK);
    }

    @GetMapping(value="/organization/{organizationId}/{supplierId}")
    public ResponseEntity<Map<String,Object>> getAllProductsBySupplierId(@PathVariable(value = "organizationId") int organizationId,
                                                                         @PathVariable(value = "supplierId") int supplierId,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<ProductSupplierDto> resultPageWrapper = productSupplierService.findAllProductsBySupplierId(organizationId, supplierId, paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductSupplierDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody ProductSupplierDto productSupplierDto){
        productSupplierDto = productSupplierService.update(id, productSupplierDto);
        return new ResponseEntity<>(productSupplierDto, HttpStatus.OK);
    }

    @Override
    public String getResource () {
        return "product_suppliers";
    }
}
