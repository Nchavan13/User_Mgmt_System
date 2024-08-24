package com.niltech.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.niltech.bindings.ActivateUser;
import com.niltech.bindings.LoginCredentials;
import com.niltech.bindings.RecoverPassword;
import com.niltech.bindings.UserAccount;
import com.niltech.entity.UserMaster;
import com.niltech.exceptions.UserMgmtException;
import com.niltech.repository.IUserMasterRepository;
import com.niltech.utils.MailUtils;

import jakarta.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserMgmtServiceImpl implements IUserMgmtService {

	private final IUserMasterRepository repo;
	private final MailUtils utils;
	private final Environment environment;

	public UserMgmtServiceImpl(IUserMasterRepository repo, MailUtils utils, Environment environment) {
		this.repo = repo;
		this.utils = utils;
		this.environment = environment;
	}

	private static final Logger logger = LoggerFactory.getLogger(UserMgmtServiceImpl.class);

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int STRING_LENGTH = 6;
	private static final Random RANDOM = new Random();

	// Method to generate a random 6-character string
	private String generateRandomString() {
		StringBuilder sb = new StringBuilder(STRING_LENGTH);
		for (int i = 0; i < STRING_LENGTH; i++) {
			int index = RANDOM.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}
		return sb.toString();
	}

	@Override
	public String registerUser(UserAccount account) throws UserMgmtException {
		try {
			// Convert UserAccount obj to UserMaster object
			UserMaster master = new UserMaster();
			BeanUtils.copyProperties(account, master);

			// Set random string of 6 characters as password
			String temppwd = generateRandomString();
			master.setPassword(temppwd);
			master.setActiveSw("inactive");

			// Save the UserMaster object to the repository
			UserMaster master2 = repo.save(master);

			// Prepare email details
			String subject = "User Registration Success";
			String body = readEmailMessageBody(environment.getProperty("mailbody.registeruser.location"),
					account.getName(), temppwd);

			// Send registration email
			utils.sendMail(account.getEmail(), subject, body);

			return master2 != null ? "User is registered with id value::" + master2.getUserid()
					: "Problem in user registration";

		} catch (Exception e) {
			// Handle any other unexpected exceptions
			throw new UserMgmtException("An unexpected error occurred during user registration", e);
		}
	}

	@Override
	public String activateUserAccount(ActivateUser user) {
		// use finder method
		UserMaster entity = repo.findByEmailAndPassword(user.getEmail(), user.getTempPassword());

		if (entity == null) {
			return "User not found for the activation";

		} else {
			entity.setPassword(user.getConfirmPassword());
			// change the account status to active

			entity.setActiveSw("Active");
			// update the object
			repo.save(entity);
			return "User activated successfully with new password.....";
		}

	}

	@Override
	public String login(LoginCredentials credentials) {
		// convert login credential object to user master object i.e entity object
		UserMaster master = new UserMaster();
		BeanUtils.copyProperties(credentials, master);
		// prepare example object
		Example<UserMaster> example = Example.of(master);
		List<UserMaster> listEntities = repo.findAll(example);
		if (listEntities.isEmpty()) {
			return "Invalid credential entered";
		} else {
			// get entity object
			UserMaster user = listEntities.get(0);
			if (user.getActiveSw().equalsIgnoreCase("Active")) {
				return "valid credential and login successful";
			} else {
				return "user account is inactive";
			}
		}
	}

	@Override
	public List<UserAccount> listUser() {
		// load all entities and convert to user acc object
		return repo.findAll().stream().map(entity -> {
			UserAccount account = new UserAccount();
			BeanUtils.copyProperties(entity, account);
			return account;
		}).toList();
	}

	@Override
	public UserAccount showUserById(Integer id) {
		// load the user by user id
		Optional<UserMaster> opt = repo.findById(id);
		UserAccount account = null;
		if (opt.isPresent()) {
			account = new UserAccount();
			BeanUtils.copyProperties(opt.get(), account);
		}
		return account;
	}

	@Override
	public UserAccount showUserByEmailAndName(String email, String name) {
		// use the custom find by method
		UserMaster master = repo.findByNameAndEmail(name, email);
		UserAccount account = null;
		if (master != null) {
			account = new UserAccount();
			BeanUtils.copyProperties(master, account);
		}
		return account;
	}

	@Override
	public String updateUser(UserAccount user) {
		// get user account object based on name and mail
		Optional<UserMaster> opt = repo.findById(user.getUserid());
		if (opt.isPresent()) {
			// get entity object
			UserMaster master = opt.get();
			BeanUtils.copyProperties(user, master);
			repo.save(master);
			return "User details are updated successfully....";
		}
		return "User not found";
	}

	@Override
	public String deleteUserById(Integer id) {
		// load the object
		Optional<UserMaster> opt = repo.findById(id);
		if (opt.isPresent()) {
			repo.deleteById(id);
			return "user is deleted...";
		}
		return "User not found...";
	}

	@Override
	public String changeUserStatus(Integer id, String status) {
		// load the object
		Optional<UserMaster> opt = repo.findById(id);
		if (opt.isPresent()) {
			// get entity object
			UserMaster master = opt.get();
			// change the status
			master.setActiveSw(status);
			// update the object
			repo.save(master);
			return "User status changed";
		}
		return "user not found..";
	}

	@Override
	public String recoverPassword(RecoverPassword password) throws UserMgmtException {
		try {
			// get user master object i.e entity object by name and email
			UserMaster master = repo.findByNameAndEmail(password.getName(), password.getEmail());

			if (master != null) {
				String pwd = master.getPassword();
				// send the recovered password to email
				String subject = "Mail for password recovery";
				String mailBody = readEmailMessageBody(environment.getProperty("mailbody.recoverpwd.location"),
						password.getName(), pwd);
				utils.sendMail(password.getEmail(), subject, mailBody);

				return pwd;
			}
			return "User and email id not found";
		} catch (Exception e) {
			// Handle any other unexpected exceptions
			throw new UserMgmtException("An unexpected error occurred during password recovery", e);
		}
	}

	private String readEmailMessageBody(String fileName, String fullName, String pwd) throws UserMgmtException {
		String mailBody = null;
		String url = "http://localhost:9090/activate";
		try (FileReader reader = new FileReader(fileName); BufferedReader br = new BufferedReader(reader)) {
			// Read file content to StringBuilder object line by line
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}

			mailBody = builder.toString();
			mailBody = mailBody.replace("{full-name}", fullName);
			mailBody = mailBody.replace("{pwd}", pwd);
			mailBody = mailBody.replace("{URL}", url);

		} catch (IOException e) { 
			throw new UserMgmtException("Failed to read the email message body from the file: " + fileName, e);
		} catch (Exception e) { // Catch any other unexpected exceptions
			logger.error("An unexpected error occurred while processing the email template: {}", fileName, e);
		}

		return mailBody;
	}

}
