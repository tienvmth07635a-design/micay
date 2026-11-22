<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Nhân Sự - Mì Cay Seoul 7 Cấp</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        /* Tùy chỉnh phong cách cho 2 nút tab chuyển đổi trạng thái */
        .tab-btn {
            background-color: #e2e8f0;
            color: #475569;
            border: none;
            padding: 6px 16px;
            font-size: 0.85rem;
            font-weight: 600;
            border-radius: 6px;
            transition: all 0.2s ease;
        }
        .tab-btn.active-tab {
            background-color: #0f172a;
            color: #ffffff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<div class="app-wrapper">
    <!-- Sidebar -->
    <aside class="sidebar">
        <div>
            <div class="sidebar-brand">
                <span>🍜</span>
                <span>Mì Cay Seoul 7 Cấp</span>
            </div>

            <div class="sidebar-admin">
                <span class="admin-avatar">Q</span>
                <div>
                    <div class="fw-semibold small">Quản Lý Cửa Hàng</div>
                    <div class="text-white-50" style="font-size: 0.72rem;">Admin</div>
                </div>
            </div>

            <div class="sidebar-group-title">Tổng quan &amp; Bán hàng</div>
            <ul class="sidebar-menu">
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/HomeServletServlet-servlet">
                        <span>🏠</span><span>Trang chủ</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/ban">
                        <span>🍽️</span><span>Bàn</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/san-pham">
                        <span>🍜</span><span>Sản phẩm</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/hoa-don">
                        <span>🧾</span><span>Hóa đơn</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/hoa-don-chi-tiet">
                        <span>📋</span><span>Chi tiết hóa đơn</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/khach-hang">
                        <span>🙋</span><span>Khách hàng</span>
                    </a>
                </li>
            </ul>

            <div class="sidebar-group-title">Ưu đãi, Nhân sự &amp; Hệ thống</div>
            <ul class="sidebar-menu">
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/khuyen-mai">
                        <span>🏷️</span><span>Khuyến mãi</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/voucher">
                        <span>🎟️</span><span>Voucher</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link active" href="${pageContext.request.contextPath}/nhan-vien">
                        <span>👨‍🍳</span><span>Nhân viên</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/quan-ly">
                        <span>👔</span><span>Quản lý</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/nhat-ki">
                        <span>🕒</span><span>Nhật ký hoạt động</span>
                    </a>
                </li>
            </ul>
        </div>

        <div class="sidebar-footer">
            <a class="sidebar-link mb-0" href="${pageContext.request.contextPath}/HomeServletServlet-servlet">
                <span>↪</span><span>Đăng xuất</span>
            </a>
        </div>
    </aside>

    <!-- Main Content -->
    <div class="main-content">
        <header class="topbar">
            <h1 class="topbar-title">Quản Lý Nhân Sự Cửa Hàng</h1>
            <div class="d-flex align-items-center gap-2">
                <span class="text-muted small">Xin chào, Admin</span>
                <span class="user-badge">A</span>
            </div>
        </header>

        <main class="content-container">
            <!-- Form Thêm / Sửa Nhân Viên -->
            <section class="section-panel mb-4">
                <h2 class="h6 fw-bold mb-3 text-primary">
                    <span>➕</span> ${not empty nv ? 'Cập Nhật Thông Tin Nhân Viên' : 'Thêm Nhân Viên Mới'}
                </h2>
                <form action="${not empty nv ? pageContext.request.contextPath.concat('/nhan-vien?action=sua') : pageContext.request.contextPath.concat('/nhan-vien?action=them')}" method="POST">
                    <input type="hidden" name="maNhanVien" value="${nv.maNhanVien}">

                    <div class="row">
                        <div class="col-md-3 mb-3">
                            <label class="form-label small fw-bold text-secondary">Tên nhân viên:</label>
                            <input type="text" class="form-control form-control-sm" name="tenNhanVien" value="${nv.tenNhanVien}" placeholder="Nhập tên nhân viên..." required>
                        </div>

                        <div class="col-md-3 mb-3">
                            <label class="form-label small fw-bold text-secondary">Số điện thoại:</label>
                            <input type="text" class="form-control form-control-sm" name="soDienThoai" value="${nv.soDienThoai}" placeholder="Nhập số điện thoại..." required>
                        </div>

                        <div class="col-md-3 mb-3">
                            <label class="form-label small fw-bold text-secondary">Chức vụ:</label>
                            <select class="form-select form-select-sm" name="chucVu">
                                <option value="Nhân viên" ${nv.chucVu == 'Nhân viên' ? 'selected' : ''}>Nhân viên</option>
                                <option value="Quản lý" ${nv.chucVu == 'Quản lý' ? 'selected' : ''}>Quản lý</option>
                            </select>
                        </div>

                        <div class="col-md-3 mb-3">
                            <label class="form-label small fw-bold text-secondary">Tiền lương / giờ (VNĐ):</label>
                            <input type="number" step="1000" class="form-control form-control-sm" name="tienLuongGio" value="${nv.tienLuongGio}" placeholder="Ví dụ: 25000">
                        </div>

                        <div class="col-md-3 mb-3">
                            <label class="form-label small fw-bold text-secondary">Tính giờ làm (Số giờ):</label>
                            <input type="number" step="0.5" class="form-control form-control-sm" name="tinhGioLam" value="${nv.tinhGioLam}" placeholder="Ví dụ: 8.5">
                        </div>

                        <div class="col-md-3 mb-3">
                            <label class="form-label small fw-bold text-secondary">Trạng thái làm việc:</label>
                            <select class="form-select form-select-sm" name="trangThai">
                                <option value="Đang làm việc" ${nv.trangThai == 'Đang làm việc' ? 'selected' : ''}>Đang làm việc</option>
                                <option value="Hết giờ làm" ${nv.trangThai == 'Hết giờ làm' ? 'selected' : ''}>Nhân viên hết giờ làm</option>
                                <option value="Nghỉ làm" ${nv.trangThai == 'Nghỉ làm' ? 'selected' : ''}>Nghỉ làm / Khóa</option>
                            </select>
                        </div>
                    </div>

                    <div class="d-flex gap-2 mt-2">
                        <button type="submit" class="btn btn-sm btn-primary px-4 fw-semibold">
                            💾 ${not empty nv ? 'Cập nhật' : 'Thêm nhân viên'}
                        </button>
                        <a href="${pageContext.request.contextPath}/nhan-vien" class="btn btn-sm btn-light border text-secondary fw-semibold">
                            🔄 Làm mới
                        </a>
                    </div>
                </form>
            </section>

            <!-- Các Tab chuyển đổi trạng thái -->
            <div class="d-flex gap-2 mb-3">
                <button id="btn-active" class="tab-btn active-tab" onclick="filterTable('active')">
                    🟢 Đang hoạt động / Làm việc
                </button>
                <button id="btn-inactive" class="tab-btn" onclick="filterTable('inactive')">
                    🔴 Nghỉ làm / Khóa
                </button>
            </div>

            <!-- Bảng Danh Sách Nhân Viên -->
            <section class="section-panel">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h2 class="h6 fw-bold m-0 text-secondary">Danh sách nhân viên cửa hàng</h2>
                </div>

                <div class="table-responsive">
                    <table class="table align-middle text-center mb-0">
                        <thead>
                            <tr>
                                <th>Mã NV</th>
                                <th>Tên nhân viên</th>
                                <th>Số điện thoại</th>
                                <th>Chức vụ</th>
                                <th>Lương / h</th>
                                <th>Giờ làm</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${listNhanVien}">
                                <tr class="emp-row" data-status="${item.trangThai}">
                                    <td class="fw-bold text-primary">NV0${item.maNhanVien}</td>
                                    <td class="fw-semibold">${item.tenNhanVien != null ? item.tenNhanVien : 'Chưa cập nhật'}</td>
                                    <td>${item.soDienThoai != null ? item.soDienThoai : 'N/A'}</td>
                                    <td>
                                        <span class="badge ${item.chucVu == 'Quản lý' ? 'bg-danger' : 'bg-info text-dark'}">
                                            ${item.chucVu}
                                        </span>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${item.tienLuongGio != null ? item.tienLuongGio : 0}" pattern="#,##0"/> đ
                                    </td>
                                    <td>${item.tinhGioLam != null ? item.tinhGioLam : 0} h</td>
                                    <td>
                                        <span class="badge ${item.trangThai == 'Đang làm việc' ? 'bg-success' : (item.trangThai == 'Hết giờ làm' ? 'bg-warning text-dark' : 'bg-secondary')} px-2 py-1" style="font-size: 0.75rem;">
                                            ${item.trangThai != null ? item.trangThai : 'Đang làm việc'}
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/nhan-vien?action=chi-tiet&id=${item.maNhanVien}" class="btn btn-outline-primary btn-sm px-2 py-1">Sửa</a>
                                        <a href="${pageContext.request.contextPath}/nhan-vien?action=khoa&id=${item.maNhanVien}" class="btn btn-outline-danger btn-sm px-2 py-1" onclick="return confirm('Bạn có chắc chắn muốn cho nhân viên này nghỉ làm (Khóa)?');">Khóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listNhanVien}">
                                <tr>
                                    <td colspan="8" class="text-muted py-4">Chưa có dữ liệu nhân viên nào trong hệ thống.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </main>
    </div>
</div>

<script>
    function filterTable(type) {
        const rows = document.querySelectorAll('.emp-row');
        const btnActive = document.getElementById('btn-active');
        const btnInactive = document.getElementById('btn-inactive');

        if (type === 'active') {
            btnActive.classList.add('active-tab');
            btnInactive.classList.remove('active-tab');
            rows.forEach(row => {
                const status = row.getAttribute('data-status');
                if (status === 'Nghỉ làm') {
                    row.style.display = 'none';
                } else {
                    row.style.display = '';
                }
            });
        } else if (type === 'inactive') {
            btnInactive.classList.add('active-tab');
            btnActive.classList.remove('active-tab');
            rows.forEach(row => {
                const status = row.getAttribute('data-status');
                if (status === 'Nghỉ làm') {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
    }

    // Tự động chạy bộ lọc ẩn nhân viên "Nghỉ làm" khi vừa vào trang
    window.onload = function() {
        filterTable('active');
    };
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
