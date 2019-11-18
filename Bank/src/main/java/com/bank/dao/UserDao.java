package com.bank.dao;

import java.util.List;
import java.util.Map;

import com.bank.beans.Login;
import com.bank.beans.TransferRequest;
import com.bank.beans.User;

public interface UserDao {

	public Integer createUser(User user);

	public List<Map<String, Object>> getUsers();

	public Login userLogin(Login login);

	public String transferFunds(TransferRequest toAcc);

}
