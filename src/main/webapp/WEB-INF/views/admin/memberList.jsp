<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 관리</title>
</head>

<!-- 체크사항 ! 
	신고글 넘어가는거 구현하기  :  
	더보기 구현하기 : 
	검색기능
 -->
<body>
<div class="grid-Wrapper">
	<div class="grid">  
		<div class="grid-sizer"></div>
		<div class="gutter-sizer"></div>
			<div class="grid-item grid-item--width6 ">
				<table class="tableCss table">
					<tr>
						<th>아이디</th>
						<th>별명</th>
						<th>생년월일</th>
						<th>신고글</th>
						<th>신고회수</th>
						<th>가입일</th>
						<th>강퇴</th>
					</tr>
					
					<c:forEach var="memberList" items="${memberList }">
						<tr>
							<td><a href="#"></a>${memberList.id}</td>
							<td>${memberList.nickname}</td>
							<td>${memberList.birth}</td>
							<td>${memberList.reportNumber}<!-- jk0001, jk0002 로 저장되므로 스플릿으로 나누어
							a태그로 걸기 --></td>
							<c:if test="${memberList.reportCount != 0 }">
							<td>${memberList.reportCount}</td>
							</c:if>
							<c:if test="${memberList.reportCount == 0 }">
							<td></td>
							</c:if>
							<td>${memberList.insertDate}</td>
							<td>
							<p id="${memberList.id }">
								<c:if test="${memberList.isBan == 'N'}">
								<a onclick="exitUser('${memberList.id}')">강퇴</a>
								</c:if>
								<c:if test="${memberList.isBan == 'Y'}">
								강퇴됨
							</c:if>
							</p>
							</td>
						</tr>	
					</c:forEach>
				</table>
				<!-- 더 보기 구현하기 -->
				<div>
					<button id="addBtn" onclick="moreList();">더보기</button>
				</div>
			
				<!-- pager -->
				<div align="center" class="pageNums">
						<!-- 게시글이 있을때만 보여주기 -->
						<c:if test="${count>0}">
							<!-------------------------------------------------------------------------->
							<c:if test="${pageVO.startPage > pageVO.pageBlock}">
								<a href="/admin/memberList?pageNum=${pageVO.startPage-pageVO.pageBlock}">&lt;</a>
							</c:if>
							<c:forEach var="i" begin="${pageVO.startPage}" end="${pageVO.endPage}" step="1"> 
										<a href="/admin/memberList?pageNum=${i}" class="pageNums">&nbsp;${i}&nbsp;</a>
							</c:forEach>
							<c:if test="${pageVO.endPage < pageVO.pageCount}">
								<a href="/admin/memberList?pageNum=${pageVO.startPage+pageVO.pageBlock}">&gt;</a>
							</c:if>
							<!-------------------------------------------------------------------------->
						</c:if>
				</div>
				<div>
					<form action="memberList" name="schMember" id="schMember">
					<div>
						<span>id : <input type="text" name=schMemVal /></span>
					</div>
					<div><input type="submit" value="검색" class="btn btn-sm btn-blue" /></div>
					</form>
				</div>
			</div><!-- <div class="grid-item grid-item--width6 "> -->
	</div>	<!-- grid -->
</div> <!-- grid-Wrapper -->

	<!-- 데이터 스크롤해서 붙이는 스크립트  -->
	
	<script type="text/javascript">
	$(window).scroll(
		function() {
			// A(B+C) : document 높이 (고정)
			console.log($(document).height());
			// B : browser 높이 (최상단 기본값)
			console.log($(window).height());
			// C : 스크롤 위치
			console.log('SCROLL_TOP' + $(window).scrollTop());
			if ($(window).scrollTop() >= $(document).height()
					- $(window).height() - 100) {
				//호출 메서드
			}
		});
	</script>
<script>
function exitUser(memberId){

	alert("해당 아이디를 강퇴하였습니다.");
	alert(memberId);

	
	var context = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	$.ajax({
		url: context + '/exitUser?memberId='+memberId,
		type:"get",
		success : function(data){
			$('#' + memberId).empty();
			$('#' + memberId).append('<p>강퇴됨</p>')
			

		}
	})
	
}
</script>	

</body>


</html>