package com.example.forum.security;

import com.example.forum.domain.model.user.Role;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import com.example.forum.infrastructure.security.JwtAuthenticationFilter;
import com.example.forum.infrastructure.security.JwtProvider;
import com.example.forum.infrastructure.security.SecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestSecurityController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = true)
class SecurityConfigTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    JwtAuthenticationFilter jwtFilter;

    @MockitoBean
    JwtProvider jwtProvider;

    @BeforeEach
    void setupJwtMock() throws Exception {
        doAnswer(invocation -> {
            HttpServletRequest req = invocation.getArgument(0);
            HttpServletResponse res = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);

            chain.doFilter(req, res);
            return null;
        }).when(jwtFilter).doFilter(any(), any(), any());
    }

    private TestingAuthenticationToken auth(Role role) {
        return new TestingAuthenticationToken(
                new AuthUserPrincipal("u1", role),
                null,
                "ROLE_" + role.name()
        );
    }

    @Test
    void moderatorCannotAccessAdmin() throws Exception {
        mvc.perform(get("/api/admin/secured")
                        .with(authentication(auth(Role.MODERATOR))))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessAdmin() throws Exception {
        mvc.perform(get("/api/admin/secured")
                        .with(authentication(auth(Role.ADMIN))))
                .andExpect(status().isOk());
    }

    @Test
    void userCannotAccessMod() throws Exception {
        mvc.perform(get("/api/mod/secured")
                        .with(authentication(auth(Role.USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void moderatorCanAccessMod() throws Exception {
        mvc.perform(get("/api/mod/secured")
                        .with(authentication(auth(Role.MODERATOR))))
                .andExpect(status().isOk());
    }

    @Test
    void openIsPublic() throws Exception {
        mvc.perform(get("/api/open"))
                .andExpect(status().isOk());
    }
}
