package com.example.quanlyquanmicay.controller;

import com.example.quanlyquanmicay.model.Ban;
import com.example.quanlyquanmicay.repository.BanRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BanServlet", urlPatterns = {"/ban"})
public class BanServlet extends HttpServlet {

    private static final String TRONG = "Trống";
    private static final String DANG_PHUC_VU = "Đang phục vụ";

    private static final List<String> DS_MON = List.of(
            "Bibimbap",
            "Canh kim chi",
            "Chả cá",
            "Coca",
            "Mì cay bò",
            "Mì hải sản",
            "Mì kim chi",
            "Mì thập cẩm",
            "Mì xúc xích",
            "Pepsi",
            "Thịt heo",
            "Tokbokki"
    );

    private final BanRepository banRepository = new BanRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            request.setAttribute("banEdit", banRepository.findById(parseId(request.getParameter("maBan"))));
        } else if ("delete".equals(action)) {
            try {
                banRepository.deleteById(parseId(request.getParameter("maBan")));
                request.getSession().setAttribute("success", "Đã xóa bàn thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("error", "Không thể xóa bàn: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/ban");
            return;
        }

        hienThiDanhSach(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String tenBan = trim(request.getParameter("tenBan"));
        String monGoi = trim(request.getParameter("monGoi"));
        String trangThai = trim(request.getParameter("trangThai"));
        if (!TRONG.equals(trangThai) && !DANG_PHUC_VU.equals(trangThai)) {
            trangThai = TRONG;
        }

        Ban ban = new Ban();
        ban.setMaBan(parseId(request.getParameter("maBan")));
        ban.setTenBan(tenBan);
        ban.setTrangThai(trangThai);
        ban.setMonGoi(monGoi.isEmpty() ? null : monGoi);

        // Validate
        String loi = null;
        if (tenBan.isEmpty()) loi = "Tên bàn không được để trống!";
        else if (tenBan.length() > 100) loi = "Tên bàn tối đa 100 ký tự!";
        else if (!monGoi.isEmpty() && !DS_MON.contains(monGoi)) loi = "Món đã chọn không hợp lệ!";

        if (loi == null) {
            try {
                banRepository.save(ban);
            } catch (Exception e) {
                e.printStackTrace();
                loi = "Lỗi khi lưu bàn: " + e.getMessage();
            }
        }

        if (loi != null) {
            request.setAttribute("error", loi);
            request.setAttribute("banEdit", ban);
            hienThiDanhSach(request, response);
            return;
        }

        request.getSession().setAttribute("success",
                ban.getMaBan() == null ? "Đã thêm bàn mới thành công!" : "Đã cập nhật bàn thành công!");
        response.sendRedirect(request.getContextPath() + "/ban");
    }

    private void hienThiDanhSach(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        for (String key : new String[]{"success", "error"}) {
            Object msg = request.getSession().getAttribute(key);
            if (msg != null) {
                request.setAttribute(key, msg);
                request.getSession().removeAttribute(key);
            }
        }

        String keyword = trim(request.getParameter("keyword"));
        String trangThaiLoc = request.getParameter("trangThaiLoc");

        request.setAttribute("dsMon", DS_MON);
        try {
            request.setAttribute("dsBan", banRepository.findAll(keyword, trangThaiLoc));
            request.setAttribute("tongBan", banRepository.count());
            request.setAttribute("banTrong", banRepository.countByTrangThai(TRONG));
            request.setAttribute("banDangPhucVu", banRepository.countByTrangThai(DANG_PHUC_VU));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách bàn: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/ban.jsp").forward(request, response);
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
