package com.kfc.app.controller;

import com.kfc.app.dto.MechanicDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UserDto;
import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.service.MechanicService;
import com.kfc.app.service.UserService;
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

@RestController
@RequestMapping("/v1/app/mechanic")
public class MechanicController extends AbstractController {

    private final MechanicService mechanicService;
    private static final Logger log = Logger.getLogger(MechanicController.class.getName());

    @Autowired
    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<MechanicDto> getById(@PathVariable(value = "id") Integer id) {
        MechanicDto mechanicDTO = mechanicService.getById(id);
        if (mechanicDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(mechanicDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<MechanicDto> save(@Valid @RequestBody MechanicDto mechanicDTO) {
        mechanicDTO = mechanicService.save(mechanicDTO);
        return new ResponseEntity<>(mechanicDTO, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MechanicDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody MechanicDto mechanicDTO) {
        mechanicDTO = mechanicService.update(id, mechanicDTO);
        return new ResponseEntity<>(mechanicDTO, HttpStatus.OK);
    }

    @GetMapping(value="/organization/{organizationId}")
    public ResponseEntity<Map<String,Object>> getAllByOrgId(@PathVariable(value = "organizationId") int organizationId,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<MechanicDto> resultPageWrapper = mechanicService.findByOrganizationId(organizationId, paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Override
    protected String getResource() {
        return "mechanics";
    }
}