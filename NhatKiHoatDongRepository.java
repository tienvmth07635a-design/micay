package com.example.quanlyquanmicay.repository;

import com.example.quanlyquanmicay.model.NhatKiHoatDong;
import com.example.quanlyquanmicay.util.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class NhatKiHoatDongRepository {

    private static final int MA_ADMIN = 1;

    // Chỗ DUY NHẤT phụ thuộc cách kết nối của nhóm.
    // Nếu nhóm dùng class khác thì chỉ sửa dòng return bên dưới.
    private Session openSession() {
        return HibernateConfig.getSessionFactory().openSession();
    }

    // Lấy danh sách nhật kí, có thể tìm theo từ khóa (hành động hoặc mã nhật kí)
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
            for (NhatKiHoatDong nk : list) {
                if (nk.getMaNhanVien() == null || nk.getMaNhanVien() == MA_ADMIN) {
                    nk.setTenNhanVienHienThi("Quản lý / Admin");
                } else {
                    nk.setTenNhanVienHienThi("Nhân viên #" + nk.getMaNhanVien());
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Các chức năng khác của nhóm gọi hàm này để ghi nhật kí
    // Ví dụ: new NhatKiHoatDongRepository().ghiNhatKy(1, "Thêm sản phẩm: Mì cay cấp độ 3");
    public void ghiNhatKy(Integer maNhanVien, String hanhDong) {
        Transaction tx = null;
        try (Session session = openSession()) {
            tx = session.beginTransaction();
            NhatKiHoatDong log = NhatKiHoatDong.builder()
                    .maNhanVien(maNhanVien)
                    .hanhDong(hanhDong)
                    .thoiGian(LocalDateTime.now())
                    .build();
            session.persist(log);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
