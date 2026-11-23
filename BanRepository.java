package com.example.quanlyquanmicay.repository;

import com.example.quanlyquanmicay.model.Ban;
import com.example.quanlyquanmicay.util.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BanRepository {

    private static final String TRANG_THAI_DA_THANH_TOAN = "Đã thanh toán";

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
                    "select ma_san_pham, ten_san_pham, anh_san_pham, gia_tien from SAN_PHAM order by ma_san_pham",
                    Object[].class).getResultList();

            List<Map<String, Object>> ds = new ArrayList<>();
            for (Object[] r : rows) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("ma", ((Number) r[0]).intValue());
                m.put("ten", String.valueOf(r[1]));
                m.put("anh", chuanHoaAnh(r[2]));
                m.put("gia", r[3] == null ? BigDecimal.ZERO : new BigDecimal(r[3].toString()));
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

    public Map<Integer, List<Map<String, Object>>> findChiTietMonTatCaBan() {
        Map<Integer, List<Map<String, Object>>> map = new LinkedHashMap<>();
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            List<Object[]> rows = session.createNativeQuery(
                    "select bm.ma_ban, sp.ten_san_pham, sp.anh_san_pham, bm.so_luong, sp.gia_tien "
                            + "from BAN_MON bm join SAN_PHAM sp on sp.ma_san_pham = bm.ma_san_pham "
                            + "order by bm.ma_ban, bm.ma_ban_mon",
                    Object[].class).getResultList();
            for (Object[] r : rows) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("ten", String.valueOf(r[1]));
                m.put("anh", chuanHoaAnh(r[2]));
                m.put("soLuong", ((Number) r[3]).intValue());
                m.put("gia", r[4] == null ? BigDecimal.ZERO : new BigDecimal(r[4].toString()));
                map.computeIfAbsent(((Number) r[0]).intValue(), k -> new ArrayList<>()).add(m);
            }
        }
        return map;
    }

    public BigDecimal thanhToan(Integer maBan, String phuongThuc) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            tx = session.beginTransaction();

            Ban ban = session.get(Ban.class, maBan);
            if (ban == null) throw new IllegalStateException("Không tìm thấy bàn!");

            BigDecimal tong = session.doReturningWork(conn -> {
                BigDecimal tongTien;
                int soDong;
                try (PreparedStatement ps = conn.prepareStatement(
                        "select coalesce(sum(bm.so_luong * sp.gia_tien), 0), count(*) "
                                + "from BAN_MON bm join SAN_PHAM sp on sp.ma_san_pham = bm.ma_san_pham "
                                + "where bm.ma_ban = ?")) {
                    ps.setInt(1, maBan);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        tongTien = rs.getBigDecimal(1);
                        soDong = rs.getInt(2);
                    }
                }
                if (soDong == 0) throw new IllegalStateException("Bàn này chưa có món nào để thanh toán!");

                int maHoaDon;
                try (PreparedStatement ps = conn.prepareStatement(
                        "insert into HOA_DON (ngay_lap, tong_tien, trang_thai, phuong_thuc_thanh_toan) "
                                + "values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setBigDecimal(2, tongTien);
                    ps.setNString(3, TRANG_THAI_DA_THANH_TOAN);
                    ps.setNString(4, phuongThuc);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        maHoaDon = keys.getInt(1);
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "insert into HOA_DON_CHI_TIET (so_luong, don_gia, thanh_tien, ma_hoa_don, ma_san_pham) "
                                + "select bm.so_luong, sp.gia_tien, bm.so_luong * sp.gia_tien, ?, bm.ma_san_pham "
                                + "from BAN_MON bm join SAN_PHAM sp on sp.ma_san_pham = bm.ma_san_pham "
                                + "where bm.ma_ban = ?")) {
                    ps.setInt(1, maHoaDon);
                    ps.setInt(2, maBan);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement("delete from BAN_MON where ma_ban = ?")) {
                    ps.setInt(1, maBan);
                    ps.executeUpdate();
                }
                return tongTien;
            });

            ban.setTrangThai("Trống");
            tx.commit();
            return tong;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public BigDecimal doanhThu(LocalDateTime tu, LocalDateTime den) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            BigDecimal v = session.createNativeQuery(
                            "select coalesce(sum(tong_tien), 0) from HOA_DON "
                                    + "where trang_thai = :tt and ngay_lap >= :tu and ngay_lap < :den",
                            BigDecimal.class)
                    .setParameter("tt", TRANG_THAI_DA_THANH_TOAN)
                    .setParameter("tu", tu)
                    .setParameter("den", den)
                    .getSingleResult();
            return v == null ? BigDecimal.ZERO : v;
        }
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

    private String chuanHoaAnh(Object value) {
        String anh = value == null ? "" : value.toString().trim();
        if (!anh.isEmpty() && !anh.startsWith("/") && !anh.startsWith("http")) anh = "/image/" + anh;
        return anh;
    }
}
