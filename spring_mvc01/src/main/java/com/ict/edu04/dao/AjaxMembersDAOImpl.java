package com.ict.edu04.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ict.edu04.vo.MembersVO;

@Repository
public class AjaxMembersDAOImpl implements AjaxMembersDAO {
	
	@Autowired	// Mapper 가게 해줌
	private SqlSessionTemplate sqlsessionTemplate; // DB 처리 (mapper로 이동)

	@Override
	public List<MembersVO> getMemberList() {
		return sqlsessionTemplate.selectList("ajaxmembers.getMembersList");
	}

	@Override
	public MembersVO getMemberDetail(String m_idx) {

		return null;
	}

	@Override
	public String getMemberInsert(MembersVO mvo) {
		// 자동 커밋이다.
		int result = sqlsessionTemplate.insert("ajaxmembers.getMemberInsert", mvo);
		return String.valueOf(result);
	}

	@Override
	public String getMemberDelete(String m_idx) {
		// 자동 커밋이다.
		int result = sqlsessionTemplate.delete("ajaxmembers.getMemberDelete", m_idx);
		return String.valueOf(result);
	}

	@Override
	public String getMemberUpdate(MembersVO mvo) {

		return null;
	}

	@Override
	public String getMemberIdChk(String m_id) {
		// 있으면 1, 없으면 0
		int result = sqlsessionTemplate.selectOne("ajaxmembers.getMemberIdChk", m_id);
		return String.valueOf(result);
	}
}
