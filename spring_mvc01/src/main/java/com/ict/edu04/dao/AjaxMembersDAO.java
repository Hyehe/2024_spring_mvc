package com.ict.edu04.dao;

import java.util.List;

import com.ict.edu04.vo.MembersVO;

public interface AjaxMembersDAO {
	public List<MembersVO> getMemberList();		// 리스트
	public String getMemberIdChk(String m_id);
	public MembersVO getMemberDetail(String m_idx); //Detail이면 상세보기니까 뭘 받아야 하는
	public String getMemberInsert(MembersVO mvo);	// 삽입
	public String getMemberDelete(String m_idx);	// 삭제
	public String getMemberUpdate(MembersVO mvo);	// 업데이트

	
	
}
