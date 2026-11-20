package com.example.quanlyquanmicay.controller;

import com.example.quanlyquanmicay.model.Voucher;
import com.example.quanlyquanmicay.repository.VoucherRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

@WebServlet(name = "VoucherServlet", urlPatterns = {"/voucher"})
public class VoucherServlet extends HttpServlet {
    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private final VoucherRepository voucherRepository = new VoucherRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String tab = request.getParameter("tab");
        if (!"expired".equalsIgnoreCase(tab)) {
            tab = "active";
        }

        String action = request.getParameter("action");
        if ("lock".equalsIgnoreCase(action)) {
            String id = request.getParameter("id");
            if (!isNullOrEmpty(id)) {
                voucherRepository.khoaHetHan(id.trim());
            }
            response.sendRedirect(request.getContextPath() + "/voucher?tab=" + tab);
            return;
        }

        request.setAttribute("activeTab", tab);
        request.setAttribute("dsConHan", voucherRepository.findConHan());
        request.setAttribute("dsHetHan", voucherRepository.findHetHan());
        request.getRequestDispatcher("/jsp/voucher.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        LocalDate todayVN = LocalDate.now(VN_ZONE);

        String maVoucher = trimOrEmpty(request.getParameter("txt_maVoucher"));
        String maCode = trimOrEmpty(request.getParameter("txt_maCode"));
        String tenVoucher = trimOrEmpty(request.getParameter("txt_tenVoucher"));
        String loaiGiam = trimOrEmpty(request.getParameter("sel_loaiGiam"));
        if (loaiGiam.isEmpty()) {
            loaiGiam = "Tiền mặt";
        }

        BigDecimal giaTriGiam = parseDecimal(request.getParameter("txt_giaTriGiam"), BigDecimal.ZERO);
        BigDecimal donToiThieu = parseDecimal(request.getParameter("txt_donToiThieu"), BigDecimal.ZERO);

        String ngayKetThucStr = request.getParameter("txt_ngayKetThuc");
        if (isNullOrEmpty(ngayKetThucStr)) {
            forwardWithError(request, response, "Vui lòng chọn ngày kết thúc (hạn dùng) cho voucher.");
            return;
        }

        LocalDate ngayKetThuc;
        try {
            ngayKetThuc = LocalDate.parse(ngayKetThucStr.trim());
        } catch (DateTimeParseException e) {
            forwardWithError(request, response, "Ngày kết thúc không đúng định dạng, vui lòng chọn lại.");
            return;
        }

        if (ngayKetThuc.isBefore(todayVN)) {
            forwardWithError(request, response, "Không thể chọn ngày đã qua theo lịch Việt Nam. Vui lòng chọn từ hôm nay trở đi.");
            return;
        }

        Voucher v = new Voucher();
        v.setMaVoucher(maVoucher);
        v.setMaCode(maCode);
        v.setTenVoucher(tenVoucher);
        v.setLoaiGiam(loaiGiam);
        v.setGiaTriGiam(giaTriGiam);
        v.setGiaTriDonToiThieu(donToiThieu);
        v.setSoLuong(parseInt(request.getParameter("txt_soLuong"), 1));
        v.setSoLuongDaDung(0);
        v.setNgayKetThuc(ngayKetThuc);

        String trangThaiParam = trimOrEmpty(request.getParameter("sel_trangThai"));
        boolean conHan = !ngayKetThuc.isBefore(todayVN) && !"Hết hạn".equalsIgnoreCase(trangThaiParam);
        v.setTrangThai(conHan);

        try {
            voucherRepository.save(v);
        } catch (Exception e) {
            forwardWithError(request, response, buildFriendlyErrorMessage(e));
            return;
        }

        String redirectTab = conHan ? "active" : "expired";
        response.sendRedirect(request.getContextPath() + "/voucher?tab=" + redirectTab);
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.setAttribute("activeTab", "active");
        request.setAttribute("dsConHan", voucherRepository.findConHan());
        request.setAttribute("dsHetHan", voucherRepository.findHetHan());
        request.getRequestDispatcher("/jsp/voucher.jsp").forward(request, response);
    }

    private String buildFriendlyErrorMessage(Throwable e) {
        String raw = getAllMessages(e).toLowerCase();

        if (raw.contains("primary key") || raw.contains("duplicate key")) {
            return "Mã voucher này đã tồn tại trên hệ thống, vui lòng nhập mã voucher khác.";
        }
        if (raw.contains("unique") || raw.contains("uq_")) {
            return "Mã code này đã được sử dụng, vui lòng nhập mã code khác.";
        }
        if (raw.contains("cannot insert the value null") || raw.contains("not-null")) {
            return "Còn thiếu thông tin bắt buộc, vui lòng kiểm tra lại các ô nhập.";
        }
        if (raw.contains("truncation") || raw.contains("too long") || raw.contains("string or binary data would be truncated")) {
            return "Nội dung nhập vượt quá độ dài cho phép, vui lòng rút ngắn lại.";
        }
        if (raw.contains("arithmetic overflow") || raw.contains("out of range")) {
            return "Giá trị số nhập vào vượt quá giới hạn cho phép, vui lòng kiểm tra lại.";
        }
        if (raw.contains("connection") || raw.contains("communications link") || raw.contains("jdbc")) {
            return "Không kết nối được tới cơ sở dữ liệu, vui lòng thử lại sau.";
        }

        return "Không thể lưu voucher, vui lòng kiểm tra lại thông tin đã nhập.";
    }

    private String getAllMessages(Throwable t) {
        StringBuilder sb = new StringBuilder();
        Throwable cur = t;
        int depth = 0;
        while (cur != null && depth < 10) {
            if (cur.getMessage() != null) {
                sb.append(cur.getMessage()).append(" | ");
            }
            cur = cur.getCause();
            depth++;
        }
        return sb.toString();
    }

    private boolean isNullOrEmpty(String val) {
        return val == null || val.trim().isEmpty();
    }

    private String trimOrEmpty(String val) {
        return val == null ? "" : val.trim();
    }

    private BigDecimal parseDecimal(String val, BigDecimal defaultVal) {
        try {
            return isNullOrEmpty(val) ? defaultVal : new BigDecimal(val.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private Integer parseInt(String val, Integer defaultVal) {
        try {
            return isNullOrEmpty(val) ? defaultVal : Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
