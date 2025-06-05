package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.OwnerDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
public class OwnerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateOwner() throws Exception {
        // Datos del nuevo owner
        String FIRST_NAME = "Elon";
        String LAST_NAME = "Musk";
        String ADDRESS = "1 Rocket Road";
        String CITY = "Hawthorne";
        String TELEPHONE = "1234567890";

        OwnerDTO newOwner = new OwnerDTO(null, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);

        mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists()) // Verificar que se asignó un ID
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.city", is(CITY)));

    }


    @Test
    public void testFindAllOwners() throws Exception {
        OwnerDTO owner1 = new OwnerDTO(null, "Steve", "Jobs", "1 Infinite Loop", "Cupertino", "111222333");

        mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(owner1))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        OwnerDTO owner2 = new OwnerDTO(null, "Mark", "Zuckerberg", "1 Hacker Way", "Menlo Park", "444555666");

        mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(owner2))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/owners"))
                .andDo(print())

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].firstName", is("Steve")))
                .andExpect(jsonPath("$[0].lastName", is("Jobs")))
                .andExpect(jsonPath("$[1].firstName", is("Mark")))
                .andExpect(jsonPath("$[1].city", is("Menlo Park")));
    }



    @Test
    public void testFindOwnerKO() throws Exception {
        int ID_NOT_FOUND = 666;
        mockMvc.perform(get("/owners/" + ID_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

}
