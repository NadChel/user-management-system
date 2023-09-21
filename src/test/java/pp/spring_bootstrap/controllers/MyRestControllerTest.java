package pp.spring_bootstrap.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import pp.spring_bootstrap.models.Role;
import pp.spring_bootstrap.models.User;
import pp.spring_bootstrap.service.RoleService;
import pp.spring_bootstrap.service.UserService;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@MockBean(RoleService.class)
class MyRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;
    @MockBean
    PasswordEncoder passwordEncoder;
    User mickey;

    @BeforeEach
    void setUp() {
        mickey = new User();
        mickey.setUsername("mickey");
        mickey.setPassword("password");
        mickey.setName("Mickey");
        mickey.setLastName("Mouse");
        mickey.setDepartment("IT");
        mickey.setSalary(100_000);
        mickey.setEmail("mickey@gmail.com");
        mickey.setAge((byte) 100);
        mickey.setEnabledByte((byte) 1);
        mickey.setAuthorities(Set.of(new Role("USER")));

        when(passwordEncoder.encode(any())).thenReturn("password");
    }

    @Test
    void addUser() throws Exception {
        var response = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(mickey))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        User userFromResponse = objectMapper.readValue(response.getContentAsString(), User.class);
        assertThat(userFromResponse).isEqualTo(mickey);
    }

    @Test
    void enableUserByUsername() throws Exception {
        mickey.setEnabledByte((byte) 0);
        mockMvc.perform(patch("/users/" + mickey.getUsername())
                        .header("patch_type", "enable"))
                .andExpect(status().isOk());
        verify(userService, times(1)).enableUserByUsername(mickey.getUsername());
    }

    @Test
    void disableUserByUsername() throws Exception {
        mickey.setEnabledByte((byte) 1);
        mockMvc.perform(patch("/users/" + mickey.getUsername())
                        .header("patch_type", "disable"))
                .andExpect(status().isOk());
        verify(userService, times(1)).disableUserByUsername(mickey.getUsername());
    }

    @Test
    void updateUser() throws Exception {
        var response = mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(mickey))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("password_change", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        User userFromResponse = objectMapper.readValue(response.getContentAsString(), User.class);
        assertThat(userFromResponse).isEqualTo(mickey);
    }

    @Test
    void deleteUserByUsername() throws Exception {
        mockMvc.perform(delete("/users/" + mickey.getUsername()))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUserByUsername(mickey.getUsername());
    }
}