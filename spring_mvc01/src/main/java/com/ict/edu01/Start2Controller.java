package com.ict.edu01;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class Start2Controller implements Controller{
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		// 방법1)
		// ModelAndView mv = new ModelAndView();
		// mv.setViewName(뷰네임=되돌아갈 jsp이름 = "result2");
		mv.setViewName("result2");
		
		// 방법2)
		// ModelAndView mv = new ModelAndView("result2");
		
		String[] dogName = {"땅콩이", "뽀송이", "구름이", "체리"};
		mv.addObject("dogName", dogName);
		
		ArrayList<String> catName = new ArrayList<String>(); 
		catName.add("뽀미");
		catName.add("나비");
		catName.add("알콩");
		catName.add("달콩");
		mv.addObject("catName", catName);
		
		return mv;  // 이렇게 만든 건 무조건 리턴이 ModelAndView이다.
	}
}
