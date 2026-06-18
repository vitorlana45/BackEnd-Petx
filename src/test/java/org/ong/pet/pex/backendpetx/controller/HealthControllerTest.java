package org.ong.pet.pex.backendpetx.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Smoke test de web (MockMvc): garante que o endpoint público de health
 * responde 200. Estabelece o padrão de teste de controller via MockMvc.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void statusApi_retorna200() throws Exception {
        mockMvc.perform(get("/health/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Api is running"));
    }
}
