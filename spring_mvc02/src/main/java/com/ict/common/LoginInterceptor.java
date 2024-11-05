package com.ict.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
	// preHandle / postHandle / afterCompletion -> 반환형 boolean형

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 결과가 true이면 controller, false이면 특정 jsp로 이동

		// 로그인하면 세션에 로그인 성공했다는 정보를 담거나, 사용자 정보를 담는다.
		// 로그인 체크를 해서 만약에 로그인이 안된 상태이면 value에 false 저장

		// 만약에 session이 삭제된 상태라면 새로운 session을 생성해 준다.
		// 삭제가 안된 상태라면 사용하고 있는 그대로 전달해 준다. (기존 session호출)
		Object obj = request.getSession(true).getAttribute("loginchk");

		if (obj == null) { // 로그인 안된 상태
			// 자바스크립트를 통한 경고창과 리디렉트
			String script = "<script>"
					+ "alert('로그인이 필요합니다.');"
					+ " location.href='" + request.getContextPath() + "/sns_login';"
					+ "</script>";
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(script);

			return false; // null이면 특정 jsp로 이동해
		}

		return true; // null이 아니라면 controller로 이동해서 진행해
	}

	/*
	 * @Override public void postHandle(HttpServletRequest request,
	 * HttpServletResponse response, Object handler, ModelAndView modelAndView)
	 * throws Exception { // TODO Auto-generated method stub
	 * HandlerInterceptor.super.postHandle(request, response, handler,
	 * modelAndView); }
	 * 
	 * @Override public void afterCompletion(HttpServletRequest request,
	 * HttpServletResponse response, Object handler, Exception ex) throws Exception
	 * { // TODO Auto-generated method stub
	 * HandlerInterceptor.super.afterCompletion(request, response, handler, ex); }
	 */
}
