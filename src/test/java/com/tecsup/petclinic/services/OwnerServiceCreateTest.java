package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Slf4j
@Transactional
public class OwnerServiceCreateTest {

    @Autowired
    private OwnerService ownerService;

    @Test
    public void testCreateOwnerSuccessfully() {
        String firstName = "Carlos";
        String lastName = "Sanchez";
        String address = "Av. Primavera 123";
        String city = "Lima";
        String telephone = "987654321";

        Owner newOwner = new Owner(firstName, lastName, address, city, telephone);
        Owner createdOwner = ownerService.create(newOwner);

        log.info("OWNER CREATED: " + createdOwner.toString());

        assertNotNull(createdOwner.getId(), "Owner ID should not be null after creation.");
        assertEquals(firstName, createdOwner.getFirstName());
        assertEquals(lastName, createdOwner.getLastName());
        assertEquals(address, createdOwner.getAddress());
        assertEquals(city, createdOwner.getCity());
        assertEquals(telephone, createdOwner.getTelephone());

        try {
            Owner foundOwner = ownerService.findById(createdOwner.getId());
            assertNotNull(foundOwner);
            assertEquals(firstName, foundOwner.getFirstName());
        } catch (Exception e) {
            fail("Failed to find the created owner: " + e.getMessage());
        }
    }

    @Test
    public void testCreateOwnerWithEmptyTelephone() {
        String firstName = "Ana";
        String lastName = "Gomez";
        String address = "Calle Sol 456";
        String city = "Arequipa";
        String telephone = "";

        Owner newOwner = new Owner(firstName, lastName, address, city, telephone);
        Owner createdOwner = ownerService.create(newOwner);

        log.info("OWNER CREATED (empty telephone): " + createdOwner.toString());

        assertNotNull(createdOwner.getId());
        assertEquals(firstName, createdOwner.getFirstName());
        assertEquals(telephone, createdOwner.getTelephone(), "Telephone should be empty.");
    }



}