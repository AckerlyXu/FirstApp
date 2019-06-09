<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
</head>
<body>
   <h2 style="text-align:center">修改密码</h2>
   
   <form action="/user/changePassword" method="post" class="form-inline text-center" >
    <label>原密码:<input type="password" name="originalPassword"></label>
    
    <c:if test="${originError!=null}" >&nbsp;&nbsp;<span  class="alert alert-danger ">${originError} </span>  <br/></c:if>
      
    <br/>
  
     <label>新密码:<input  type="password" name="newPassword"></label>
     <c:if test="${newError!=null}" >&nbsp;&nbsp;<span  class="alert alert-danger ">${newError} </span> <br/></c:if>
    
     <br/>
     <input type="submit" class="btn btn-primary">
   </form>
</body>
</html>