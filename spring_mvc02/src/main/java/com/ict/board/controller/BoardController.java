package com.ict.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.board.service.BoardService;
import com.ict.board.vo.BoardVO;
import com.ict.common.Paging;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private Paging paging;
	
	@RequestMapping("/board_list")
	public ModelAndView boardList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("board/list");
		
		// 페이징 기법
		
		// 전체 게시물의 수 (DB처리)
		int count = boardService.getTotalCount();
		paging.setTotalRecord(count);
		
		// 전체 페이지의 수를 구한다.
		if (paging.getTotalRecord() <= paging.getNumPerPage()) {
			paging.setTotalPage(1);
		}else {
			paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
			if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
				paging.setTotalPage(paging.getTotalPage()+1);
			}
		}
		
		// 파라미터에서 넘어오는 cPage(보고싶은 현재 페이지)를 구하자
		String cPage = request.getParameter("cPage");
		System.out.println("cPage :" + cPage);
		// 만약에 cPage가 null이면 무조건 1page이다.
		if (cPage == null) {
			paging.setNowPage(1);
		}else {
			paging.setNowPage(Integer.parseInt(cPage));
		}
		
		// cPage 기준으로 begin, end, beginBlock, endBlock
		// MySql, MariaDB는 limit와 offset을 이용해야 된다. (begin, end는 필요없다.)
		// Oracle에서는 begin, end 필요하다.
		// offset = limit * (현재페이지 - 1)
		// limit = numPerPage
		// select * from 테이블 order by pk DESC LIMIT 6 OFFSET 0 | 6 | 12 | 18 ;
		
		paging.setOffset(paging.getNumPerPage()* (paging.getNowPage() - 1));
		
		// 시작 블록, 끝 블록
		paging.setBeginBlock(
				(int)(((paging.getNowPage()-1) / paging.getPagePerBlock()) * paging.getPagePerBlock()+1));
		paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);
		
		// 주의사항
		// endBlock(3,6,9...) 나온다. 그런데 실제 가지고 있는 총 페이지는 21개일 경우 4페이지까지
		if (paging.getEndBlock() > paging.getTotalPage()) {
			paging.setEndBlock(paging.getTotalPage());
		}
		
		// DB 처리
		List<BoardVO> board_list = boardService.getBoardList(paging.getOffset(), paging.getNumPerPage());
		if (board_list != null) {
			mv.addObject("board_list", board_list);
			mv.addObject("paging", paging);
			return mv;
		}
		return null;
	}
	
	@GetMapping("/board_write")
	public ModelAndView boardWrite(@ModelAttribute("cPage") String cPage) {
		return new ModelAndView("board/write");
	}
	
	@PostMapping("/board_write_ok")
	public ModelAndView boardWriteOK(BoardVO bovo, HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("redirect:/board_list");
			String path = request.getSession().getServletContext().getRealPath("/resources/upload");
			MultipartFile file = bovo.getFile_name();
			
			if (file.isEmpty()) {
				bovo.setF_name("");
			}else {
				UUID uuid = UUID.randomUUID();
				String f_name = uuid.toString() + "_" + file.getOriginalFilename();
				bovo.setF_name(f_name);
				
				// 업로드 
				file.transferTo(new File(path, f_name));
			}
			// 암호화
			String pwd = passwordEncoder.encode(bovo.getPwd());
			bovo.setPwd(pwd);
			
			int result = boardService.getBoardInsert(bovo);
			if (result > 0) {
				return mv;
			}
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@GetMapping("/board_detail")	// cPage, idx가 넘어옴
	public ModelAndView boardDetail(@ModelAttribute("cPage") String cPage, 
			@RequestParam("idx") String idx) {
		ModelAndView mv = new ModelAndView("board/view");
		
		// hit 업데이트
		int result = boardService.getBoardHit(idx);
		
		// 상세보기 (정보 가져오기) 
		BoardVO bovo = boardService.getBoardDetail(idx);
		
		if (bovo != null) {
			mv.addObject("bovo", bovo);
			return mv;
		} 
		return null;
	}
	
	// 다운로드
	@GetMapping("/board_down")
	public void boardDown(HttpServletRequest request, HttpServletResponse response) {
		try {
			String f_name = request.getParameter("f_name");
			String path = request.getSession().getServletContext().getRealPath("/resources/upload" + f_name);
			String r_path = URLEncoder.encode(path, "UTF-8");
			
			response.setContentType("aplication/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="+r_path);
			
			File file = new File(new String(path.getBytes(),"UTF-8"));
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			
			FileCopyUtils.copy(in, out);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	
	@PostMapping("/ans_write")
	public ModelAndView ansWrite(@ModelAttribute("idx") String idx,
			@ModelAttribute("cPage") String cPage) {
		return new ModelAndView("board/ans_write");
	}
	
	@PostMapping("/board_ans_write_ok")
	public ModelAndView ansWriteOK(BoardVO bovo, HttpServletRequest request, @ModelAttribute("cPage") String cPage) {
		try {
			// 파라미터 값과 파일이 넘어온다.
			// 답글에서만 처리할 일들
			// 1. 원글의 b_groups, b_step, b_lev를 가져오자
			
			// 상세보기 (정보 가져오기) 
			BoardVO bovo2 = boardService.getBoardDetail(bovo.getIdx());
			int b_groups = Integer.parseInt(bovo2.getB_groups());
			int b_step = Integer.parseInt(bovo2.getB_step());
			int b_lev = Integer.parseInt(bovo2.getB_lev());
			
			// 2. step, lev를 하나씩 증가시키자
			b_step++;
			b_lev++;
			
			// 3. DB에 b_groups를 기준으로 b_lev를 업데이트하자
			// ** b_groups가 같은 글을 찾아서 기존 데이터에 레벨이 같거나 크면 기존 데이터의 레벨을 증가시키자
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("b_groups", b_groups);
			map.put("b_lev", b_lev);
			
			int result = boardService.getLevUpdate(map);
			
			// 4. 받은 정보와 함께 bovo에 넣고 insert하기
			// 다시 들어갈 땐 문자로 되어야 함.
			bovo.setB_groups(String.valueOf(b_groups));
			bovo.setB_step(String.valueOf(b_step));
			bovo.setB_lev(String.valueOf(b_lev));
			
			ModelAndView mv = new ModelAndView("redirect:/board_list");
			
			String path = request.getSession().getServletContext().getRealPath("/resources/upload");
			MultipartFile file = bovo.getFile_name();
			if (file.isEmpty()) {
				bovo.setF_name("");
			}else {
				UUID uuid = UUID.randomUUID();
				String f_name = uuid.randomUUID()+"_"+file.getOriginalFilename();
				bovo.setF_name(f_name);
				
				file.transferTo(new File(path,f_name));
			}
			
			// 암호화
			String pwd = passwordEncoder.encode(bovo.getPwd());
			bovo.setPwd(pwd);
			
			int result2 = boardService.getAnsInsert(bovo);
			if (result2 > 0) {
				return mv;
			}
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@PostMapping("/board_delete")
	public ModelAndView boardDelete(@ModelAttribute("cPage") String cPage, @ModelAttribute("idx") String idx) {
		return new ModelAndView("board/delete");
	}
	
	@PostMapping("/board_delete_ok")
	public ModelAndView boardDeleteOK(
			@RequestParam("pwd") String pwd, 
			@ModelAttribute("idx") String idx,
			@ModelAttribute("cPage") String cPage) {
		ModelAndView mv = new ModelAndView();
		
		// 비밀번호 체크
		BoardVO bovo = boardService.getBoardDetail(idx);
		String dbpwd = bovo.getPwd();
		if (passwordEncoder.matches(pwd, dbpwd)) { // 암호화 된것, 안된것
			// 글삭제
			// active 컬럼을 0 -> 1로 변경
			int result = boardService.getBoardDelete(idx);
			if (result > 0) {
				mv.setViewName("redirect:/board_list");
				return mv;
			}
		} else {
			// 비밀번호가 틀릴 경우
			mv.setViewName("board/delete");
			mv.addObject("pwdchk", "fail");
			return mv;
		}
		return null;
	}
	
	@PostMapping("/board_update")
	public ModelAndView boardUpdate(@ModelAttribute("cPage") String cPage, @ModelAttribute("idx") String idx) {
		ModelAndView mv = new ModelAndView("board/delete");
		BoardVO bovo = boardService.getBoardDetail(idx);
		if (bovo != null) {
			mv.addObject("bovo", bovo);
			return mv;
		}
		return null;
	}
	
	@PostMapping("/board_update_ok")
	public ModelAndView boardUpdateOK(@ModelAttribute("cPage") String cPage, 
			@ModelAttribute("idx") String idx, BoardVO bovo, HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView();
		BoardVO bovo2 = boardService.getBoardDetail(idx);
		String dbpwd = bovo2.getPwd();
		
		if (passwordEncoder.matches(bovo.getPwd(), dbpwd)) {
			// 비밀번호가 맞음
			try {
				mv.setViewName("redirect:/board_detail");
				String path = request.getSession().getServletContext().getRealPath("/resources/upload");
				MultipartFile file = bovo.getFile_name();
				
				if (file.isEmpty()) {
					bovo.setF_name(bovo.getOld_f_name());
				}else {
					UUID uuid = UUID.randomUUID();
					String f_name = uuid.toString() + "_" + file.getOriginalFilename();
					bovo.setF_name(f_name);
					
					// 업로드 
					file.transferTo(new File(path, f_name));
				}
				// 암호화
				String pwd = passwordEncoder.encode(bovo.getPwd());
				bovo.setPwd(pwd);
				
				int result = boardService.getBoardUpdate(bovo);
				if (result > 0) {
					return mv;
				}
				return null;
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}
		} else {
			// 비밀번호가 틀림
			mv.setViewName("board/update");
			mv.addObject("pwdchk", "fail");
			mv.addObject("bovo", bovo);
			return mv;
		}
		
	}
}
