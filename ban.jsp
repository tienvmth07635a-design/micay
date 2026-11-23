<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý bàn &amp; gọi món - Quản Lý Quán Mì Cay</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .kpi-b { border-left: 4px solid #dee2e6 !important; height: 100%; }
        .kpi-cyan { border-left-color: #0dcaf0 !important; }
        .kpi-red { border-left-color: #dc3545 !important; }
        .kpi-orange { border-left-color: #fd7e14 !important; }
        .kpi-green { border-left-color: #28a745 !important; }
        .kpi-pink { border-left-color: #d63384 !important; }
        .kpi-yellow { border-left-color: #ffc107 !important; }
        .kpi-sub { font-size: .75rem; color: #6c757d; }
        .kpi-sub.kpi-up { color: #198754; }
        .kpi-sub.kpi-down { color: #dc3545; }
        .ban-card { background: #fff; border-radius: 10px; box-shadow: 0 1px 6px rgba(0,0,0,.12); overflow: hidden; height: 100%; display: flex; flex-direction: column; }
        .ban-media { position: relative; height: 110px; background: #e9ecef; }
        .ban-media img { width: 100%; height: 100%; object-fit: cover; display: block; }
        .ban-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; font-size: 2.6rem; background: linear-gradient(135deg, #dfe6f3, #f4f6fb); }
        .ban-status { position: absolute; top: 8px; right: 8px; font-size: .68rem; font-weight: 600; color: #fff; padding: 2px 8px; border-radius: 20px; }
        .ban-status.free { background: #198754; }
        .ban-status.busy { background: #dc3545; }
        .ban-body { padding: 10px; display: flex; flex-direction: column; flex: 1; }
        .ban-name { font-weight: 700; font-size: .9rem; }
        .ban-sub { font-size: .75rem; color: #6c757d; min-height: 1.2em; }
        .ban-sub.tamtinh { color: #dc3545; }
        .ban-btn { display: block; width: 100%; text-align: center; font-size: .78rem; padding: 6px 4px; border-radius: 4px; color: #fff; text-decoration: none; margin: 8px 0 4px; }
        .ban-btn.open { background: #0d6efd; }
        .ban-btn.pay { background: #dc3545; }
        .ban-btn:hover { color: #fff; opacity: .9; }
        .ban-tomtat { font-size: .66rem; color: #6c757d; text-align: center; min-height: 1.2em; }
        .ban-actions { margin-top: auto; padding-top: 4px; text-align: center; font-size: .7rem; }
        .ban-actions a, .ban-actions button { color: #6c757d; text-decoration: none; background: none; border: 0; padding: 0 4px; font-size: .7rem; }
        .ban-actions a:hover, .ban-actions button:hover { color: #0d6efd; }
    </style>
</head>
<body>
<div class="app-wrapper">

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

    <main class="main-content">
        <header class="topbar">
            <div class="topbar-title">Quản Lý Bàn &amp; Gọi Món</div>
        </header>

        <div class="content-container">

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

            <div class="row row-cols-1 row-cols-md-3 row-cols-xl-6 g-3 mb-4">
                <div class="col">
                    <div class="kpi-card kpi-b kpi-cyan">
                        <div class="kpi-label">Bàn trống</div>
                        <div class="kpi-value">${banTrong != null ? banTrong : 0}</div>
                        <div class="kpi-sub">Sẵn sàng đón khách</div>
                    </div>
                </div>
                <div class="col">
                    <div class="kpi-card kpi-b kpi-red">
                        <div class="kpi-label">Bàn có khách</div>
                        <div class="kpi-value">${banCoKhach != null ? banCoKhach : 0}</div>
                        <div class="kpi-sub">Đang phục vụ</div>
                    </div>
                </div>
                <div class="col">
                    <div class="kpi-card kpi-b kpi-orange">
                        <div class="kpi-label">Tô mì đang làm</div>
                        <div class="kpi-value">${toMiDangLam != null ? toMiDangLam : 0}</div>
                        <div class="kpi-sub">Bếp đang chế biến</div>
                    </div>
                </div>
                <div class="col">
                    <div class="kpi-card kpi-b kpi-green">
                        <div class="kpi-label">Doanh thu hôm nay</div>
                        <div class="kpi-value"><fmt:formatNumber value="${doanhThuHomNay != null ? doanhThuHomNay : 0}" pattern="#,##0"/></div>
                        <div class="kpi-sub ${ssHomNay.huong > 0 ? 'kpi-up' : (ssHomNay.huong < 0 ? 'kpi-down' : '')}">
                            ${ssHomNay.huong > 0 ? '▲ ' : (ssHomNay.huong < 0 ? '▼ ' : '')}<c:out value="${ssHomNay.noiDung}"/>
                        </div>
                    </div>
                </div>
                <div class="col">
                    <div class="kpi-card kpi-b kpi-pink">
                        <div class="kpi-label">Doanh thu tháng này</div>
                        <div class="kpi-value"><fmt:formatNumber value="${doanhThuThangNay != null ? doanhThuThangNay : 0}" pattern="#,##0"/></div>
                        <div class="kpi-sub ${ssThangNay.huong > 0 ? 'kpi-up' : (ssThangNay.huong < 0 ? 'kpi-down' : '')}">
                            ${ssThangNay.huong > 0 ? '▲ ' : (ssThangNay.huong < 0 ? '▼ ' : '')}<c:out value="${ssThangNay.noiDung}"/>
                        </div>
                    </div>
                </div>
                <div class="col">
                    <div class="kpi-card kpi-b kpi-yellow">
                        <div class="kpi-label">Đơn chờ thanh toán</div>
                        <div class="kpi-value">${donChoThanhToan != null ? donChoThanhToan : 0}</div>
                        <a href="${pageContext.request.contextPath}/hoa-don"
                           class="btn btn-sm btn-outline-primary w-100 mt-1" style="font-size: .72rem;">Xem hóa đơn</a>
                    </div>
                </div>
            </div>

            <section class="section-panel">
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
                    <h2 class="h6 fw-bold mb-0">Danh sách trạng thái bàn</h2>

                    <div class="d-flex flex-wrap align-items-center gap-2">
                        <form id="frm_locBan" method="get" action="${pageContext.request.contextPath}/ban"
                              class="d-flex flex-wrap align-items-center gap-2">
                            <select id="sel_trangThaiLoc" name="trangThaiLoc"
                                    class="form-select form-select-sm" style="width: 170px;">
                                <option value="ALL"
                                    ${param.trangThaiLoc == 'ALL' || empty param.trangThaiLoc ? 'selected' : ''}>-- Tất cả trạng thái --</option>
                                <option value="Trống"
                                    ${param.trangThaiLoc == 'Trống' ? 'selected' : ''}>Trống</option>
                                <option value="Đang phục vụ"
                                    ${param.trangThaiLoc == 'Đang phục vụ' ? 'selected' : ''}>Có khách</option>
                            </select>

                            <input type="text" id="txt_timBan" name="keyword" class="form-control form-control-sm"
                                   style="width: 200px;" placeholder="Nhập số bàn (VD: Bàn 01)"
                                   value="<c:out value='${param.keyword}'/>">

                            <button type="submit" id="btn_timKiemBan" class="btn btn-sm btn-order-blue px-3">🔍 Tìm kiếm</button>
                        </form>

                        <button type="button" class="btn btn-sm btn-outline-primary" id="btn_themBan"
                                data-bs-toggle="modal" data-bs-target="#modalBan">➕ Thêm bàn</button>
                    </div>
                </div>

                <div class="row row-cols-2 row-cols-md-3 row-cols-xl-4 row-cols-xxl-6 g-3">
                    <c:forEach var="card" items="${dsCard}">
                        <c:choose>
                            <c:when test="${fn:startsWith(card.anh, 'http')}">
                                <c:set var="imgSrc" value="${card.anh}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="imgSrc" value="${pageContext.request.contextPath}${card.anh}"/>
                            </c:otherwise>
                        </c:choose>

                        <div class="col">
                            <div class="ban-card">
                                <div class="ban-media">
                                    <c:choose>
                                        <c:when test="${not empty card.anh}">
                                            <img src="${imgSrc}" alt="<c:out value='${card.ban.tenBan}'/>"
                                                 onerror="this.outerHTML='<div class=&quot;ban-placeholder&quot;>🪑</div>'">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="ban-placeholder">🪑</div>
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="ban-status ${card.dangPhucVu ? 'busy' : 'free'}">
                                        ● ${card.dangPhucVu ? 'Có khách' : 'Trống'}
                                    </span>
                                </div>

                                <div class="ban-body">
                                    <div class="ban-name"><c:out value="${card.ban.tenBan}"/></div>

                                    <c:choose>
                                        <c:when test="${card.coMon}">
                                            <div class="ban-sub tamtinh">
                                                Tạm tính: <fmt:formatNumber value="${card.tamTinh}" pattern="#,##0"/> VNĐ
                                            </div>
                                            <a class="ban-btn pay"
                                               href="${pageContext.request.contextPath}/ban?action=order&maBan=${card.ban.maBan}">
                                                🧾 Thêm món / Thanh toán
                                            </a>
                                            <div class="ban-tomtat"><c:out value="${card.tomTat}"/></div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="ban-sub">Chưa có món gọi</div>
                                            <a class="ban-btn open"
                                               href="${pageContext.request.contextPath}/ban?action=order&maBan=${card.ban.maBan}">
                                                ➕ Gọi món / Mở bàn
                                            </a>
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="ban-actions">
                                        <button type="button" class="btn-sua-ban"
                                                data-ma="${card.ban.maBan}"
                                                data-ten="<c:out value='${card.ban.tenBan}'/>"
                                                data-bs-toggle="modal" data-bs-target="#modalBan">✏️ Đổi tên</button>
                                        <a href="${pageContext.request.contextPath}/ban?action=delete&maBan=${card.ban.maBan}"
                                           onclick="return confirm('Bạn có chắc muốn xóa bàn này?');">🗑️ Xóa</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${empty dsCard}">
                    <div class="text-center text-muted py-4">Chưa có bàn nào phù hợp. Bấm "➕ Thêm bàn" để tạo bàn mới.</div>
                </c:if>
            </section>
        </div>
    </main>
</div>

<div class="modal fade" id="modalBan" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-sm">
        <form class="modal-content" method="post" action="${pageContext.request.contextPath}/ban" id="frm_ban">
            <div class="modal-header">
                <h5 class="modal-title h6 fw-bold" id="modalBanTitle">Thêm bàn mới</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="action" value="luuBan">
                <input type="hidden" name="maBan" id="hid_maBan" value="">
                <label for="txt_tenBan" class="form-label small fw-semibold">Tên bàn</label>
                <input type="text" id="txt_tenBan" name="tenBan" class="form-control"
                       maxlength="100" placeholder="VD: Bàn 07" required>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" id="btn_luuBan" class="btn btn-order-blue btn-sm">💾 Lưu</button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function () {
        var hidMa = document.getElementById('hid_maBan');
        var txtTen = document.getElementById('txt_tenBan');
        var title = document.getElementById('modalBanTitle');

        document.getElementById('btn_themBan').addEventListener('click', function () {
            hidMa.value = '';
            txtTen.value = '';
            title.textContent = 'Thêm bàn mới';
        });

        document.querySelectorAll('.btn-sua-ban').forEach(function (btn) {
            btn.addEventListener('click', function () {
                hidMa.value = btn.dataset.ma;
                txtTen.value = btn.dataset.ten;
                title.textContent = 'Đổi tên bàn';
            });
        });

        document.getElementById('modalBan').addEventListener('shown.bs.modal', function () {
            txtTen.focus();
        });
    })();
</script>
</body>
</html>
