package com.example.quanlyquanmicay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "QUAN_LY")
public class QuanLy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_quan_ly")
    private Integer maQuanLy;

    @Column(name = "ten_quan_ly")
    private String tenQuanLy;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "email")
    private String email;

    @Column(name = "mat_khau")
    private String matKhau;

    public QuanLy() {
    }

    public QuanLy(Integer maQuanLy, String tenQuanLy, String soDienThoai, String trangThai) {
        this.maQuanLy = maQuanLy;
        this.tenQuanLy = tenQuanLy;
        this.soDienThoai = soDienThoai;
        this.trangThai = trangThai;
    }

    public Integer getMaQuanLy() { return maQuanLy; }
    public void setMaQuanLy(Integer maQuanLy) { this.maQuanLy = maQuanLy; }

    public String getTenQuanLy() { return tenQuanLy; }
    public void setTenQuanLy(String tenQuanLy) { this.tenQuanLy = tenQuanLy; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
}
