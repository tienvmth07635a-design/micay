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

    // Chỗ DUY NHẤT phụ thuộc cách kết nối của nhóm.
    // Nếu nhóm trưởng dùng cách khác thì chỉ sửa dòng return này.
    private Session openSession() {
        return HibernateConfig.getSessionFactory().openSession();
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

            // Tra chức vụ riêng: nếu lỗi thì chỉ mất tên hiển thị, KHÔNG mất danh sách
            Map<Integer, String> chucVu = layChucVu(session);

            for (NhatKiHoatDong nk : list) {
                Integer ma = nk.getMaNhanVien();
                if (ma == null) {
                    nk.setTenNhanVienHienThi("Không xác định");
                } else {
                    String cv = chucVu.get(ma);
                    nk.setTenNhanVienHienThi(cv != null ? cv + " (#" + ma + ")" : "Nhân viên #" + ma);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Đọc chức vụ từ bảng NHAN_VIEN có sẵn (chỉ SELECT)
    private Map<Integer, String> layChucVu(Session session) {
        Map<Integer, String> map = new HashMap<>();
        try {
            List<Object[]> rows = session
                    .createNativeQuery("SELECT ma_nhan_vien, chuc_vu FROM NHAN_VIEN", Object[].class)
                    .list();
            for (Object[] r : rows) {
                map.put(((Number) r[0]).intValue(), (String) r[1]);
            }
        } catch (Exception e) {
            System.out.println("Khong lay duoc chuc vu nhan vien: " + e.getMessage());
        }
        return map;
    }

    // ===== GHI: các chức năng khác của nhóm gọi các hàm này =====

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

    // Bản ngắn: không cần mã nhân viên (khi nhóm chưa có đăng nhập).
    // Ví dụ: new NhatKiHoatDongRepository().ghiNhatKy("Thanh toán hóa đơn #12");
    public void ghiNhatKy(String hanhDong) {
        ghiNhatKy(null, hanhDong);
    }
}
