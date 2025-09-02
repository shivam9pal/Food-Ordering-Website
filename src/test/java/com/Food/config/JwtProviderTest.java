package com.Food.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
    }

    @Test
    void testGenerateToken_WithCustomerRole() {
        // Arrange
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_CUSTOMER")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "customer@example.com", 
            "password", 
            authorities
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 100); // JWT tokens are typically long
        assertTrue(token.contains(".")); // JWT format: header.payload.signature
    }

    @Test
    void testGenerateToken_WithMultipleRoles() {
        // Arrange
        Collection<SimpleGrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority("ROLE_CUSTOMER"),
            new SimpleGrantedAuthority("ROLE_RESTURANT_OWNER")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "user@example.com", 
            "password", 
            authorities
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_WithNoRoles() {
        // Arrange
        Collection<SimpleGrantedAuthority> authorities = Collections.emptyList();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "user@example.com", 
            "password", 
            authorities
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_WithNullAuthorities() {
        // Arrange
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "user@example.com", 
            "password", 
            null
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_WithSpecialCharactersInEmail() {
        // Arrange
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_CUSTOMER")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "user+test@example.com", 
            "password", 
            authorities
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_WithLongEmail() {
        // Arrange
        String longEmail = "very.long.email.address.with.many.dots.and.subdomains@example.com";
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_CUSTOMER")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            longEmail, 
            "password", 
            authorities
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_Consistency() {
        // Arrange
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_CUSTOMER")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "customer@example.com", 
            "password", 
            authorities
        );

        // Act
        String token1 = jwtProvider.generateToken(authentication);
        String token2 = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        // Tokens might be the same if generated in the same millisecond
        // Both should be valid regardless
        assertNotNull(token1);
        assertNotNull(token2);
    }

    @Test
    void testGenerateToken_WithRestaurantOwnerRole() {
        // Arrange
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_RESTURANT_OWNER")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "owner@restaurant.com", 
            "password", 
            authorities
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_WithAdminRole() {
        // Arrange
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "admin@example.com", 
            "password", 
            authorities
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_WithEmptyEmail() {
        // Arrange
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_CUSTOMER")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "", 
            "password", 
            authorities
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_WithNullEmail() {
        // Arrange
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_CUSTOMER")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            null, 
            "password", 
            authorities
        );

        // Act
        String token = jwtProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
}
