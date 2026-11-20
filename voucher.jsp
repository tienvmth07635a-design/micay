<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Voucher - Quán Mì Cay</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .voucher-table {
            table-layout: fixed;
            width: 100%;
        }
        .voucher-table th,
        .voucher-table td {
            vertical-align: middle;
            word-wrap: break-word;
        }
        input[type="date"].date-future-only {
            background-color: #fff;
            cursor: pointer;
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
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/HomeServlet"><span>🏠</span><span>Trang chủ</span></a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/ban"><span>🍽️</span><span>Bàn</span></a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/san-pham"><span>🍜</span><span>Sản phẩm</span></a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/hoa-don"><span>🧾</span><span>Hóa đơn</span></a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/hoa-don-chi-tiet"><span>📋</span><span>Chi tiết hóa đơn</span></a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/khach-hang"><span>🙋</span><span>Khách hàng</span></a></li>
            </ul>

            <div class="sidebar-group-title">Ưu đãi, Nhân sự &amp; Hệ thống</div>
            <ul class="sidebar-menu">
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/khuyen-mai"><span>🏷️</span><span>Khuyến mãi</span></a></li>
                <li><a class="sidebar-link active" href="${pageContext.request.contextPath}/voucher"><span>🎟️</span><span>Voucher</span></a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/nhan-vien"><span>👨‍🍳</span><span>Nhân viên</span></a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/quan-ly"><span>👔</span><span>Quản lý</span></a></li>
                <li><a class="sidebar-link" href="${pageContext.request.contextPath}/nhat-ki"><span>🕒</span><span>Nhật ký hoạt động</span></a></li>
            </ul>
        </div>

        <div class="sidebar-footer">
            <a class="sidebar-link mb-0" href="${pageContext.request.contextPath}/home"><span>↪</span><span>Đăng xuất</span></a>
        </div>
    </aside>

    <!-- Nội dung chính -->
    <div class="main-content">
        <header class="topbar">
            <h1 class="topbar-title">Quản Lý Voucher</h1>
            <div class="d-flex align-items-center gap-2">
                <span class="text-muted small">Xin chào, Admin</span>
                <span class="user-badge">A</span>
            </div>
        </header>

        <main class="content-container">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger py-2" role="alert">
                        ${errorMessage}
                </div>
            </c:if>

            <!-- Form thêm Voucher kèm đặt thời gian -->
            <section class="section-panel mb-4">
                <h2 class="h6 fw-bold mb-3">Thêm Voucher mới &amp; Đặt thời gian</h2>

                <form id="frm_themVoucher" method="post" action="${pageContext.request.contextPath}/voucher" novalidate>
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label for="txt_maVoucher" class="form-label small fw-semibold">Mã voucher <span class="text-danger">*</span></label>
                            <input type="text" id="txt_maVoucher" name="txt_maVoucher" class="form-control form-control-sm" placeholder="VD: VC01">
                            <div class="invalid-feedback">Vui lòng nhập mã voucher.</div>
                        </div>

                        <div class="col-md-3">
                            <label for="txt_maCode" class="form-label small fw-semibold">Mã code <span class="text-danger">*</span></label>
                            <input type="text" id="txt_maCode" name="txt_maCode" class="form-control form-control-sm" placeholder="VD: MICAY7CAP">
                            <div class="invalid-feedback">Vui lòng nhập mã code.</div>
                        </div>

                        <div class="col-md-6">
                            <label for="txt_tenVoucher" class="form-label small fw-semibold">Tên voucher <span class="text-danger">*</span></label>
                            <input type="text" id="txt_tenVoucher" name="txt_tenVoucher" class="form-control form-control-sm" placeholder="VD: Giảm 20% cho đơn mì cay">
                            <div class="invalid-feedback">Vui lòng nhập tên voucher.</div>
                        </div>

                        <div class="col-md-3">
                            <label for="sel_loaiGiam" class="form-label small fw-semibold">Loại giảm</label>
                            <select id="sel_loaiGiam" name="sel_loaiGiam" class="form-select form-select-sm" onchange="updateDiscountUnit()">
                                <option value="Tiền mặt">Tiền mặt (đ)</option>
                                <option value="Phần trăm">Phần trăm (%)</option>
                            </select>
                        </div>

                        <div class="col-md-3">
                            <label for="txt_giaTriGiam" class="form-label small fw-semibold">Giá trị giảm <span class="text-danger">*</span></label>
                            <div class="input-group input-group-sm">
                                <input type="number" step="any" min="0.01" id="txt_giaTriGiam" name="txt_giaTriGiam"
                                       class="form-control" placeholder="VD: 20000">
                                <span class="input-group-text" id="unit_giaTriGiam">đ</span>
                            </div>
                            <div class="invalid-feedback" id="err_giaTriGiam">Giá trị giảm phải lớn hơn 0.</div>
                        </div>

                        <div class="col-md-3">
                            <label for="txt_donToiThieu" class="form-label small fw-semibold">Đơn tối thiểu (đ)</label>
                            <input type="number" step="100" min="0" id="txt_donToiThieu" name="txt_donToiThieu"
                                   class="form-control form-control-sm" value="0">
                        </div>

                        <div class="col-md-3">
                            <label for="txt_soLuong" class="form-label small fw-semibold">Số lượng <span class="text-danger">*</span></label>
                            <input type="number" min="1" id="txt_soLuong" name="txt_soLuong" class="form-control form-control-sm" value="50">
                            <div class="invalid-feedback">Số lượng phải ít nhất là 1.</div>
                        </div>

                        <div class="col-md-4">
                            <label for="txt_ngayKetThuc" class="form-label small fw-semibold">Ngày kết thúc (Hạn dùng) <span class="text-danger">*</span></label>
                            <input type="date" id="txt_ngayKetThuc" name="txt_ngayKetThuc"
                                   class="form-control form-control-sm date-future-only">
                            <div class="invalid-feedback" id="err_ngayKetThuc">Chỉ được chọn từ ngày mai trở đi (ngày hiện tại và quá khứ bị khóa).</div>
                        </div>

                        <div class="col-md-4">
                            <label for="sel_trangThai" class="form-label small fw-semibold">Trạng thái</label>
                            <select id="sel_trangThai" name="sel_trangThai" class="form-select form-select-sm">
                                <option value="Còn hạn">Còn hạn</option>
                                <option value="Hết hạn">Hết hạn</option>
                            </select>
                        </div>

                        <div class="col-md-4 d-flex align-items-end">
                            <button type="submit" id="btn_themVoucher" class="btn btn-sm btn-order-blue w-100 py-2">
                                + Thêm Voucher
                            </button>
                        </div>
                    </div>
                </form>
            </section>

            <!-- 2 tab đồng bộ 1 khung bảng duy nhất -->
            <section class="section-panel">
                <ul class="nav nav-tabs mb-3" role="tablist">
                    <li class="nav-item" role="presentation">
                        <a class="nav-link ${activeTab == 'active' ? 'active fw-bold' : ''}"
                           href="${pageContext.request.contextPath}/voucher?tab=active">
                            Voucher còn hạn
                            <span class="badge bg-success ms-1">${dsConHan != null ? dsConHan.size() : 0}</span>
                        </a>
                    </li>
                    <li class="nav-item" role="presentation">
                        <a class="nav-link ${activeTab == 'expired' ? 'active fw-bold' : ''}"
                           href="${pageContext.request.contextPath}/voucher?tab=expired">
                            Voucher hết hạn
                            <span class="badge bg-secondary ms-1">${dsHetHan != null ? dsHetHan.size() : 0}</span>
                        </a>
                    </li>
                </ul>

                <c:set var="hienThiList" value="${activeTab == 'expired' ? dsHetHan : dsConHan}"/>

                <div class="table-responsive">
                    <table class="table table-hover voucher-table mb-0">
                        <thead class="table-light">
                        <tr>
                            <th style="width: 8%;">Mã</th>
                            <th style="width: 11%;">Code</th>
                            <th style="width: 19%;">Tên voucher</th>
                            <th style="width: 11%;">Loại giảm</th>
                            <th style="width: 14%;">Giá trị giảm</th>
                            <th style="width: 12%;">Đơn tối thiểu</th>
                            <th style="width: 8%;">SL dùng</th>
                            <th style="width: 9%;">Hạn dùng</th>
                            <th style="width: 10%;" class="text-end">Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="v" items="${hienThiList}">
                            <tr>
                                <td class="fw-semibold">${v.maVoucher}</td>
                                <td><span class="badge bg-light text-dark border">${v.maCode}</span></td>
                                <td>${v.tenVoucher}</td>
                                <td>${v.loaiGiam}</td>
                                <td class="fw-semibold text-primary">
                                    <c:choose>
                                        <c:when test="${v.loaiGiam == 'Phần trăm'}">
                                            <fmt:formatNumber value="${v.giaTriGiam}" pattern="#,##0.##"/>%
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber value="${v.giaTriGiam}" pattern="#,##0"/>đ
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td><fmt:formatNumber value="${v.giaTriDonToiThieu}" pattern="#,##0"/>đ</td>
                                <td>${v.soLuongDaDung != null ? v.soLuongDaDung : 0}/${v.soLuong}</td>
                                <td>${v.ngayKetThuc}</td>
                                <td class="text-end">
                                    <c:choose>
                                        <c:when test="${activeTab == 'active'}">
                                            <a href="${pageContext.request.contextPath}/voucher?action=lock&id=${v.maVoucher}&tab=active"
                                               class="btn btn-sm btn-pay-red"
                                               onclick="return confirm('Khóa và chuyển voucher ${v.maCode} sang hết hạn?');">
                                                🔒 Khóa
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">Hết hạn</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty hienThiList}">
                            <tr>
                                <td colspan="9" class="text-center text-muted py-4">
                                    Không có voucher nào trong mục này.
                                </td>
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
    function getVietnamTomorrowISO() {
        const parts = new Intl.DateTimeFormat("en-CA", {
            timeZone: "Asia/Ho_Chi_Minh",
            year: "numeric",
            month: "2-digit",
            day: "2-digit"
        }).formatToParts(new Date());

        const year = Number(parts.find(p => p.type === "year").value);
        const month = Number(parts.find(p => p.type === "month").value);
        const day = Number(parts.find(p => p.type === "day").value);

        const vnMidnightUTC = new Date(Date.UTC(year, month - 1, day + 1));
        const y = vnMidnightUTC.getUTCFullYear();
        const m = String(vnMidnightUTC.getUTCMonth() + 1).padStart(2, "0");
        const d = String(vnMidnightUTC.getUTCDate()).padStart(2, "0");
        return `${y}-${m}-${d}`;
    }

    function applyVietnamMinDate() {
        const minAllowedVN = getVietnamTomorrowISO();
        const ngayKetThuc = document.getElementById("txt_ngayKetThuc");
        if (ngayKetThuc) {
            ngayKetThuc.setAttribute("min", minAllowedVN);
        }
    }

    function enforceVietnamDateRange() {
        const minAllowedVN = getVietnamTomorrowISO();
        const ngayKetThuc = document.getElementById("txt_ngayKetThuc");
        const errNgay = document.getElementById("err_ngayKetThuc");
        if (!ngayKetThuc) return;

        ngayKetThuc.setAttribute("min", minAllowedVN);

        const val = ngayKetThuc.value.trim();
        if (val && val < minAllowedVN) {
            ngayKetThuc.value = "";
            ngayKetThuc.classList.add("is-invalid");
            if (errNgay) {
                errNgay.textContent = "Ngày hiện tại và ngày quá khứ đã bị khóa. Chỉ được chọn từ ngày mai trở đi.";
            }
        } else {
            ngayKetThuc.classList.remove("is-invalid");
        }
    }

    function updateDiscountUnit() {
        const loai = document.getElementById("sel_loaiGiam").value;
        const unit = document.getElementById("unit_giaTriGiam");
        const input = document.getElementById("txt_giaTriGiam");
        const err = document.getElementById("err_giaTriGiam");

        if (loai === "Phần trăm") {
            unit.textContent = "%";
            input.placeholder = "VD: 20";
            input.max = "100";
            err.textContent = "Phần trăm giảm phải từ 1 đến 100.";
        } else {
            unit.textContent = "đ";
            input.placeholder = "VD: 20000";
            input.removeAttribute("max");
            err.textContent = "Giá trị giảm phải lớn hơn 0.";
        }
    }

    function validateVoucherForm() {
        let isValid = true;
        const loai = document.getElementById("sel_loaiGiam").value;

        const maVoucher = document.getElementById("txt_maVoucher");
        const maCode = document.getElementById("txt_maCode");
        const tenVoucher = document.getElementById("txt_tenVoucher");
        const giaTri = document.getElementById("txt_giaTriGiam");
        const soLuong = document.getElementById("txt_soLuong");
        const ngayKetThuc = document.getElementById("txt_ngayKetThuc");
        const errNgay = document.getElementById("err_ngayKetThuc");

        const valGiaTri = Number(giaTri.value);
        const giaTriOk = loai === "Phần trăm"
            ? (valGiaTri > 0 && valGiaTri <= 100)
            : (valGiaTri > 0);

        const minAllowedVN = getVietnamTomorrowISO();
        const ngayKetThucVal = ngayKetThuc.value.trim();
        let ngayKetThucOk = true;
        if (!ngayKetThucVal) {
            ngayKetThucOk = false;
            errNgay.textContent = "Vui lòng chọn ngày kết thúc từ ngày mai trở đi.";
        } else if (ngayKetThucVal < minAllowedVN) {
            ngayKetThucOk = false;
            ngayKetThuc.value = "";
            errNgay.textContent = "Ngày hiện tại và ngày quá khứ đã bị khóa. Chỉ được chọn từ ngày mai trở đi.";
        }

        const checks = [
            { el: maVoucher, ok: maVoucher.value.trim().length > 0 },
            { el: maCode, ok: maCode.value.trim().length > 0 },
            { el: tenVoucher, ok: tenVoucher.value.trim().length > 0 },
            { el: giaTri, ok: giaTriOk },
            { el: soLuong, ok: Number(soLuong.value) >= 1 },
            { el: ngayKetThuc, ok: ngayKetThucOk }
        ];

        checks.forEach(({ el, ok }) => {
            if (!ok) {
                el.classList.add("is-invalid");
                isValid = false;
            } else {
                el.classList.remove("is-invalid");
            }
        });

        return isValid;
    }

    document.getElementById("frm_themVoucher").addEventListener("submit", function (event) {
        if (!validateVoucherForm()) {
            event.preventDefault();
        }
    });

    const ngayInput = document.getElementById("txt_ngayKetThuc");
    if (ngayInput) {
        ngayInput.addEventListener("focus", applyVietnamMinDate);
        ngayInput.addEventListener("click", applyVietnamMinDate);
        ngayInput.addEventListener("keydown", function (e) {
            if (e.key !== "Tab") {
                e.preventDefault();
            }
        });
        ngayInput.addEventListener("paste", function (e) {
            e.preventDefault();
        });
        ngayInput.addEventListener("change", enforceVietnamDateRange);
        ngayInput.addEventListener("input", enforceVietnamDateRange);
    }

    applyVietnamMinDate();
    updateDiscountUnit();
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
