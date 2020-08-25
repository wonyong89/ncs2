package project.spring.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;

import project.spring.beans.KakaoLogin;
import project.spring.beans.KakaoLogout;
import project.spring.vo.UserVO;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	@Autowired
	private String nativeAppkey;
	@Autowired
	private String restApikey;
	@Autowired
	private String javaScriptkey;
	@Autowired
	private String adminkey;
	@Autowired
	private String callback_URL;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,
			HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("restApikey",
				restApikey);
		request.setAttribute("callback_URL",
				callback_URL);
		return "/util/kakao_auth_form";
	}

	@RequestMapping(value = "/authResult", produces = "application/json", method = {
			RequestMethod.GET,
			RequestMethod.POST })
	public String kakaoLogin(
			@RequestParam("code") String code,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		System.out.println("code="+code);

		//getToken
		JsonNode tokenJson = KakaoLogin.getAccessToken(code);
		String token = tokenJson.path("access_token").toString();
		System.out.println("tokenJson="+tokenJson);
		System.out.println("==================================");
		//useToken getuserInfo
		JsonNode userInfo = KakaoLogin.getKakaoUserInfo(token);
		System.out.println("userInfo="+userInfo);
		//String id = userInfo.get("id").toString();
		String id = userInfo.path("id").asText();
		JsonNode properties = userInfo.path("properties");
		System.out.println("properties="+properties);
		String nickname = properties.path("nickname").toString();
		request.setAttribute("id", id);
		
		HttpSession session = request.getSession();
		session.setAttribute("nickname", nickname);
		session.setAttribute("code", code);
		session.setAttribute("token", token);
		System.out.println("==================================");
		
		//add Db Loginc
		return "redirect:main";
	}
	@RequestMapping(value = "/auth/logout", 
			produces = "application/json",
			method = {
			RequestMethod.GET,
			RequestMethod.POST })
	public String kakaoLogout(
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String token = session.getAttribute("token").toString();
		System.out.println("token="+token);
		JsonNode logoutInfo = KakaoLogout.doLogout(token);
		System.out.println("logoutInfo="+logoutInfo);
		System.out.println("=======API_token_out==========================");
		return "main";
	}
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(Locale locale,
			Model model) {
		return "/util/kakao_auth_result";
	}
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale,
			Model model) {
		return "homeCss";
	}
}
