package com.example.quanlyquanmicay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "NHAN_VIEN")
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nhan_vien")
    private Integer maNhanVien;

    @Column(name = "ten_nhan_vien")
    private String tenNhanVien;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "chuc_vu")
    private String chucVu;

    @Column(name = "tien_luong_gio")
    private BigDecimal tienLuongGio;

    @Column(name = "tinh_gio_lam")
    private Double tinhGioLam;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "ma_nguoi_dung")
    private Integer maNguoiDung;

    @Column(name = "email")
    private String email;

    @Column(name = "mat_khau")
    private String matKhau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_quan_ly")
    private QuanLy quanLy;

    public NhanVien() {
    }

    public NhanVien(Integer maNhanVien, String tenNhanVien, String soDienThoai, String chucVu,
                    BigDecimal tienLuongGio, Double tinhGioLam, String trangThai,
                    Integer maNguoiDung, QuanLy quanLy) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.soDienThoai = soDienThoai;
        this.chucVu = chucVu;
        this.tienLuongGio = tienLuongGio;
        this.tinhGioLam = tinhGioLam;
        this.trangThai = trangThai;
        this.maNguoiDung = maNguoiDung;
        this.quanLy = quanLy;
    }

    public Integer getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(Integer maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public BigDecimal getTienLuongGio() { return tienLuongGio; }
    public void setTienLuongGio(BigDecimal tienLuongGio) { this.tienLuongGio = tienLuongGio; }

    public Double getTinhGioLam() { return tinhGioLam; }
    public void setTinhGioLam(Double tinhGioLam) { this.tinhGioLam = tinhGioLam; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Integer getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(Integer maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public QuanLy getQuanLy() { return quanLy; }
    public void setQuanLy(QuanLy quanLy) { this.quanLy = quanLy; }
}
