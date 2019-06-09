package start.myapp.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import start.myapp.model.User;
import start.myapp.model.UserFile;
import start.myapp.service.FileService;
import start.myapp.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private FileService fileService;

	@RequestMapping(path = "/register", method = RequestMethod.GET)
	public String register(ModelMap model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public String register(User user, ModelMap model, NativeWebRequest webRequest, HttpServletRequest request) {
		boolean hasError = false;
		model.addAttribute("user", user);
		// 如果用户名为空,返回注册页面
		if (!StringUtils.isNotBlank(user.getUsername())) {
			model.addAttribute("usernameError", "用户名不能为空");
			hasError = true;
		}
		// 如果密码为空,返回注册页面
		if (!StringUtils.isNotBlank(user.getPassword())) {
			model.addAttribute("passwordError", "密码不能为空");
			hasError = true;
		}
		if (hasError) {
			return "register";
		}
		User user2 = userService.findUserByUsername(user.getUsername());
		if (user2 != null) {
			model.addAttribute("usernameError", "该用户已经被注册");
			return "register";
		}
		// 注册用户,密码加密以后存入数据库
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

		user = userService.save(user);
		// 默认给每个用户创建一个文件夹，用来存储他（她）的文件
		String path = request.getServletContext().getRealPath("") + File.separator + user.getId().toString();
		File file = new File(path);
		file.mkdirs();

		// 注册完毕以后默认是登录状态
		webRequest.setAttribute("user", user, NativeWebRequest.SCOPE_SESSION);
		return "redirect:/index";
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST)

	public String login(ModelMap model, User user, NativeWebRequest request) {
		boolean hasError = false;
		model.addAttribute("user", user);

		// 如果用户名为空,返回注册页面
		if (!StringUtils.isNotBlank(user.getUsername())) {
			model.addAttribute("usernameError", "用户名不能为空");
			hasError = true;
		}
		// 如果密码为空,返回注册页面
		if (!StringUtils.isNotBlank(user.getPassword())) {
			model.addAttribute("passwordError", "密码不能为空");
			hasError = true;
		}
		if (hasError) {
			return "login";
		}
		// 加密以后再到数据库查找
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		user = userService.findUserByUsernameAndPassword(user);
		if (user == null) {
			model.addAttribute("userError", "用户名或者密码错误");
		} else {
			// 成功登录,把用户存储在session中,返回index页面
			request.setAttribute("user", user, NativeWebRequest.SCOPE_SESSION);
			if (user.getUsername().equals("admin")) {
				return "redirect:admin";
			}
			return "redirect:/index";
		}
		return "login";
	}

	@RequestMapping(path = "/login", method = RequestMethod.GET)

	public String login(ModelMap modelMap) {

		modelMap.addAttribute("user", new User());
		return "login";
	}

	@RequestMapping(path = "/changePassword", method = RequestMethod.GET)

	public String changePassword(ModelMap modelMap) {

		return "changePassword";
	}

	@RequestMapping(path = "/changePassword", method = RequestMethod.POST)

	public String changePassword(String originalPassword, String newPassword, HttpSession session, ModelMap model) {
		boolean hasError = false;
		// 非空校验
		if (!StringUtils.isNotBlank(originalPassword)) {
			hasError = true;
			model.addAttribute("originError", "原密码不能为空");
		}
		if (!StringUtils.isNotBlank(newPassword)) {
			hasError = true;
			model.addAttribute("newError", "新密码不能为空");
		}
		// 有错误，返回原页面
		if (hasError) {
			return "changePassword";
		}
		// 获取用户
		User user = (User) session.getAttribute("user");
		user = userService.findUserByUsername(user.getUsername());
		String encryptedPassword = DigestUtils.md5DigestAsHex(originalPassword.getBytes());
		// 比对密码
		if (user.getPassword().equals(encryptedPassword)) {
			user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
			userService.changePassword(user);
			return "redirect:/index";
		} else {
			model.addAttribute("originError", "原密码错误");
			return "changePassword";
		}

	}

	@RequestMapping(method = RequestMethod.GET, path = "/logout")
	@ResponseBody
	public void logout(HttpSession session) {
		session.removeAttribute("user");

	}

	@RequestMapping(method = RequestMethod.GET, path = "/admin")
	public String userList(ModelMap model) {

		List<User> users = userService.findAll();

		model.addAttribute("users", users);
		return "userList";
	}

	@RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable UUID id, HttpServletRequest request) {
		User user = userService.findOne(id);
		if (user != null && !user.getUsername().equals("admin")) {
			List<UserFile> fileList = fileService.findToDelete(user);
			String rootPath = request.getServletContext().getRealPath(user.getId().toString());
			for (UserFile userFile : fileList) {
				File file = new File(rootPath + File.separator + userFile.getFilename());
				file.delete();
			}
			new File(rootPath).delete();
			userService.delete(user);
			return "redirect:userList";
		}
		return "redirect:userList";
	}

}
