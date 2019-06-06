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
   <h2 style="text-align:center">修改密码</h2>
   
   <form action="/user/changePassword" method="post" class="form-inline text-center">
    <label>原密码:<input name="originalPassword"></label><br/>
     <label>新密码:<input name="newPassword"></label><br/>
     <input type="submit" class="btn btn-primary">
   </form>
</body>
</html>