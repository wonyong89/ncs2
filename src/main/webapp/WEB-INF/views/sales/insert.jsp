<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="/resources/js/formCheck.js"></script>
<script src="/resources/js/jquery.selectric.js"></script>
<link rel="stylesheet" href="/resources/css/selectric.css">
<script type="text/javascript" src="/resources/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="/resources/ckeditor/adapters/jquery.js"></script>
<script type="text/javascript">

	// ckeditor 설정
	CKEDITOR.on('dialogDefinition', function (ev) {
		
		var dialogName = ev.data.name;
		var dialog = ev.data.definition.dialog;
		var dialogDefinition = ev.data.definition;

		if (dialogName == 'image') {
			dialog.on('show', function (obj) {
				this.selectPage('Upload'); //사진 추가 버튼 클릭시 업로드탭으로 시작
			});	
			dialogDefinition.removeContents('advanced'); // 자세히탭 제거
			dialogDefinition.removeContents('Link'); // 링크탭 제거
		}
	});

	//이미지 업로드 url 설정
	var ckedit_config = {
		filebrowserUploadUrl : '/editor/ckuploader' ,  // 통신할 컨트롤러 매핑 주소 
		toolbar : ''
	}
	// ckeditor 설정 종료
	
</script>

<style>
     #star_grade a{
        text-decoration: none;
        color: gray;
    }
    #star_grade a.on{
        color: red;
    }
</style>

<div class="grid-Wrapper">
	<div class="grid2"> 
	
		<div class="detail-sizer"></div>
		<div class="gutter-sizer"></div>
		<div class="detail-item detail-width6">
			<h3 class="text-left pad-y10">새 정보 등록</h3>
			<form action="insertPro" name="prForm" method="post" enctype="multipart/form-data">
				<table class="tableCss tbl-lg text-left">
					<tr>
						<th>주류 종류</th>
						<td>
							<select id="prSkind" name="prSkind" class="sel short required" type="select-one" msg="소분류를" >
								<c:forEach items="${secondCategory}" var="secondCategory">
									<option value="${secondCategory.code}">${secondCategory.value}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>상품명</th>
						<td>
							<input type="text" name="prName" class="input-lg required" msg="상품명을" />
						</td>
					</tr>
					<tr>
						<th>상품 이미지</th>
						<td>
							<input type="file" name="primage" class="required" msg="이미지를"/>
						</td>
					</tr>
					<tr>
						<th>양조장</th>
						<td>
							<input type="text" name="prPlace" class="input-lg"/>
						</td>
					</tr>
					<tr>
						<th>국가/도시</th>
						<td>
							<input type="text" name="prCountry" class="input-sm required" placeholder="국가" msg="국가를"/>
							&nbsp;<input type="text" name="prCity" class="input-sm required" placeholder="도시" msg="도시를"/>
						</td>
					</tr>
					<tr>
						<th>알콜 도수</th>
						<td>
							<input type="text" name="prAlcohol_" class="input-xs required" msg="도수를"/> 도
						</td>
					</tr>
					<tr>
						<th>어울리는 안주</th>
						<td>
							<input type="text" name="prFood" class="input-lg" />
						</td>
					</tr>
					<tr>
						<th>가격</th>
						<td>
							<input type="text" name="prPrice_" class="input-sm required" msg="가격을"/>  원	
						</td>
					</tr>
					<tr>
						<th>수량</th>
						<td>
							<input type="text" name="prAmount_" class="input-xs required" msg="수량을"/> 병
						</td>
					</tr>
					<tr>
						<th>판매 개시</th>
						<td>
							YES - <input type="radio" name="prUse" value="Y" checked="checked"/> / 
							NO - <input type="radio" name="prUse" value="N"/>
						</td>
					</tr>
					<tr>
						<th>상품 정보</th>
						<td>
							<textarea name="prContent" id="prContent" type="textarea" class="required" style="width:98%" ></textarea>
							<script type="text/javascript">CKEDITOR.replace( 'prContent' , ckedit_config);</script>
						</td>
					</tr>
					
				</table>
				<div class="text-center pad-top10">
					<input type="button" class="btn btn-lg btn-blue" value="저장" onclick="insertProduct()" />
					<input type="button" class="btn btn-lg btn-grey" value="취소" onclick="window.location='index'" />
				</div>
			</form>
		</div>
	</div>
</div>
<script>
	$(function() {
		

		// select 형태 바꿔주는 JS 실행
		$(".sel").selectric();
		
		// masonry 실행
		var msnry = new Masonry('.grid2', {
			itemSelector : '.detail-item',
			columnWidth : '.detail-sizer',
			percentPosition : true,
			gutter: '.gutter-sizer'
		});
		imagesLoaded('.grid2').on('progress', function() {
			msnry.layout();
		});
		
		// 대분류의 값이 바뀌었을 경우 소분류 값과 항목명 가져오는 ajax 실행
	    $('#dkBkind').change(function() {
	    	getSmallCategory(this.value);
	    	getItemValues(this.value);
	    });

	});

    function insertProduct(){
    	if (checkFormjquery()) {
    		
    		$("form[name='prForm']").submit();
    	}
    }
    
</script>