<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">

<title>Insert title here</title>
</head>
<body>
<h2 style="text-align: center">添加文件或者文件夹</h2>
<br>
<br>

 <form:form action="/file/add" modelAttribute="userfile" method="POST"  enctype="multipart/form-data" cssClass="form-inline text-center">
  <label>  请输入文件名:<form:input path="filename" />
      </label> <br><br/>
      
      <label>是否文件夹:<form:checkbox path="directory"/></label><br><br/>
   <label>是否共享:<form:checkbox path="shared"/></label><br><br/>
        请选择父文件夹:<br/>
       
       
        <form:select path="parent.id">
            <form:option  value="" label="根目录" />
            
            <form:options items="${directories}" itemValue="id" itemLabel="filePath"/>
        </form:select> <br/>
     <%--  <form:radiobuttons path="parent.id"   items="${directories}" itemLabel="filePath" itemValue="id"/><br/> --%>
     <div id="filepart">
      <strong>请选择要上传的文件:</strong><br>
<label><input type="file" name="file"   ></label>
     <br/>
     </div>
      <input type="submit" class="btn btn-primary">
 </form:form>

 
  
</body>
<script>
   $(function(){
	   
	   
	   $("form :checkbox:first").change(function(){
		    if($(this).prop("checked")){
		    	 $("#filepart").hide();
		    	 $("form :checkbox:last").prop("disabled",true);
		    }else{
		    	 $("#filepart").show();
		    	 $("form :checkbox:last").prop("disabled",false);
		    }
		   
	   })
	   $("form").submit(function(){
		   if($(":checkbox:first").prop("checked")){
		    	 $("#filepart").remove();
		    	
		    }
		   
	   })
   })
</script>
</html>