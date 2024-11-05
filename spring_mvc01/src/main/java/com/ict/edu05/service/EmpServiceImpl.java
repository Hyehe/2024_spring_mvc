package com.ict.edu05.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.edu05.dao.EmpDAO;
import com.ict.edu05.vo.EmpVO;

@Service
public class EmpServiceImpl implements EmpService {
	@Autowired
	private EmpDAO empDAO;
	
	@Override
	public List<EmpVO> getList() throws Exception {
		// TODO Auto-generated method stub
		return empDAO.getList();
	}

	@Override
	public List<EmpVO> getSearch(String deptno) throws Exception {
		// TODO Auto-generated method stub
		return empDAO.getSearch(deptno);
	}
	
	@Override
	public List<EmpVO> getSearch(EmpVO empVO) throws Exception {
		// TODO Auto-generated method stub
		return empDAO.getSearch(empVO);
	}

	@Override
	public List<EmpVO> getSearch(String idx, String keyword) throws Exception {
		// TODO Auto-generated method stub
		return empDAO.getSearch(idx, keyword);
	}

}
