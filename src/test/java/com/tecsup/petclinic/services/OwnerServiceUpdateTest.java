
package com.tecsup.petclinic.services;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exception.OwnerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// Importaciones de Assertions de JUnit 5
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
@Transactional
public class OwnerServiceUpdateTest {

    @Autowired
    private OwnerService ownerService;
//vefica que que pueda actualizar exitosamente
    @Test
    public void testUpdateOwnerSuccessfully() {

        String initialFirstName = "OriginalName";
        String initialLastName = "OriginalSurname";
        Owner originalOwner = ownerService.create(new Owner(initialFirstName, initialLastName, "1 Original St", "OriginalCity", "000000000"));
        Integer ownerId = originalOwner.getId(); // Guardar el ID para la actualización
        assertNotNull(ownerId, "Owner ID should not be null after creation for update test.");
        log.info("Original Owner for update test: " + originalOwner);

        // 2. Preparar los nuevos datos para la actualización.

        String updatedFirstName = "UpdatedFirstName";
        String updatedAddress = "2 Updated Avenue";
        String updatedTelephone = "999888777";

        // Crear un objeto Owner con los datos de actualización.

        Owner ownerWithUpdates = new Owner();
        ownerWithUpdates.setId(ownerId); // Identifica qué owner actualizar.
        ownerWithUpdates.setFirstName(updatedFirstName);
        ownerWithUpdates.setLastName(initialLastName); // Mantiene el apellido original
        ownerWithUpdates.setAddress(updatedAddress);
        ownerWithUpdates.setCity(originalOwner.getCity()); // Mantiene la ciudad original
        ownerWithUpdates.setTelephone(updatedTelephone);

        log.info("Data for update: " + ownerWithUpdates);

        // 3. Ejecutar la operación de actualización.
        try {
            Owner updatedOwner = ownerService.update(ownerWithUpdates);
            log.info("Owner after update service call: " + updatedOwner);

            // 4. Verificar que los campos se actualizaron correctamente.
            assertNotNull(updatedOwner, "Updated owner should not be null.");
            assertEquals(ownerId, updatedOwner.getId(), "Owner ID should remain the same after update.");
            assertEquals(updatedFirstName, updatedOwner.getFirstName(), "First name should be updated.");
            assertEquals(initialLastName, updatedOwner.getLastName(), "Last name should NOT have changed.");
            assertEquals(updatedAddress, updatedOwner.getAddress(), "Address should be updated.");
            assertEquals(originalOwner.getCity(), updatedOwner.getCity(), "City should NOT have changed.");
            assertEquals(updatedTelephone, updatedOwner.getTelephone(), "Telephone should be updated.");

            // 5. Opcional: Verificar recuperando el owner de la BBDD nuevamente.
            Owner fetchedAfterUpdate = ownerService.findById(ownerId);
            assertNotNull(fetchedAfterUpdate);
            assertEquals(updatedFirstName, fetchedAfterUpdate.getFirstName(), "Fetched first name after update is incorrect.");
            assertEquals(updatedAddress, fetchedAfterUpdate.getAddress(), "Fetched address after update is incorrect.");

        } catch (OwnerNotFoundException e) {
            fail("OwnerNotFoundException was thrown unexpectedly during update: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateOwnerNotFound() {
        Integer nonExistentId = -100; // Un ID que seguramente no existe.

        // Crea un objeto Owner con datos de intento de actualización
        Owner ownerToUpdate = new Owner();
        ownerToUpdate.setId(nonExistentId);
        ownerToUpdate.setFirstName("AttemptUpdateFirst");
        ownerToUpdate.setLastName("AttemptUpdateLast");
        ownerToUpdate.setAddress("123 Nowhere St");
        ownerToUpdate.setCity("NoCity");
        ownerToUpdate.setTelephone("000000");

        log.info("Attempting to update non-existent owner with ID: " + nonExistentId);

        // Verificar que se lanza OwnerNotFoundException

        Exception exception = assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.update(ownerToUpdate);
        });

        log.info("Exception message: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Owner not found with ID: " + nonExistentId));
    }

    @Test
    public void testUpdateOwnerSetFieldToNull() {
        // 1. Crear owner
        Owner owner = ownerService.create(new Owner("TestName", "TestLastName", "Test Address", "Test City", "123456789"));
        Integer ownerId = owner.getId();
        assertNotNull(ownerId);

        // 2. Preparar actualización para poner un campo (que lo permita) a null

        Owner ownerWithNullUpdate = new Owner();
        ownerWithNullUpdate.setId(ownerId);
        ownerWithNullUpdate.setFirstName(owner.getFirstName());
        ownerWithNullUpdate.setLastName(owner.getLastName());
        ownerWithNullUpdate.setAddress(null); // Intentar poner la dirección a null
        ownerWithNullUpdate.setCity(owner.getCity());
        ownerWithNullUpdate.setTelephone(owner.getTelephone());

        // 3. Ejecutar actualización
        try {
            Owner updatedOwner = ownerService.update(ownerWithNullUpdate);
            assertNull(updatedOwner.getAddress(), "Address should be null after update.");

            // Verificar en BBDD
            Owner fetchedOwner = ownerService.findById(ownerId);
            assertNull(fetchedOwner.getAddress(), "Fetched address should be null.");

        } catch (OwnerNotFoundException e) {
            fail("Owner not found during update to null field test: " + e.getMessage());
        }
        // Nota: Si el campo en la BBDD es NOT NULL, esta prueba podría fallar con una DataIntegrityViolationException
        // o similar, lo cual también sería un resultado válido a probar si esa es la restricción.
    }
}