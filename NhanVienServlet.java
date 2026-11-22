package com.example.quanlyquanmicay.controller;

import com.example.quanlyquanmicay.model.NhanVien;
import com.example.quanlyquanmicay.repository.NhanVienRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "NhanVienServlet", value = {"/nhan-vien"})
public class NhanVienServlet extends HttpServlet {
    private final NhanVienRepository repository = new NhanVienRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "hien-thi";
        }

        switch (action) {
            case "khoa": // Khóa = Chuyển trạng thái thành "Nghỉ làm" nhưng không xóa khỏi DB
                try {
                    Integer id = Integer.valueOf(request.getParameter("id"));
                    NhanVien nv = repository.getById(id);
                    if (nv != null) {
                        nv.setTrangThai("Nghỉ làm");
                        repository.update(nv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect(request.getContextPath() + "/nhan-vien");
                break;
            case "chi-tiet":
                try {
                    Integer id = Integer.valueOf(request.getParameter("id"));
                    NhanVien nv = repository.getById(id);
                    request.setAttribute("nv", nv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<NhanVien> listDetail = repository.getAll();
                request.setAttribute("listNhanVien", listDetail);
                request.getRequestDispatcher("/jsp/nhan_vien.jsp").forward(request, response);
                break;
            case "hien-thi":
            default:
                List<NhanVien> list = repository.getAll();
                request.setAttribute("listNhanVien", list);
                request.getRequestDispatcher("/jsp/nhan_vien.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        String tenNhanVien = request.getParameter("tenNhanVien");
        String soDienThoai = request.getParameter("soDienThoai");
        String chucVu = request.getParameter("chucVu");
        String trangThai = request.getParameter("trangThai");

        BigDecimal tienLuongGio = request.getParameter("tienLuongGio") != null && !request.getParameter("tienLuongGio").isEmpty()
                ? new BigDecimal(request.getParameter("tienLuongGio")) : BigDecimal.ZERO;

        Double tinhGioLam = request.getParameter("tinhGioLam") != null && !request.getParameter("tinhGioLam").isEmpty()
                ? Double.valueOf(request.getParameter("tinhGioLam")) : 0.0;

        if ("them".equals(action)) {
            NhanVien nv = new NhanVien();
            nv.setTenNhanVien(tenNhanVien);
            nv.setSoDienThoai(soDienThoai);
            nv.setChucVu(chucVu);
            nv.setTienLuongGio(tienLuongGio);
            nv.setTinhGioLam(tinhGioLam);
            nv.setTrangThai(trangThai != null ? trangThai : "Đang làm việc");
            repository.save(nv);
            response.sendRedirect(request.getContextPath() + "/nhan-vien");
        } else if ("sua".equals(action)) {
            try {
                Integer id = Integer.valueOf(request.getParameter("maNhanVien"));
                NhanVien nv = repository.getById(id);
                if (nv != null) {
                    nv.setTenNhanVien(tenNhanVien);
                    nv.setSoDienThoai(soDienThoai);
                    nv.setChucVu(chucVu);
                    nv.setTienLuongGio(tienLuongGio);
                    nv.setTinhGioLam(tinhGioLam);
                    nv.setTrangThai(trangThai);
                    repository.update(nv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.sendRedirect(request.getContextPath() + "/nhan-vien");
        } else {
            response.sendRedirect(request.getContextPath() + "/nhan-vien");
        }
    }
}
