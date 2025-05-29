package com.tecsup.petclinic.services; // O el paquete de pruebas que usen

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.services.OwnerService; // Asegúrate de tener la interfaz y la implementación
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
@Transactional // <--- Recomendado para aislar las pruebas
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

        // Opcional: Verificar directamente con findById para asegurar que está en la BBDD
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
        // Suponiendo que el teléfono puede ser vacío pero no nulo según la BBDD (VARCHAR)
        String firstName = "Ana";
        String lastName = "Gomez";
        String address = "Calle Sol 456";
        String city = "Arequipa";
        String telephone = ""; // Teléfono vacío

        Owner newOwner = new Owner(firstName, lastName, address, city, telephone);
        Owner createdOwner = ownerService.create(newOwner);

        log.info("OWNER CREATED (empty telephone): " + createdOwner.toString());

        assertNotNull(createdOwner.getId());
        assertEquals(firstName, createdOwner.getFirstName());
        assertEquals(telephone, createdOwner.getTelephone(), "Telephone should be empty.");
    }

    // Podrías añadir más casos:
    // - Crear Owner con datos nulos donde la BBDD lo permita (si aplica).
    // - (Más avanzado) Si tuvieras validaciones a nivel de servicio (antes de la BBDD)
    //   para campos obligatorios no cubiertos por `NOT NULL` en BBDD, probarías
    //   que se lancen las excepciones correspondientes.
}