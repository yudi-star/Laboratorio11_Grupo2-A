package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exception.OwnerNotFoundException;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;


    @Autowired
    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Owner create(Owner owner) {
        log.info("Creating new owner: {}", owner);

        return ownerRepository.save(owner);
    }

    @Override
    public Owner update(Owner owner) throws OwnerNotFoundException {
        log.info("Attempting to update owner with ID: {}", owner.getId());

        Owner existingOwner = ownerRepository.findById(owner.getId())
                .orElseThrow(() -> {
                    log.warn("Owner not found for update with ID: {}", owner.getId());
                    return new OwnerNotFoundException("Owner not found with ID: " + owner.getId() + " for update.");
                });


        log.info("Updating owner: {}", owner);
        return ownerRepository.save(owner);
    }

    @Override
    public void delete(Integer id) throws OwnerNotFoundException {
        log.info("Attempting to delete owner with ID: {}", id);
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Owner not found for deletion with ID: {}", id);
                    return new OwnerNotFoundException("Owner not found with ID: " + id + " for deletion.");
                });


        ownerRepository.delete(owner);
        log.info("Successfully deleted owner with ID: {}", id);
    }

    @Override
    public Owner findById(Integer id) throws OwnerNotFoundException {
        log.info("Finding owner by ID: {}", id);
        Optional<Owner> ownerOpt = ownerRepository.findById(id);
        if (!ownerOpt.isPresent()) {
            log.warn("Owner not found with ID: {}", id);
            throw new OwnerNotFoundException("Owner not found with ID: " + id);
        }
        return ownerOpt.get();
    }

    @Override
    public List<Owner> findAll() {
        log.info("Finding all owners");
        return ownerRepository.findAll();
    }

    @Override
    public List<Owner> findByLastName(String lastName) {
        log.info("Finding owners by last name: {}", lastName);
        List<Owner> owners = ownerRepository.findByLastName(lastName);
        owners.forEach(owner -> log.debug("Found owner: {}", owner));
        return owners;
    }

    @Override
    public List<Owner> findByCity(String city) {
        log.info("Finding owners by city: {}", city);
        List<Owner> owners = ownerRepository.findByCity(city);
        owners.forEach(owner -> log.debug("Found owner: {}", owner));
        return owners;
    }

    @Override
    public List<Owner> findByFirstName(String firstName) {
        log.info("Finding owners by first name: {}", firstName);
        List<Owner> owners = ownerRepository.findByFirstName(firstName);
        owners.forEach(owner -> log.debug("Found owner: {}", owner));
        return owners;
    }

    @Override
    public List<Owner> findByTelephone(String telephone) {
        log.info("Finding owners by telephone: {}", telephone);
        List<Owner> owners = ownerRepository.findByTelephone(telephone);
        owners.forEach(owner -> log.debug("Found owner: {}", owner));
        return owners;
    }
}
