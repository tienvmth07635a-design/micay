package com.example.quanlyquanmicay.controller;

import com.example.quanlyquanmicay.model.Ban;
import com.example.quanlyquanmicay.repository.BanRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "BanServlet", urlPatterns = {"/ban"})
public class BanServlet extends HttpServlet {

    private static final String TRONG = "Trống";
    private static final String DANG_PHUC_VU = "Đang phục vụ";
    private static final int SO_LUONG_TOI_DA = 99;

    private final BanRepository banRepository = new BanRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            Ban banEdit = banRepository.findById(parseId(request.getParameter("maBan")));
            request.setAttribute("banEdit", banEdit);
            if (banEdit != null) {
                request.setAttribute("monMap", banRepository.findMonByBan(banEdit.getMaBan()));
            }
        } else if ("delete".equals(action)) {
            try {
                banRepository.deleteById(parseId(request.getParameter("maBan")));
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("error", "Không thể xóa bàn: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/ban");
                return;
            }
            request.getSession().setAttribute("success", "Đã xóa bàn thành công!");
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

        String maBanStr = request.getParameter("maBan");
        String tenBan = trim(request.getParameter("tenBan"));
        String trangThai = trim(request.getParameter("trangThai"));
        if (!TRONG.equals(trangThai) && !DANG_PHUC_VU.equals(trangThai)) {
            trangThai = TRONG;
        }

        Map<Integer, Integer> monChon = new LinkedHashMap<>();
        String[] maSp = request.getParameterValues("maSanPham");
        String[] soLuong = request.getParameterValues("soLuong");
        if (maSp != null && soLuong != null) {
            int n = Math.min(maSp.length, soLuong.length);
            for (int i = 0; i < n; i++) {
                Integer id = parseId(maSp[i]);
                int sl = parseSoLuong(soLuong[i]);
                if (id != null && sl > 0) {
                    monChon.merge(id, sl, (a, b) -> Math.min(a + b, SO_LUONG_TOI_DA));
                }
            }
        }

        Ban ban = new Ban();
        ban.setMaBan(parseId(maBanStr));
        ban.setTenBan(tenBan);
        ban.setTrangThai(trangThai);

        String loi = null;
        if (tenBan.isEmpty()) loi = "Tên bàn không được để trống!";
        else if (tenBan.length() > 100) loi = "Tên bàn tối đa 100 ký tự!";

        if (loi == null) {
            try {
                Set<Integer> hopLe = new HashSet<>();
                for (Map<String, Object> m : banRepository.findMonAn()) {
                    hopLe.add((Integer) m.get("ma"));
                }
                monChon.keySet().retainAll(hopLe);

                banRepository.save(ban, monChon);
            } catch (Exception e) {
                e.printStackTrace();
                loi = "Lỗi khi lưu bàn: " + e.getMessage();
            }
        }

        if (loi != null) {
            request.setAttribute("error", loi);
            request.setAttribute("banEdit", ban);
            request.setAttribute("monMap", monChon);
            hienThiDanhSach(request, response);
            return;
        }

        request.getSession().setAttribute("success",
                parseId(maBanStr) == null ? "Đã thêm bàn mới thành công!" : "Đã cập nhật bàn thành công!");
        response.sendRedirect(request.getContextPath() + "/ban"); // Post-Redirect-Get
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

        try {
            request.setAttribute("dsMon", banRepository.findMonAn());
            request.setAttribute("monHienThi", banRepository.findMonHienThi());
            request.setAttribute("dsBan", banRepository.findAll(keyword, trangThaiLoc));
            request.setAttribute("tongBan", banRepository.count());
            request.setAttribute("banTrong", banRepository.countByTrangThai(TRONG));
            request.setAttribute("banDangPhucVu", banRepository.countByTrangThai(DANG_PHUC_VU));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không tải được danh sách bàn: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/ban.jsp").forward(request, response);
    }

    private int parseSoLuong(String s) {
        try {
            int v = Integer.parseInt(s == null ? "0" : s.trim());
            return Math.max(0, Math.min(v, SO_LUONG_TOI_DA));
        } catch (NumberFormatException e) {
            return 0;
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
