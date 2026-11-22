package com.example.quanlyquanmicay.repository;

import com.example.quanlyquanmicay.model.CaiDatHeThong;
import com.example.quanlyquanmicay.util.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class CaiDatHeThongRepository {

    // Chỗ DUY NHẤT phụ thuộc cách kết nối của nhóm.
    private Session openSession() {
        return HibernateConfig.getSessionFactory().openSession();
    }

    // Lấy cài đặt hiện tại (bảng chỉ dùng 1 dòng). Chưa có dòng nào thì trả về null.
    public CaiDatHeThong layCaiDat() {
        try (Session session = openSession()) {
            return session.createQuery(
                            "FROM CaiDatHeThong c ORDER BY c.maCaiDat ASC", CaiDatHeThong.class)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lưu cài đặt: có id thì cập nhật, chưa có thì thêm mới
    public boolean luu(CaiDatHeThong caiDat) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                caiDat.setNgayCapNhat(LocalDateTime.now());
                session.merge(caiDat);
                tx.commit();
                return true;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
