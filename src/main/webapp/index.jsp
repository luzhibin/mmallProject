<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<body>
<h2>Hello index.jsp!</h2><br>
<h3>SpringMVC上传文件</h3><br>
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="SpringMVC上传文件"/>
</form><br>
<h3>富文本图片上传</h3>
<form name="form" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="富文本图片上传">
</form>
</body>
</html>
