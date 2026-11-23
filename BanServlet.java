package com.example.quanlyquanmicay.controller;

import com.example.quanlyquanmicay.model.Ban;
import com.example.quanlyquanmicay.model.BanCard;
import com.example.quanlyquanmicay.model.SoSanh;
import com.example.quanlyquanmicay.repository.BanRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "BanServlet", urlPatterns = {"/ban"})
public class BanServlet extends HttpServlet {

    private static final String TRONG = "Trống";
    private static final String DANG_PHUC_VU = "Đang phục vụ";
    private static final int SO_LUONG_TOI_DA = 100;
    private static final List<String> DS_THANH_TOAN =
            List.of("MoMo", "ZaloPay", "Viettel Money", "ShopeePay", "VNPAY");

    private final BanRepository banRepository = new BanRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        if ("order".equals(action)) {
            hienThiGoiMon(request, response, parseId(request.getParameter("maBan")));
        } else if ("delete".equals(action)) {
            try {
                banRepository.deleteById(parseId(request.getParameter("maBan")));
                request.getSession().setAttribute("success", "Đã xóa bàn thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("error", "Không thể xóa bàn: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/ban");
        } else {
            hienThiDanhSach(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        switch (trim(request.getParameter("action"))) {
            case "goiMon":
                xuLyGoiMon(request, response);
                break;
            case "thanhToan":
                xuLyThanhToan(request, response);
                break;
            default:
                xuLyLuuBan(request, response);
        }
    }

    private void xuLyLuuBan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer maBan = parseId(request.getParameter("maBan"));
        String tenBan = trim(request.getParameter("tenBan"));

        String loi = null;
        if (tenBan.isEmpty()) loi = "Tên bàn không được để trống!";
        else if (tenBan.length() > 100) loi = "Tên bàn tối đa 100 ký tự!";

        Ban ban = null;
        if (loi == null) {
            ban = (maBan == null) ? new Ban() : banRepository.findById(maBan);
            if (ban == null) loi = "Không tìm thấy bàn cần sửa!";
        }

        if (loi == null) {
            try {
                ban.setTenBan(tenBan);
                if (maBan == null) ban.setTrangThai(TRONG);
                banRepository.save(ban);
                request.getSession().setAttribute("success",
                        maBan == null ? "Đã thêm bàn mới thành công!" : "Đã cập nhật tên bàn thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                loi = "Lỗi khi lưu bàn: " + e.getMessage();
            }
        }

        if (loi != null) request.getSession().setAttribute("error", loi);
        response.sendRedirect(request.getContextPath() + "/ban");
    }

    private void xuLyGoiMon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer maBan = parseId(request.getParameter("maBan"));
        try {
            Ban ban = banRepository.findById(maBan);
            if (ban == null) {
                chuyenVe(request, response, null, null, "Không tìm thấy bàn!");
                return;
            }

            Map<Integer, Integer> monChon = new LinkedHashMap<>();
            for (Map<String, Object> mon : banRepository.findMonAn()) {
                Integer ma = (Integer) mon.get("ma");
                int soLuong = docSoLuong(request.getParameter("sl_" + ma));
                if (soLuong < 0 || soLuong > SO_LUONG_TOI_DA) {
                    chuyenVe(request, response, maBan, null,
                            "Số lượng mỗi món phải từ 0 đến " + SO_LUONG_TOI_DA + "!");
                    return;
                }
                if (soLuong > 0) monChon.put(ma, soLuong);
            }

            if (monChon.isEmpty()) {
                chuyenVe(request, response, maBan, null, "Vui lòng chọn ít nhất 1 món (số lượng từ 1 trở lên)!");
                return;
            }

            ban.setTrangThai(DANG_PHUC_VU);
            banRepository.save(ban, monChon);
            request.getSession().setAttribute("success", "Đã gọi món cho " + ban.getTenBan() + "!");
            response.sendRedirect(request.getContextPath() + "/ban");
        } catch (Exception e) {
            e.printStackTrace();
            chuyenVe(request, response, maBan, null, "Lỗi khi gọi món: " + e.getMessage());
        }
    }

    private void xuLyThanhToan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer maBan = parseId(request.getParameter("maBan"));
        String phuongThuc = trim(request.getParameter("phuongThuc"));

        if (!DS_THANH_TOAN.contains(phuongThuc)) {
            chuyenVe(request, response, maBan, null, "Vui lòng chọn phương thức thanh toán!");
            return;
        }

        try {
            Ban ban = banRepository.findById(maBan);
            if (ban == null) {
                chuyenVe(request, response, null, null, "Không tìm thấy bàn!");
                return;
            }

            BigDecimal tong = banRepository.thanhToan(maBan, phuongThuc);

            String tien = NumberFormat.getInstance(Locale.US).format(tong);
            request.getSession().setAttribute("success",
                    "Đã thanh toán " + tien + " VNĐ cho " + ban.getTenBan() + " bằng " + phuongThuc
                            + ". Hóa đơn đã được lưu.");
            response.sendRedirect(request.getContextPath() + "/ban");
        } catch (IllegalStateException e) {
            chuyenVe(request, response, maBan, null, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            chuyenVe(request, response, maBan, null, "Lỗi khi thanh toán: " + e.getMessage());
        }
    }

    private void hienThiDanhSach(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        chuyenThongBao(request);

        String keyword = trim(request.getParameter("keyword"));
        String trangThaiLoc = request.getParameter("trangThaiLoc");

        try {
            List<Ban> dsBan = banRepository.findAll(keyword, trangThaiLoc);
            Map<Integer, List<Map<String, Object>>> chiTiet = banRepository.findChiTietMonTatCaBan();

            List<BanCard> dsCard = new ArrayList<>();
            for (Ban ban : dsBan) {
                List<Map<String, Object>> mon = chiTiet.get(ban.getMaBan());
                boolean coMon = mon != null && !mon.isEmpty();
                BigDecimal tamTinh = BigDecimal.ZERO;
                StringBuilder tomTat = new StringBuilder();
                String anh = "";
                if (coMon) {
                    for (Map<String, Object> m : mon) {
                        int sl = (Integer) m.get("soLuong");
                        tamTinh = tamTinh.add(((BigDecimal) m.get("gia")).multiply(BigDecimal.valueOf(sl)));
                        if (tomTat.length() > 0) tomTat.append(", ");
                        tomTat.append(sl).append(' ').append(m.get("ten"));
                    }
                    anh = (String) mon.get(0).get("anh");
                }
                dsCard.add(new BanCard(ban, DANG_PHUC_VU.equals(ban.getTrangThai()),
                        coMon, tamTinh, tomTat.toString(), anh));
            }

            int toMi = 0;
            for (List<Map<String, Object>> mon : chiTiet.values()) {
                for (Map<String, Object> m : mon) {
                    if (laMon_Mi((String) m.get("ten"))) toMi += (Integer) m.get("soLuong");
                }
            }

            request.setAttribute("dsCard", dsCard);
            request.setAttribute("banTrong", banRepository.countByTrangThai(TRONG));
            request.setAttribute("banCoKhach", banRepository.countByTrangThai(DANG_PHUC_VU));
            request.setAttribute("toMiDangLam", toMi);
            request.setAttribute("donChoThanhToan", chiTiet.size()); // số bàn đã gọi món nhưng chưa thanh toán
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách bàn: " + e.getMessage());
        }

        napDoanhThu(request);
        request.getRequestDispatcher("/jsp/ban.jsp").forward(request, response);
    }

    private void napDoanhThu(HttpServletRequest request) {
        BigDecimal homNay = BigDecimal.ZERO;
        BigDecimal thangNay = BigDecimal.ZERO;
        SoSanh ssNgay = new SoSanh(0, "Chưa có dữ liệu");
        SoSanh ssThang = new SoSanh(0, "Chưa có dữ liệu");

        try {
            LocalDate homNayLd = LocalDate.now();
            LocalDateTime dauHomNay = homNayLd.atStartOfDay();
            LocalDateTime dauNgayMai = homNayLd.plusDays(1).atStartOfDay();
            LocalDateTime dauHomQua = homNayLd.minusDays(1).atStartOfDay();

            LocalDate dauThangLd = homNayLd.withDayOfMonth(1);
            LocalDateTime dauThang = dauThangLd.atStartOfDay();
            LocalDateTime dauThangSau = dauThangLd.plusMonths(1).atStartOfDay();
            LocalDateTime dauThangTruoc = dauThangLd.minusMonths(1).atStartOfDay();

            homNay = banRepository.doanhThu(dauHomNay, dauNgayMai);
            BigDecimal homQua = banRepository.doanhThu(dauHomQua, dauHomNay);
            thangNay = banRepository.doanhThu(dauThang, dauThangSau);
            BigDecimal thangTruoc = banRepository.doanhThu(dauThangTruoc, dauThang);

            ssNgay = soSanh(homNay, homQua, "hôm qua");
            ssThang = soSanh(thangNay, thangTruoc, "tháng trước");
        } catch (Exception e) {
            e.printStackTrace();
            if (request.getAttribute("error") == null) {
                request.setAttribute("error", "Không đọc được doanh thu từ bảng HOA_DON: " + e.getMessage());
            }
        }

        request.setAttribute("doanhThuHomNay", homNay);
        request.setAttribute("doanhThuThangNay", thangNay);
        request.setAttribute("ssHomNay", ssNgay);
        request.setAttribute("ssThangNay", ssThang);
    }

    private SoSanh soSanh(BigDecimal hienTai, BigDecimal truoc, String moc) {
        if (truoc.signum() == 0) {
            return hienTai.signum() == 0
                    ? new SoSanh(0, "Chưa có doanh thu")
                    : new SoSanh(0, "Chưa có doanh thu " + moc);
        }
        BigDecimal phanTram = hienTai.subtract(truoc)
                .multiply(BigDecimal.valueOf(100))
                .divide(truoc, 1, RoundingMode.HALF_UP);
        return new SoSanh(phanTram.signum(), phanTram.abs().toPlainString() + "% so với " + moc);
    }

    private boolean laMon_Mi(String ten) {
        if (ten == null) return false;
        String n = Normalizer.normalize(ten, Normalizer.Form.NFC).trim().toLowerCase(Locale.ROOT);
        return n.startsWith(Normalizer.normalize("mì", Normalizer.Form.NFC));
    }

    private void hienThiGoiMon(HttpServletRequest request, HttpServletResponse response, Integer maBan)
            throws ServletException, IOException {
        try {
            Ban ban = banRepository.findById(maBan);
            if (ban == null) {
                chuyenVe(request, response, null, null, "Không tìm thấy bàn cần gọi món!");
                return;
            }

            Map<Integer, Integer> monDaChon = banRepository.findMonByBan(maBan);
            List<Map<String, Object>> dsMon = banRepository.findMonAn();
            for (Map<String, Object> mon : dsMon) {
                mon.put("soLuong", monDaChon.getOrDefault((Integer) mon.get("ma"), 0));
            }

            chuyenThongBao(request);
            request.setAttribute("banGoiMon", ban);
            request.setAttribute("dsMon", dsMon);
            request.setAttribute("monDaChon", monDaChon);
            request.setAttribute("tamTinh", tinhTong(monDaChon, dsMon));
            request.setAttribute("soLuongToiDa", SO_LUONG_TOI_DA);
            request.getRequestDispatcher("/jsp/goi-mon.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            chuyenVe(request, response, null, null, "Không tải được trang gọi món: " + e.getMessage());
        }
    }

    private BigDecimal tinhTong(Map<Integer, Integer> monDaChon, List<Map<String, Object>> dsMon) {
        BigDecimal tong = BigDecimal.ZERO;
        for (Map<String, Object> mon : dsMon) {
            int sl = monDaChon.getOrDefault((Integer) mon.get("ma"), 0);
            tong = tong.add(((BigDecimal) mon.get("gia")).multiply(BigDecimal.valueOf(sl)));
        }
        return tong;
    }

    private void chuyenVe(HttpServletRequest request, HttpServletResponse response,
                          Integer maBan, String success, String error) throws IOException {
        if (success != null) request.getSession().setAttribute("success", success);
        if (error != null) request.getSession().setAttribute("error", error);
        String url = request.getContextPath() + "/ban";
        if (maBan != null) url += "?action=order&maBan=" + maBan;
        response.sendRedirect(url);
    }

    private void chuyenThongBao(HttpServletRequest request) {
        for (String key : new String[]{"success", "error"}) {
            Object msg = request.getSession().getAttribute(key);
            if (msg != null) {
                request.setAttribute(key, msg);
                request.getSession().removeAttribute(key);
            }
        }
    }

    private int docSoLuong(String s) {
        if (s == null || s.isBlank()) return 0;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Integer parseId(String s) {
        try {
            return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
