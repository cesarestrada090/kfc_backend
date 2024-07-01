package com.kfc.app.controller;

import com.kfc.app.dto.MaintenanceDto;
import com.kfc.app.dto.MechanicDto;
import com.kfc.app.dto.ProductDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.service.ProductService;
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

@RequestMapping("v1/app/product")
@RestController
public class ProductController extends AbstractController {

    private final ProductService productService;
    private static final Logger log = Logger.getLogger(ProductController.class.getName());

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> save(@Valid @RequestBody ProductDto productDto){
        productDto = productService.save(productDto);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<ProductDto> getById(@PathVariable(value = "id") Integer id){
        ProductDto productDto = productService.getById(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping(value="/organization/{organizationId}")
    public ResponseEntity<Map<String,Object>> getAllByOrgId(@PathVariable(value = "organizationId") int organizationId,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<ProductDto> resultPageWrapper = productService.findByOrganizationId(organizationId, paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ProductDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody ProductDto brandDto){
//        brandDto = productService.update(id, brandDto);
//        return new ResponseEntity<>(brandDto, HttpStatus.OK);
//    }

    @GetMapping()
    public ResponseEntity<Map<String,Object>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<ProductDto> resultPageWrapper = productService.getAll(paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public String getResource () {
        return "products";
    }
}
