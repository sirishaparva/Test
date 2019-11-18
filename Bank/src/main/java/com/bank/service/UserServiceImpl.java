package com.bank.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.beans.Login;
import com.bank.beans.TransferRequest;
import com.bank.beans.User;
import com.bank.dao.UserDao;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public Integer createUser(User user) {
		Random rand = new Random();
		int no = rand.nextInt(1000000000);
		user.setAccountNo(no);
		return userDao.createUser(user);
	}

	@Override
	public List<Map<String, Object>> getUsers() {
		return userDao.getUsers();
	}
	
	@Override
	public Login userLogin(Login login) {
		return userDao.userLogin(login);
	}

	@Override
	public String transferFunds(TransferRequest toAcc) {
		return userDao.transferFunds(toAcc);
	}
}
