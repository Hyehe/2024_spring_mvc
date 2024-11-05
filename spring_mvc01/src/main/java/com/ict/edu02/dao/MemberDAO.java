package com.ict.edu02.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.ict.edu02.vo.MembersVO;

public class MemberDAO {
	private SqlSessionTemplate sqlSessionTemplate;

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public List<MembersVO> getMemberList(){
		try {
			List<MembersVO> list = null;
			// DAO에서 mapper로 이동
			list = sqlSessionTemplate.selectList("memebers.getList"); // 파라미터 값 없음("이름.ID")
			return list;
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
