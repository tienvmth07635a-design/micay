<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%-- ============================================================
     ĐƯỜNG DẪN MENU: chỗ DUY NHẤT cần sửa link.
     Mở JSP trang chủ của nhóm, copy đúng href từng mục rồi dán vào đây.
     ============================================================ --%>
<c:set var="urlTrangChu"  value="${ctx}/home"/>
<c:set var="urlBan"       value="${ctx}/ban"/>
<c:set var="urlSanPham"   value="${ctx}/san-pham"/>
<c:set var="urlHoaDon"    value="${ctx}/hoa-don"/>
<c:set var="urlChiTietHD" value="${ctx}/hoa-don-chi-tiet"/>
<c:set var="urlKhachHang" value="${ctx}/khach-hang"/>
<c:set var="urlKhuyenMai" value="${ctx}/khuyen-mai"/>
<c:set var="urlVoucher"   value="${ctx}/voucher"/>
<c:set var="urlNhanVien"  value="${ctx}/nhan-vien"/>
<c:set var="urlQuanLy"    value="${ctx}/quan-ly"/>
<c:set var="urlNhatKi"    value="${ctx}/nhat-ki"/>
<c:set var="urlDangXuat"  value="${ctx}/dang-xuat"/>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Nhật Ký Hoạt Động Hệ Thống</title>
    <style>
        body { margin: 0; }
        .mc-layout, .mc-layout * { box-sizing: border-box; }
        .mc-layout {
            display: flex; min-height: 100vh;
            font-family: "Segoe UI", Roboto, Arial, sans-serif;
            background: #f4f6fb; color: #1f2937;
        }

        /* ===== MENU TRÁI ===== */
        .mc-sidebar {
            width: 204px; flex-shrink: 0;
            background: linear-gradient(180deg, #1b2a94 0%, #0e1a70 100%);
            color: #fff; display: flex; flex-direction: column;
            position: sticky; top: 0; height: 100vh;
        }
        .mc-brand {
            display: flex; align-items: center; gap: 8px;
            padding: 16px 14px; font-size: 16px; font-weight: 700;
            border-bottom: 1px solid rgba(255,255,255,.12);
        }
        .mc-profile {
            display: flex; align-items: center; gap: 10px; padding: 14px;
            border-bottom: 1px solid rgba(255,255,255,.12);
        }
        .mc-avatar-red {
            width: 34px; height: 34px; border-radius: 50%; background: #e03e4e;
            display: flex; align-items: center; justify-content: center;
            font-weight: 700; font-size: 13px;
        }
        .mc-profile .mc-name { font-size: 14px; font-weight: 600; }
        .mc-profile .mc-role { font-size: 11px; opacity: .7; }
        .mc-menu { flex: 1; overflow-y: auto; padding: 6px; }
        .mc-menu-title {
            font-size: 11px; font-weight: 700; letter-spacing: .4px; text-transform: uppercase;
            color: #a9b6ef; padding: 14px 8px 6px;
        }
        .mc-menu a {
            display: flex; align-items: center; gap: 10px;
            padding: 10px 12px; margin: 2px 0; border-radius: 8px;
            color: #e6ebff; text-decoration: none; font-size: 14px;
        }
        .mc-menu a:hover { background: rgba(255,255,255,.1); }
        .mc-menu a.active { background: #2f5be6; color: #fff; font-weight: 600; }
        .mc-ico { width: 20px; text-align: center; }
        .mc-logout { border-top: 1px solid rgba(255,255,255,.12); padding: 14px; }
        .mc-logout a {
            display: flex; align-items: center; gap: 10px;
            color: #e6ebff; text-decoration: none; font-size: 14px;
        }

        /* ===== NỘI DUNG ===== */
        .mc-main { flex: 1; min-width: 0; display: flex; flex-direction: column; }
        .mc-topbar {
            background: #fff; border-bottom: 1px solid #e5e8f0;
            height: 52px; padding: 0 24px;
            display: flex; align-items: center; justify-content: space-between;
        }
        .mc-topbar h1 { font-size: 17px; font-weight: 600; margin: 0; }
        .mc-user { display: flex; align-items: center; gap: 10px; font-size: 13px; color: #555; }
        .mc-avatar-blue {
            width: 30px; height: 30px; border-radius: 50%; background: #1a73e8; color: #fff;
            display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 13px;
        }
        .mc-content { padding: 18px 24px; }
        .mc-card {
            background: #fff; border-radius: 8px; padding: 22px;
            box-shadow: 0 1px 3px rgba(16,24,40,.08);
        }

        /* ===== BẢNG NHẬT KÝ (hàng tiêu đề đen, 4 cột) ===== */
        .nk-title { font-size: 18px; font-weight: 600; margin: 0 0 16px; }
        .nk-table { width: 100%; border-collapse: collapse; font-size: 14px; }
        .nk-table thead th {
            background: #1f2125; color: #fff;
            text-align: left; padding: 11px 8px; font-weight: 600;
        }
        .nk-table tbody td { padding: 10px 8px; }
        .nk-table tbody tr:nth-child(odd) { background: #f1f1f1; }
        .nk-table tbody tr:nth-child(even) { background: #fff; }
        .nk-empty { text-align: center; color: #888; padding: 20px; }
    </style>
</head>
<body>

<div class="mc-layout">

    <!-- ===== MENU TRÁI: đủ 11 mục như trang chủ của nhóm ===== -->
    <aside class="mc-sidebar">
        <div class="mc-brand"><span>🍜</span>Mì Cay Seoul 7 Cấp</div>

        <div class="mc-profile">
            <div class="mc-avatar-red">Q</div>
            <div>
                <div class="mc-name">Quản Lý Cửa Hàng</div>
                <div class="mc-role">Admin</div>
            </div>
        </div>

        <nav class="mc-menu">
            <div class="mc-menu-title">Tổng quan &amp; bán hàng</div>
            <a href="${urlTrangChu}"><span class="mc-ico">🏠</span>Trang chủ</a>
            <a href="${urlBan}"><span class="mc-ico">🍽️</span>Bàn</a>
            <a href="${urlSanPham}"><span class="mc-ico">🍜</span>Sản phẩm</a>
            <a href="${urlHoaDon}"><span class="mc-ico">🧾</span>Hóa đơn</a>
            <a href="${urlChiTietHD}"><span class="mc-ico">📋</span>Chi tiết hóa đơn</a>
            <a href="${urlKhachHang}"><span class="mc-ico">👥</span>Khách hàng</a>

            <div class="mc-menu-title">Ưu đãi, nhân sự &amp; hệ thống</div>
            <a href="${urlKhuyenMai}"><span class="mc-ico">🏷️</span>Khuyến mãi</a>
            <a href="${urlVoucher}"><span class="mc-ico">🎟️</span>Voucher</a>
            <a href="${urlNhanVien}"><span class="mc-ico">👤</span>Nhân viên</a>
            <a href="${urlQuanLy}"><span class="mc-ico">👔</span>Quản lý</a>
            <a href="${urlNhatKi}" class="active"><span class="mc-ico">🕒</span>Nhật ký hoạt động</a>
        </nav>

        <div class="mc-logout">
            <a href="${urlDangXuat}"><span class="mc-ico">↪</span>Đăng xuất</a>
        </div>
    </aside>

    <!-- ===== NỘI DUNG ===== -->
    <div class="mc-main">
        <div class="mc-topbar">
            <h1>Nhật Ký Hoạt Động Hệ Thống</h1>
            <div class="mc-user">Xin chào, <b>Admin</b> <div class="mc-avatar-blue">A</div></div>
        </div>

        <div class="mc-content">
            <div class="mc-card">
                <h2 class="nk-title">Nhật ký hoạt động hệ thống</h2>

                <table class="nk-table">
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
                            <tr><td colspan="4" class="nk-empty">Chưa có dữ liệu</td></tr>
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
    </div>

</div>

</body>
</html>
