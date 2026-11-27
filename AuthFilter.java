package com.example.quanlyquanmicay.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String ctx = req.getContextPath();
        String path = req.getRequestURI().substring(ctx.length());

        if (path.equals("/dang-nhap") || path.equals("/dang-xuat")
                || path.startsWith("/css") || path.startsWith("/js")
                || path.startsWith("/image") || path.startsWith("/images")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        Object vaiTroObj = (session != null) ? session.getAttribute("vaiTro") : null;
        String vaiTro = vaiTroObj != null ? vaiTroObj.toString() : "";

        if (!"QUAN_LY".equals(vaiTro) && !"NHAN_VIEN".equals(vaiTro)) {
            res.sendRedirect(ctx + "/dang-nhap");
            return;
        }

        chain.doFilter(request, response);
    }
}
