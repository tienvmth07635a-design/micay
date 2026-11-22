<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cài Đặt Hệ Thống</title>
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

        /* ===== FORM ===== */
        .card {
            background: #fff; border-radius: 8px;
            padding: 22px 22px 26px; margin-top: 22px;
            max-width: 750px;
            box-shadow: 0 1px 3px rgba(0,0,0,.06);
        }
        .card h2 { font-size: 18px; font-weight: 600; margin-bottom: 18px; }
        .form-group { margin-bottom: 16px; }
        .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 22px; }
        label { display: block; font-size: 14px; font-weight: 600; margin-bottom: 6px; }
        input[type=text], input[type=number] {
            width: 100%; padding: 9px 12px;
            border: 1px solid #dcdfe6; border-radius: 6px;
            font-size: 14px; background: #fff;
        }
        input[type=text]:focus, input[type=number]:focus {
            outline: none; border-color: #1976d2; box-shadow: 0 0 0 2px rgba(25,118,210,.15);
        }
        .checkbox { display: flex; align-items: center; gap: 8px; font-size: 14px; margin-bottom: 16px; }
        .checkbox input { width: 16px; height: 16px; }
        .btn-save {
            background: #1a6ff0; color: #fff; border: none; cursor: pointer;
            padding: 10px 14px; border-radius: 6px; font-size: 14px; font-weight: 600;
            display: inline-flex; align-items: center; gap: 8px;
        }
        .btn-save:hover { background: #1558c4; }
        .alert { padding: 10px 14px; border-radius: 6px; font-size: 14px; margin-bottom: 16px; }
        .alert-success { background: #e6f4ea; color: #1e6b34; border: 1px solid #b7dfc2; }
        .alert-error { background: #fdecea; color: #a12622; border: 1px solid #f5c2bf; }
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
        <a href="${pageContext.request.contextPath}/nhat-ki"><i class="fa-solid fa-clock-rotate-left"></i>Nhật ký</a>
        <a href="${pageContext.request.contextPath}/cai-dat" class="active"><i class="fa-solid fa-gear"></i>Cài đặt</a>
    </div>

    <div class="logout">
        <a href="${pageContext.request.contextPath}/dang-xuat"><i class="fa-solid fa-right-from-bracket"></i>Đăng xuất</a>
    </div>
</aside>

<!-- ===== NỘI DUNG ===== -->
<div class="main">
    <div class="topbar">
        <h1>Cài Đặt Hệ Thống</h1>
        <div class="user">Xin chào, <b>Admin</b> <div class="avatar-blue">A</div></div>
    </div>

    <div class="card">
        <h2>Cài đặt hệ thống cửa hàng</h2>

        <c:if test="${thanhCong}">
            <div class="alert alert-success"><i class="fa-solid fa-circle-check"></i> Lưu cài đặt thành công.</div>
        </c:if>
        <c:if test="${not empty loi}">
            <div class="alert alert-error"><i class="fa-solid fa-circle-exclamation"></i> <c:out value="${loi}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/cai-dat" method="post">
            <div class="form-group">
                <label for="tenQuan">Tên quán</label>
                <input type="text" id="tenQuan" name="tenQuan" maxlength="255" required
                       value="<c:out value='${caiDat.tenQuan}'/>">
            </div>

            <div class="form-group">
                <label for="diaChi">Địa chỉ cửa hàng</label>
                <input type="text" id="diaChi" name="diaChi" maxlength="500" required
                       value="<c:out value='${caiDat.diaChi}'/>">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="soDienThoai">Số điện thoại liên hệ</label>
                    <input type="text" id="soDienThoai" name="soDienThoai" maxlength="10" required
                           value="<c:out value='${caiDat.soDienThoai}'/>">
                </div>
                <div class="form-group">
                    <label for="thueVat">Thuế VAT (%)</label>
                    <input type="number" id="thueVat" name="thueVat" min="0" max="100" step="0.01" required
                           value="<c:out value='${caiDat.thueVatHienThi}'/>">
                </div>
            </div>

            <label class="checkbox" style="font-weight: normal;">
                <input type="checkbox" name="tuDongInHoaDon" ${caiDat.tuDongInHoaDon ? 'checked' : ''}>
                Tự động in hóa đơn khi thanh toán hoàn tất
            </label>

            <button type="submit" class="btn-save"><i class="fa-solid fa-floppy-disk"></i> Lưu thay đổi</button>
        </form>
    </div>
</div>

</body>
</html>
