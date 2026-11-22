package com.example.quanlyquanmicay.controller;

import com.example.quanlyquanmicay.model.NhatKiHoatDong;
import com.example.quanlyquanmicay.repository.NhatKiHoatDongRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "NhatKiHoatDongServlet", value = "/nhat-ki")
public class NhatKiHoatDongServlet extends HttpServlet {

    private final NhatKiHoatDongRepository repo = new NhatKiHoatDongRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String keyword = request.getParameter("keyword");
        List<NhatKiHoatDong> dsNhatKy = repo.findAll(keyword);

        request.setAttribute("dsNhatKy", dsNhatKy);
        request.setAttribute("keyword", keyword);

        // Forward tới trang riêng, KHÔNG phải index.jsp
        request.getRequestDispatcher("/jsp/nhat_ki.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
