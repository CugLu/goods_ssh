<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="">
    
    <title>body</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
a {text-decoration: none;}
</style>
  </head>
  <body>
    <h1>欢迎进入LJ网上书城系统</h1>
    <a href="https://www.amazon.cn/" target="_top">
      <img src="<c:url value='/images/amazon.jpg'/>" border="0" style="border:1px #DCD8D7 solid;"/>
    </a>
    <br/>
    <a href="http://www.dangdang.com/" target="_top">
      <img src="<c:url value='/images/DDlogoNEW.gif'/>" border="0"/>
    </a>
    <a href="https://www.taobao.com/" target="_top">
      <img src="<c:url value='/images/taobao.jpg'/>" border="0"/>
    </a>
  </body>
</html>
