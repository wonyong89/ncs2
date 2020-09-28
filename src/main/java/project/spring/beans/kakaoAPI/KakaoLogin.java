package project.spring.beans.kakaoAPI;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import project.spring.kakao.vo.UserVO;


public class KakaoLogin {
	
	private static String restApiKey_static = "ccd42c0f6ac58e519759c5baf3c7ceb4";
	private static String adminkey_static = "e454a8dba20ab15cac38b5db0ec0c22a";
	private static String callback_URL_static = "http://localhost:8080/mvc/authResult";
	
	public static  JsonNode getAccessToken(String autorize_code) {
		System.out.println("restAPIkeyStatic="+restApiKey_static);
		final String RequestUrl = "https://kauth.kakao.com/oauth/token";
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
		postParams.add(new BasicNameValuePair("client_id", restApiKey_static)); // REST API KEY
		postParams.add(new BasicNameValuePair("redirect_uri",callback_URL_static )); // 리다이렉트 URI
		postParams.add(new BasicNameValuePair("code", autorize_code)); // 로그인 과정중 얻은 code 값

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(RequestUrl);
		JsonNode returnJsonNode = null;
		
		try {
			//json
			post.setEntity(new UrlEncodedFormEntity(postParams));
			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("Sending 'POST' request to URL=" + RequestUrl);
			System.out.println("Post parameters=" + postParams);
			System.out.println("Response Code=" + responseCode);
			// JSON 형태 반환값 처리
			ObjectMapper mapper = new ObjectMapper();
			returnJsonNode = mapper.readTree(response.getEntity().getContent());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return returnJsonNode;
	}

	public static JsonNode getKakaoUserInfo(String autorize_code) {
		final String RequestUrl = "https://kapi.kakao.com/v2/user/me";
		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(RequestUrl);
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		
		// add header
		post.addHeader("Authorization", "Bearer " + autorize_code);
		//postParams.add(new BasicNameValuePair("property_keys", "[\"properties.nickname\"]"));
		// add body
		JsonNode returnJsonNode = null;

		try {
			//post.setEntity(new UrlEncodedFormEntity(postParams));
			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();
			System.out.println("Sending 'POST' request to URL : " + RequestUrl);
			System.out.println("Response Code : " + responseCode);
			// JSON 형태 반환값 처리
			ObjectMapper mapper = new ObjectMapper();
			returnJsonNode = mapper.readTree(response.getEntity().getContent());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return returnJsonNode;
	}
	public static UserVO changeData(JsonNode userInfo) {
		UserVO vo = new UserVO();
		//vo.setUser_snsId(userInfo.path("id").asText()); // id -> vo 넣기
		if (userInfo.path("kaccount_email_verified").asText().equals("true")) { // 이메일 받기 허용 한 경우
			vo.setUser_email(userInfo.path("kaccount_email").asText()); // email -> vo 넣기
		} else { // 이메일 거부 할 경우 코드 추후 개발
		}
		JsonNode properties = userInfo.path("properties"); // 추가정보 받아오기
		if (properties.has("nickname"))
			vo.setUser_name(properties.path("nickname").asText());
			vo.setUser_profileImagePath(properties.path("profile_image").asText());
		return vo;
	}
}