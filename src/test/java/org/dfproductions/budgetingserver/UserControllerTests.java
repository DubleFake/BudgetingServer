package org.dfproductions.budgetingserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dfproductions.budgetingserver.backend.templates.requests.PasswordRequest;
import org.dfproductions.budgetingserver.backend.templates.requests.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    private static final String BASE_URL = "http://localhost:8080/api/user";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void loginEndpoint() throws Exception {

        PasswordRequest pr = new PasswordRequest("user", "user");

        this.mockMvc.perform(get(BASE_URL + "/login")
                        .content(new ObjectMapper().writeValueAsString(pr))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void registerEndpoint() throws Exception {

        UserRequest ur = new UserRequest();
        ur.setEmail("testuser");
        ur.setPasswordHash("xHU1Z9fYLUwhppJXE0Y6de6qyW2t31bJBAg0zqNQprU=");
        ur.setPasswordSalt("9769ea022edb5230b6f8623ce27758cd");
        ur.setName("testuser");

        MvcResult result = mockMvc.perform(post(BASE_URL + "/create")
                    .content(new ObjectMapper().writeValueAsString(ur))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("User created.");

    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteEndpointWithUser() throws Exception {

        this.mockMvc.perform(delete(BASE_URL + "/delete/testuser")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteEndpointWithAdmin() throws Exception {

        MvcResult result = mockMvc.perform(delete(BASE_URL + "/delete/testuser")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("Deleted.");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void createUserWithTakenEmail() throws Exception {

        UserRequest ur = new UserRequest();
        ur.setEmail("user");
        ur.setPasswordHash("xHU1Z9fYLUwhppJXE0Y6de6qyW2t31bJBAg0zqNQprU=");
        ur.setPasswordSalt("9769ea022edb5230b6f8623ce27758cd");
        ur.setName("user");

        MvcResult result = mockMvc.perform(post(BASE_URL + "/create")
                        .content(new ObjectMapper().writeValueAsString(ur))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("Email already taken.");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void createUserWithEmptyEmail() throws Exception {
        UserRequest ur = new UserRequest();
        ur.setEmail("");
        ur.setPasswordHash("xHU1Z9fYLUwhppJXE0Y6de6qyW2t31bJBAg0zqNQprU=");
        ur.setPasswordSalt("9769ea022edb5230b6f8623ce27758cd");
        ur.setName("user");

        MvcResult result = mockMvc.perform(post(BASE_URL + "/create")
                        .content(new ObjectMapper().writeValueAsString(ur))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void createUserWithEmptyPasswordHash() throws Exception {
        UserRequest ur = new UserRequest();
        ur.setEmail("testuser");
        ur.setPasswordHash("");
        ur.setPasswordSalt("9769ea022edb5230b6f8623ce27758cd");
        ur.setName("testuser");

        MvcResult result = mockMvc.perform(post(BASE_URL + "/create")
                        .content(new ObjectMapper().writeValueAsString(ur))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void createUserWithEmptyPasswordSalt() throws Exception {
        UserRequest ur = new UserRequest();
        ur.setEmail("testuser");
        ur.setPasswordHash("xHU1Z9fYLUwhppJXE0Y6de6qyW2t31bJBAg0zqNQprU=");
        ur.setPasswordSalt("");
        ur.setName("testuser");

        MvcResult result = mockMvc.perform(post(BASE_URL + "/create")
                        .content(new ObjectMapper().writeValueAsString(ur))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void createUserWithEmptyName() throws Exception {
        UserRequest ur = new UserRequest();
        ur.setEmail("testuser");
        ur.setPasswordHash("xHU1Z9fYLUwhppJXE0Y6de6qyW2t31bJBAg0zqNQprU=");
        ur.setPasswordSalt("9769ea022edb5230b6f8623ce27758cd");
        ur.setName("");

        MvcResult result = mockMvc.perform(post(BASE_URL + "/create")
                        .content(new ObjectMapper().writeValueAsString(ur))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void createUserWithAllEmptyFields() throws Exception {
        UserRequest ur = new UserRequest();
        ur.setEmail("");
        ur.setPasswordHash("");
        ur.setPasswordSalt("");
        ur.setName("");

        MvcResult result = mockMvc.perform(post(BASE_URL + "/create")
                        .content(new ObjectMapper().writeValueAsString(ur))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void validatePasswordOfExistingUser() throws Exception{
        PasswordRequest pr = new PasswordRequest("user","user");

        MvcResult result = mockMvc.perform(get(BASE_URL + "/login")
                        .content(new ObjectMapper().writeValueAsString(pr))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("Granted.");

    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void validateWrongPasswordOfExistingUser() throws Exception{
        PasswordRequest pr = new PasswordRequest("user","awdadwsauser");

        MvcResult result = mockMvc.perform(get(BASE_URL + "/login")
                        .content(new ObjectMapper().writeValueAsString(pr))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("Denied.");

    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void validatePasswordOfNonExistingUser() throws Exception{
        PasswordRequest pr = new PasswordRequest("thisuserdoesnotexistihope","passw0d");

        MvcResult result = mockMvc.perform(get(BASE_URL + "/login")
                        .content(new ObjectMapper().writeValueAsString(pr))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("Denied.");

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteNonExistingUserWithAdmin() throws Exception {

        MvcResult result = mockMvc.perform(delete(BASE_URL + "/delete/idontthinkthisuserexists")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

    }
}
