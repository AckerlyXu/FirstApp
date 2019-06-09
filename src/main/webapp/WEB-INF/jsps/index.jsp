<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
      <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

<title>Insert title here</title>
</head>
<body>
   <h1 style="text-align: center">首页</h1>

<nav class="navbar navbar-default">
  <div class="container-fluid">
   

   
    <div class="collapse navbar-collapse text-center" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="active"></li>
          <c:if test="${user!=null}">
        <li > <a href="/file/list" style="color:blue ">我的硬盘</a></li>
        <li> <a href="/file/add" style="color:blue ">添加文件或文件夹</a></li>
        
       </c:if>
       
       
      </ul>
   
   
         <c:if test="${user!=null}">
            <ul class="nav navbar-nav navbar-right">
        <li > <a href="#">欢迎:${user.username }</a></li>
        <li><a href="javascript:void(0)" style="color:blue ">退出登录</a></li>
        </ul>
       </c:if>
       
       <c:if test="${user ==null}">
         <ul class="nav  ">
        <li class="active"> <a href="/user/login"  >你好，请登陆</a></li>
         </ul>
       </c:if>
        
     
    </div>
  </div>
  
  
</nav>
<h3 style="text-align: center">共享文件</h3>
            <table class="table">
     <thead>
        <tr>
           <th>文件名</th>
           <th>路径</th>
           <th>文件大小</th>
           <th>上传时间</th>
                <th>分享者</th>
           <th>下载</th>
      
        </tr>
     </thead>
     <c:forEach items="${page.files }" var="file" >
      <tr>
      <td> <img src="${file.fileImage.path }" alt="图标" width="48" height="48">${file.filename}</td>
       <td>${file.filePath }</td>
      <td>${file.fileSize}</td>
      <td>${file.createdDate }</td>
       
         <td>${file.user.username }</td>
      <td>
      
      <c:if test="${!file.directory }">
       <a href="/file/download?fileId=${file.id}">下载</a>
       </c:if>
       </td>
       </tr>
      </c:forEach>
   </table>
   <div class="text-center">
   
   <nav aria-label="...">
  <ul class="pagination">
  <c:if test="${page.currentPage!=1 }">
   <li class="page-item">
      <a class="page-link" href="?currentPage=${page.currentPage-1 }" aria-label="Previous">
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>
    </c:if>
    <c:forEach  begin="1" end="${page.totalPage }" var="number">
       <li class="page-item  ${number==page.currentPage?'active':'' }"><a class="page-link" href="?currentPage=${number }">${number }</a></li>
    </c:forEach>
   
   
      
     <c:if test="${page.currentPage!=page.totalPage && page.totalPage !=0 }">
     <li class="page-item">
      <a class="page-link" href="?currentPage=${page.currentPage+1 }" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  
    </c:if>
  
  </ul>
</nav>
   </div>
</body>
<script>
  $(function(){
	  
	    $(".navbar-right a:contains('退出登录')").click(function(){
	    	
	    	 $.ajax({
	    		 url:"/user/logout",
	    		 method:"get",
	    		 
	    		 success:function(data){
	    			 
	    				$(".navbar-right").html(
	    					'<li class="active"> <a href="/user/login"  >你好，请登陆</a></li>'	
	    				);
	    				$(".navbar-right").removeClass('navbar-right navbar-nav')
	    				$(".navbar-collapse .nav:first").html("");
	    				
	    			
	    		 }
	    	 })
	    })
	  
  })
</script>
</html>