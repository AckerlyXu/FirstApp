<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
         <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
<script
  src="http://code.jquery.com/jquery-3.4.1.js"
></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

</head>
<body>
   
   <h2 style="text-align:center">列表页面</h2>
   <a href="/file/add"  class="btn btn-primary">添加文件或文件夹</a>
   <form:form cssClass="form-inline" modelAttribute="page" action="/file/list" >
   <label>
     请输入要搜索的文件名:<form:input  path="filename"></form:input>
     </label> 
     <label>请选择要搜索的文件夹:<form:select path="directoryId" >
            <form:option  value="" label="根目录" />
            <form:options items="${directories}" itemValue="id" itemLabel="filePath"/>
        </form:select></label>
        <input type="submit" class="btn btn-primary">
   </form:form>
   <table class="table">
     <thead>
        <tr>
           <th>文件名</th>
           <th>路径</th>
           <th>文件大小</th>
           <th>上传时间</th>
           <th>是否共享</th>
           <th>移动文件</th>
           <th>下载</th>
        </tr>
     </thead>
     <c:forEach items="${page.files }" var="file" >
      <tr>
      <td> <img src="${file.fileImage.path }" alt="图标" width="48" height="48">${file.filename}</td>
       <td>${file.filePath }</td>
      <td>${file.fileSize}</td>
      <td>${file.createdDate }</td>
      <td>  <c:if test="${!file.directory }"> ${ file.shared?"是":"否" }</c:if></td>
       <td><c:if test="${!file.directory }">
        <input type="hidden" value="${file.id }"></input>
         <input type="hidden" value="${file.parent.id }"></input>
       <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#moveFile">
  移动文件
</button>
       
       </c:if></td>
       
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
      <a class="page-link" href="?currentPage=${page.currentPage-1 }&filename=${page.filename}&directoryId=${page.directoryId}" aria-label="Previous">
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>
    </c:if>
    <c:forEach  begin="1" end="${page.totalPage }" var="number">
       <li class="page-item  ${number==page.currentPage?'active':'' }"><a class="page-link" href="?currentPage=${number }&filename=${page.filename}&directoryId=${page.directoryId}">${number }</a></li>
    </c:forEach>
   
   
      
     <c:if test="${page.currentPage!=page.totalPage && page.totalPage !=0 }">
     <li class="page-item">
      <a class="page-link" href="?currentPage=${page.currentPage+1 }&filename=${page.filename}&directoryId=${page.directoryId}" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  
    </c:if>
  
  </ul>
</nav>
   </div>
   
   <div class="modal" tabindex="-1" role="dialog" id="moveFile">
     <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Modal title</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
         <form:form cssClass="form-inline text-center" modelAttribute="page" action="/file/list" >
  <input type="hidden" >
        <label>请选择要移动到的文件夹:<form:select  path="" >
      
            <form:option  value="" label="根目录"  />
            <form:options items="${directories}" itemValue="id" itemLabel="filePath"/>
        </form:select></label>
   
   </form:form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary">移动</button>
      </div>
    </div>
  </div>
</div>
</body>
<script>
        $(function() {
         
			 $("table tr td:nth-child(6) button").click(function(){
				 
				  $("#moveFile  input[type=hidden]" ).val( $(this).siblings(":eq(0)").val());
				  $("#moveFile select").val($(this).siblings(":eq(1)").val());
			 })
			 $("#moveFile button:contains(移动)").click(function(){
				  var fileId = $("#moveFile  input[type=hidden]" ).val();
				  var parentId = $("#moveFile select").val();
				  var parentPath = $("#moveFile select option:selected").text();
				  $.ajax({
					 url:"/file/move",
					  type:"POST",
					 dataType:"json",
					 data:{parentId:parentId,id:fileId},
					  success:function(data){
						  if(data.code ==200){
							    var parentTr=  $("table tr td:nth-child(6) input:nth-child(1)").filter("[value="+fileId+"]").parents("tr");
							       parentTr.find("td:eq(1)").html(parentPath);
							       parentTr.find("td:eq(5) input:eq(1)").val(parentId);
						  }else{
							  alert(data.msg);
						  }
					
					  },
					  error: function(){
						 alert("移动失败");
					  }
				 }) 
			 })
		})
</script>
</html>