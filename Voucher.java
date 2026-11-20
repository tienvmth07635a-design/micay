package com.example.quanlyquanmicay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "VOUCHER")
public class Voucher {
    @Id
    @Column(name = "ma_voucher")
    private String maVoucher;

    @Column(name = "ma_code")
    private String maCode;

    @Column(name = "ten_voucher")
    private String tenVoucher;

    @Column(name = "loai_giam")
    private String loaiGiam;

    @Column(name = "gia_tri_giam")
    private BigDecimal giaTriGiam;

    @Column(name = "gia_tri_don_toi_thieu")
    private BigDecimal giaTriDonToiThieu;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "so_luong_da_dung")
    private Integer soLuongDaDung;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    public Voucher() {
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public String getTenVoucher() {
        return tenVoucher;
    }

    public void setTenVoucher(String tenVoucher) {
        this.tenVoucher = tenVoucher;
    }

    public String getLoaiGiam() {
        return loaiGiam;
    }

    public void setLoaiGiam(String loaiGiam) {
        this.loaiGiam = loaiGiam;
    }

    public BigDecimal getGiaTriGiam() {
        return giaTriGiam;
    }

    public void setGiaTriGiam(BigDecimal giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }

    public BigDecimal getGiaTriDonToiThieu() {
        return giaTriDonToiThieu;
    }

    public void setGiaTriDonToiThieu(BigDecimal giaTriDonToiThieu) {
        this.giaTriDonToiThieu = giaTriDonToiThieu;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Integer getSoLuongDaDung() {
        return soLuongDaDung;
    }

    public void setSoLuongDaDung(Integer soLuongDaDung) {
        this.soLuongDaDung = soLuongDaDung;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}
