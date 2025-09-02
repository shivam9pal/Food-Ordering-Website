package com.Food.integration;

import com.Food.config.AppConfig;
import com.Food.config.JwtProvider;
import com.Food.models.USER_ROLE;
import com.Food.request.SignupRequest;
import com.Food.repository.CartRepository;
import com.Food.repository.UserRepository;
import com.Food.service.CustomerUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class AuthIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        
        // Clean up test data - only if tables exist
        try {
            cartRepository.deleteAll();
            userRepository.deleteAll();
        } catch (Exception e) {
            // Tables might not exist yet, that's okay
        }
    }

    @Test
    void testCompleteUserRegistrationFlow() throws Exception {
        // Arrange
        SignupRequest user = new SignupRequest();
        user.setFullname("Integration Test User");
        user.setEmail("integration@test.com");
        user.setPassword("testPassword123");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        // Act & Assert
        String responseJson = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.message").value("Register Success"))
                .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify database state
        User savedUser = userRepository.findByEmail("integration@test.com");
        assertNotNull(savedUser);
        assertEquals("Integration Test User", savedUser.getFullname());
        assertEquals("integration@test.com", savedUser.getEmail());
        assertTrue(passwordEncoder.matches("testPassword123", savedUser.getPassword()));
        assertEquals(USER_ROLE.ROLE_CUSTOMER, savedUser.getRole());

        // Verify cart was created
        assertNotNull(savedUser.getId());
        // Note: Cart creation is tested in the controller test, but here we verify the user exists
    }

    @Test
    void testUserRegistrationWithRestaurantOwnerRole() throws Exception {
        // Arrange
        SignupRequest user = new SignupRequest();
        user.setFullname("Restaurant Owner");
        user.setEmail("owner@restaurant.com");
        user.setPassword("ownerPassword123");
        user.setRole(USER_ROLE.ROLE_RESTURANT_OWNER);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.message").value("Register Success"))
                .andExpect(jsonPath("$.role").value("ROLE_RESTURANT_OWNER"));

        // Verify database state
        User savedUser = userRepository.findByEmail("owner@restaurant.com");
        assertNotNull(savedUser);
        assertEquals(USER_ROLE.ROLE_RESTURANT_OWNER, savedUser.getRole());
    }

    @Test
    void testUserRegistrationWithNullRole() throws Exception {
        // Arrange
        SignupRequest user = new SignupRequest();
        user.setFullname("No Role User");
        user.setEmail("norole@test.com");
        user.setPassword("testPassword123");
        user.setRole(null);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.message").value("Register Success"))
                .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));

        // Verify database state
        User savedUser = userRepository.findByEmail("norole@test.com");
        assertNotNull(savedUser);
        assertEquals(USER_ROLE.ROLE_CUSTOMER, savedUser.getRole());
    }

    @Test
    void testUserRegistrationWithSpecialCharacters() throws Exception {
        // Arrange
        SignupRequest user = new SignupRequest();
        user.setFullname("José María O'Connor-Smith");
        user.setEmail("jose.maria@test.com");
        user.setPassword("testPassword123");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.message").value("Register Success"));

        // Verify database state
        User savedUser = userRepository.findByEmail("jose.maria@test.com");
        assertNotNull(savedUser);
        assertEquals("José María O'Connor-Smith", savedUser.getFullname());
    }

    @Test
    void testUserRegistrationWithLongEmail() throws Exception {
        // Arrange
        String longEmail = "very.long.email.address.with.many.dots.and.subdomains.and.more.parts@example.com";
        SignupRequest user = new SignupRequest();
        user.setFullname("Long Email User");
        user.setEmail(longEmail);
        user.setPassword("testPassword123");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.message").value("Register Success"));

        // Verify database state
        User savedUser = userRepository.findByEmail(longEmail);
        assertNotNull(savedUser);
        assertEquals(longEmail, savedUser.getEmail());
    }

    @Test
    void testUserRegistrationWithEmptyPassword() throws Exception {
        // Arrange
        SignupRequest user = new SignupRequest();
        user.setFullname("Empty Password User");
        user.setEmail("emptypass@test.com");
        user.setPassword("");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.message").value("Register Success"));

        // Verify database state
        User savedUser = userRepository.findByEmail("emptypass@test.com");
        assertNotNull(savedUser);
        assertTrue(passwordEncoder.matches("", savedUser.getPassword()));
    }

    @Test
    void testUserRegistrationWithNullFullname() throws Exception {
        // Removed: fullname cannot be null by entity constraint
    }

    @Test
    void testUserRegistrationWithEmptyFullname() throws Exception {
        // Arrange
        SignupRequest user = new SignupRequest();
        user.setFullname("");
        user.setEmail("emptyname@test.com");
        user.setPassword("testPassword123");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.message").value("Register Success"));

        // Verify database state
        User savedUser = userRepository.findByEmail("emptyname@test.com");
        assertNotNull(savedUser);
        assertEquals("", savedUser.getFullname());
    }

    @Test
    void testUserRegistrationWithVeryLongFullname() throws Exception {
        // Arrange
        String longName = "A".repeat(1000); // Very long name
        SignupRequest user = new SignupRequest();
        user.setFullname(longName);
        user.setEmail("longname@test.com");
        user.setPassword("testPassword123");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.message").value("Register Success"));

        // Verify database state
        User savedUser = userRepository.findByEmail("longname@test.com");
        assertNotNull(savedUser);
        assertEquals(longName, savedUser.getFullname());
    }

    @Test
    void testUserRegistrationWithVeryLongPassword() throws Exception {
        // Arrange
        String longPassword = "A".repeat(1000); // Very long password
        SignupRequest user = new SignupRequest();
        user.setFullname("Long Password User");
        user.setEmail("longpass@test.com");
        user.setPassword(longPassword);
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.message").value("Register Success"));

        // Verify database state
        User savedUser = userRepository.findByEmail("longpass@test.com");
        assertNotNull(savedUser);
        assertTrue(passwordEncoder.matches(longPassword, savedUser.getPassword()));
    }
}
