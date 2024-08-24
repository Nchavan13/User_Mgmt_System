package com.niltech.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.niltech.bindings.ActivateUser;
import com.niltech.bindings.LoginCredentials;
import com.niltech.bindings.RecoverPassword;
import com.niltech.bindings.UserAccount;
import com.niltech.service.IUserMgmtService;

@RestController
@RequestMapping("/user-api")
public class UserMgmtController {
	
	private final IUserMgmtService service;
	
	public UserMgmtController(IUserMgmtService service) {
		this.service=service;
	}
	
	@PostMapping("/save")
	public ResponseEntity<Object> saveUser(@RequestBody UserAccount account) {
		// use service
		try {
			String resultMsg = service.registerUser(account);
			return new ResponseEntity<>(resultMsg, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/activate")
	public ResponseEntity<Object> activateuser(@RequestBody ActivateUser user) {
		// us service
		try {
			String resultMsg = service.activateUserAccount(user);
			return new ResponseEntity<>(resultMsg, HttpStatus.CREATED);

		} catch (Exception e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginCredentials credentials) {
		try {
			// use service
			String resultMsg = service.login(credentials);
			return new ResponseEntity<>(resultMsg, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/showAll")
	public ResponseEntity<Object> showUser() {
	    try {
	        List<UserAccount> list = service.listUser();
	        return new ResponseEntity<>(list, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	@GetMapping("/find/{id}")
	public ResponseEntity<Object> showUserById(@PathVariable Integer id) {
		try {
			UserAccount account = service.showUserById(id);
			return new ResponseEntity<>(account, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping("/find/{email}/{name}")
	public ResponseEntity<Object> showUserByEmailAndName(@PathVariable String email, @PathVariable String name) {
		try {
			UserAccount account = service.showUserByEmailAndName(email, name);
			return new ResponseEntity<>(account, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PutMapping("/update")
	public ResponseEntity<Object> updateUserDetails(@RequestBody UserAccount account) {
		try {
			String resultMsg = service.updateUser(account);
			return new ResponseEntity<>(resultMsg, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteUserByid(@PathVariable Integer id) {

		try {
			String resultMsg = service.deleteUserById(id);
			return new ResponseEntity<>(resultMsg, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PatchMapping("/change/{id}/{status}")
	public ResponseEntity<Object> changeUserStatus(@PathVariable Integer id, @PathVariable String status) {
		try {
			String resultMsg = service.changeUserStatus(id, status);
			return new ResponseEntity<>(resultMsg, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/reset")
	public ResponseEntity<Object> resetPassword(@RequestBody RecoverPassword password) {
		try {
			String resultMsg = service.recoverPassword(password);
			return new ResponseEntity<>(resultMsg, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
}
