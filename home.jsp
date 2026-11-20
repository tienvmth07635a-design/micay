<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ - Quản Lý Quán Mì Cay</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="app-wrapper">
    <!-- Sidebar trái đầy đủ các bảng SQL + Trang chủ -->
    <aside class="sidebar">
        <div>
            <div class="sidebar-brand">
                <span>🍜</span>
                <span>nhóm 3</span>
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
                    <a class="sidebar-link active" href="${pageContext.request.contextPath}/HomeServlet">
                        <span>🏠</span>
                        <span>Trang chủ</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/ban">
                        <span>🍽️</span>
                        <span>Bàn</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/san-pham">
                        <span>🍜</span>
                        <span>Sản phẩm</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/hoa-don">
                        <span>🧾</span>
                        <span>Hóa đơn</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/hoa-don-chi-tiet">
                        <span>📋</span>
                        <span>Chi tiết hóa đơn</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/khach-hang">
                        <span>🙋</span>
                        <span>Khách hàng</span>
                    </a>
                </li>
            </ul>

            <div class="sidebar-group-title">Ưu đãi, Nhân sự &amp; Hệ thống</div>
            <ul class="sidebar-menu">
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/khuyen-mai">
                        <span>🏷️</span>
                        <span>Khuyến mãi</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/voucher">
                        <span>🎟️</span>
                        <span>Voucher</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/nhan-vien">
                        <span>👨‍🍳</span>
                        <span>Nhân viên</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/quan-ly">
                        <span>👔</span>
                        <span>Quản lý</span>
                    </a>
                </li>
                <li>
                    <a class="sidebar-link" href="${pageContext.request.contextPath}/nhat-ki">
                        <span>🕒</span>
                        <span>Nhật ký hoạt động</span>
                    </a>
                </li>
            </ul>
        </div>

        <div class="sidebar-footer">
            <a class="sidebar-link mb-0" href="${pageContext.request.contextPath}/home">
                <span>↪</span>
                <span>Đăng xuất</span>
            </a>
        </div>
    </aside>

    <!-- Nội dung chính -->
    <div class="main-content">
        <header class="topbar">
            <h1 class="topbar-title">Trang chủ - Quản Lý Quán Mì Cay</h1>
            <div class="d-flex align-items-center gap-2">
                <span class="text-muted small">Xin chào, Admin</span>
                <span class="user-badge">A</span>
            </div>
        </header>

        <main class="content-container">
            <!-- Hàng thẻ thống kê -->
            <div class="row g-3 mb-4">
                <div class="col-6 col-md-4 col-xl-2">
                    <div class="kpi-card accent-blue">
                        <div class="kpi-label">Tổng số bàn</div>
                        <div class="kpi-value">${dsBan != null ? dsBan.size() : 0}</div>
                        <div class="kpi-sub">Khu vực phục vụ</div>
                    </div>
                </div>
                <div class="col-6 col-md-4 col-xl-2">
                    <div class="kpi-card accent-amber">
                        <div class="kpi-label">Tổng sản phẩm</div>
                        <div class="kpi-value">${tongSanPham != null ? tongSanPham : 0}</div>
                        <div class="kpi-sub">Món ăn &amp; đồ uống</div>
                    </div>
                </div>
                <div class="col-6 col-md-4 col-xl-2">
                    <div class="kpi-card accent-green">
                        <div class="kpi-label">Tổng hóa đơn</div>
                        <div class="kpi-value">${tongHoaDon != null ? tongHoaDon : 0}</div>
                        <div class="kpi-sub">Đơn hàng đã tạo</div>
                    </div>
                </div>
                <div class="col-6 col-md-4 col-xl-2">
                    <div class="kpi-card accent-blue">
                        <div class="kpi-label">Doanh thu hôm nay</div>
                        <div class="kpi-value">
                            <fmt:formatNumber value="${doanhThuHomNay != null ? doanhThuHomNay : 0}" pattern="#,##0"/>
                        </div>
                        <div class="kpi-trend-up">▲ VNĐ</div>
                    </div>
                </div>
                <div class="col-6 col-md-4 col-xl-2">
                    <div class="kpi-card accent-green">
                        <div class="kpi-label">Doanh thu tháng này</div>
                        <div class="kpi-value">
                            <fmt:formatNumber value="${doanhThuThangNay != null ? doanhThuThangNay : 0}" pattern="#,##0"/>
                        </div>
                        <div class="kpi-trend-up">▲ VNĐ</div>
                    </div>
                </div>
                <div class="col-6 col-md-4 col-xl-2">
                    <div class="kpi-card accent-amber">
                        <div class="kpi-label">Thao tác nhanh</div>
                        <div class="kpi-value" style="font-size: 1rem;">POS / Đơn</div>
                        <a id="btn_xemHoaDon" href="${pageContext.request.contextPath}/hoa-don"
                           class="btn btn-sm btn-outline-primary w-100 mt-1 py-0" style="font-size: 0.75rem;">
                            Xem hóa đơn
                        </a>
                    </div>
                </div>
            </div>

            <!-- Danh sách trạng thái bàn + bộ lọc -->
            <section class="section-panel">
                <form id="frm_locBan" method="get" action="${pageContext.request.contextPath}/home"
                      class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
                    <h2 class="h6 fw-bold mb-0">Danh sách trạng thái bàn</h2>

                    <div class="d-flex flex-wrap align-items-center gap-2">
                        <select id="sel_trangThai" name="trangThai" class="form-select form-select-sm" style="width: 170px;">
                            <option value="ALL" ${param.trangThai == 'ALL' || empty param.trangThai ? 'selected' : ''}>-- Tất cả trạng thái --</option>
                            <option value="Trống" ${param.trangThai == 'Trống' ? 'selected' : ''}>Trống</option>
                            <option value="Đang phục vụ" ${param.trangThai == 'Đang phục vụ' ? 'selected' : ''}>Đang phục vụ</option>
                        </select>

                        <input type="text" id="txt_timBan" name="keyword" value="${param.keyword}"
                               class="form-control form-control-sm" style="width: 210px;"
                               placeholder="Nhập tên hoặc mã bàn...">

                        <button type="submit" id="btn_timKiemBan" class="btn btn-sm btn-order-blue px-3">
                            🔍 Tìm kiếm
                        </button>
                    </div>
                </form>

                <div class="row g-3">
                    <c:forEach var="ban" items="${dsBan}">
                        <c:set var="isEmptyTable" value="${ban.trangThai == 'Trống'}"/>
                        <div class="col-6 col-md-4 col-lg-2">
                            <div class="table-card">
                                <div class="table-thumb">
                                    <img src="${pageContext.request.contextPath}/WEB-INF/image/ban-default.jpg"
                                         alt="${ban.tenBan}">
                                    <span class="badge-floor">${ban.maBan}</span>
                                    <span class="badge-status ${isEmptyTable ? 'empty' : 'occupied'}">
                                        • ${ban.trangThai}
                                    </span>
                                </div>

                                <div class="table-body">
                                    <div>
                                        <div class="table-title">${ban.tenBan}</div>
                                        <div class="table-meta ${isEmptyTable ? '' : 'amount'}">
                                            <c:choose>
                                                <c:when test="${isEmptyTable}">Sẵn sàng đón khách</c:when>
                                                <c:otherwise>Đang phục vụ</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <c:choose>
                                        <c:when test="${isEmptyTable}">
                                            <a href="${pageContext.request.contextPath}/hoa-don?maBan=${ban.maBan}"
                                               class="btn btn-sm btn-order-blue w-100">
                                                + Gọi món / Mở bàn
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/hoa-don?maBan=${ban.maBan}"
                                               class="btn btn-sm btn-pay-red w-100">
                                                🧾 Thêm món / Thanh toán
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${empty dsBan}">
                        <div class="col-12 text-center text-muted py-4">
                            Chưa có dữ liệu bàn nào trong hệ thống.
                        </div>
                    </c:if>
                </div>
            </section>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
