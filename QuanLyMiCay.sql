-- Kiểm tra và tạo Database trong SQL Server
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'QuanLyQuanMiCay')
BEGIN
    CREATE DATABASE QuanLyQuanMiCay;
END
GO

USE QuanLyQuanMiCay;
GO

-- 1. Bảng Khách Hàng
CREATE TABLE KHACH_HANG (
    ma_khach_hang INT IDENTITY(1,1) PRIMARY KEY,
    ho_ten NVARCHAR(255) NOT NULL
);
GO

-- 2. Bảng Quản Lý
CREATE TABLE QUAN_LY (
    ma_quan_ly INT IDENTITY(1,1) PRIMARY KEY,
    ngay_nhan_chuc DATE,
    ma_nguoi_dung INT
);
GO

-- 3. Bảng Nhân Viên
CREATE TABLE NHAN_VIEN (
    ma_nhan_vien INT IDENTITY(1,1) PRIMARY KEY,
    chuc_vu NVARCHAR(100),
    trang_thai BIT,
    ma_nguoi_dung INT,
    ma_quan_ly INT,
    CONSTRAINT FK_NV_QuanLy FOREIGN KEY (ma_quan_ly) REFERENCES QUAN_LY(ma_quan_ly)
);
GO

-- 4. Bảng Bàn
CREATE TABLE BAN (
    ma_ban INT IDENTITY(1,1) PRIMARY KEY,
    ten_ban NVARCHAR(100) NOT NULL,
    trang_thai NVARCHAR(50)
);
GO

-- 5. Bảng Sản Phẩm
CREATE TABLE SAN_PHAM (
    ma_san_pham INT IDENTITY(1,1) PRIMARY KEY,
    ten_san_pham NVARCHAR(255) NOT NULL,
    gia_tien DECIMAL(12, 2) NOT NULL,
    anh_san_pham VARCHAR(500),
    mo_ta NVARCHAR(MAX)
);
ALTER TABLE SAN_PHAM
ADD trang_thai NVARCHAR(50) NOT NULL DEFAULT N'Đang bán';
GO
GO

-- 6. Bảng Khuyến Mãi
CREATE TABLE KHUYEN_MAI (
    ma_khuyen_mai INT IDENTITY(1,1) PRIMARY KEY,
    ma_code VARCHAR(50) UNIQUE,
    ten_khuyen_mai NVARCHAR(255),
    loai_giam VARCHAR(50),
    giam_toi_da DECIMAL(12, 2),
    gia_tri_giam DECIMAL(12, 2),
    ngay_bat_dau DATETIME,
    ngay_ket_thuc DATETIME,
    trang_thai BIT
);
GO

-- 7. Bảng Voucher
CREATE TABLE VOUCHER (
    ma_voucher INT IDENTITY(1,1) PRIMARY KEY,
    ma_code VARCHAR(50) UNIQUE,
    ten_voucher NVARCHAR(255),
    loai_giam VARCHAR(50),
    gia_tri_giam DECIMAL(12, 2),
    gia_tri_don_toi_thieu DECIMAL(12, 2),
    giam_toi_da DECIMAL(12, 2),
    so_luong INT,
    so_luong_da_dung INT,
    trang_thai BIT,
    ngay_ket_thuc DATETIME
);
GO

-- 8. Bảng Hóa Đơn
CREATE TABLE HOA_DON (
    ma_hoa_don INT IDENTITY(1,1) PRIMARY KEY,
    ngay_lap DATETIME DEFAULT GETDATE(),
    tong_tien DECIMAL(12, 2),
    trang_thai NVARCHAR(50),
    phuong_thuc_thanh_toan NVARCHAR(50),
    ma_don_hang INT,
    ma_nhan_vien INT,
    ma_khach_hang INT,
    ma_khuyen_mai INT NULL,
    ma_voucher INT NULL,
    CONSTRAINT FK_HD_NhanVien FOREIGN KEY (ma_nhan_vien) REFERENCES NHAN_VIEN(ma_nhan_vien),
    CONSTRAINT FK_HD_KhachHang FOREIGN KEY (ma_khach_hang) REFERENCES KHACH_HANG(ma_khach_hang),
    CONSTRAINT FK_HD_KhuyenMai FOREIGN KEY (ma_khuyen_mai) REFERENCES KHUYEN_MAI(ma_khuyen_mai),
    CONSTRAINT FK_HD_Voucher FOREIGN KEY (ma_voucher) REFERENCES VOUCHER(ma_voucher)
);
GO

-- 9. Bảng Hóa Đơn Chi Tiết
CREATE TABLE HOA_DON_CHI_TIET (
    ma_ct_hoa_don INT IDENTITY(1,1) PRIMARY KEY,
    so_luong INT NOT NULL,
    don_gia DECIMAL(12, 2) NOT NULL,
    thanh_tien DECIMAL(12, 2) NOT NULL,
    ma_hoa_don INT,
    ma_san_pham INT,
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (ma_hoa_don) REFERENCES HOA_DON(ma_hoa_don),
    CONSTRAINT FK_CTHD_SanPham FOREIGN KEY (ma_san_pham) REFERENCES SAN_PHAM(ma_san_pham)
);
GO

-- 10. Bảng Nhật Ký Hoạt Động
CREATE TABLE NHAT_KI_HOAT_DONG (
    ma_nhat_ki INT IDENTITY(1,1) PRIMARY KEY,
    hanh_dong NVARCHAR(255),
    thoi_gian DATETIME DEFAULT GETDATE(),
    ma_nhan_vien INT,
    CONSTRAINT FK_NK_NhanVien FOREIGN KEY (ma_nhan_vien) REFERENCES NHAN_VIEN(ma_nhan_vien)
);
GO
