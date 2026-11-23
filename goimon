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
    <title>Gọi món - <c:out value="${banGoiMon.tenBan}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .mon-card { background: #fff; border-radius: 10px; box-shadow: 0 1px 6px rgba(0,0,0,.12); overflow: hidden; height: 100%; border: 2px solid transparent; }
        .mon-card.chosen { border-color: #0d6efd; }
        .mon-card img, .mon-noimg { width: 100%; height: 130px; object-fit: cover; display: block; }
        .mon-noimg { display: flex; align-items: center; justify-content: center; font-size: 2.6rem; background: #f1f3f5; }
        .mon-body { padding: 10px; }
        .mon-name { font-weight: 600; font-size: .9rem; min-height: 2.4em; }
        .mon-gia { color: #dc3545; font-weight: 700; font-size: .85rem; margin-bottom: 8px; }
        .qty-box { display: flex; align-items: center; gap: 4px; }
        .qty-box .qty-input { width: 60px; text-align: center; }
        .side-sticky { position: sticky; top: 12px; }
        .pay-option { display: flex; align-items: center; gap: 10px; border: 1px solid #dee2e6; border-radius: 8px; padding: 8px 10px; cursor: pointer; margin-bottom: 8px; font-size: .88rem; }
        .pay-option:has(input:checked) { border-color: #0d6efd; background: #eef4ff; }
        .pay-badge { width: 30px; height: 30px; border-radius: 8px; color: #fff; font-weight: 700; font-size: .72rem; display: inline-flex; align-items: center; justify-content: center; }
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
            <div class="topbar-title">Gọi món - <c:out value="${banGoiMon.tenBan}"/></div>
        </header>

        <div class="content-container">

            <a href="${pageContext.request.contextPath}/ban" class="btn btn-sm btn-outline-secondary mb-3">← Quay lại danh sách bàn</a>

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

            <div class="row g-3">

                <div class="col-12 col-xl-8">
                    <section class="section-panel">
                        <h2 class="h6 fw-bold mb-3">Thực đơn</h2>

                        <c:if test="${empty dsMon}">
                            <div class="text-muted py-3">
                                Chưa có sản phẩm nào. Hãy thêm sản phẩm ở trang
                                <a href="${pageContext.request.contextPath}/san-pham">Sản phẩm</a>.
                            </div>
                        </c:if>

                        <form id="frmGoiMon" method="post" action="${pageContext.request.contextPath}/ban">
                            <input type="hidden" name="action" value="goiMon">
                            <input type="hidden" name="maBan" value="${banGoiMon.maBan}">

                            <div class="row row-cols-2 row-cols-md-3 g-3">
                                <c:forEach var="mon" items="${dsMon}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(mon.anh, 'http')}">
                                            <c:set var="imgSrc" value="${mon.anh}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="imgSrc" value="${pageContext.request.contextPath}${mon.anh}"/>
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="col">
                                        <div class="mon-card ${mon.soLuong > 0 ? 'chosen' : ''}">
                                            <c:choose>
                                                <c:when test="${not empty mon.anh}">
                                                    <img src="${imgSrc}" alt="<c:out value='${mon.ten}'/>"
                                                         onerror="this.outerHTML='<div class=&quot;mon-noimg&quot;>🍜</div>'">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="mon-noimg">🍜</div>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="mon-body">
                                                <div class="mon-name"><c:out value="${mon.ten}"/></div>
                                                <div class="mon-gia"><fmt:formatNumber value="${mon.gia}" pattern="#,##0"/> VNĐ</div>
                                                <div class="qty-box">
                                                    <button type="button" class="btn btn-sm btn-outline-secondary qty-minus">−</button>
                                                    <input type="number" class="form-control form-control-sm qty-input"
                                                           name="sl_${mon.ma}" value="${mon.soLuong}"
                                                           min="0" max="${soLuongToiDa}" data-gia="${mon.gia}">
                                                    <button type="button" class="btn btn-sm btn-outline-secondary qty-plus">+</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </form>
                        <div class="small text-muted mt-3">Mỗi món chọn tối đa ${soLuongToiDa} phần.</div>
                    </section>
                </div>

                <div class="col-12 col-xl-4">
                    <div class="side-sticky">
                        <section class="section-panel mb-3">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <span class="fw-semibold">Tạm tính</span>
                                <span class="fs-5 fw-bold text-danger"><span id="tamTinh">0</span> VNĐ</span>
                            </div>
                            <button type="submit" form="frmGoiMon" id="btn_goiMon" class="btn btn-order-blue w-100">
                                ${empty monDaChon ? '🍜 Gọi món' : '🔄 Cập nhật món'}
                            </button>
                        </section>

                        <section class="section-panel">
                            <h2 class="h6 fw-bold mb-3">Phương thức thanh toán</h2>

                            <form id="frmThanhToan" method="post" action="${pageContext.request.contextPath}/ban"
                                  onsubmit="return confirm('Xác nhận thanh toán cho bàn này?');">
                                <input type="hidden" name="action" value="thanhToan">
                                <input type="hidden" name="maBan" value="${banGoiMon.maBan}">

                                <label class="pay-option">
                                    <input type="radio" name="phuongThuc" value="MoMo" required>
                                    <span class="pay-badge" style="background:#a50064;">Mo</span> MoMo
                                </label>
                                <label class="pay-option">
                                    <input type="radio" name="phuongThuc" value="ZaloPay">
                                    <span class="pay-badge" style="background:#0068ff;">ZP</span> ZaloPay
                                </label>
                                <label class="pay-option">
                                    <input type="radio" name="phuongThuc" value="Viettel Money">
                                    <span class="pay-badge" style="background:#ee0033;">VM</span> Viettel Money
                                </label>
                                <label class="pay-option">
                                    <input type="radio" name="phuongThuc" value="ShopeePay">
                                    <span class="pay-badge" style="background:#ee4d2d;">SP</span> ShopeePay
                                </label>
                                <label class="pay-option">
                                    <input type="radio" name="phuongThuc" value="VNPAY">
                                    <span class="pay-badge" style="background:#005baa;">VN</span> VNPAY
                                </label>

                                <button type="submit" id="btn_thanhToan" class="btn btn-success w-100 mt-2"
                                        ${empty monDaChon ? 'disabled' : ''}>💳 Thanh toán</button>
                                <div class="small text-muted mt-2">
                                    <c:choose>
                                        <c:when test="${empty monDaChon}">Gọi món trước rồi mới thanh toán được.</c:when>
                                        <c:otherwise>Thanh toán tính theo các món đã gọi (đã lưu). Xong bàn sẽ về trạng thái trống.</c:otherwise>
                                    </c:choose>
                                </div>
                            </form>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function () {
        var MAX = ${soLuongToiDa};
        var inputs = document.querySelectorAll('.qty-input');

        function clamp(v) {
            v = parseInt(v, 10);
            if (isNaN(v) || v < 0) return 0;
            return v > MAX ? MAX : v;
        }

        function tinh() {
            var tong = 0;
            inputs.forEach(function (inp) {
                var sl = clamp(inp.value);
                tong += sl * parseFloat(inp.dataset.gia);
                inp.closest('.mon-card').classList.toggle('chosen', sl > 0);
            });
            document.getElementById('tamTinh').textContent = Math.round(tong).toLocaleString('en-US');
        }

        document.querySelectorAll('.qty-box').forEach(function (box) {
            var inp = box.querySelector('.qty-input');
            box.querySelector('.qty-minus').addEventListener('click', function () {
                inp.value = Math.max(0, clamp(inp.value) - 1);
                tinh();
            });
            box.querySelector('.qty-plus').addEventListener('click', function () {
                inp.value = Math.min(MAX, clamp(inp.value) + 1);
                tinh();
            });
            inp.addEventListener('input', tinh);
            inp.addEventListener('change', function () {
                inp.value = clamp(inp.value);
                tinh();
            });
        });

        tinh();
    })();
</script>
</body>
</html>
