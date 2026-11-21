package com.example.quanlyquanmicay.controller;

import com.example.quanlyquanmicay.model.SanPham;
import com.example.quanlyquanmicay.repository.SanPhamRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "SanPhamServlet", value = "/san-pham")
public class SanPhamServlet extends HttpServlet {

    private final SanPhamRepository repository = new SanPhamRepository();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String edit = request.getParameter("edit");

        if (edit != null) {
            try {
                int id = Integer.parseInt(edit);
                SanPham sanPham = repository.findById(id);

                if (sanPham != null) {
                    request.setAttribute("sanPhamSua", sanPham);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        request.setAttribute("dsSanPham", repository.findAll());

        request.getRequestDispatcher("/jsp/san_pham.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {

                SanPham sp = new SanPham();

                sp.setTenSanPham(request.getParameter("tenSanPham").trim());
                sp.setGiaTien(new BigDecimal(request.getParameter("giaTien")));
                sp.setAnhSanPham(request.getParameter("anhSanPham"));
                sp.setMoTa(request.getParameter("moTa"));

                if (sp.getTenSanPham().isEmpty()
                        || sp.getGiaTien().compareTo(BigDecimal.ZERO) <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                repository.save(sp);

            } else if ("update".equals(action)) {

                int id = Integer.parseInt(request.getParameter("maSanPham"));

                SanPham sp = repository.findById(id);

                if (sp == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                String ten = request.getParameter("tenSanPham").trim();
                BigDecimal gia = new BigDecimal(request.getParameter("giaTien"));

                if (ten.isEmpty() || gia.compareTo(BigDecimal.ZERO) <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                sp.setTenSanPham(ten);
                sp.setGiaTien(gia);
                sp.setAnhSanPham(request.getParameter("anhSanPham"));
                sp.setMoTa(request.getParameter("moTa"));

                repository.update(sp);

            } else if ("stop".equals(action)) {

                int id = Integer.parseInt(request.getParameter("maSanPham"));

                repository.stopSelling(id);

            } else if ("continue".equals(action)) {

                int id = Integer.parseInt(request.getParameter("maSanPham"));

                repository.continueSelling(id);

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/san-pham?success=1"
            );

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Mã sản phẩm hoặc giá tiền không hợp lệ."
            );
        } catch (Exception e) {
            throw new ServletException("Lỗi xử lý sản phẩm", e);
        }
    }
}
