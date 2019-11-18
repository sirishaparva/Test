package com.bank.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.beans.Login;
import com.bank.beans.Response;
import com.bank.beans.TransferRequest;
import com.bank.beans.User;
import com.bank.service.UserService;

@RestController
public class BankUserController {

	@Autowired
	private UserService userService;
	
	/**
	 * Loads the index page
	 * @return
	 */
	@GetMapping(value = "/")
	public String homePage() {
		return "index";
	}

	@PostMapping(value = "/create-user")
	public Response createUser(@RequestBody User user) {
		Response response = new Response();
		Integer acc = userService.createUser(user);
		response.setResMsg("Successfully created= " + acc);
		response.setStatus("Success");
		return response;

	}

	@GetMapping(value = "getUsers")
	public List<Map<String, Object>> getUsers() {
		return userService.getUsers();
	}

	@PostMapping(value = "userLogin")
	public Response userLogin(@RequestBody Login login) {
		Response response = new Response();
		Login status = userService.userLogin(login);
		if (status != null) {
			response.setStatus("Success");
			response.setResMsg("Successfully login");
		} else {
			response.setStatus("Fail");
			response.setResMsg("Login failed, try agin");
		}
		return response;

	}

	@PostMapping(value = "/fundsTransfer")
	public Response transforFunds(@RequestBody TransferRequest funds) {

		Response response = new Response();
		response.setStatus("Success");
		response.setResMsg(userService.transferFunds(funds));
		return response;
	}

}
