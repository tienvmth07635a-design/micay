package com.example.quanlyquanmicay.controller;

import com.example.quanlyquanmicay.model.CaiDatHeThong;
import com.example.quanlyquanmicay.repository.CaiDatHeThongRepository;
import com.example.quanlyquanmicay.repository.NhatKiHoatDongRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@WebServlet(name = "CaiDatServlet", value = "/cai-dat")
public class CaiDatServlet extends HttpServlet {

    // Tạm dùng mã 1 (Quản lý). Nếu nhóm có đăng nhập lưu session thì lấy mã nhân viên từ session.
    private static final int MA_NHAN_VIEN_HIEN_TAI = 1;

    private final CaiDatHeThongRepository repo = new CaiDatHeThongRepository();
    private final NhatKiHoatDongRepository nhatKiRepo = new NhatKiHoatDongRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        CaiDatHeThong caiDat = repo.layCaiDat();
        if (caiDat == null) {
            caiDat = macDinh();
        }

        request.setAttribute("caiDat", caiDat);
        request.setAttribute("thanhCong", "1".equals(request.getParameter("success")));
        request.getRequestDispatcher("/jsp/cai_dat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Phải đặt encoding TRƯỚC khi đọc parameter, nếu không tiếng Việt bị lỗi font
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String tenQuan = trim(request.getParameter("tenQuan"));
        String diaChi = trim(request.getParameter("diaChi"));
        String soDienThoai = trim(request.getParameter("soDienThoai"));
        String thueVatStr = trim(request.getParameter("thueVat")).replace(',', '.');
        boolean tuDongIn = request.getParameter("tuDongInHoaDon") != null;

        // Lấy dòng cài đặt hiện có để giữ nguyên id (cập nhật chứ không thêm dòng mới)
        CaiDatHeThong caiDat = repo.layCaiDat();
        if (caiDat == null) {
            caiDat = macDinh();
        }
        caiDat.setTenQuan(tenQuan);
        caiDat.setDiaChi(diaChi);
        caiDat.setSoDienThoai(soDienThoai);
        caiDat.setTuDongInHoaDon(tuDongIn);

        // ===== Kiểm tra dữ liệu =====
        String loi = null;
        BigDecimal thueVat = null;

        if (tenQuan.isEmpty()) {
            loi = "Tên quán không được để trống.";
        } else if (diaChi.isEmpty()) {
            loi = "Địa chỉ cửa hàng không được để trống.";
        } else if (!soDienThoai.matches("^0\\d{9}$")) {
            loi = "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0.";
        } else {
            try {
                thueVat = new BigDecimal(thueVatStr).setScale(2, RoundingMode.HALF_UP);
                if (thueVat.compareTo(BigDecimal.ZERO) < 0 || thueVat.compareTo(new BigDecimal("100")) > 0) {
                    loi = "Thuế VAT phải nằm trong khoảng từ 0 đến 100.";
                }
            } catch (NumberFormatException e) {
                loi = "Thuế VAT phải là một số hợp lệ.";
            }
        }

        if (loi != null) {
            if (thueVat != null) {
                caiDat.setThueVat(thueVat);
            }
            request.setAttribute("caiDat", caiDat);
            request.setAttribute("loi", loi);
            request.getRequestDispatcher("/jsp/cai_dat.jsp").forward(request, response);
            return;
        }

        caiDat.setThueVat(thueVat);

        // ===== Lưu + ghi nhật kí =====
        if (repo.luu(caiDat)) {
            String hanhDong = "Cập nhật cài đặt hệ thống: quán '" + tenQuan + "', VAT "
                    + caiDat.getThueVatHienThi() + "%, tự động in hóa đơn: "
                    + (tuDongIn ? "Bật" : "Tắt");
            if (hanhDong.length() > 255) {
                hanhDong = hanhDong.substring(0, 255);
            }
            nhatKiRepo.ghiNhatKy(MA_NHAN_VIEN_HIEN_TAI, hanhDong);

            // Redirect sau khi lưu để F5 không bị gửi lại form
            response.sendRedirect(request.getContextPath() + "/cai-dat?success=1");
        } else {
            request.setAttribute("caiDat", caiDat);
            request.setAttribute("loi", "Lưu thất bại, vui lòng kiểm tra lại kết nối database.");
            request.getRequestDispatcher("/jsp/cai_dat.jsp").forward(request, response);
        }
    }

    private CaiDatHeThong macDinh() {
        return CaiDatHeThong.builder()
                .tenQuan("")
                .diaChi("")
                .soDienThoai("")
                .thueVat(new BigDecimal("8.00"))
                .tuDongInHoaDon(true)
                .build();
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
