<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Nhật kí hoạt động</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; background: #f5f5f5; }
        h2 { color: #c0392b; }
        .search-box { margin-bottom: 16px; }
        .search-box input[type=text] { padding: 8px; width: 280px; }
        .search-box button, .search-box a {
            padding: 8px 14px; border: none; background: #c0392b; color: #fff;
            cursor: pointer; text-decoration: none; font-size: 14px;
        }
        .search-box a { background: #7f8c8d; }
        table { width: 100%; border-collapse: collapse; background: #fff; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background: #c0392b; color: #fff; }
        tr:nth-child(even) { background: #fafafa; }
        .empty { text-align: center; color: #888; }
    </style>
</head>
<body>

<h2>Nhật kí hoạt động</h2>

<form class="search-box" action="${pageContext.request.contextPath}/nhat-ki" method="get">
    <input type="text" name="keyword" value="${fn:escapeXml(keyword)}"
           placeholder="Tìm theo hành động hoặc mã nhật kí...">
    <button type="submit">Tìm kiếm</button>
    <a href="${pageContext.request.contextPath}/nhat-ki">Làm mới</a>
</form>

<table>
    <thead>
    <tr>
        <th>Mã nhật kí</th>
        <th>Nhân viên</th>
        <th>Hành động</th>
        <th>Thời gian</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${empty dsNhatKy}">
            <tr><td colspan="4" class="empty">Chưa có dữ liệu</td></tr>
        </c:when>
        <c:otherwise>
            <c:forEach var="nk" items="${dsNhatKy}">
                <tr>
                    <td>${nk.maNhatKi}</td>
                    <td><c:out value="${nk.tenNhanVienHienThi}"/></td>
                    <td><c:out value="${nk.hanhDong}"/></td>
                    <td>${nk.thoiGianFormatted}</td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>

</body>
</html>
