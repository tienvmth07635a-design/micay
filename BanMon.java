package com.example.quanlyquanmicay.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BAN_MON")
@Getter
@Setter
@NoArgsConstructor
public class BanMon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ban_mon")
    private Integer maBanMon;

    @Column(name = "ma_ban", nullable = false)
    private Integer maBan;

    @Column(name = "ten_mon", nullable = false, length = 255)
    private String tenMon;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    public BanMon(String tenMon, Integer soLuong) {
        this.tenMon = tenMon;
        this.soLuong = soLuong;
    }
}
