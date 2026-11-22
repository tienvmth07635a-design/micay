package com.example.quanlyquanmicay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "NHAN_VIEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private String chucVu; // "Nhân viên" hoặc "Quản lý"

    @Column(name = "tien_luong_gio")
    private BigDecimal tienLuongGio;

    @Column(name = "tinh_gio_lam")
    private Double tinhGioLam; // Số giờ làm

    @Column(name = "trang_thai")
    private String trangThai; // "Đang làm việc", "Hết giờ làm", "Nghỉ làm"

    @Column(name = "ma_nguoi_dung")
    private Integer maNguoiDung;

    @ManyToOne
    @JoinColumn(name = "ma_quan_ly")
    private QuanLy quanLy;
}
