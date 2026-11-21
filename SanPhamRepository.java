
package com.example.quanlyquanmicay.repository;

import com.example.quanlyquanmicay.model.SanPham;
import com.example.quanlyquanmicay.util.HibernateConfig;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SanPhamRepository {


    public List<SanPham> findAll() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery(
                    "FROM SanPham ORDER BY maSanPham ASC",
                    SanPham.class
            ).list();
        }
    }


    public SanPham findById(int id) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.get(SanPham.class, id);
        }
    }


    public void save(SanPham sanPham) {
        Transaction transaction = null;

        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();

            sanPham.setTrangThai(true);

            session.persist(sanPham);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }


    public void update(SanPham sanPham) {
        Transaction transaction = null;

        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();

            SanPham sp = session.get(SanPham.class, sanPham.getMaSanPham());

            if (sp != null) {
                sp.setTenSanPham(sanPham.getTenSanPham());
                sp.setGiaTien(sanPham.getGiaTien());
                sp.setAnhSanPham(sanPham.getAnhSanPham());
                sp.setMoTa(sanPham.getMoTa());
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }


    public void stopSelling(int id) {
        Transaction transaction = null;

        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();

            SanPham sanPham = session.get(SanPham.class, id);

            if (sanPham != null) {
                sanPham.setTrangThai(false);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }


    public void continueSelling(int id) {
        Transaction transaction = null;

        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();

            SanPham sanPham = session.get(SanPham.class, id);

            if (sanPham != null) {
                sanPham.setTrangThai(true);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }


    public long countAll() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery(
                    "SELECT COUNT(sp) FROM SanPham sp",
                    Long.class
            ).uniqueResult();
        }
    }
}
