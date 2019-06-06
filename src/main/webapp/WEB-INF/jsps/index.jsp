<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
      <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
   <h1>这是index.jsp</h1>
   <c:if test="${user!=null}"><div style="text-align:center" class="alert alert-primary">欢迎:${user.username }</div></c:if>
</body>
</html>