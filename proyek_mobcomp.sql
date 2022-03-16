-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 09, 2021 at 05:45 AM
-- Server version: 10.5.12-MariaDB
-- PHP Version: 7.3.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `proyek_mobcomp`
--
CREATE DATABASE IF NOT EXISTS `proyek_mobcomp` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `proyek_mobcomp`;

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

DROP TABLE IF EXISTS `barang`;
CREATE TABLE `barang` (
  `id` int(11) NOT NULL,
  `fk_seller` varchar(50) NOT NULL,
  `fk_kategori` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `deskripsi` varchar(1000) NOT NULL,
  `harga` int(11) NOT NULL,
  `stok` int(11) NOT NULL,
  `gambar` varchar(50) NOT NULL DEFAULT 'default.jpg',
  `is_deleted` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`id`, `fk_seller`, `fk_kategori`, `nama`, `deskripsi`, `harga`, `stok`, `gambar`, `is_deleted`) VALUES
(1, 'asd', 2, 'Arcana Phantom Assassin', 'Arcana Phantom Assassin Dota 2', 400000, 999, 'produk_1.png', 0),
(2, 'asd', 1, 'Steam wallet IDR 12000', 'Steam Wallet 12K', 15000, 123, 'produk_2.jpg', 0),
(3, 'asd', 1, 'Steam Wallet IDR 45000', 'Steam Wallet 45K', 50000, 100, 'produk_3.jpg', 0),
(4, 'asd', 2, 'Arcana Juggernaut', 'Arcana Juggernaut Dota 2', 450000, 200, 'produk_4.png', 0),
(5, 'asd', 1, 'Steam wallet IDR 60000', 'steam wallet 60K', 68000, 123, 'produk_5.png', 0),
(6, 'asd', 4, 'Ak 47 | Legion of Anubis', 'skin csgo : ak 47 legion of anubis', 360000, 20, 'produk_6.png', 0),
(7, 'asd', 4, 'Desert Eagle | Code Red', 'skin csgo : desert eagle code red', 450000, 28, 'produk_7.png', 0),
(8, 'asd', 1, 'asd', 'asd', 80000, 123, 'produk_8.jpg', 1),
(9, 'asdasd', 4, 'M4A1-S | Decimator', 'Skin CSGO : M4A1-S | Decimator', 80000, 123, 'produk_9.png', 0),
(10, 'asdasd', 4, 'Ak 47 | Bloodsport', 'Skin CSGO : Ak 47 | Bloodsport', 280000, 20, 'produk_10.png', 0),
(11, 'asdasd', 4, 'AWP | Dragon Lore', 'Skin CSGO : AWP | Dragon Lore', 15000000, 3, 'produk_11.png', 0),
(12, 'asdasd', 4, 'Butterfly Knife | Crimson Web', 'Skin CSGO : Butterfly Knife | Crimson Web', 10500000, 4, 'produk_12.png', 0),
(13, 'asdasd', 4, 'AWP | Hyper Beast', 'Skin CSGO : AWP | Hyper Beast', 650000, 8, 'produk_13.jpeg', 0),
(14, 'asdasd', 4, 'Ak47 | Frontside Misty', 'Skin CSGO : Ak47 | Frontside Misty', 225000, 23, 'produk_14.jpg', 0),
(15, 'asdasd', 4, 'Gut Knife | Autotronic', 'Skin CSGO : Gut Knife | Autotronic', 1360000, 10, 'produk_15.jpg', 0),
(16, 'asdasd', 4, 'Butterfly Knife | Slaughter', 'Skin CSGO : Butterfly Knife | Slaughter', 8500000, 9, 'produk_16.jpg', 0),
(17, 'asdasd', 4, 'Flip Knife | Doppler Phase 4', 'Skin CSGO : Flip Knife | Doppler Phase 4', 1280000, 21, 'produk_17.jpg', 0),
(18, 'asdasd', 4, 'M4A4 | Neo Noir', 'Skin CSGO : M4A4 | Neo Noir', 295000, 16, 'produk_18.jpeg', 0),
(19, 'zxc', 3, 'Google Play Voucher IDR 50000', 'Google Play Voucher 50K', 54000, 23, 'produk_19.jpg', 0),
(20, 'zxc', 3, 'Google Play Voucher IDR 150000', 'Google Play Voucher 150K', 165000, 40, 'produk_20.jpg', 0),
(21, 'zxc', 3, 'Google Play Voucher IDR 100000', 'Google Play Voucher 100K', 109000, 11, 'produk_21.jpg', 0),
(22, 'zxc', 3, 'Google Play Voucher IDR 20000', 'Google Play Voucher 20K', 24000, 56, 'produk_22.jpg', 0),
(23, 'zxc', 3, 'Google Play Voucher IDR 300000', 'Google Play Voucher 300K', 320000, 8, 'produk_23.png', 0),
(24, 'zxc', 2, 'Arcana Terrorblade', 'Arcana Terrorblade Dota 2', 430000, 9, 'produk_24.png', 0),
(25, 'rty', 2, 'Katunsa', 'Item DOta 2 : Katunsa', 80000, 40, 'produk_25.png', 0),
(26, 'rty', 2, 'Dragonclaw Hook', 'Item DOta 2 : Dragonclaw Hook', 2550000, 3, 'produk_26.png', 0),
(27, 'rty', 2, 'Arcana Ogre', 'Item DOta 2 : Arcana Ogre', 460000, 10, 'produk_27.png', 0),
(28, 'rty', 2, 'Lightning Orchid', 'Item DOta 2 : Lightning Orchid', 25000, 48, 'produk_28.png', 0),
(29, 'rty', 2, 'Rapier of The Burning', 'Item Dota 2 : Rapier of The Burning', 65000, 40, 'produk_29.png', 0),
(30, 'rty', 2, 'Arcana Shadow Fiend', 'Item DOta 2 : Arcana Shadow Fiend', 435000, 20, 'produk_30.jpg', 0),
(31, 'rty', 2, 'Blade of Tear', 'Item DOta 2 : Blade of Tear', 240000, 34, 'produk_31.png', 0),
(32, 'zxc', 1, 'Steam Wallet IDR 600000', 'Steam Wallet 600K', 650000, 14, 'produk_32.jpg', 0),
(33, 'zxc', 1, 'Steam Wallet IDR 250000', 'Steam Wallet 250K', 275000, 17, 'produk_33.jpg', 0),
(34, 'zxc', 1, 'Steam Wallet IDR 120000', 'Steam Wallet 120K', 145000, 36, 'produk_34.jpg', 0),
(35, 'zxc', 5, 'Apple Gift Card Rp 180.000', 'Apple GIft Card nichhhh cuma 185k gasss dibeli!!!', 185000, 21, 'produk_35.jpg', 0),
(36, 'zxc', 5, 'Apple Gift Card Rp 100.000', 'Apple Gift Card cuma 102500', 102500, 5, 'produk_36.jpg', 0),
(37, 'zxc', 5, 'Apple Gift Card Rp 300.000', 'Apple Gift Card 300K, 10 menit kirim', 310000, 10, 'produk_37.jpg', 0),
(38, 'zxc', 5, 'Apple Gift Card Rp 250.000', 'Apple gift card murah cuma 255k, besok harga naik', 255000, 50, 'produk_38.jpg', 0),
(39, 'zxc', 5, 'Apple Gift Card Rp 80.000', 'Apple Gift Card promo only 81k, stock terbatas', 81000, 3, 'produk_39.jpg', 0),
(40, 'zxc', 5, 'Apple Gift Card Rp 400.000', 'Apple Gift Card 400K, hanya 425k gass dibeli', 425000, 30, 'produk_40.jpg', 0),
(41, 'zxc', 5, 'Apple Gift Card Rp 1.000.000', 'Apple Gift Card Rp 1.000.000, Sultan ONLY GASKAN!!', 1100000, 3, 'produk_41.jpg', 0);

-- --------------------------------------------------------

--
-- Table structure for table `diskusi`
--

DROP TABLE IF EXISTS `diskusi`;
CREATE TABLE `diskusi` (
  `id` int(11) NOT NULL,
  `fk_barang` int(11) NOT NULL,
  `fk_user` varchar(50) NOT NULL,
  `konten` varchar(255) NOT NULL,
  `tanggal` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `diskusi`
--

INSERT INTO `diskusi` (`id`, `fk_barang`, `fk_user`, `konten`, `tanggal`) VALUES
(1, 5, 'qwe', 'barangnya ready?', '2021-12-04 00:00:00'),
(2, 2, 'qweqwe', 'barangnya ready?', '2021-12-04 00:00:00'),
(3, 2, 'qweqwe', 'barangnya ready?', '2021-12-04 23:14:14'),
(4, 8, 'qwe', 'barangnya ready?', '2021-12-06 00:50:43'),
(5, 32, 'qwe', 'halo', '2021-12-06 07:28:35'),
(6, 3, 'hans', 'ready kah ?', '2021-12-06 07:39:34'),
(7, 7, 'qwe', 'halo hans', '2021-12-06 07:40:35'),
(8, 30, 'MrBEAST', 'waow murah banget disiniii ><', '2021-12-06 08:00:57'),
(9, 41, 'superadmin', 'ready ?', '2021-12-09 12:13:29');

-- --------------------------------------------------------

--
-- Table structure for table `dtrans`
--

DROP TABLE IF EXISTS `dtrans`;
CREATE TABLE `dtrans` (
  `id` int(11) NOT NULL,
  `fk_htrans` int(11) NOT NULL,
  `fk_barang` int(11) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `subtotal` int(11) NOT NULL,
  `rating` int(11) DEFAULT NULL,
  `review` varchar(255) DEFAULT NULL,
  `fk_seller` varchar(50) NOT NULL,
  `status` varchar(20) NOT NULL,
  `notes_seller` varchar(255) DEFAULT NULL,
  `notes_customer` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `dtrans`
--

INSERT INTO `dtrans` (`id`, `fk_htrans`, `fk_barang`, `jumlah`, `subtotal`, `rating`, `review`, `fk_seller`, `status`, `notes_seller`, `notes_customer`) VALUES
(19, 35, 6, 1, 360000, NULL, NULL, 'asd', 'sent', NULL, NULL),
(20, 35, 2, 1, 80000, NULL, NULL, 'asd', 'sent', 'asd', NULL),
(21, 36, 2, 1, 80000, NULL, NULL, 'asd', 'rejected', NULL, NULL),
(22, 36, 1, 1, 80000, NULL, NULL, 'asd', 'rejected', NULL, NULL),
(23, 36, 9, 12, 120000, NULL, NULL, 'asdasd', 'rejected', NULL, NULL),
(24, 37, 8, 1, 80000, NULL, NULL, 'asd', 'completed', 'kode', 'halo'),
(25, 38, 2, 1, 80000, NULL, NULL, 'asd', 'pending', NULL, NULL),
(26, 39, 2, 1, 80000, NULL, NULL, 'asd', 'completed', 'ini kode steamnya', NULL),
(27, 40, 2, 1, 80000, NULL, NULL, 'asd', 'completed', NULL, NULL),
(28, 40, 9, 1, 10000, NULL, NULL, 'asdasd', 'completed', NULL, NULL),
(29, 41, 18, 1, 295000, NULL, NULL, 'asdasd', 'pending', NULL, NULL),
(30, 42, 7, 1, 450000, NULL, NULL, 'asd', 'completed', 'trade jam 1', NULL),
(31, 43, 2, 1, 15000, NULL, NULL, 'asd', 'completed', 'qwe-2312-2131-ad', NULL),
(32, 44, 30, 1, 435000, NULL, NULL, 'rty', 'pending', NULL, '1 menit ga dikirim cancel ya!'),
(33, 45, 7, 5, 2250000, NULL, NULL, 'asd', 'completed', 'trade jam 4 sore', '30 detik ga dikirim cancel ty'),
(34, 47, 31, 34, 8160000, NULL, NULL, 'rty', 'pending', NULL, 'MrBEAST NICH HATI2 KM'),
(35, 48, 31, 34, 8160000, NULL, NULL, 'rty', 'pending', NULL, 'MrBEAST NICH HATI2 KM'),
(36, 49, 26, 3, 7650000, NULL, NULL, 'rty', 'pending', NULL, '?'),
(37, 51, 32, 1, 650000, NULL, NULL, 'zxc', 'pending', NULL, NULL),
(38, 51, 2, 1, 15000, NULL, NULL, 'asd', 'pending', NULL, NULL),
(39, 51, 5, 1, 68000, NULL, NULL, 'asd', 'pending', NULL, NULL),
(40, 52, 2, 1, 15000, NULL, NULL, 'asd', 'pending', NULL, 'cepat');

-- --------------------------------------------------------

--
-- Table structure for table `htrans`
--

DROP TABLE IF EXISTS `htrans`;
CREATE TABLE `htrans` (
  `id` int(11) NOT NULL,
  `fk_customer` varchar(50) NOT NULL,
  `grandtotal` int(11) NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `htrans`
--

INSERT INTO `htrans` (`id`, `fk_customer`, `grandtotal`, `tanggal`) VALUES
(35, 'qwe', 440000, '2021-05-03'),
(36, 'qwe', 280000, '2021-06-04'),
(37, 'qwe', 80000, '2021-01-04'),
(38, 'qwe', 80000, '2021-03-04'),
(39, 'qwe', 80000, '2021-07-04'),
(40, 'qwe', 90000, '2021-10-04'),
(41, 'qwe', 295000, '2021-12-06'),
(42, 'qwe', 450000, '2021-12-06'),
(43, 'qwe', 15000, '2021-12-06'),
(44, 'MrBEAST', 435000, '2021-12-06'),
(45, 'MrBEAST', 2250000, '2021-12-06'),
(46, 'qwe', 0, '2021-12-06'),
(47, 'MrBEAST', 8160000, '2021-12-06'),
(48, 'MrBEAST', 8160000, '2021-12-06'),
(49, 'MrBEAST', 7650000, '2021-12-06'),
(50, 'MrBEAST', 0, '2021-12-06'),
(51, 'superadmin', 733000, '2021-12-09'),
(52, 'superadmin', 15000, '2021-12-09');

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

DROP TABLE IF EXISTS `kategori`;
CREATE TABLE `kategori` (
  `id` int(11) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `tipe` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id`, `nama`, `tipe`) VALUES
(1, 'Steam Wallet', 'voucher'),
(2, 'Dota 2', 'item'),
(3, 'Google Play Voucher', 'voucher'),
(4, 'Counter Strike', 'item'),
(5, 'Apple Gift Card', 'voucher');

-- --------------------------------------------------------

--
-- Table structure for table `komentar`
--

DROP TABLE IF EXISTS `komentar`;
CREATE TABLE `komentar` (
  `id` int(11) NOT NULL,
  `fk_diskusi` int(11) NOT NULL,
  `fk_user` varchar(50) NOT NULL,
  `isi` varchar(255) NOT NULL,
  `tanggal` datetime NOT NULL,
  `is_seller` int(1) NOT NULL COMMENT '0 = not seller, 1 = seller'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `komentar`
--

INSERT INTO `komentar` (`id`, `fk_diskusi`, `fk_user`, `isi`, `tanggal`, `is_seller`) VALUES
(1, 1, 'qwe', 'ready', '2021-12-04 23:12:37', 0),
(2, 2, 'qweqwe', 'ready', '2021-12-04 23:13:23', 0),
(3, 4, 'qwe', 'redy', '2021-12-06 00:52:51', 0),
(4, 5, 'qwe', 'halo juga', '2021-12-06 07:28:43', 0),
(5, 6, 'hans', 'halo ?', '2021-12-06 07:39:44', 0),
(6, 6, 'asd', 'halo barang ready.', '2021-12-06 07:40:45', 1),
(7, 7, 'asd', 'barang ready! silahkan dibeli', '2021-12-06 07:41:21', 1),
(8, 7, 'qwe', 'ok sudah saya pesan, tolong diproses ya gan ga pake lama!!!!', '2021-12-06 07:42:51', 0),
(9, 9, 'superadmin', 'heiiii', '2021-12-09 12:14:25', 0);

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` int(11) NOT NULL,
  `fk_htrans` int(11) NOT NULL,
  `fk_dtrans` int(11) NOT NULL,
  `fk_user` varchar(50) NOT NULL,
  `star` int(1) NOT NULL COMMENT 'hanya antara 1, 2, 3, 4, 5',
  `isi` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`id`, `fk_htrans`, `fk_dtrans`, `fk_user`, `star`, `isi`) VALUES
(1, 42, 30, 'qwe', 4, 'pengirimannya cepat, tapi penjualnya galak'),
(2, 43, 31, 'qwe', 4, 'pengiriman cepat, seller ramah, recommended!'),
(3, 45, 33, 'MrBEAST', 1, 'SEBENERNYA BAGUS TOKONYA TAPI SAYA SUKA BINTANG 1, JADI SAYA KASIH BINTANG 1 AJA');

-- --------------------------------------------------------

--
-- Table structure for table `topup`
--

DROP TABLE IF EXISTS `topup`;
CREATE TABLE `topup` (
  `id` int(11) NOT NULL,
  `fk_username` varchar(50) NOT NULL,
  `jumlah_topup` int(11) NOT NULL,
  `bukti_topup` text DEFAULT NULL,
  `status_topup` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `topup`
--

INSERT INTO `topup` (`id`, `fk_username`, `jumlah_topup`, `bukti_topup`, `status_topup`, `created_at`, `updated_at`) VALUES
(1, 'asd', 15000, NULL, 1, '2021-12-04 13:54:21', '2021-12-05 13:21:08'),
(2, 'asd', 50000, NULL, 1, '2021-12-04 14:05:28', '2021-12-05 13:19:53'),
(4, 'qwe', 10000, 'bukti_4.jpg', 1, '2021-12-05 11:12:38', '2021-12-06 07:57:15'),
(5, 'qwe', 20000, 'bukti_5.png', -1, '2021-12-05 11:13:38', '2021-12-05 12:56:03'),
(6, 'asd', 50000, NULL, 1, '2021-12-05 11:15:07', '2021-12-05 13:19:56'),
(7, 'asd', 48750, NULL, 0, '2021-12-05 16:58:36', '2021-12-05 16:58:36'),
(8, 'qwe', 10000, 'bukti_8.png', 1, '2021-12-05 17:38:07', '2021-12-06 07:56:18'),
(9, 'qweqwe', 1000000, 'bukti_9.jpg', 1, '2021-12-06 07:25:16', '2021-12-06 07:37:36'),
(10, 'hans', 50000, 'bukti_10.jpg', -1, '2021-12-06 07:36:41', '2021-12-06 07:37:52'),
(11, 'qwe', 1000000, 'bukti_11.png', 1, '2021-12-06 07:36:53', '2021-12-06 07:37:47'),
(12, 'MrBEAST', 99999999, 'bukti_12.jpg', 1, '2021-12-06 07:44:46', '2021-12-06 07:56:12'),
(13, 'MrBEAST', 10000, 'bukti_13.jpg', 1, '2021-12-06 07:58:35', '2021-12-06 07:59:26');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(50) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `rekening` varchar(20) NOT NULL,
  `saldo` int(11) NOT NULL DEFAULT 0,
  `toko` varchar(50) DEFAULT '',
  `role` varchar(10) NOT NULL,
  `gambar` varchar(60) DEFAULT 'default.jpg',
  `status` int(11) NOT NULL DEFAULT 1,
  `is_verified` int(1) NOT NULL DEFAULT 0 COMMENT 'untuk cek sudah verif atau tidak'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `password`, `email`, `nama`, `rekening`, `saldo`, `toko`, `role`, `gambar`, `status`, `is_verified`) VALUES
('asd', '$2y$10$jfbAeOLStrpbtguDYHYpTe2o5OtVevBUxwiNO4Hqd/dSR8EcD0vV6', 'asd@gmail.com', 'asd', '123', 2265000, 'TOKO 1', 'SELLER', 'default.jpg', 1, 1),
('asdasd', '$2y$10$NzbnyU7ymYzc1LJ/O9Ehi.teJMU89WIzomMymn.dc/JccT5BVSrAC', 'asdasd@gmail.com', 'asdasdasd', '123', 0, 'TOKO 2', 'SELLER', 'default.jpg', 1, 1),
('hans', '$2y$10$U5jYuUHYMmQPoDpZPGIk7u5ppdAb9DkQrGeScl8SuB92I.s0/ZP96', 'hans.f20@mhs.istts.ac.id', 'Hans F', '232312315463', 0, '', 'CUSTOMER', 'default.jpg', 1, 1),
('MrBEAST', '$2y$10$OXlKyw/V8MJZ4XGxAF.MaOmhd19J0Yod0qUjaLlxMaRUGlDjwvjPu', 'hansenvalentino71@yahoo.com', 'MrBEAST', '08188881', 73354999, '', 'CUSTOMER', 'picture_MrBEAST.jpg', 1, 1),
('qwe', '$2y$10$mOHUx0uAbfVkq8QA2RGH4uOTWO3/PWsfP5NS6kYpAwPXPPoXqnuGa', 'qwe@gmail.com', 'qwe', '123', 260000, '', 'CUSTOMER', 'default.jpg', 1, 1),
('qweqwe', '$2y$10$C8Er1DTmCMGqMAgr.8kiFuj/74TXHme6PI1UzXFDTswcVG/ci8yRW', 'qweqwe@gmail.com', 'qwe', '123', 1000000, '', 'CUSTOMER', 'default.jpg', 1, 1),
('rty', '$2y$10$RSfV7Wk6MOr2EO5kOD3KK.74nhW/yROCdGbpwzRYgm.P9gRlw.zj.', 'rty@gmail.com', 'rty rty', '567', 0, 'TOKO 4', 'SELLER', 'default.jpg', 1, 1),
('sennn', '$2y$10$bH1FR4cE5.pzGr1O4Gv9s.stmyaylB1L9bg6FsztnSSStk7iB4pke', 'hansenvalentino21@gmail.com', 'hansen', '08188881', 0, '', 'CUSTOMER', 'default.jpg', 1, 0),
('superadmin', '$2y$10$eD8jp27lrxIqNucROZsVP.5qAtRmSCWpbaJVMBV65m98O3OtIPGVu', 'superadmin@gmail.com', 'superadmin', '123', 99251999, 'TOKO ADMIN', 'superadmin', 'default.jpg', 1, 1),
('zxc', '$2y$10$eqQhoIECyeNV6HEPePzq9elKYwfPzntjfk2C4g4sLtHiW5UFmH2ae', 'zxc@gmail.com', 'zxc zxc', '231', 0, 'TOKO 3', 'SELLER', 'default.jpg', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
CREATE TABLE `wishlist` (
  `id` int(11) NOT NULL,
  `fk_user` varchar(50) NOT NULL,
  `fk_barang` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `wishlist`
--

INSERT INTO `wishlist` (`id`, `fk_user`, `fk_barang`) VALUES
(25, 'qwe', 3),
(34, 'qwe', 6),
(35, 'qwe', 1),
(44, 'qwe', 7),
(57, 'qwe', 2),
(58, 'qwe', 5),
(62, 'superadmin', 34);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `diskusi`
--
ALTER TABLE `diskusi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dtrans`
--
ALTER TABLE `dtrans`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `htrans`
--
ALTER TABLE `htrans`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `komentar`
--
ALTER TABLE `komentar`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `topup`
--
ALTER TABLE `topup`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `wishlist`
--
ALTER TABLE `wishlist`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `barang`
--
ALTER TABLE `barang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `diskusi`
--
ALTER TABLE `diskusi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `dtrans`
--
ALTER TABLE `dtrans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `htrans`
--
ALTER TABLE `htrans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `komentar`
--
ALTER TABLE `komentar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `review`
--
ALTER TABLE `review`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `topup`
--
ALTER TABLE `topup`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `wishlist`
--
ALTER TABLE `wishlist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
