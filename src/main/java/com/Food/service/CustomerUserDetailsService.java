package com.Food.service;

import com.Food.models.USER_ROLE;
import com.Food.models.User;
import com.Food.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("Username not found with this email"+username);
        }
        USER_ROLE role=user.getRole();
        if(role==null){
            role=USER_ROLE.ROLE_CUSTOMER;
        }
        return null;
    }
}
