package com.kfc.app.controller;

import com.kfc.app.dto.ProductTypeDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.service.ProductTypeService;
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

@RequestMapping("v1/app/product_type")
@RestController
public class ProductTypeController extends AbstractController {

    private final ProductTypeService productTypeService;
    private static final Logger log = Logger.getLogger(ProductTypeController.class.getName());

    @Autowired
    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<ProductTypeDto> save(@Valid @RequestBody ProductTypeDto productTypeDto){
        productTypeDto = productTypeService.save(productTypeDto);
        return new ResponseEntity<>(productTypeDto, HttpStatus.CREATED);
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<ProductTypeDto> getById(@PathVariable(value = "id") Integer id){
        ProductTypeDto productTypeDto = productTypeService.getById(id);
        return new ResponseEntity<>(productTypeDto, HttpStatus.OK);
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductTypeDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody ProductTypeDto productTypeDto){
        productTypeDto = productTypeService.update(id, productTypeDto);
        return new ResponseEntity<>(productTypeDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Map<String,Object>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<ProductTypeDto> resultPageWrapper = productTypeService.getAll(paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected String getResource() {
        return "product_type";
    }
}
