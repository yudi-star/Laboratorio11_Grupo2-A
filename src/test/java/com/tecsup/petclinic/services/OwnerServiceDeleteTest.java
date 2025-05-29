package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;

import com.tecsup.petclinic.exception.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Slf4j
@Transactional
public class OwnerServiceDeleteTest {

    @Autowired
    private OwnerService ownerService;



    @Test
    public void testDeleteOwnerSuccessfully() {
        String firstName = "ToDelete";
        String lastName = "Successfully";
        Owner ownerToDelete = ownerService.create(new Owner(firstName, lastName, "1 Delete St", "DeleteCity", "555111222"));
        Integer ownerId = ownerToDelete.getId();
        assertNotNull(ownerId, "Owner ID should not be null after creation for delete test.");
        log.info("Owner created for successful deletion test: " + ownerToDelete);

        try {
            ownerService.delete(ownerId);
            log.info("Attempted to delete owner with ID: " + ownerId);
        } catch (OwnerNotFoundException e) {
            fail("OwnerNotFoundException was thrown unexpectedly during delete: " + e.getMessage());
        }

        final Integer finalOwnerId = ownerId;
        Exception exception = assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.findById(finalOwnerId);
        }, "OwnerNotFoundException should be thrown after deletion, as owner should not be found.");

        log.info("Verification after delete: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Owner not found with ID: " + finalOwnerId));
    }

    @Test
    public void testDeleteOwnerNotFound() {
        Integer nonExistentId = -101;
        log.info("Attempting to delete non-existent owner with ID: " + nonExistentId);

        Exception exception = assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.delete(nonExistentId);
        });

        log.info("Exception message when deleting non-existent owner: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Owner not found with ID: " + nonExistentId));
    }

}
