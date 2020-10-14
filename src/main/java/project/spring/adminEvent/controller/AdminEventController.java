package project.spring.adminEvent.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import project.spring.adminEvent.service.AdminEventService;
import project.spring.adminEvent.service.AdminEventServiceImpl;
import project.spring.adminEvent.vo.AdminEventVO;
import project.spring.article.service.ArticleServiceImpl;
import project.spring.beans.PageVO;
import project.spring.beans.Pager;

@Controller
@RequestMapping("/admin/event/")
public class AdminEventController {
		@Autowired
		private AdminEventServiceImpl adminEventService = null;
		
		@Autowired
		private ArticleServiceImpl articleService = null;
		
		@RequestMapping("insertEvent")
		public String eventList(Model model, Locale locale)throws SQLException{
			
			
			return "admin/event/insertEvent.mn";
		}
		
		// 이벤트 처리 페이지
		@RequestMapping("insertEventPro")
		public String insertEvent(@ModelAttribute AdminEventVO vo, Model model, MultipartHttpServletRequest request, HttpServletResponse response)throws SQLException{
			
			
			System.out.println("content 확인:"  + vo.getContent() );

			vo.setEvStart(vo.getEvStart().replace("-", ""));
			vo.setEvEnd(vo.getEvEnd().replace("-", ""));
			
			
			// img 경로만 따로 꺼내기!
			// 애러 방지
			
			/*
			 * if(vo.getContent().contains("src=")) { String orgContent = vo.getContent();
			 * int idx = orgContent.indexOf("src="); int lastidx =
			 * orgContent.indexOf("style=");
			 * 
			 * System.out.println("idx : " + idx); System.out.println("last: " + lastidx);
			 * 
			 * 
			 * String imgName = orgContent.substring((idx+4), lastidx-1);
			 * 
			 * System.out.println("imgName = " + imgName);
			 * 
			 * System.out.println(request.getAttribute("title"));
			 * 
			 * vo.setThumImg(imgName); }
			 */
			

						
			
			// 대표이미지 파일 세팅
			
			int size = 1024*1024*20;
			MultipartFile mf = null;
			String path = null;
			
			try {
				mf = request.getFile("eventImg");
				path = request.getRealPath("/img/event");
				System.out.println("path : " + path);
				
				String orgName = mf.getOriginalFilename();
				String imgName = orgName.substring(0,orgName.lastIndexOf('.'));
				
				String ext = orgName.substring(orgName.lastIndexOf('.'));
				Long date = System.currentTimeMillis();
				String newName = imgName + date + ext;
				System.out.println("newName  :       " + newName);
				String newImgPath = path + "\\" + newName;
				File copyFile = new File(newImgPath);
				mf.transferTo(copyFile);
				
				// vo 에 넣어주기
				vo.setThumImg(newName);
				
				System.out.println("img 경로 : " + vo.getThumImg());
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("check---------------------------------------------------2222");

			System.out.println(vo.getEvStart());
			System.out.println("content : " + vo.getContent());
			System.out.println("ed_idx :: " +  vo.getEd_idx());
			System.out.println("stardDay:" +  vo.getEvStart());
			System.out.println("endDay" + vo.getEvEnd());
			System.out.println("evnetName : " + vo.getEventName());
			System.out.println("productCod : " + vo.getProductCode());
			System.out.println("content : " + vo.getContent());
			System.out.println("thumImg : " + vo.getThumImg());
			
			//나머지 세팅
			vo.setInsertId((String)request.getSession().getAttribute("memId"));
			
			
			System.out.println("controller 확인");
			int result = adminEventService.insertItem(vo);

			return "redirect:/admin/event/eventList.mn";
		}
		
		
		// eventList 보기 페이지
		@RequestMapping("eventList")
		public String eventView(String pageNum, Model model, HttpServletRequest request)throws SQLException{
			
			if(pageNum == null) {
				pageNum = "1";
			}
			
			
			Map<String, String> schMap = null;
			
			int count = 1;
			
			List eventList = null;
			
			Pager pager = new Pager();
			
			PageVO pageVo = null;
			
			int number = 0;
			
			if(request.getParameter("isSearch") != null) {
				schMap = new HashMap();
				//이벤트명 기간 상태
				if(request.getParameter("schEvName") != null && request.getParameter("schEvName").length() != 0)schMap.put("eventName", request.getParameter("schEvName"));
				if(request.getParameter("schEvStart") != null && request.getParameter("schEvStart").length() != 0) schMap.put("evStart", request.getParameter("schEvStart").replace("-", ""));
				if(request.getParameter("schEvEnd") != null && request.getParameter("schEvEnd").length() != 0)schMap.put("evEnd", request.getParameter("schEvEnd").replace("-", ""));
				if(request.getParameter("schIsOpen") != null && request.getParameter("schIsOpen").length() != 0)schMap.put("isOpen", request.getParameter("schIsOpen"));
				if(request.getParameter("schPrName") != null && request.getParameter("schPrName").length() != 0)schMap.put("prName", request.getParameter("schPrName"));
				System.out.println("해쉬맵 확인하기 : " + schMap.size());
				
				count = adminEventService.eventCount(schMap);
				
				
				if(count > 0) {
					
					pageVo = pager.pager(pageNum, count);
					eventList = adminEventService.eventList(pageVo.getStartRow(), pageVo.getEndRow(), schMap);
					number = count-(pageVo.getCurrPage()-1)*pageVo.getPageSize();
				}
			}else {
			
				// 이벤트 글 가져오기
				count = adminEventService.eventCount();
				
				
				
				
				pageVo = pager.pager(pageNum, count);
				
				
				
				if(count > 0) {
					
					// 완료 기간이 지나면 자동으로 비활성화 하기
					SimpleDateFormat spd = new SimpleDateFormat("yyyyMMdd");
					Date date = new Date();
					String today = spd.format(date);
					
					adminEventService.checkDate(today);
					
					
					eventList = adminEventService.eventList(pageVo.getStartRow(), pageVo.getEndRow());

					
					number = count-(pageVo.getCurrPage()-1)*pageVo.getPageSize();
				}
			}
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("eventList", eventList);
			model.addAttribute("count", count);
			model.addAttribute("pageVO", pageVo);
			model.addAttribute("number", number);

			System.out.println("===========================");
			System.out.println("number ;           " + number);
			System.out.println("count :              " + count);

			return "admin/event/eventList.mn";
		}
		
		@RequestMapping("drinkCodeSearch")
		@ResponseBody
		public List drinkSearch(@RequestParam(value = "input", required = false) String input)throws SQLException {
			System.out.println("검색기능 확인1");
			List list = null;
			System.out.println(input);
			if(input !=  null && !input.equals("")) {
				list = adminEventService.getDrinkSearch(input);
				for(int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i));
				}
			}
			return list;
		}
		
		// 이벤트 사용 여부 변경
		@RequestMapping("chEventCode")
		@ResponseBody
		public void chEventCode(@RequestParam String eventCode)throws SQLException {
			System.out.println("eventCode!!!"+eventCode);
			
			adminEventService.chOpen(eventCode);

		}

		@RequestMapping("modifyEvent")
		public String modifyEvent(@RequestParam(value="eventCode", required = false) String eventCode, Model model)throws SQLException {
			
			AdminEventVO vo = adminEventService.eventInfo(eventCode);
			model.addAttribute("vo",vo);
			
			
			
			return "admin/event/modifyEvent.mn";
		}
		
		@RequestMapping("modifyEvnetPro")
		public String modifyPro(AdminEventVO vo, MultipartHttpServletRequest request)throws SQLException{
			
			System.out.println("check---------------------------------------------------3333");

			System.out.println(vo.getEvStart());
			System.out.println("content : " + vo.getContent());
			System.out.println("ed_idx :: " +  vo.getEd_idx());
			System.out.println("stardDay:" +  vo.getEvStart());
			System.out.println("endDay" + vo.getEvEnd());
			System.out.println("evnetName : " + vo.getEventName());
			System.out.println("productCod : " + vo.getProductCode());
			System.out.println("content : " + vo.getContent());
			System.out.println("thumImg : " + vo.getThumImg());
			System.out.println(request.getFile("eventImg").getOriginalFilename());
			
			if(!request.getFile("eventImg").getOriginalFilename().equals("null") && !request.getFile("eventImg").getOriginalFilename().equals("")) {
				int size = 1024*1024*20;
				MultipartFile mf = null;
				String path = null;
				
				try {
					mf = request.getFile("eventImg");
					path = request.getRealPath("/img/event");
					System.out.println("path : " + path);
					
					String orgName = mf.getOriginalFilename();
					String imgName = orgName.substring(0,orgName.lastIndexOf('.'));
					
					String ext = orgName.substring(orgName.lastIndexOf('.'));
					Long date = System.currentTimeMillis();
					String newName = imgName + date + ext;
					System.out.println("newName  :       " + newName);
					String newImgPath = path + "\\" + newName;
					File copyFile = new File(newImgPath);
					mf.transferTo(copyFile);
					
					// vo 에 넣어주기
					vo.setThumImg(newName);
					
					System.out.println("img 경로 : " + vo.getThumImg());
					
				}catch (Exception e) {
					e.printStackTrace();
				}

			}else {
				vo.setThumImg(request.getParameter("oldImg"));
			}
			vo.setInsertId((String)request.getSession().getAttribute("memId"));
			adminEventService.updateItem(vo);
			
			return "redirect:/admin/event/eventList.mn";
		}
		

		
}
