package com.example.quanlyquanmicay.filter;

import com.example.quanlyquanmicay.model.NhanVien;
import com.example.quanlyquanmicay.repository.NhanVienRepository;
import com.example.quanlyquanmicay.repository.NhatKiHoatDongRepository;
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
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@WebFilter(filterName = "NhatKiFilter", urlPatterns = "/*")
public class NhatKiFilter implements Filter {

    private static final boolean DEBUG = true;
    private static final boolean MAC_DINH_LA_QUAN_LY = false;

    private static final Map<String, String> CHUC_NANG = new LinkedHashMap<>();
    static {
        CHUC_NANG.put("/hoa-don-chi-tiet", "Chi tiết hóa đơn");
        CHUC_NANG.put("/hoa-don", "Hóa đơn");
        CHUC_NANG.put("/san-pham", "Sản phẩm");
        CHUC_NANG.put("/khach-hang", "Khách hàng");
        CHUC_NANG.put("/khuyen-mai", "Khuyến mãi");
        CHUC_NANG.put("/voucher", "Voucher");
        CHUC_NANG.put("/nhan-vien", "Nhân viên");
        CHUC_NANG.put("/quan-ly", "Quản lý");
        CHUC_NANG.put("/goi-mon", "Gọi món");
        CHUC_NANG.put("/ban", "Bàn");
        CHUC_NANG.put("/home", "Trang chủ");
    }

    private static final Map<String, String> POST_MAC_DINH = new LinkedHashMap<>();
    static {
        POST_MAC_DINH.put("/san-pham", "Thêm");
        POST_MAC_DINH.put("/voucher", "Thêm");
        POST_MAC_DINH.put("/khuyen-mai", "Thêm");
        POST_MAC_DINH.put("/khach-hang", "Thêm");
        POST_MAC_DINH.put("/quan-ly", "Thêm");
    }

    private static final Set<String> GET_CAN_GHI = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "Xóa", "Khóa", "Mở khóa", "Đổi trạng thái", "Ngừng bán", "Tiếp tục bán"
            ))
    );

    private static final String[] BO_QUA = {
            "/css", "/js", "/image", "/images", "/jsp", "/WEB-INF", "/nhat-ki", "/dang-nhap", "/dang-xuat"
    };

    private static final String[] TU_KHOA_LOI = {
            "error", "err", "loi", "fail", "thatbai", "invalid", "exception"
    };

    private final NhatKiHoatDongRepository nhatKi = new NhatKiHoatDongRepository();
    private final NhanVienRepository nhanVienRepo = new NhanVienRepository();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);

        try {
            ghiNeuLaThaoTac(req, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ghiNeuLaThaoTac(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(false);
        Object vt = (session != null) ? session.getAttribute("vaiTro") : null;
        if (!"QUAN_LY".equals(vt) && !"NHAN_VIEN".equals(vt)) return;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.contains(".")) return;
        for (String tienTo : BO_QUA) {
            if (path.startsWith(tienTo)) return;
        }

        int status = res.getStatus();
        if (status >= 400) return;

        String method = req.getMethod();
        String action = tham(req, "action");
        String khoa = timKhoaChucNang(path);
        String thaoTac = xacDinhThaoTac(action);

        boolean laPost = "POST".equalsIgnoreCase(method);
        boolean laGetDoiDuLieu = "GET".equalsIgnoreCase(method)
                && thaoTac != null && GET_CAN_GHI.contains(thaoTac);
        if (!laPost && !laGetDoiDuLieu) return;

        boolean chuyenHuong = status >= 300 && status < 400;

        if (DEBUG) {
            System.out.println("[NhatKiFilter] " + method + " " + path
                    + " | status=" + status
                    + " | action=" + action
                    + " | thamSo=" + Collections.list(req.getParameterNames())
                    + " | thuocTinh=" + tenThuocTinh(req));
        }

        if (!chuyenHuong && coThongBaoLoi(req)) {
            if (DEBUG) System.out.println("[NhatKiFilter] -> bo qua vi co thong bao loi");
            return;
        }

        if (thaoTac == null && laPost) {
            thaoTac = action.isEmpty() && khoa != null && POST_MAC_DINH.containsKey(khoa)
                    ? POST_MAC_DINH.get(khoa) : "Cập nhật dữ liệu";
        }

        String noiDung = moTa(path, khoa, thaoTac, req);
        if (noiDung.length() > 255) {
            noiDung = noiDung.substring(0, 252) + "...";
        }

        nhatKi.ghiNhatKy(layMaNhanVien(req), noiDung);
    }

    private String moTa(String path, String khoa, String thaoTac, HttpServletRequest req) {
        if ("/san-pham".equals(khoa)) {
            String s = moTaSanPham(thaoTac, req);
            if (s != null) return s;
        } else if ("/voucher".equals(khoa)) {
            String s = moTaVoucher(thaoTac, req);
            if (s != null) return s;
        } else if ("/nhan-vien".equals(khoa)) {
            String s = moTaNhanVien(thaoTac, req);
            if (s != null) return s;
        } else if ("/goi-mon".equals(khoa)) {
            return moTaGoiMon(req);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(thaoTac).append(" - ").append(tenChucNang(path, khoa));
        String nhanDien = layNhanDien(req);
        if (nhanDien != null) {
            sb.append(": ").append(nhanDien);
        }
        return sb.toString();
    }

    private String moTaSanPham(String thaoTac, HttpServletRequest req) {
        String ma   = thamNhieu(req, "maSanPham", "txt_maSanPham", "ma_san_pham", "id");
        String ten  = thamNhieu(req, "tenSanPham", "txt_tenSanPham", "ten_san_pham");
        String gia  = thamNhieu(req, "giaTien", "txt_giaTien", "gia_tien", "gia");
        String anh  = thamNhieu(req, "anhSanPham", "txt_anhSanPham", "anh_san_pham");
        String moTa = thamNhieu(req, "moTa", "txt_moTa", "mo_ta");

        if ("Thêm".equals(thaoTac)) {
            StringBuilder sb = new StringBuilder("Thêm sản phẩm");
            if (!ten.isEmpty()) sb.append(" \"").append(ten).append("\"");
            if (!gia.isEmpty()) sb.append(" | Giá: ").append(tien(gia));
            if (!anh.isEmpty()) sb.append(" | Có ảnh");
            if (!moTa.isEmpty()) sb.append(" | Mô tả: ").append(moTa);
            return sb.toString();
        }
        if ("Sửa".equals(thaoTac) || "Cập nhật".equals(thaoTac)) {
            StringBuilder sb = new StringBuilder("Cập nhật sản phẩm");
            if (!ten.isEmpty()) sb.append(" \"").append(ten).append("\"");
            if (!ma.isEmpty()) sb.append(" (mã ").append(ma).append(")");
            if (!gia.isEmpty()) sb.append(" | Giá: ").append(tien(gia));
            if (!moTa.isEmpty()) sb.append(" | Mô tả: ").append(moTa);
            return sb.toString();
        }
        if ("Xóa".equals(thaoTac) || "Ngừng bán".equals(thaoTac) || "Tiếp tục bán".equals(thaoTac)) {
            return thaoTac + " sản phẩm"
                    + (!ten.isEmpty() ? " \"" + ten + "\"" : "")
                    + (!ma.isEmpty() ? " (mã " + ma + ")" : "");
        }
        return null;
    }

    private String moTaVoucher(String thaoTac, HttpServletRequest req) {
        if ("Thêm".equals(thaoTac)) {
            String ma        = thamNhieu(req, "txt_maVoucher", "maVoucher", "ma_voucher");
            String code      = thamNhieu(req, "txt_maCode", "maCode", "ma_code");
            String ten       = thamNhieu(req, "txt_tenVoucher", "tenVoucher", "ten_voucher");
            String loai      = thamNhieu(req, "sel_loaiGiam", "loaiGiam", "loai_giam");
            if (loai.isEmpty()) loai = "Tiền mặt";
            String giaTri    = thamNhieu(req, "txt_giaTriGiam", "giaTriGiam", "gia_tri_giam");
            String toiThieu  = thamNhieu(req, "txt_donToiThieu", "giaTriDonToiThieu", "gia_tri_don_toi_thieu");
            String soLuong   = thamNhieu(req, "txt_soLuong", "soLuong", "so_luong");
            String hanDung   = thamNhieu(req, "txt_ngayKetThuc", "ngayKetThuc", "ngay_ket_thuc");
            String trangThai = thamNhieu(req, "sel_trangThai", "trangThai", "trang_thai");

            boolean laPhanTram = "Phần trăm".equalsIgnoreCase(loai)
                    || "PHAN_TRAM".equalsIgnoreCase(loai)
                    || "%".equals(loai);

            String trangThaiHan = trangThai;
            if (!hanDung.isEmpty()) {
                try {
                    String ngayCat = hanDung.length() >= 10 ? hanDung.substring(0, 10) : hanDung;
                    boolean conHan = !LocalDate.parse(ngayCat).isBefore(LocalDate.now());
                    if (trangThaiHan.isEmpty()) {
                        trangThaiHan = conHan ? "Còn hạn" : "Đã hết hạn";
                    }
                } catch (Exception ignored) {
                    if (trangThaiHan.isEmpty()) trangThaiHan = "Còn hạn";
                }
            } else if (trangThaiHan.isEmpty()) {
                trangThaiHan = "Còn hạn";
            }

            StringBuilder sb = new StringBuilder("Thêm voucher");
            if (!ma.isEmpty()) sb.append(" ").append(ma);
            if (!ten.isEmpty()) sb.append(" \"").append(ten).append("\"");
            if (!code.isEmpty()) sb.append(" (code ").append(code).append(")");
            sb.append(": loại ").append(laPhanTram ? "Phần trăm" : loai);
            if (!giaTri.isEmpty()) {
                sb.append(", giảm ").append(laPhanTram ? soThuong(giaTri) + "%" : tien(giaTri));
            }
            sb.append(laSoKhong(toiThieu) ? ", không yêu cầu đơn tối thiểu" : ", đơn tối thiểu " + tien(toiThieu));
            if (!soLuong.isEmpty()) sb.append(", số lượng ").append(soLuong);
            if (!hanDung.isEmpty()) sb.append(", hạn dùng ").append(ngay(hanDung));
            sb.append(", ").append(trangThaiHan);
            return sb.toString();
        }
        if ("Khóa".equals(thaoTac)) {
            return "Khóa voucher " + tham(req, "id") + " (chuyển sang hết hạn)";
        }
        return null;
    }

    private String moTaNhanVien(String thaoTac, HttpServletRequest req) {
        String ten = tham(req, "tenNhanVien");
        String chucVu = tham(req, "chucVu");
        String trangThai = tham(req, "trangThai");
        String luong = tham(req, "tienLuongGio");

        if ("Thêm".equals(thaoTac)) {
            return "Thêm nhân viên \"" + ten + "\": chức vụ " + (chucVu.isEmpty() ? "Nhân viên" : chucVu)
                    + (luong.isEmpty() ? "" : ", lương " + tien(luong) + "/giờ")
                    + ", trạng thái " + (trangThai.isEmpty() ? "Đang làm việc" : trangThai);
        }
        if ("Sửa".equals(thaoTac)) {
            String ma = tham(req, "maNhanVien");
            return "Sửa thông tin nhân viên \"" + ten + "\"" + (ma.isEmpty() ? "" : " (mã " + ma + ")")
                    + (chucVu.isEmpty() ? "" : ": chức vụ " + chucVu)
                    + (luong.isEmpty() ? "" : ", lương " + tien(luong) + "/giờ")
                    + (trangThai.isEmpty() ? "" : ", trạng thái " + trangThai);
        }
        if ("Khóa".equals(thaoTac)) {
            String id = tham(req, "id");
            String tenTrongDb = tenNhanVienTheoMa(id);
            return "Khóa nhân viên " + (tenTrongDb.isEmpty() ? "" : "\"" + tenTrongDb + "\" ")
                    + "(mã " + id + "): chuyển sang Nghỉ làm";
        }
        return null;
    }

    private String moTaGoiMon(HttpServletRequest req) {
        String maBan = tham(req, "maBan");
        String[] mon = req.getParameterValues("maSanPham");
        int soMon = mon == null ? 0 : mon.length;
        return "Gọi món cho bàn" + (maBan.isEmpty() ? "" : " (mã " + maBan + ")")
                + (soMon > 0 ? ": " + soMon + " món" : "");
    }

    private Integer layMaNhanVien(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object vaiTro = session.getAttribute("vaiTro");
            if (vaiTro != null) {
                String vt = vaiTro.toString().trim().toLowerCase();
                if (vt.equals("khach") || vt.equals("khách") || vt.equals("guest")) {
                    return null;
                }
            }
            Object o = session.getAttribute("maNhanVien");
            if (o instanceof Integer) {
                return (Integer) o;
            }
            Object nv = session.getAttribute("nhanVien");
            if (nv instanceof NhanVien) {
                return ((NhanVien) nv).getMaNhanVien();
            }
        }

        if (!MAC_DINH_LA_QUAN_LY) {
            return null;
        }
        try {
            List<NhanVien> ds = nhanVienRepo.getAll();
            for (NhanVien nv : ds) {
                String cv = nv.getChucVu() == null ? "" : nv.getChucVu().trim();
                String tt = nv.getTrangThai() == null ? "" : nv.getTrangThai().trim();
                if ("Quản lý".equalsIgnoreCase(cv) && !"Nghỉ làm".equalsIgnoreCase(tt)) {
                    return nv.getMaNhanVien();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String tenNhanVienTheoMa(String id) {
        try {
            if (id.matches("\\d{1,9}")) {
                NhanVien nv = nhanVienRepo.getById(Integer.valueOf(id));
                if (nv != null && nv.getTenNhanVien() != null) {
                    return nv.getTenNhanVien().trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String xacDinhThaoTac(String action) {
        String s = action == null ? "" : action.toLowerCase();
        if (s.isEmpty()) return null;
        if (s.contains("xoa") || s.contains("delete") || s.contains("remove")) return "Xóa";
        if (s.contains("unlock") || s.contains("mokhoa") || s.contains("mo-khoa") || s.contains("mo_khoa")) return "Mở khóa";
        if (s.contains("khoa") || s.contains("lock")) return "Khóa";
        if (s.contains("continue")) return "Tiếp tục bán";
        if (s.contains("stop")) return "Ngừng bán";
        if (s.contains("status") || s.contains("trangthai") || s.contains("trang-thai") || s.contains("toggle")) return "Đổi trạng thái";
        if (s.equals("sua") || s.contains("update") || s.contains("edit") || s.contains("capnhat") || s.contains("cap-nhat")) {
            return s.equals("sua") ? "Sửa" : "Cập nhật";
        }
        if (s.contains("them") || s.contains("add") || s.contains("create") || s.contains("insert")) return "Thêm";
        return null;
    }

    private String timKhoaChucNang(String path) {
        for (String k : CHUC_NANG.keySet()) {
            if (path.equals(k) || path.startsWith(k + "/")) {
                return k;
            }
        }
        return null;
    }

    private String tenChucNang(String path, String khoa) {
        if (khoa != null) return CHUC_NANG.get(khoa);
        return path.replaceFirst("^/", "").replace('-', ' ');
    }

    private List<String> tenThuocTinh(HttpServletRequest req) {
        List<String> ds = new ArrayList<>();
        for (String ten : Collections.list(req.getAttributeNames())) {
            if (ten.startsWith("jakarta.") || ten.startsWith("javax.")
                    || ten.startsWith("org.apache.")) {
                continue;
            }
            ds.add(ten);
        }
        return ds;
    }

    private boolean coThongBaoLoi(HttpServletRequest req) {
        for (String ten : tenThuocTinh(req)) {
            String t = ten.toLowerCase();
            for (String tuKhoa : TU_KHOA_LOI) {
                if (t.contains(tuKhoa)) {
                    Object giaTri = req.getAttribute(ten);
                    if (giaTri != null && !giaTri.toString().trim().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String layNhanDien(HttpServletRequest req) {
        List<String> dsMa = new ArrayList<>();
        List<String> dsCode = new ArrayList<>();
        List<String> dsTen = new ArrayList<>();

        for (String ten : Collections.list(req.getParameterNames())) {
            String t = ten.toLowerCase().replaceFirst("^(txt_|sel_)", "");
            if (t.contains("khau") || t.contains("pass") || t.contains("token") || t.equals("action")) continue;

            String v = tham(req, ten);
            if (v.isEmpty()) continue;

            if (t.equals("id") || t.matches("ma[a-z_]*") && !t.contains("code")) {
                dsMa.add(v);
            } else if (t.contains("code")) {
                dsCode.add(v);
            } else if (t.startsWith("ten")) {
                dsTen.add(v);
            }
        }

        List<String> tatCa = new ArrayList<>();
        tatCa.addAll(dsTen);
        tatCa.addAll(dsMa);
        tatCa.addAll(dsCode);

        List<String> ketQua = new ArrayList<>();
        for (String v : tatCa) {
            if (!ketQua.contains(v) && ketQua.size() < 3) {
                ketQua.add(v);
            }
        }
        return ketQua.isEmpty() ? null : String.join(", ", ketQua);
    }

    private String thamNhieu(HttpServletRequest req, String... tenThamSo) {
        for (String ten : tenThamSo) {
            String v = tham(req, ten);
            if (!v.isEmpty()) return v;
        }
        return "";
    }

    private String tham(HttpServletRequest req, String ten) {
        String v = req.getParameter(ten);
        if (v == null) return "";
        v = v.trim().replaceAll("\\s+", " ");
        return v.length() > 60 ? v.substring(0, 60) : v;
    }

    private boolean laSoKhong(String s) {
        if (s == null || s.isEmpty()) return true;
        try {
            return new BigDecimal(s).compareTo(BigDecimal.ZERO) == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String tien(String s) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));
            nf.setMaximumFractionDigits(2);
            return nf.format(new BigDecimal(s)) + "đ";
        } catch (Exception e) {
            return s;
        }
    }

    private String soThuong(String s) {
        try {
            return new BigDecimal(s).stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            return s;
        }
    }

    private String ngay(String s) {
        try {
            String ngayCat = s.length() >= 10 ? s.substring(0, 10) : s;
            return LocalDate.parse(ngayCat).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return s;
        }
    }
}
