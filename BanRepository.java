package com.example.quanlyquanmicay.repository;

import com.example.quanlyquanmicay.model.Ban;
import com.example.quanlyquanmicay.util.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BanRepository {
    public List<Ban> findAll() {
        return findAll(null, null);
    }
  
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

    public void save(Ban ban) {
        save(ban, null);
    }

    public void save(Ban ban, Map<Integer, Integer> monChon) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            tx = session.beginTransaction();

            Ban daLuu;
            if (ban.getMaBan() == null) {
                session.persist(ban);
                daLuu = ban;
            } else {
                daLuu = session.merge(ban);
            }
            session.flush();

            if (monChon != null) {
                session.createNativeMutationQuery("delete from BAN_MON where ma_ban = :ban")
                        .setParameter("ban", daLuu.getMaBan())
                        .executeUpdate();
                for (Map.Entry<Integer, Integer> e : monChon.entrySet()) {
                    session.createNativeMutationQuery(
                                    "insert into BAN_MON (ma_ban, ma_san_pham, so_luong) values (:ban, :sp, :sl)")
                            .setParameter("ban", daLuu.getMaBan())
                            .setParameter("sp", e.getKey())
                            .setParameter("sl", e.getValue())
                            .executeUpdate();
                }
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

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
            session.createNativeMutationQuery("delete from BAN_MON where ma_ban = :ban")
                    .setParameter("ban", id)
                    .executeUpdate();
            session.remove(ban);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<Map<String, Object>> findMonAn() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            List<Object[]> rows = session.createNativeQuery(
                    "select ma_san_pham, ten_san_pham, anh_san_pham from SAN_PHAM order by ma_san_pham",
                    Object[].class).getResultList();

            List<Map<String, Object>> ds = new ArrayList<>();
            for (Object[] r : rows) {
                String anh = r[2] == null ? "" : r[2].toString().trim();
                if (!anh.isEmpty() && !anh.startsWith("/")) anh = "/image/" + anh;

                Map<String, Object> m = new LinkedHashMap<>();
                m.put("ma", ((Number) r[0]).intValue());
                m.put("ten", String.valueOf(r[1]));
                m.put("anh", anh);
                ds.add(m);
            }
            return ds;
        }
    }

    public Map<Integer, Integer> findMonByBan(Integer maBan) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        if (maBan == null) return map;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            List<Object[]> rows = session.createNativeQuery(
                            "select ma_san_pham, so_luong from BAN_MON where ma_ban = :ban order by ma_ban_mon",
                            Object[].class)
                    .setParameter("ban", maBan)
                    .getResultList();
            for (Object[] r : rows) {
                map.put(((Number) r[0]).intValue(), ((Number) r[1]).intValue());
            }
        }
        return map;
    }

    public Map<Integer, List<String>> findMonHienThi() {
        Map<Integer, List<String>> map = new LinkedHashMap<>();
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            List<Object[]> rows = session.createNativeQuery(
                    "select bm.ma_ban, sp.ten_san_pham, bm.so_luong "
                            + "from BAN_MON bm join SAN_PHAM sp on sp.ma_san_pham = bm.ma_san_pham "
                            + "order by bm.ma_ban, bm.ma_ban_mon",
                    Object[].class).getResultList();
            for (Object[] r : rows) {
                map.computeIfAbsent(((Number) r[0]).intValue(), k -> new ArrayList<>())
                        .add(r[1] + " x" + ((Number) r[2]).intValue());
            }
        }
        return map;
    }

    public long count() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("select count(b) from Ban b", Long.class).getSingleResult();
        }
    }

    public long countByTrangThai(String trangThai) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery(
                            "select count(b) from Ban b where b.trangThai = :tt", Long.class)
                    .setParameter("tt", trangThai)
                    .getSingleResult();
        }
    }
}
