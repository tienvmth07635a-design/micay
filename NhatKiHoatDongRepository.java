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
    private Session openSession() {
        return HibernateConfig.getSessionFactory().openSession();
    }

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

            // Lấy chức vụ từ bảng NHAN_VIEN để hiển thị
            Map<Integer, String> chucVu = new HashMap<>();
            List<Object[]> rows = session
                    .createNativeQuery("SELECT ma_nhan_vien, chuc_vu FROM NHAN_VIEN", Object[].class)
                    .list();
            for (Object[] r : rows) {
                chucVu.put(((Number) r[0]).intValue(), (String) r[1]);
            }

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

    // Chức năng khác gọi hàm này để ghi nhật kí.
    // Ví dụ: new NhatKiHoatDongRepository().ghiNhatKy(1, "Thêm món: Mì cay cấp 3");
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
