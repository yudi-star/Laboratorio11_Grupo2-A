package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exception.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import static org.junit.jupiter.api.Assertions.assertFalse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Slf4j
@Transactional
public class OwnerServiceReadTest {

    @Autowired
    private OwnerService ownerService;

    private Integer owner1Id;
    private Integer owner2Id;
    private Integer ownerSameLastNameId;

    // DATOS PARA LAS PRUEBAS
    private final String OWNER1_FIRST_NAME = "Jorge";
    private final String OWNER1_LAST_NAME = "Perez";
    private final String OWNER1_CITY = "Miraflores";

    private final String OWNER2_FIRST_NAME = "Lucia";
    private final String OWNER2_LAST_NAME = "Chavez";
    private final String OWNER2_CITY = "San Isidro";
    private final String OWNER_SAME_LAST_NAME_FIRST_NAME = "Martin";
    private final String OWNER_SAME_LAST_NAME_CITY = "Surco";


    @BeforeEach
        // EL METODO, SE EJECUTA ANTES DE CADA METODO TEST EN ESTA CLASE
    void setUpTestData() {

        Owner owner1 = ownerService.create(new Owner(OWNER1_FIRST_NAME, OWNER1_LAST_NAME, "Calle Falsa 123", OWNER1_CITY, "111111111"));
        Owner owner2 = ownerService.create(new Owner(OWNER2_FIRST_NAME, OWNER2_LAST_NAME, "Av. Real 456", OWNER2_CITY, "222222222"));
        Owner ownerSameLastName = ownerService.create(new Owner(OWNER_SAME_LAST_NAME_FIRST_NAME, OWNER1_LAST_NAME, "Jr. Luna 789", OWNER_SAME_LAST_NAME_CITY, "333333333"));

        this.owner1Id = owner1.getId();
        this.owner2Id = owner2.getId();
        this.ownerSameLastNameId = ownerSameLastName.getId();

        log.info("SETUP: Created owner1 ID: {}, owner2 ID: {}, ownerSameLastName ID: {}", owner1Id, owner2Id, ownerSameLastNameId);
    }

    //PRUEBAS
    @Test
    public void testFindOwnerByIdSuccessfully() {
        Integer idToFind = owner1Id;
        log.info("Attempting to find owner by ID: " + idToFind);

        try {
            Owner foundOwner = ownerService.findById(idToFind);
            assertNotNull(foundOwner, "Owner should not be null.");
            assertEquals(OWNER1_FIRST_NAME, foundOwner.getFirstName(), "First name does not match.");
            assertEquals(OWNER1_LAST_NAME, foundOwner.getLastName(), "Last name does not match.");
            assertEquals(OWNER1_CITY, foundOwner.getCity(), "City does not match.");
            log.info("Found Owner: " + foundOwner.toString());
        } catch (OwnerNotFoundException e) {
            fail("OwnerNotFoundException was thrown when not expected: " + e.getMessage());
        }
    }

    @Test
    public void testFindOwnerByIdNotFound() {
        Integer nonExistentId = -99;
        log.info("Attempting to find owner by non-existent ID: " + nonExistentId);

        Exception exception = assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.findById(nonExistentId);
        }, "OwnerNotFoundException should be thrown for non-existent ID.");

        log.info("Exception message: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Owner not found with ID: " + nonExistentId));
    }

    @Test
    public void testFindAllOwners() {
        log.info("Attempting to find all owners.");
        List<Owner> owners = ownerService.findAll();
        assertNotNull(owners, "List of owners should not be null.");
        assertEquals(3, owners.size(), "Should find exactly 3 owners created in setUp.");
        log.info("Found " + owners.size() + " owners.");

        assertTrue(owners.stream().anyMatch(o -> o.getId().equals(owner1Id) && o.getFirstName().equals(OWNER1_FIRST_NAME)));
        assertTrue(owners.stream().anyMatch(o -> o.getId().equals(owner2Id) && o.getFirstName().equals(OWNER2_FIRST_NAME)));
        assertTrue(owners.stream().anyMatch(o -> o.getId().equals(ownerSameLastNameId) && o.getFirstName().equals(OWNER_SAME_LAST_NAME_FIRST_NAME)));
    }


}