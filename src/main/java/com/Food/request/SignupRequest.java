package com.Food.request;

import com.Food.models.USER_ROLE;
import lombok.Data;

@Data
public class SignupRequest {
	private String fullname;
	private String email;
	private String password;
	private USER_ROLE role;
}


