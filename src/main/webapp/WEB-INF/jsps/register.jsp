<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>注册</title>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
</head>
<body>
<h2 style="text-align:center">注册页面</h2>
    <form:form  action="/user/register" method="post" modelAttribute="user" class="form-inline text-center" >
       <br/>
       <br/>
       <label>username:<form:input path="username" /></label>  
  <c:if test="${usernameError!=null}" ><span  class="alert alert-danger ">${usernameError} </span> </c:if>
       <br/>
            <br/>
         <label>password:<form:password path="password"  /></label>
         
                 <c:if test="${passwordError!=null}" ><span  class="alert alert-danger">${passwordError} </span> </c:if>
          <br/>
         <input type="submit" class="btn btn-primary">
    </form:form>
</body>
</html>