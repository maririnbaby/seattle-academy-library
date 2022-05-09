<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="UTF-8">
<title>一括登録｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="resources/js/thumbnail.js"></script>
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />       
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <form action="<%=request.getContextPath()%>/bulkResistBooks" method="post" enctype="multipart/form-data" id="data_upload_form">
    <div class="bulk_form">
    <h2>CSVファイルをアップロードすることで、書籍を一括登録することができます。</h2>
    <div class="caution">
    <p>「書籍名,著者名,出版社,出版日,ISBN」の形式で記載してください。</p>
    <p>※サムネイル画像は一括登録できません。編集画面で１冊編集してください。</p>
    </div> 
             
  
   <input type="file" name="file" accept=".csv">
 
    <div class="edtDelBookBtn_box">
                <button type="submit" id="bulk-btn" class="btn_bulkRegist">一括登録</button>
            </div>
            <div class="content_right">
            <c:forEach var="bulkError" items="${bulkError}">
            <c:if test="${!empty bulkError}">
            <div class="error" >
                <p>${bulkError}</p>
            </div></c:if>
            </c:forEach>
                           <c:if test="${!empty nofile}">
                           <div class="error" > 
                           <p>${nofile}</p>
                           </div></c:if>
                           </div>
    </div>          
              </form>
                 </main>
                 </body>
                 </html>
               
