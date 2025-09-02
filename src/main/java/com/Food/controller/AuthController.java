package com.Food.controller;

import com.Food.config.JwtProvider;
import com.Food.models.Cart;
import com.Food.models.USER_ROLE;
import com.Food.models.User;
import com.Food.repository.CartRepository;
import com.Food.repository.UserRepository;
import com.Food.request.LoginRequest;
import com.Food.request.SignupRequest;
import com.Food.response.AuthResponse;
import com.Food.service.CustomerUserDetailsService;
import com.Food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest req) throws Exception {
        User toCreate = new User();
        toCreate.setFullname(req.getFullname());
        toCreate.setEmail(req.getEmail());
        toCreate.setPassword(req.getPassword());
        toCreate.setRole(req.getRole() != null ? req.getRole() : USER_ROLE.ROLE_CUSTOMER);

        // create user (validates and encodes password)
        User created = userService.createUser(toCreate);

        // create cart for the user
        Cart cart = new Cart();
        cart.setCustomer(created);
        cartRepository.save(cart);

        // authenticate context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                created.getEmail(),
                null,
                java.util.List.of(() -> created.getRole().name())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // issue JWT
        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register Success");
        authResponse.setRole(created.getRole());
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req) {
        Authentication authentication = authenticate(req.getEmail(), req.getPassword());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login Success");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Username");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}