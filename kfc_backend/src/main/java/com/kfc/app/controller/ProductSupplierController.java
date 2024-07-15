package com.kfc.app.controller;

import com.kfc.app.dto.ProductDto;
import com.kfc.app.dto.ProductSupplierDto;
import com.kfc.app.service.ProductService;
import com.kfc.app.service.ProductSupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    public String getResource () {
        return "product_supplier";
    }
}
