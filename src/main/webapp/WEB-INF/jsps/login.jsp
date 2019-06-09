<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
       <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
</head>
<body>
<h2 style="text-align:center">登录页面</h2>
    <form:form  action="/user/login" method="post" modelAttribute="user" class="form-inline text-center" >
       <br/>
       <br/>
       <label>username:<form:input path="username" /></label>  
   <c:if test="${usernameError!=null}" ><span  class="alert alert-danger ">${usernameError} </span> </c:if>
       <br/>
            <br/>
         <label>password:<form:password path="password"  /></label>
         
                 <c:if test="${passwordError!=null}" ><span  class="alert alert-danger">${passwordError} </span> </c:if>
          <br/>
    <c:if test="${userError!=null}" ><br/><div class="text-center"><span  class="alert alert-danger ">${userError} </span></div> <br/></c:if>
         <a class="btn btn-primary" href="/user/register">注册</a><input type="submit" class="btn btn-primary" value="登陆"> 
    </form:form>
</body>
</html>