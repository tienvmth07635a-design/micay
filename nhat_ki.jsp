<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Nhật Ký Hoạt Động Hệ Thống</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            font-family: "Segoe UI", Roboto, Arial, sans-serif;
            background: #f3f4f8;
            color: #222;
            display: flex;
            min-height: 100vh;
        }

        /* ===== SIDEBAR ===== */
        .sidebar {
            width: 234px;
            background: #1a237e;
            color: #fff;
            display: flex;
            flex-direction: column;
            position: fixed;
            top: 0; bottom: 0; left: 0;
        }
        .brand {
            padding: 18px 16px;
            font-size: 18px;
            font-weight: 700;
            color: #ffd54f;
            border-bottom: 1px solid rgba(255,255,255,.12);
        }
        .brand i { margin-right: 8px; color: #ffb300; }
        .profile {
            display: flex; align-items: center; gap: 10px;
            padding: 14px 16px;
            border-bottom: 1px solid rgba(255,255,255,.12);
        }
        .avatar-red {
            width: 32px; height: 32px; border-radius: 50%;
            background: #e53950; display: flex; align-items: center; justify-content: center;
            font-weight: 700; font-size: 13px;
        }
        .profile .name { font-size: 13px; font-weight: 600; }
        .profile .role { font-size: 11px; opacity: .7; }
        .menu-title {
            font-size: 11px; letter-spacing: .5px; text-transform: uppercase;
            color: rgba(255,255,255,.55);
            padding: 16px 16px 6px;
        }
        .menu a {
            display: flex; align-items: center; gap: 12px;
            margin: 2px 16px; padding: 10px 12px;
            color: #e8eaf6; text-decoration: none; font-size: 14px;
            border-radius: 8px;
        }
        .menu a i { width: 18px; text-align: center; }
        .menu a:hover { background: rgba(255,255,255,.1); }
        .menu a.active { background: #3949ab; color: #fff; }
        .logout { margin-top: auto; padding: 18px 16px; }
        .logout a {
            color: #ffd54f; text-decoration: none; font-size: 14px;
            display: flex; align-items: center; gap: 10px;
        }

        /* ===== NỘI DUNG ===== */
        .main { margin-left: 234px; flex: 1; padding: 16px 20px; }
        .topbar {
            background: #fff; border-radius: 8px;
            padding: 16px 20px;
            display: flex; justify-content: space-between; align-items: center;
            box-shadow: 0 1px 3px rgba(0,0,0,.06);
        }
        .topbar h1 { font-size: 18px; font-weight: 600; }
        .topbar .user { display: flex; align-items: center; gap: 10px; font-size: 13px; color: #555; }
        .avatar-blue {
            width: 34px; height: 34px; border-radius: 50%;
            background: #1976d2; color: #fff;
            display: flex; align-items: center; justify-content: center; font-weight: 700;
        }
        .card {
            background: #fff; border-radius: 8px;
            padding: 22px 22px 28px; margin-top: 22px;
            box-shadow: 0 1px 3px rgba(0,0,0,.06);
        }
        .card h2 { font-size: 18px; font-weight: 600; margin-bottom: 16px; }
        table { width: 100%; border-collapse: collapse; font-size: 14px; }
        thead th {
            background: #1f2125; color: #fff;
            text-align: left; padding: 11px 8px; font-weight: 600;
        }
        tbody td { padding: 10px 8px; }
        tbody tr:nth-child(odd) { background: #f1f1f1; }
        tbody tr:nth-child(even) { background: #fff; }
        .empty { text-align: center; color: #888; padding: 20px; }
    </style>
</head>
<body>

<!-- ===== SIDEBAR ===== -->
<aside class="sidebar">
    <div class="brand"><i class="fa-solid fa-fire-flame-curved"></i>Mì Cay Seoul 7 Cấp</div>

    <div class="profile">
        <div class="avatar-red">Q</div>
        <div>
            <div class="name">Quản Lý Cửa Hàng</div>
            <div class="role">Admin</div>
        </div>
    </div>

    <div class="menu">
        <div class="menu-title">Quản lý chung</div>
        <a href="${pageContext.request.contextPath}/bao-cao"><i class="fa-solid fa-chart-line"></i>Báo cáo</a>
        <a href="${pageContext.request.contextPath}/ban"><i class="fa-solid fa-utensils"></i>Bàn</a>
        <a href="${pageContext.request.contextPath}/san-pham"><i class="fa-solid fa-bowl-food"></i>Mặt hàng</a>

        <div class="menu-title">Nhân sự &amp; hệ thống</div>
        <a href="${pageContext.request.contextPath}/nhan-vien"><i class="fa-solid fa-users"></i>Quản lí nhân viên</a>
        <a href="${pageContext.request.contextPath}/nhat-ki" class="active"><i class="fa-solid fa-clock-rotate-left"></i>Nhật ký</a>
        <a href="${pageContext.request.contextPath}/cai-dat"><i class="fa-solid fa-gear"></i>Cài đặt</a>
    </div>

    <div class="logout">
        <a href="${pageContext.request.contextPath}/dang-xuat"><i class="fa-solid fa-right-from-bracket"></i>Đăng xuất</a>
    </div>
</aside>

<!-- ===== NỘI DUNG ===== -->
<div class="main">
    <div class="topbar">
        <h1>Nhật Ký Hoạt Động Hệ Thống</h1>
        <div class="user">Xin chào, <b>Admin</b> <div class="avatar-blue">A</div></div>
    </div>

    <div class="card">
        <h2>Nhật ký hoạt động hệ thống</h2>
        <table>
            <thead>
            <tr>
                <th style="width:10%">Mã NK</th>
                <th style="width:20%">Thời gian</th>
                <th style="width:25%">Nhân viên thực hiện</th>
                <th>Hành động chi tiết</th>
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
                            <td>#${nk.maNhatKi}</td>
                            <td>${nk.thoiGianFormatted}</td>
                            <td><c:out value="${nk.tenNhanVienHienThi}"/></td>
                            <td><c:out value="${nk.hanhDong}"/></td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
