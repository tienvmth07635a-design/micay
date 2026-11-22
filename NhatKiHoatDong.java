package com.example.quanlyquanmicay.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "NHAT_KI_HOAT_DONG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhatKiHoatDong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nhat_ki")
    private Integer maNhatKi;

    @Column(name = "hanh_dong", nullable = false, length = 255)
    private String hanhDong;

    @Column(name = "thoi_gian")
    private LocalDateTime thoiGian;

    @Column(name = "ma_nhan_vien")
    private Integer maNhanVien;

    // Không lưu xuống DB, chỉ dùng để hiển thị trên JSP
    @Transient
    private String tenNhanVienHienThi;

    // JSP gọi bằng ${nk.thoiGianFormatted} -> 16/11/2026 - 15:30:12
    public String getThoiGianFormatted() {
        if (thoiGian == null) {
            return "";
        }
        return thoiGian.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
    }
}
