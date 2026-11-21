package com.example.quanlyquanmicay.repository;

import com.example.quanlyquanmicay.model.Ban;
import com.example.quanlyquanmicay.util.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BanRepository {

    /** Lấy toàn bộ bàn (dùng cho trang chủ) */
    public List<Ban> findAll() {
        return findAll(null, null);
    }

    /** Lấy danh sách bàn, lọc theo từ khóa (tên/mã bàn) và trạng thái. Tham số null/rỗng = không lọc */
    public List<Ban> findAll(String keyword, String trangThai) {
        boolean coKeyword = keyword != null && !keyword.isBlank();
        boolean coTrangThai = trangThai != null && !trangThai.isBlank() && !"ALL".equals(trangThai);

        StringBuilder hql = new StringBuilder("from Ban b where 1 = 1");
        if (coKeyword) {
            hql.append(" and (lower(b.tenBan) like :kw or cast(b.maBan as string) like :kw)");
        }
        if (coTrangThai) {
            hql.append(" and b.trangThai = :tt");
        }
        hql.append(" order by b.maBan");

        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            var query = session.createQuery(hql.toString(), Ban.class);
            if (coKeyword) query.setParameter("kw", "%" + keyword.trim().toLowerCase() + "%");
            if (coTrangThai) query.setParameter("tt", trangThai);
            return query.getResultList();
        }
    }

    public Ban findById(Integer id) {
        if (id == null) return null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.get(Ban.class, id);
        }
    }

    /** Thêm mới (maBan == null) hoặc cập nhật (maBan != null) */
    public void save(Ban ban) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            tx = session.beginTransaction();
            if (ban.getMaBan() == null) {
                session.persist(ban);
            } else {
                session.merge(ban);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    /** @return true nếu xóa được, false nếu không tìm thấy bàn */
    public boolean deleteById(Integer id) {
        if (id == null) return false;
        Transaction tx = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            tx = session.beginTransaction();
            Ban ban = session.get(Ban.class, id);
            if (ban == null) {
                tx.rollback();
                return false;
            }
            session.remove(ban);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    /** Đếm tổng số bàn */
    public long count() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("select count(b) from Ban b", Long.class).getSingleResult();
        }
    }

    /** Đếm số bàn theo trạng thái */
    public long countByTrangThai(String trangThai) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery(
                            "select count(b) from Ban b where b.trangThai = :tt", Long.class)
                    .setParameter("tt", trangThai)
                    .getSingleResult();
        }
    }
}
