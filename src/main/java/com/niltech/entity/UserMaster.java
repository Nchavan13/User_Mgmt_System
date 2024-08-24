package com.niltech.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userid;
	
	@Column(length = 30)
	private String name;
	
	@Column(length = 30)
    private String password;
	
	@Column(length = 30,unique = true, nullable = false)
	private String email;
	
	private Long mobileNo;
	
	private String aadharNo;
	
	@Column(length = 30)
	private String gender;
	
	private LocalDate dob;
	
	private String activeSw;
	
	//metadata
	@Column(updatable = false,insertable = true)
	private LocalDateTime createdOn;
	
	@Column(updatable = true,insertable = false)
	private LocalDateTime updatedOn;
	
	@Column(length = 30)
	private String createdBy;
	
	@Column(length = 30)
	private String updatedBy;
	

}
