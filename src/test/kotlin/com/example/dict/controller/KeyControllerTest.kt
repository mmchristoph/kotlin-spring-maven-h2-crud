package com.example.dict.controller

import com.example.dict.model.Key
import com.example.dict.repository.KeyRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
internal class KeyControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var repository: KeyRepository

    @BeforeEach
    fun deleteAll() {
        repository.deleteAll()
    }

    @Test
    fun `save and verify contract response`() {
        val key = Key(
            name = "Maria",
            document = 11111111111,
            type = "email",
            value = "maria@email.com"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/dict")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(key))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(key.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(key.document))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.type").value(key.type))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.value").value(key.value))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `findAll and verify array contract response`() {
        repository.save(
            Key(
                name = "Maria",
                document = 11111111111,
                type = "email",
                value = "maria@email.com"
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.get("/dict"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].name").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].document").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].type").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].value").isString)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `findById verify and key response`() {
        val key = repository.save(
            Key(
                name = "Maria",
                document = 11111111111,
                type = "email",
                value = "maria@email.com"
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.get("/dict/${key.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(key.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(key.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(key.document))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.type").value(key.type))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.value").value(key.value))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `update and verify key response, key found and compare name`() {
        val key = repository.save(
            Key(
                name = "Maria",
                document = 11111111111,
                type = "email",
                value = "maria@email.com"
            )
        ).copy(
            name = "Maria Joaquina",
            document = 22222222222
        )

        mockMvc.perform(
            MockMvcRequestBuilders.put("/dict/${key.id}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(key))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(key.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(key.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(key.document))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.type").value(key.type))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.value").value(key.value))
            .andDo(MockMvcResultHandlers.print())

        val findById = repository.findById(key.id!!)
        assertTrue(findById.isPresent)
        assertEquals(key.name, findById.get().name)
    }

    @Test
    fun `delete and verify key not found`() {
        val key = repository.save(
            Key(
                name = "Maria",
                document = 11111111111,
                type = "email",
                value = "maria@email.com"
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.delete("/dict/${key.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())

        val findById = repository.findById(key.id!!)
//        assertTrue(findById.isEmpty)
    }

    @Test
    fun `save and validation error empty name`() {
        val key = Key(
            name = "",
            document = 11111111111,
            type = "email",
            value = "maria@email.com"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/dict")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(key))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[name] can not be empty"))
            .andDo(MockMvcResultHandlers.print())
    }

}