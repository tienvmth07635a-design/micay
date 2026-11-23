package com.example.quanlyquanmicay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BAN_MON")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BanMon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ban_mon")
    private Integer maBanMon;

    @Column(name = "ma_ban", nullable = false)
    private Integer maBan;

    @Column(name = "ma_san_pham", nullable = false)
    private Integer maSanPham;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;
}
