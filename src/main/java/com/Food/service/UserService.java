package com.Food.service;

import com.Food.models.User;
import com.Food.request.SignupRequest;

public interface UserService {
	public User createUser(User user);
    public User findUserByJwtToken(String jwt) throws Exception;

    public User findUserByEmail(String email) throws Exception;
}
