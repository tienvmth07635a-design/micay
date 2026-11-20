package com.example.quanlyquanmicay.repository;

import com.example.quanlyquanmicay.model.Voucher;
import com.example.quanlyquanmicay.util.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class VoucherRepository {

    public List<Voucher> findAll() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("FROM Voucher v ORDER BY v.ngayKetThuc DESC", Voucher.class).list();
        }
    }

    public Voucher findById(String maVoucher) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.find(Voucher.class, maVoucher);
        }
    }

    public List<Voucher> findConHan() {
        LocalDate today = LocalDate.now();
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery(
                            "FROM Voucher v WHERE (v.ngayKetThuc IS NULL OR v.ngayKetThuc >= :today) " +
                                    "AND (v.trangThai IS NULL OR v.trangThai = true) " +
                                    "ORDER BY v.ngayKetThuc ASC", Voucher.class)
                    .setParameter("today", today)
                    .list();
        }
    }

    public List<Voucher> findHetHan() {
        LocalDate today = LocalDate.now();
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery(
                            "FROM Voucher v WHERE (v.ngayKetThuc IS NOT NULL AND v.ngayKetThuc < :today) " +
                                    "OR v.trangThai = false " +
                                    "ORDER BY v.ngayKetThuc DESC", Voucher.class)
                    .setParameter("today", today)
                    .list();
        }
    }

    public void save(Voucher voucher) {
        Session session = HibernateConfig.getFACTORY().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(voucher);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(getRootCauseMessage(e), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void update(Voucher voucher) {
        Session session = HibernateConfig.getFACTORY().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.merge(voucher);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(getRootCauseMessage(e), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void khoaHetHan(String maVoucher) {
        Session session = HibernateConfig.getFACTORY().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Voucher v = session.find(Voucher.class, maVoucher);
            if (v != null) {
                v.setTrangThai(false);
                if (v.getNgayKetThuc() == null || !v.getNgayKetThuc().isBefore(LocalDate.now())) {
                    v.setNgayKetThuc(LocalDate.now().minusDays(1));
                }
                session.merge(v);
                session.flush();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(getRootCauseMessage(e), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    private String getRootCauseMessage(Throwable t) {
        Throwable cur = t;
        while (cur.getCause() != null && cur.getCause() != cur) {
            cur = cur.getCause();
        }
        return cur.getMessage() != null ? cur.getMessage() : t.toString();
    }
}
