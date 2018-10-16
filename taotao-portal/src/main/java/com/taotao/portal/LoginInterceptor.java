package com.taotao.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {

	/**
	 * 用户登录拦截器
	 */
	@Autowired
	private UserService userService;
	
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		TbUser user = userService.getUserByToken(request, response);
		if(user == null) {
			response.sendRedirect(SSO_LOGIN_URL + "?redirectURL=" + request.getRequestURL());
			return false;
		}
		//把用户对象放入request
		request.setAttribute("user", user);
		return true;
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