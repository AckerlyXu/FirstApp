package start.myapp.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import start.myapp.model.SharedFilePage;
import start.myapp.model.User;
import start.myapp.service.FileImageService;
import start.myapp.service.FileService;
import start.myapp.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private UserService userservice;
	@Autowired
	private FileImageService fileimageService;
	@Autowired
	private FileService fileService;

	@RequestMapping(method = RequestMethod.GET, path = { "/", "/index" })
	public String index(Integer currentPage, ModelMap model) {
		SharedFilePage sharedFilePage = new SharedFilePage();
		if (currentPage != null) {
			sharedFilePage.setCurrentPage(currentPage);
		}
		fileService.getSharedPageData(sharedFilePage);
		model.addAttribute("page", sharedFilePage);
		return "index";
	}

	@RequestMapping(method = RequestMethod.GET, path = { "/user" })
	@ResponseBody
	public User user(HttpServletRequest request) {
		User user = userservice.findUserByUsername("admin");
		if (user == null) {
			user = new User();
			user.setAdmin(true);
			user.setPassword(DigestUtils.md5DigestAsHex("123".getBytes()));
			user.setUsername("admin");
			user.setId(UUID.randomUUID());
			userservice.save(user);
			String path = request.getServletContext().getRealPath("") + File.separator + user.getId().toString();
			File file = new File(path);
			file.mkdirs();
		}
		return user;
	}
}
