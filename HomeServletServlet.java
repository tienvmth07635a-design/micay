package com.example.quanlyquanmicay.controller;

import java.io.*;

import com.example.quanlyquanmicay.repository.BanRepository;
import com.example.quanlyquanmicay.repository.HoaDonRepository;
import com.example.quanlyquanmicay.repository.SanPhamRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "HomeServletServletServlet", value = "/HomeServlet")
public class HomeServletServlet extends HttpServlet {
    private final BanRepository banRepository = new BanRepository();
    private final SanPhamRepository sanPhamRepository = new SanPhamRepository();
    private final HoaDonRepository hoaDonRepository = new HoaDonRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        //request.setAttribute("dsBan", banRepository.findAll());
        //request.setAttribute("tongSanPham", sanPhamRepository.findAll().size());
        //request.setAttribute("tongHoaDon", hoaDonRepository.findAll().size());
        request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}