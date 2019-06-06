package start.myapp.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import start.myapp.model.User;
import start.myapp.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private UserService userservice;

	@RequestMapping(method = RequestMethod.GET, path = { "/", "/index" })
	public String index() {

		return "index";
	}

	@RequestMapping(method = RequestMethod.GET, path = { "/user" })
	@ResponseBody
	public User user() {
		User user = userservice.findUserByUsername("admin");
		if (user == null) {
			user = new User();
			user.setAdmin(true);
			user.setPassword(DigestUtils.md5DigestAsHex("123".getBytes()));
			user.setUsername("admin");
			user.setId(UUID.randomUUID());
			userservice.save(user);

		}
		return user;
	}
}
