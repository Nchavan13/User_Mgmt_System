package com.niltech.bindings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateUser {
	
	private String email;
	private String tempPassword;
	private String newpassword;
	private String confirmPassword;

}
