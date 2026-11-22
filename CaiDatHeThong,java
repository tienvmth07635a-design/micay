package com.example.quanlyquanmicay.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CAI_DAT_HE_THONG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaiDatHeThong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_cai_dat")
    private Integer maCaiDat;

    @Column(name = "ten_quan", nullable = false, length = 255)
    private String tenQuan;

    @Column(name = "dia_chi", nullable = false, length = 500)
    private String diaChi;

    @Column(name = "so_dien_thoai", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "thue_vat", nullable = false, precision = 5, scale = 2)
    private BigDecimal thueVat;

    @Column(name = "tu_dong_in_hoa_don", nullable = false)
    private Boolean tuDongInHoaDon;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    // Hiển thị "8" thay vì "8.00" trên form
    public String getThueVatHienThi() {
        return thueVat == null ? "" : thueVat.stripTrailingZeros().toPlainString();
    }
}
