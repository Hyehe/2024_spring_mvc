<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>배열처리</title>
</head>
<body>
	<h1>결과보기</h1>
	<c:forEach var="k" items="${carName}">
		<h3>${k}</h3>
	</c:forEach>
	<hr>
</body>
</html>