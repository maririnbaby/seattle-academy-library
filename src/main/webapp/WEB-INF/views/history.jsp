<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=utf8"%>
<%@ page import="java.util.*"%>
<html>
<head>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
<meta charset="UTF-8">
<title>ホーム｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
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
        <div class="mx-auto" style="width: 800px;">
            <table class="table table-bordered">
                <thead>
                    <tr class="table-primary">
                        <th scope="col" style="width: 15%">書籍名</th>
                        <th scope="col" style="width: 5%">貸出日</th>
                        <th scope="col" style="width: 5%">返却日</th>
                    </tr>
                </thead>
                <c:forEach var="historyBookInfo" items="${historyBookList}">
                    <tbody>
                        <tr>
                            <td>
                                <form method="post" class="title" action="<%=request.getContextPath()%>/details">
                                    <a href="javascript:void(0)" onclick="this.parentNode.submit();">${historyBookInfo.title}</a><input type="hidden" name="bookId" value="${historyBookInfo.bookId}">
                                </form>
                            </td>
                            <td>${historyBookInfo.rentDate}</td>
                            <td>${historyBookInfo.returnDate}</td>
                        </tr>
                    </tbody>
                </c:forEach>
            </table>
        </div>
    </main>
</body>
</html>
