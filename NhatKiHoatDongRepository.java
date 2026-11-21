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

    public List<NhatKiHoatDong> findAll(String keyword) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            String hql = "FROM NhatKiHoatDong n ";
            boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
            if (hasKeyword) {
                hql += "WHERE LOWER(n.hanhDong) LIKE :kw OR CAST(n.maNhatKi AS string) LIKE :kw ";
            }
            hql += "ORDER BY n.thoiGian DESC, n.maNhatKi DESC";

            Query<NhatKiHoatDong> query = session.createQuery(hql, NhatKiHoatDong.class);
            if (hasKeyword) {
                query.setParameter("kw", "%" + keyword.trim().toLowerCase() + "%");
            }

            List<NhatKiHoatDong> list = query.list();
            for (NhatKiHoatDong nk : list) {
                if (nk.getMaNhanVien() == null || nk.getMaNhanVien() == 1) {
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

    public void ghiNhatKy(Integer maNhanVien, String hanhDong) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
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
