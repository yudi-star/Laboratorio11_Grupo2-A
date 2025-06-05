package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.OwnerDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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

        mockMvc.perform(get("/owners"))
                .andDo(print())

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].firstName", is("George")))
                .andExpect(jsonPath("$[0].lastName", is("Franklin")))
                .andExpect(jsonPath("$[1].firstName", is("Betty")))
                .andExpect(jsonPath("$[1].city", is("Sun Prairie")));
    }



    @Test
    public void testFindOwnerKO() throws Exception {
        int ID_NOT_FOUND = 666;
        mockMvc.perform(get("/owners/" + ID_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOwner() throws Exception {
        OwnerDTO newOwner = new OwnerDTO(null, "Yudith", "Pacco", " calle guirnaldas n18 santa anita", "Lima", "931840727");

        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/owners/" + id))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testUpdateOwnerAndFlow() throws Exception {
        OwnerDTO ownerToCreate = new OwnerDTO(null, "Bill", "Gates", "1835 73rd Ave NE", "Medina", "555123456");

        ResultActions createResult = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(ownerToCreate))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String responseBody = createResult.andReturn().getResponse().getContentAsString();
        Integer ownerId = JsonPath.parse(responseBody).read("$.id");

        String UPDATED_CITY = "Lima";
        String UPDATED_TELEPHONE = "999999999";

        OwnerDTO ownerToUpdate = new OwnerDTO(ownerId, "Bill", "Gates", "1835 73rd Ave NE", UPDATED_CITY, UPDATED_TELEPHONE);

        mockMvc.perform(put("/owners/" + ownerId)
                        .content(om.writeValueAsString(ownerToUpdate))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/owners/" + ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ownerId)))
                .andExpect(jsonPath("$.firstName", is("Bill")))
                .andExpect(jsonPath("$.city", is(UPDATED_CITY)))
                .andExpect(jsonPath("$.telephone", is(UPDATED_TELEPHONE)));
    }


}
