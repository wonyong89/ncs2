<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
</head>
<body>123
	<h1>Kakao_auto_result</h1>
	<h2>${sessionScope.nickname}님 환영합니다</h2>
	<hr>
	<a href="/mvc/auth/logout">
		<c:if test="${!empty sessionScope.nickname}">
			<img src="/mvc/resources/logout.png" />
		</c:if>
	</a>
</body>
</html>