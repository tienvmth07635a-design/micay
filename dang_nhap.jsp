<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Mì Cay Seoul 7 Cấp</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: "Segoe UI", Roboto, Arial, sans-serif;
            background: linear-gradient(135deg, #1b2a94 0%, #0e1a70 100%);
        }
        .login-card {
            width: 100%;
            max-width: 400px;
            background: #fff;
            border-radius: 12px;
            padding: 32px 28px;
            box-shadow: 0 12px 32px rgba(0, 0, 0, 0.25);
        }
        .brand { text-align: center; margin-bottom: 24px; }
        .brand .logo { font-size: 36px; }
        .brand h2 { margin: 6px 0 4px; color: #1b2a94; font-size: 22px; }
        .brand p { margin: 0; color: #6b7280; font-size: 13px; }
        .alert-err {
            background: #fee2e2;
            color: #b91c1c;
            padding: 10px 12px;
            border-radius: 8px;
            font-size: 13px;
            margin-bottom: 16px;
            border: 1px solid #fecaca;
        }
        .form-group { margin-bottom: 16px; }
        label {
            display: block;
            font-size: 13px;
            font-weight: 600;
            color: #374151;
            margin-bottom: 6px;
        }
        input {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            outline: none;
        }
        input:focus {
            border-color: #2f5be6;
            box-shadow: 0 0 0 3px rgba(47, 91, 230, 0.15);
        }
        button {
            width: 100%;
            padding: 11px;
            background: #e03e4e;
            color: #fff;
            border: none;
            border-radius: 8px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 6px;
        }
        button:hover { background: #c82e3e; }
        .hint {
            margin-top: 16px;
            padding-top: 12px;
            border-top: 1px solid #e5e7eb;
            font-size: 12px;
            color: #6b7280;
            line-height: 1.6;
        }
    </style>
</head>
<body>

<div class="login-card">
    <div class="brand">
        <div class="logo">🍜</div>
        <h2>Mì Cay Seoul 7 Cấp</h2>
        <p>Đăng nhập hệ thống quản lý &amp; bán hàng</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert-err"><c:out value="${errorMessage}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/dang-nhap">
        <div class="form-group">
            <label for="taiKhoan">Email hoặc Số điện thoại</label>
            <input type="text" id="taiKhoan" name="taiKhoan" required
                   value="<c:out value='${taiKhoan}'/>"
                   placeholder="VD: nhanvien@gmail.com hoặc quanly@gmail.com">
        </div>

        <div class="form-group">
            <label for="matKhau">Mật khẩu</label>
            <input type="password" id="matKhau" name="matKhau" required placeholder="Nhập mật khẩu">
        </div>

        <button type="submit">Đăng nhập</button>
    </form>

    <div class="hint">
        Tài khoản mẫu (mật khẩu <b>123456</b>):<br>
        • Quản lý: <code>quanly@gmail.com</code><br>
        • Nhân viên: <code>nhanvien@gmail.com</code>
    </div>
</div>

</body>
</html>
