package start.myapp.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import start.myapp.model.User;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
//		if (request.getRequestURI().equals("/index") || request.getRequestURI().equals("/user/register")
//				|| request.getRequestURI().equals("/user/login")) {
//
//			return true;
		// } else {
		Object user = request.getSession().getAttribute("user");
		if (user == null || !(user instanceof User)) {

			response.sendRedirect("/user/login");
			return false;
		} else {
			// 只有admin才能进入用户管理页面
			if (request.getRequestURI().equals("/user/admin") || request.getRequestURI().startsWith("/user/delete")) {
				User user2 = (User) user;
				if (!user2.getUsername().equals("admin")) {
					response.sendRedirect("/");
					return false;
				}
			}

		}
		return true;
		// }

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
