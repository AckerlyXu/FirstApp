package start.myapp.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.FlashMap;

import start.myapp.model.User;
import start.myapp.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(path = "/register", method = RequestMethod.GET)
	public String register(ModelMap model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public String register(User user, ModelMap model, NativeWebRequest webRequest) {
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

		userService.save(user);
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

	public String changePassword(ModelMap modelMap, HttpSession session, FlashMap map) {
//         if(session.getAttribute("user")==null) {
//        	  map.put("msg", "必须先登录才能修改密码")
//        	 return "redirect:login";
//         }
		return "changePassword";
	}

}
