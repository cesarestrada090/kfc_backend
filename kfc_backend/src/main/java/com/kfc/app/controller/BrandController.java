package com.kfc.app.controller;

import com.kfc.app.dto.BrandDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.service.BrandService;
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

@RequestMapping("v1/app/brand")
@RestController
public class BrandController extends AbstractController{

    private final BrandService brandService;
    private static final Logger log = Logger.getLogger(BrandController.class.getName());

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<BrandDto> save(@Valid @RequestBody BrandDto brandDto){
        brandDto = brandService.save(brandDto);
        return new ResponseEntity<>(brandDto, HttpStatus.CREATED);
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<BrandDto> getById(@PathVariable(value = "id") Integer id){
        BrandDto brandDto = brandService.getById(id);
        return new ResponseEntity<>(brandDto, HttpStatus.OK);
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BrandDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody BrandDto brandDto){
        brandDto = brandService.update(id, brandDto);
        return new ResponseEntity<>(brandDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Map<String,Object>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<BrandDto> resultPageWrapper = brandService.getAll(paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected String getResource() {
        return "brands";
    }
}
