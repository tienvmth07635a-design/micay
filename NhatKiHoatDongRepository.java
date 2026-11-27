package com.example.quanlyquanmicay.repository;

import com.example.quanlyquanmicay.model.NhatKiHoatDong;
import com.example.quanlyquanmicay.util.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NhatKiHoatDongRepository {

    // Chỗ DUY NHẤT phụ thuộc cách kết nối của nhóm (HibernateConfig của nhóm có hàm getFACTORY()).
    private Session openSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    // ===== ĐỌC: chỉ SELECT, không sửa gì trong database của nhóm =====
    public List<NhatKiHoatDong> findAll(String keyword) {
        try (Session session = openSession()) {
            boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

            String hql = "FROM NhatKiHoatDong n ";
            if (hasKeyword) {
                hql += "WHERE LOWER(n.hanhDong) LIKE :kw "
                        + "OR CAST(n.maNhatKi AS string) LIKE :kw ";
            }
            hql += "ORDER BY n.thoiGian DESC, n.maNhatKi DESC";

            Query<NhatKiHoatDong> query = session.createQuery(hql, NhatKiHoatDong.class);
            if (hasKeyword) {
                query.setParameter("kw", "%" + keyword.trim().toLowerCase() + "%");
            }
            List<NhatKiHoatDong> list = query.list();

            // Tra tên nhân viên riêng: nếu lỗi thì chỉ mất tên hiển thị, KHÔNG mất danh sách
            Map<Integer, String> tenNhanVien = layTenNhanVien(session);

            for (NhatKiHoatDong nk : list) {
                Integer ma = nk.getMaNhanVien();
                if (ma == null) {
                    // Không xác định được người thao tác (tài khoản khách, chưa đăng nhập...)
                    nk.setTenNhanVienHienThi("Không xác định");
                } else {
                    String ten = tenNhanVien.get(ma);
                    nk.setTenNhanVienHienThi(ten != null ? ten : "Nhân viên #" + ma);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Đọc tên + chức vụ từ bảng NHAN_VIEN có sẵn (chỉ SELECT).
    // Ví dụ kết quả: "Nguyễn Văn A (Admin)" nếu chức vụ là Quản lý, "Trần Thị B" nếu là nhân viên.
    private Map<Integer, String> layTenNhanVien(Session session) {
        Map<Integer, String> map = new HashMap<>();
        try {
            List<Object[]> rows = session
                    .createNativeQuery("SELECT ma_nhan_vien, ten_nhan_vien, chuc_vu FROM NHAN_VIEN", Object[].class)
                    .list();
            for (Object[] r : rows) {
                int ma = ((Number) r[0]).intValue();
                String ten = r[1] == null ? "" : ((String) r[1]).trim();
                String chucVu = r[2] == null ? "" : ((String) r[2]).trim();

                String hienThi = ten.isEmpty() ? "Nhân viên #" + ma : ten;
                if ("Quản lý".equalsIgnoreCase(chucVu)) {
                    hienThi += " (Admin)";
                }
                map.put(ma, hienThi);
            }
        } catch (Exception e) {
            System.out.println("Khong lay duoc ten nhan vien: " + e.getMessage());
        }
        return map;
    }

    // ===== GHI: các chức năng khác của nhóm (và NhatKiFilter) gọi các hàm này =====

    // Ví dụ: new NhatKiHoatDongRepository().ghiNhatKy(1, "Thêm món: Mì cay cấp 3");
    public void ghiNhatKy(Integer maNhanVien, String hanhDong) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                NhatKiHoatDong log = NhatKiHoatDong.builder()
                        .maNhanVien(maNhanVien)
                        .hanhDong(hanhDong)
                        .thoiGian(LocalDateTime.now())
                        .build();
                session.persist(log);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bản ngắn: không cần mã nhân viên (khi chưa biết ai thao tác thì hiện "Không xác định").
    // Ví dụ: new NhatKiHoatDongRepository().ghiNhatKy("Thanh toán hóa đơn #12");
    public void ghiNhatKy(String hanhDong) {
        ghiNhatKy(null, hanhDong);
    }
}
