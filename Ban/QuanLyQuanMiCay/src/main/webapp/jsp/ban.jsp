<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý bàn - Quản Lý Quán Mì Cay</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/home">🏠 Trang chủ</a></li>
                <li><a class="sidebar-link active" href="${pageContext.request.contextPath}/ban">🪑 Bàn</a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/san-pham">🍜 Sản phẩm</a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/hoa-don">🧾 Hóa đơn</a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/khach-hang">👤 Khách hàng</a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/khuyen-mai">🎁 Khuyến mãi</a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/voucher">🎟️ Voucher</a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/nhan-vien">🧑‍🍳 Nhân viên</a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/quan-ly">🛡️ Quản lý</a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/nhat-ki">📋 Nhật ký</a></li>
            </ul>
        </div>
    </aside>

    <!-- Nội dung chính -->
    <main class="main-content">
        <header class="topbar">
            <div class="topbar-title">Quản lý bàn</div>
        </header>

        <div class="content-container">

            <!-- Thông báo -->
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <c:out value="${success}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <c:out value="${error}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- KPI -->
            <div class="row g-3 mb-4">
                <div class="col-12 col-md-4">
                    <div class="kpi-card">
                        <div class="kpi-label">Tổng số bàn</div>
                        <div class="kpi-value">${tongBan != null ? tongBan : 0}</div>
                    </div>
                </div>
                <div class="col-12 col-md-4">
                    <div class="kpi-card">
                        <div class="kpi-label">Bàn trống</div>
                        <div class="kpi-value">${banTrong != null ? banTrong : 0}</div>
                    </div>
                </div>
                <div class="col-12 col-md-4">
                    <div class="kpi-card">
                        <div class="kpi-label">Đang phục vụ</div>
                        <div class="kpi-value">${banDangPhucVu != null ? banDangPhucVu : 0}</div>
                    </div>
                </div>
            </div>

            <div class="row g-3">
                <!-- Form thêm / sửa -->
                <div class="col-12 col-lg-4">
                    <section class="section-panel">
                        <h2 class="h6 fw-bold mb-3">
                            <c:choose>
                                <c:when test="${banEdit != null && banEdit.maBan != null}">Cập nhật bàn</c:when>
                                <c:otherwise>Thêm bàn mới</c:otherwise>
                            </c:choose>
                        </h2>

                        <form id="frm_ban" method="post" action="${pageContext.request.contextPath}/ban">
                            <input type="hidden" name="maBan" value="${banEdit.maBan}">

                            <div class="mb-3">
                                <label for="txt_tenBan" class="form-label small fw-semibold">Tên bàn</label>
                                <input type="text" id="txt_tenBan" name="tenBan" class="form-control"
                                       maxlength="100" placeholder="VD: Bàn 01" required
                                       value="<c:out value='${banEdit.tenBan}'/>">
                            </div>

                            <div class="mb-3">
                                <label for="sel_trangThai" class="form-label small fw-semibold">Trạng thái</label>
                                <select id="sel_trangThai" name="trangThai" class="form-select">
                                    <option value="Trống"
                                        ${banEdit == null || banEdit.trangThai == 'Trống' ? 'selected' : ''}>Trống</option>
                                    <option value="Đang phục vụ"
                                        ${banEdit.trangThai == 'Đang phục vụ' ? 'selected' : ''}>Đang phục vụ</option>
                                </select>
                            </div>

                            <div class="d-flex gap-2">
                                <button type="submit" id="btn_luuBan" class="btn btn-order-blue flex-fill">
                                    <c:choose>
                                        <c:when test="${banEdit != null && banEdit.maBan != null}">💾 Cập nhật</c:when>
                                        <c:otherwise>➕ Thêm mới</c:otherwise>
                                    </c:choose>
                                </button>
                                <c:if test="${banEdit != null}">
                                    <a href="${pageContext.request.contextPath}/ban"
                                       class="btn btn-outline-secondary">Hủy</a>
                                </c:if>
                            </div>
                        </form>
                    </section>
                </div>

                <!-- Danh sách bàn -->
                <div class="col-12 col-lg-8">
                    <section class="section-panel">
                        <form id="frm_locBan" method="get" action="${pageContext.request.contextPath}/ban"
                              class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
                            <h2 class="h6 fw-bold mb-0">Danh sách bàn</h2>

                            <div class="d-flex flex-wrap align-items-center gap-2">
                                <select id="sel_trangThaiLoc" name="trangThaiLoc"
                                        class="form-select form-select-sm" style="width: 170px;">
                                    <option value="ALL"
                                        ${param.trangThaiLoc == 'ALL' || empty param.trangThaiLoc ? 'selected' : ''}>-- Tất cả trạng thái --</option>
                                    <option value="Trống"
                                        ${param.trangThaiLoc == 'Trống' ? 'selected' : ''}>Trống</option>
                                    <option value="Đang phục vụ"
                                        ${param.trangThaiLoc == 'Đang phục vụ' ? 'selected' : ''}>Đang phục vụ</option>
                                </select>

                                <input type="text" id="txt_timBan" name="keyword" class="form-control form-control-sm"
                                       style="width: 200px;" placeholder="Nhập tên hoặc mã bàn..."
                                       value="<c:out value='${param.keyword}'/>">

                                <button type="submit" id="btn_timKiemBan" class="btn btn-sm btn-order-blue px-3">
                                    🔍 Tìm kiếm
                                </button>
                            </div>
                        </form>

                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                <tr>
                                    <th style="width: 80px;">Mã bàn</th>
                                    <th>Tên bàn</th>
                                    <th>Trạng thái</th>
                                    <th class="text-end" style="width: 170px;">Thao tác</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="ban" items="${dsBan}">
                                    <tr>
                                        <td>${ban.maBan}</td>
                                        <td><c:out value="${ban.tenBan}"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${ban.trangThai == 'Trống'}">
                                                    <span class="badge bg-success">Trống</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger"><c:out value="${ban.trangThai}"/></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end">
                                            <a href="${pageContext.request.contextPath}/ban?action=edit&maBan=${ban.maBan}"
                                               class="btn btn-sm btn-outline-primary">✏️ Sửa</a>
                                            <a href="${pageContext.request.contextPath}/ban?action=delete&maBan=${ban.maBan}"
                                               class="btn btn-sm btn-outline-danger"
                                               onclick="return confirm('Bạn có chắc muốn xóa bàn này?');">🗑️ Xóa</a>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:if test="${empty dsBan}">
                                    <tr>
                                        <td colspan="4" class="text-center text-muted py-4">
                                            Chưa có dữ liệu bàn nào trong hệ thống.
                                        </td>
                                    </tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
