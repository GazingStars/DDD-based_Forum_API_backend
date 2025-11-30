package com.example.forum.security;

import com.example.forum.TestController;
import com.example.forum.infrastructure.security.JwtAuthenticationFilter;
import com.example.forum.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestController.class)
class JwtFilterNoTokenTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .addFilter(jwtFilter)
                .build();
    }

    @Test
    void requestWithoutToken_shouldPassThroughFilter() throws Exception {
        mockMvc.perform(get("/api/protected"))
                .andExpect(status().isOk());
    }
}
