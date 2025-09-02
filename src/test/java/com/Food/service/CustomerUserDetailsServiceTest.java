package com.Food.service;

import com.Food.models.USER_ROLE;
import com.Food.models.User;
import com.Food.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerUserDetailsService customerUserDetailsService;

    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        User testUser = new User();
        testUser.setId(1L);
        testUser.setFullname("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(USER_ROLE.ROLE_CUSTOMER);

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        // Act
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());

        var authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_CUSTOMER", authorities.iterator().next().getAuthority());

        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> customerUserDetailsService.loadUserByUsername("nonexistent@example.com")
        );

        assertEquals("Username not found with this emailnonexistent@example.com", exception.getMessage());
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void testLoadUserByUsername_WithRestaurantOwnerRole() {
        // Arrange
        User testUser = new User();
        testUser.setId(1L);
        testUser.setFullname("Restaurant Owner");
        testUser.setEmail("owner@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(USER_ROLE.ROLE_RESTURANT_OWNER);

        when(userRepository.findByEmail("owner@example.com")).thenReturn(testUser);

        // Act
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername("owner@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("owner@example.com", userDetails.getUsername());

        var authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_RESTURANT_OWNER", authorities.iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsername_WithAdminRole() {
        // Arrange
        User testUser = new User();
        testUser.setId(1L);
        testUser.setFullname("Admin User");
        testUser.setEmail("admin@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(USER_ROLE.ROLE_ADMIN);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(testUser);

        // Act
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername("admin@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("admin@example.com", userDetails.getUsername());

        var authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsername_WithNullRole() {
        // Arrange
        User testUser = new User();
        testUser.setId(1L);
        testUser.setFullname("No Role User");
        testUser.setEmail("norole@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(null);

        when(userRepository.findByEmail("norole@example.com")).thenReturn(testUser);

        // Act
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername("norole@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("norole@example.com", userDetails.getUsername());

        var authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_CUSTOMER", authorities.iterator().next().getAuthority()); // Default role
    }

    @Test
    void testLoadUserByUsername_WithEmptyEmail() {
        // Arrange
        when(userRepository.findByEmail("")).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> customerUserDetailsService.loadUserByUsername("")
        );

        assertEquals("Username not found with this email", exception.getMessage());
        verify(userRepository).findByEmail("");
    }

    @Test
    void testLoadUserByUsername_WithNullEmail() {
        // Arrange
        when(userRepository.findByEmail(null)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> customerUserDetailsService.loadUserByUsername(null)
        );

        assertEquals("Username not found with this emailnull", exception.getMessage());
        verify(userRepository).findByEmail(null);
    }

    @Test
    void testLoadUserByUsername_VerifyRepositoryCall() {
        // Arrange
        User testUser = new User();
        testUser.setId(1L);
        testUser.setFullname("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(USER_ROLE.ROLE_CUSTOMER);

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        // Act
        customerUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verifyNoMoreInteractions(userRepository);
    }
}
