package com.niltech.service;

import java.util.List;

import com.niltech.bindings.ActivateUser;
import com.niltech.bindings.LoginCredentials;
import com.niltech.bindings.RecoverPassword;
import com.niltech.bindings.UserAccount;
import com.niltech.exceptions.UserMgmtException;

public interface IUserMgmtService {

	public String registerUser(UserAccount account) throws UserMgmtException;

	public String activateUserAccount(ActivateUser user);

	public String login(LoginCredentials credentials);

	public List<UserAccount> listUser();

	public UserAccount showUserById(Integer id);

	public UserAccount showUserByEmailAndName(String email, String name);

	public String updateUser(UserAccount account);

	public String deleteUserById(Integer id);

	public String changeUserStatus(Integer id, String status);

	public String recoverPassword(RecoverPassword password)throws UserMgmtException;

}
