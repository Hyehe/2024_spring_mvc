package com.ict.edu01;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Start4Controller {
	
	 @PostMapping("/start4")  //O
	//@RequestMapping("/start4")  //O
	// @GetMapping("/start4")	//X
	public ModelAndView test01() {
		ModelAndView mv = new ModelAndView("day01/result4");
			String[] carName = {"제네시스", "k9", "테슬라", "람보르기니", "벤츠"}; 
			mv.addObject("carName", carName);
		return mv;
	}
}
