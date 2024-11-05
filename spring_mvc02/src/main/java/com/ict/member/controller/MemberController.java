package com.ict.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.member.service.MemberService;
import com.ict.member.vo.MemberVO;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// 회원가입 폼으로 이동
	 @GetMapping("/login_join_form")
	 public ModelAndView getLogInJoinForm() {
		 ModelAndView mv = new ModelAndView("shop/login_joinForm");
		 return mv;
	 }

	 // 회원가입
	 @PostMapping("/login_join_ok")
	 public ModelAndView getLogInJoinOK(MemberVO mvo) {
		 ModelAndView mv = new ModelAndView("shop/login_joinForm");
		 try {
			// 비밀번호 암호화
			 String m_pw = passwordEncoder.encode(mvo.getM_pw());
			 mvo.setM_pw(m_pw); // 암호화해서 다시 VO로 넣어주기
			 
			 int result = memberService.getMemberJoin(m_pw);
			 if (result > 0) {
				mv.addObject("result", "1");
			} else {
				mv.addObject("result", "0");
			}
			 return mv;
		} catch (Exception e) {
			System.out.println(e);
			mv.addObject("result", "0");
		}
		 return mv;
	 }
	 
	 // 로그인
	@PostMapping("/member_login")
	public ModelAndView getMemberLogIn(MemberVO mvo, HttpSession session) {
		try {
			ModelAndView mv = new ModelAndView();
			MemberVO mvo2 = memberService.getMemberLogIn(mvo.getM_id());

			if (mvo2 == null) {
				// 아이디가 없어서 로그인 실패
				return new ModelAndView("sns/login_error");
			} else {
				// 비밀번호 검사
				if (passwordEncoder.matches(mvo.getM_pw(), mvo2.getM_pw())) {
					// 성공
					session.setAttribute("loginchk", "ok");
					// 일반유저와 관리자 유저 구분
					if (mvo2.getM_id().equals("admin")) {
						session.setAttribute("admin", "ok");
					}
					mv.setViewName("redirect:/shop");
					session.setAttribute("mvo2", mvo2);
					return mv;
				} else {
					// 비밀번호가 안 맞아서 로그인 실패
					// request에 값을 저장해서 loginForm에서 로그인 실패를 alert 할 수 있다.
					return new ModelAndView("sns/login_error");
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@GetMapping("/member_logout")
	public ModelAndView getMemberLogOut(HttpSession session) {
		// 세션 초기화 (전체삭제)
		session.invalidate();

		// 필요정보만 삭제
		// session.removeAttribute("loginchk");
		// session.removeAttribute("admin");
		// session.removeAttribute("mvo2");

		return new ModelAndView("redirect:/shop");
	}

}
