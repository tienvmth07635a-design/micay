
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý sản phẩm</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
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
                    <a class="sidebar-link"
                       href="${pageContext.request.contextPath}/HomeServletServlet-servlet">
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
                    <a class="sidebar-link active"
                       href="${pageContext.request.contextPath}/san-pham">
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
            <a class="sidebar-link mb-0"
               href="${pageContext.request.contextPath}/HomeServletServlet-servlet">
                <span>↪</span>
                <span>Đăng xuất</span>
            </a>
        </div>
    </aside>

    <!-- Nội dung chính -->
    <div class="main-content">

        <header class="topbar">
            <h1 class="topbar-title">Quản lý sản phẩm</h1>

            <div class="d-flex align-items-center gap-2">
                <span class="text-muted small">Xin chào, Admin</span>
                <span class="user-badge">A</span>
            </div>
        </header>

        <main class="content-container">

            <!-- Thông báo -->
            <c:if test="${param.success == '1'}">
                <div class="alert alert-success">
                    Thao tác sản phẩm thành công!
                </div>
            </c:if>

            <!-- Form thêm / sửa -->
            <section class="section-panel mb-4">

                <h2 class="h6 fw-bold mb-3">
                    <c:choose>
                        <c:when test="${sanPhamSua != null}">
                            Cập nhật sản phẩm
                        </c:when>
                        <c:otherwise>
                            Thêm sản phẩm
                        </c:otherwise>
                    </c:choose>
                </h2>

                <form method="post"
                      action="${pageContext.request.contextPath}/san-pham">

                    <c:choose>
                        <c:when test="${sanPhamSua != null}">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden"
                                   name="maSanPham"
                                   value="${sanPhamSua.maSanPham}">
                        </c:when>

                        <c:otherwise>
                            <input type="hidden" name="action" value="add">
                        </c:otherwise>
                    </c:choose>

                    <div class="row g-3">

                        <div class="col-md-6">
                            <label class="form-label">Tên sản phẩm</label>
                            <input type="text"
                                   name="tenSanPham"
                                   class="form-control"
                                   value="${sanPhamSua.tenSanPham}"
                                   maxlength="255"
                                   placeholder="Nhập tên sản phẩm"
                                   required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Giá tiền (VNĐ)</label>
                            <input type="number"
                                   name="giaTien"
                                   class="form-control"
                                   value="${sanPhamSua.giaTien}"
                                   min="0.01"
                                   step="0.01"
                                   placeholder="Nhập giá tiền"
                                   required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Ảnh sản phẩm</label>
                            <input type="text"
                                   name="anhSanPham"
                                   class="form-control"
                                   value="${sanPhamSua.anhSanPham}"
                                   placeholder="Nhập đường dẫn ảnh">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Mô tả</label>
                            <input type="text"
                                   name="moTa"
                                   class="form-control"
                                   value="${sanPhamSua.moTa}"
                                   placeholder="Nhập mô tả sản phẩm">
                        </div>

                    </div>

                    <div class="mt-3 d-flex gap-2">

                        <button type="submit" class="btn btn-primary">
                            <c:choose>
                                <c:when test="${sanPhamSua != null}">
                                    Cập nhật
                                </c:when>
                                <c:otherwise>
                                    Thêm sản phẩm
                                </c:otherwise>
                            </c:choose>
                        </button>

                        <c:if test="${sanPhamSua != null}">
                            <a href="${pageContext.request.contextPath}/san-pham"
                               class="btn btn-secondary">
                                Hủy sửa
                            </a>
                        </c:if>

                    </div>
                </form>
            </section>

            <!-- Danh sách sản phẩm -->
            <section class="section-panel">

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h2 class="h6 fw-bold mb-0">Danh sách sản phẩm</h2>

                    <span class="text-muted small">
                        Tổng số: ${empty dsSanPham ? 0 : dsSanPham.size()} sản phẩm
                    </span>
                </div>

                <div class="table-responsive">
                    <table class="table table-bordered table-hover align-middle">

                        <thead class="table-light">
                            <tr>
                                <th>STT</th>
                                <th>Mã SP</th>
                                <th>Tên sản phẩm</th>
                                <th>Giá tiền</th>
                                <th>Ảnh sản phẩm</th>
                                <th>Mô tả</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="sp" items="${dsSanPham}" varStatus="st">
                                <tr>

                                   <td>${st.count}</td>
                                   <td>SP<fmt:formatNumber value="${st.count}" pattern="00"/></td>
                                    <td>${sp.tenSanPham}</td>

                                    <td>
                                        <fmt:formatNumber value="${sp.giaTien}"
                                                          pattern="#,##0.##"/> VNĐ
                                    </td>

                                    <td>
                                        <img src="${pageContext.request.contextPath}${sp.anhSanPham}"
                                             alt="${sp.tenSanPham}"
                                             width="80"
                                             height="80"
                                             style="object-fit: cover;">
                                    </td>
                                    <td>${sp.moTa}</td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${sp.trangThai}">
                                                <span class="badge bg-success">Đang bán</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Ngừng bán</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <div class="d-flex gap-2">
                                            <a href="${pageContext.request.contextPath}/san-pham?edit=${sp.maSanPham}"
                                               class="btn btn-sm btn-warning">Sửa</a>

                                            <c:choose>
                                                <c:when test="${sp.trangThai}">
                                                    <form method="post"
                                                          action="${pageContext.request.contextPath}/san-pham"
                                                          onsubmit="return confirm('Bạn có chắc chắn muốn ngừng bán sản phẩm này không?')">
                                                        <input type="hidden" name="action" value="stop">
                                                        <input type="hidden" name="maSanPham" value="${sp.maSanPham}">
                                                        <button type="submit" class="btn btn-sm btn-danger">
                                                            Ngừng bán
                                                        </button>
                                                    </form>
                                                </c:when>

                                                <c:otherwise>
                                                    <form method="post"
                                                          action="${pageContext.request.contextPath}/san-pham"
                                                          onsubmit="return confirm('Bạn có muốn tiếp tục bán sản phẩm này không?')">
                                                        <input type="hidden" name="action" value="continue">
                                                        <input type="hidden" name="maSanPham" value="${sp.maSanPham}">
                                                        <button type="submit" class="btn btn-sm btn-success">
                                                            Tiếp tục bán
                                                        </button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty dsSanPham}">
                                <tr>
                                    <td colspan="8" class="text-center">
                                        Chưa có sản phẩm nào.
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
