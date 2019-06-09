<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
<script
  src="http://code.jquery.com/jquery-3.4.1.js"
></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
</head>
<body>
       <h1 style="text-align: center">用户管理</h1>
       <table class="table">
         <thead>
          <tr>
           <th>用户名</th>
           <th>删除</th>
          </tr>
         </thead>
         <tbody>
         <c:forEach items="${users }" var="user">
          <tr>
            <td> ${user.username }</td>
            <td>
            
              <c:if test="${!(user.username == 'admin') }"><a href="/user/delete/${user.id }">删除用户</a></c:if>
            </td>
          </tr>
          </c:forEach>
         </tbody>
       </table>
</body>
 <script>
           $("table tr td:nth-child(2) a").click(function(e) {
			if(!confirm("您确定删除该用户吗?")){ e.preventDefault();}
			  
		}
           
           )
 </script>
</html>