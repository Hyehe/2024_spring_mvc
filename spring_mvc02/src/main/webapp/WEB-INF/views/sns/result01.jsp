<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>카카오 결과</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js" crossorigin="anonymous"></script>
</head>
<body>
	<h2>kakao 로그인 성공</h2>
	<div id="result"></div>
	
	<!-- 카카오계정과 함께 로그아웃 -->
	<a href="https://kauth.kakao.com/oauth/logout?client_id=93006d28ac6f217d33503f6f0f67d192&logout_redirect_uri=http://localhost:8080/kakaologout">로그아웃</a>
	
	   <!--  <script type="text/javascript">
     $(function() {
         $("#result").empty();
         $.ajax({
             url : "/kakaoUserInfo",
             method : "post",
             dataType : "json",
             success : function() {
                 let str  = "<li>닉네임 : ${nickname} </li>";
                     str += "<li>이미지 : <img src= ${profileImage}> </li>";
                 $('#result').append(str);
             }, 
             error : function() {
                 alert("정보를 불러오는 데 실패했습니다.");
             }
         });
      });
    </script> --> 
    
	<script type="text/javascript">
	// $(document).ready(function() { });
	$(function () {
		$("#result").empty();
		$.ajax({
			url : "/kakaoUserInfo",
			method : "post",
			dataType: "json",
			success : function (data) {
				// 받은 다음 값 변경 안하는 것들은 const 사용이 더 나음.
				const name = data.properties.nickname;
				const img = data.properties.profile_image;
				const email = data.kakao_account.email;
				
				let str = "<li>닉네임 : ${nickname} </li> ";
					str += "<li>email : " + email + " </li> ";
					str += "<li>이미지 : <img src= " + img +" > </li> ";
				$('#result').append(str);
			},
			error : function() {
				alert("정보를 불러오는데 실패했습니다.");
			}
		});
	});
	</script>
</body>
</html>