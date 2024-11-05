package com.ict.member.service;

import com.ict.member.vo.MemberVO;

public interface MemberService {
	// 로그인
	public MemberVO getMemberLogIn(String m_id) throws Exception;

	// 회원가입
	public int getMemberJoin(String m_id) throws Exception;
	
}
