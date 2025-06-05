package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exception.OwnerNotFoundException;
import com.tecsup.petclinic.mapper.OwnerMapper;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OwnerController {

    private final OwnerService ownerService;
    private final OwnerMapper ownerMapper;

    public OwnerController(OwnerService ownerService, OwnerMapper ownerMapper) {
        this.ownerService = ownerService;
        this.ownerMapper = ownerMapper;
    }

    @GetMapping(value = "/owners")
    public ResponseEntity<List<OwnerDTO>> findAllOwners() {
        List<Owner> owners = ownerService.findAll();
        List<OwnerDTO> ownerDTOs = this.ownerMapper.toOwnerDTOList(owners);
        log.info("Found {} owners", ownerDTOs.size());
        return ResponseEntity.ok(ownerDTOs);
    }

    @PostMapping(value = "/owners")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OwnerDTO> create(@RequestBody OwnerDTO ownerDTO) {
        Owner newOwner = this.ownerMapper.toOwner(ownerDTO);
        Owner createdOwner = this.ownerService.create(newOwner);
        OwnerDTO newOwnerDTO = this.ownerMapper.toOwnerDTO(createdOwner);
        log.info("Created owner with ID: {}", newOwnerDTO.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newOwnerDTO);
    }

    @GetMapping(value = "/owners/{id}")
    public ResponseEntity<OwnerDTO> findById(@PathVariable Integer id) {
        try {
            Owner owner = this.ownerService.findById(id);
            OwnerDTO ownerDTO = this.ownerMapper.toOwnerDTO(owner);
            log.info("Found owner with ID: {}", id);
            return ResponseEntity.ok(ownerDTO);
        } catch (OwnerNotFoundException e) {
            log.warn("Owner not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/owners/{id}")
    public ResponseEntity<OwnerDTO> update(@RequestBody OwnerDTO ownerDTO, @PathVariable Integer id) {
        try {
            ownerDTO.setId(id);
            Owner ownerToUpdate = this.ownerMapper.toOwner(ownerDTO);
            Owner updatedOwner = this.ownerService.update(ownerToUpdate);
            OwnerDTO updatedOwnerDTO = this.ownerMapper.toOwnerDTO(updatedOwner);
            log.info("Updated owner with ID: {}", id);
            return ResponseEntity.ok(updatedOwnerDTO);
        } catch (OwnerNotFoundException e) {
            log.warn("Owner not found for update with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/owners/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            this.ownerService.delete(id);
            log.info("Deleted owner with ID: {}", id);
            return ResponseEntity.ok("Deleted owner with ID: " + id);
        } catch (OwnerNotFoundException e) {
            log.warn("Owner not found for deletion with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}