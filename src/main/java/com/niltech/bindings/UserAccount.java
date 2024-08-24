package com.niltech.bindings;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
	
	private Integer userid;
	private String name;
	private String email;
	private long mobileNo;
	private String gender="Female";
	private LocalDate dob=LocalDate.now();
	private String aadharNo;

}
