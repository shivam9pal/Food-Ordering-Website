package com.Food.controller;

import com.Food.config.JwtProvider;
import com.Food.models.Cart;
import com.Food.models.USER_ROLE;
import com.Food.models.User;
import com.Food.repository.CartRepository;
import com.Food.repository.UserRepository;
import com.Food.response.AuthResponse;
import com.Food.service.CustomerUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private CustomerUserDetailsService customerUserDetailsService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private String payload(String fullname, String email, String password, USER_ROLE role) throws Exception {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        if (fullname != null) map.put("fullname", fullname);
        if (email != null) map.put("email", email);
        if (password != null) map.put("password", password);
        if (role != null) map.put("role", role.name());
        return objectMapper.writeValueAsString(map);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
    }

    @Test
    void testSuccessfulUserRegistration() throws Exception {
        // Arrange
        User user = new User();
        user.setFullname("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        User savedUser = new User();
//        savedUser.setId(1L);
        savedUser.setFullname("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(USER_ROLE.ROLE_CUSTOMER);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            savedUser.getEmail(), 
            user.getPassword(), 
            Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()))
        );

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(customerUserDetailsService.loadUserByUsername(anyString())).thenReturn(
            new org.springframework.security.core.userdetails.User(
                savedUser.getEmail(), 
                savedUser.getPassword(), 
                Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()))
            )
        );
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwt_token_123");

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("John Doe", "john@example.com", "password123", USER_ROLE.ROLE_CUSTOMER)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").value("jwt_token_123"))
                .andExpect(jsonPath("$.message").value("Register Success"))
                .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));

        // Verify interactions
        verify(userRepository).findByEmail("john@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(cartRepository).save(any(Cart.class));
        verify(jwtProvider).generateToken(any(Authentication.class));
    }

    @Test
    void testUserRegistrationWithDuplicateEmail() throws Exception {
        // Arrange
        User user = new User();
        user.setFullname("John Doe");
        user.setEmail("existing@example.com");
        user.setPassword("password123");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(existingUser);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("John Doe", "existing@example.com", "password123", USER_ROLE.ROLE_CUSTOMER)))
                .andExpect(status().isInternalServerError());

        // Verify no user was saved
        verify(userRepository, never()).save(any(User.class));
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testUserRegistrationWithNullEmail() throws Exception {
        // Arrange
        User user = new User();
        user.setFullname("John Doe");
        user.setEmail(null);
        user.setPassword("password123");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("John Doe", null, "password123", USER_ROLE.ROLE_CUSTOMER)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUserRegistrationWithEmptyPassword() throws Exception {
        // Arrange
        User user = new User();
        user.setFullname("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("John Doe", "john@example.com", "", USER_ROLE.ROLE_CUSTOMER)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUserRegistrationWithRestaurantOwnerRole() throws Exception {
        // Arrange
        User user = new User();
        user.setFullname("Restaurant Owner");
        user.setEmail("owner@restaurant.com");
        user.setPassword("password123");
        user.setRole(USER_ROLE.ROLE_RESTURANT_OWNER);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullname("Restaurant Owner");
        savedUser.setEmail("owner@restaurant.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(USER_ROLE.ROLE_RESTURANT_OWNER);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            savedUser.getEmail(), 
            user.getPassword(), 
            Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE.ROLE_RESTURANT_OWNER.toString()))
        );

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(customerUserDetailsService.loadUserByUsername(anyString())).thenReturn(
            new org.springframework.security.core.userdetails.User(
                savedUser.getEmail(), 
                savedUser.getPassword(), 
                Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE.ROLE_RESTURANT_OWNER.toString()))
            )
        );
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwt_token_restaurant");

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("Restaurant Owner", "owner@restaurant.com", "password123", USER_ROLE.ROLE_RESTURANT_OWNER)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").value("jwt_token_restaurant"))
                .andExpect(jsonPath("$.message").value("Register Success"))
                .andExpect(jsonPath("$.role").value("ROLE_RESTURANT_OWNER"));
    }

    @Test
    void testUserRegistrationWithNullRole() throws Exception {
        // Arrange
        User user = new User();
        user.setFullname("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setRole(null);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullname("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(null);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(savedUser);

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(customerUserDetailsService.loadUserByUsername(anyString())).thenReturn(
            new org.springframework.security.core.userdetails.User(
                savedUser.getEmail(), 
                savedUser.getPassword(), 
                Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()))
            )
        );
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwt_token_123");

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("John Doe", "john@example.com", "password123", null)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUserRegistrationWithSpecialCharactersInName() throws Exception {
        // Arrange
        User user = new User();
        user.setFullname("José María O'Connor-Smith");
        user.setEmail("jose@example.com");
        user.setPassword("password123");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullname("José María O'Connor-Smith");
        savedUser.setEmail("jose@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(USER_ROLE.ROLE_CUSTOMER);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            savedUser.getEmail(), 
            user.getPassword(), 
            Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()))
        );

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(customerUserDetailsService.loadUserByUsername(anyString())).thenReturn(
            new org.springframework.security.core.userdetails.User(
                savedUser.getEmail(), 
                savedUser.getPassword(), 
                Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()))
            )
        );
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwt_token_special");

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("José María O'Connor-Smith", "jose@example.com", "password123", USER_ROLE.ROLE_CUSTOMER)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullname").doesNotExist()) // Response doesn't include fullname
                .andExpect(jsonPath("$.jwt").value("jwt_token_special"));
    }
}
