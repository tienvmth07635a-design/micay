package com.example.quanlyquanmicay.controller;

import com.example.quanlyquanmicay.model.NhanVien;
import com.example.quanlyquanmicay.model.QuanLy;
import com.example.quanlyquanmicay.repository.NhanVienRepository;
import com.example.quanlyquanmicay.repository.QuanLyRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DangNhapServlet", value = "/dang-nhap")
public class DangNhapServlet extends HttpServlet {

    private final NhanVienRepository nhanVienRepo = new NhanVienRepository();
    private final QuanLyRepository quanLyRepo = new QuanLyRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("vaiTro") != null) {
            res.sendRedirect(req.getContextPath() + "/HomeServlet");
            return;
        }
        req.getRequestDispatcher("/jsp/dang_nhap.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String taiKhoan = req.getParameter("taiKhoan");
        String matKhau = req.getParameter("matKhau");
        String tk = taiKhoan == null ? "" : taiKhoan.trim();
        String mk = matKhau == null ? "" : matKhau.trim();

        req.setAttribute("taiKhoan", taiKhoan);

        if (tk.isEmpty() || mk.isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            req.getRequestDispatcher("/jsp/dang_nhap.jsp").forward(req, res);
            return;
        }

        String vaiTro = null;
        String vaiTroHienThi = null;
        String tenHienThi = null;
        Integer maNhanVien = null;

        List<NhanVien> dsNV = nhanVienRepo.getAll();

        // 1. Kiểm tra trong bảng QUAN_LY (1 quản lý duy nhất)
        for (QuanLy ql : quanLyRepo.findAll()) {
            String email = ql.getEmail() == null ? "" : ql.getEmail().trim();
            String sdt   = ql.getSoDienThoai() == null ? "" : ql.getSoDienThoai().trim();
            String pass  = ql.getMatKhau() == null ? "" : ql.getMatKhau().trim();

            if ((!email.isEmpty() && email.equalsIgnoreCase(tk)) || (!sdt.isEmpty() && sdt.equalsIgnoreCase(tk))) {
                if (!pass.equals(mk)) {
                    req.setAttribute("errorMessage", "Mật khẩu không đúng!");
                    req.getRequestDispatcher("/jsp/dang_nhap.jsp").forward(req, res);
                    return;
                }
                vaiTro = "QUAN_LY";
                vaiTroHienThi = "Quản lý";
                tenHienThi = ql.getTenQuanLy();
                maNhanVien = timMaNhanVienQuanLy(dsNV);
                break;
            }
        }

        // 2. Nếu chưa khớp Quản lý -> kiểm tra trong bảng NHAN_VIEN
        if (vaiTro == null) {
            for (NhanVien nv : dsNV) {
                String email = nv.getEmail() == null ? "" : nv.getEmail().trim();
                String sdt   = nv.getSoDienThoai() == null ? "" : nv.getSoDienThoai().trim();
                String pass  = nv.getMatKhau() == null ? "" : nv.getMatKhau().trim();

                if ((!email.isEmpty() && email.equalsIgnoreCase(tk)) || (!sdt.isEmpty() && sdt.equalsIgnoreCase(tk))) {
                    if ("Nghỉ làm".equalsIgnoreCase(nv.getTrangThai())) {
                        req.setAttribute("errorMessage", "Tài khoản nhân viên đã nghỉ làm, không thể đăng nhập!");
                        req.getRequestDispatcher("/jsp/dang_nhap.jsp").forward(req, res);
                        return;
                    }
                    if (!pass.equals(mk)) {
                        req.setAttribute("errorMessage", "Mật khẩu không đúng!");
                        req.getRequestDispatcher("/jsp/dang_nhap.jsp").forward(req, res);
                        return;
                    }
                    boolean laQL = nv.getChucVu() != null && "Quản lý".equalsIgnoreCase(nv.getChucVu().trim());
                    vaiTro = laQL ? "QUAN_LY" : "NHAN_VIEN";
                    vaiTroHienThi = laQL ? "Quản lý" : "Nhân viên";
                    tenHienThi = nv.getTenNhanVien();
                    maNhanVien = nv.getMaNhanVien();
                    break;
                }
            }
        }

        if (vaiTro == null) {
            req.setAttribute("errorMessage", "Tài khoản không tồn tại trong hệ thống!");
            req.getRequestDispatcher("/jsp/dang_nhap.jsp").forward(req, res);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("vaiTro", vaiTro);
        session.setAttribute("vaiTroHienThi", vaiTroHienThi);
        session.setAttribute("tenHienThi", (tenHienThi != null && !tenHienThi.trim().isEmpty()) ? tenHienThi : vaiTroHienThi);
        if (maNhanVien != null) {
            session.setAttribute("maNhanVien", maNhanVien);
        } else {
            session.removeAttribute("maNhanVien");
        }

        res.sendRedirect(req.getContextPath() + "/HomeServlet");
    }

    private Integer timMaNhanVienQuanLy(List<NhanVien> dsNV) {
        for (NhanVien nv : dsNV) {
            if ("Nghỉ làm".equalsIgnoreCase(nv.getTrangThai())) continue;
            if (nv.getChucVu() != null && "Quản lý".equalsIgnoreCase(nv.getChucVu().trim())) {
                return nv.getMaNhanVien();
            }
        }
        return null;
    }
}
