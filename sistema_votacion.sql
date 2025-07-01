-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 01-07-2025 a las 17:33:33
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `sistema_votacion`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `candidatos`
--

CREATE TABLE `candidatos` (
  `id` int(11) NOT NULL,
  `numero` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `partido` varchar(100) NOT NULL,
  `votos` int(11) DEFAULT 0,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `candidatos`
--

INSERT INTO `candidatos` (`id`, `numero`, `nombre`, `partido`, `votos`, `fecha_registro`) VALUES
(1, 1, 'Javier', 'Popular', 0, '2025-06-30 04:15:16'),
(2, 2, 'Kervyn', 'K', 0, '2025-06-30 04:16:04'),
(3, 3, 'Karla', 'Lapiz', 0, '2025-06-30 15:55:21'),
(4, 4, 'Nelsy', 'Casita', 1, '2025-06-30 16:19:30'),
(5, 5, 'Melany', 'Naranja', 2, '2025-07-01 08:48:51');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `dni` varchar(20) NOT NULL,
  `ya_voto` tinyint(1) DEFAULT 0,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  `rol` varchar(20) DEFAULT 'USUARIO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `apellido`, `email`, `password`, `dni`, `ya_voto`, `fecha_registro`, `rol`) VALUES
(2, 'Admin', 'Admin', 'admin@admin.com', 'admin123', '00000000', 0, '2025-06-30 04:12:16', 'ADMIN'),
(5, 'Usuario1', 'Apellido1', 'usuario1@gmail.com', 'clave1', '10000001', 1, '2025-07-01 06:53:43', 'USUARIO'),
(6, 'Usuario2', 'Apellido2', 'usuario2@gmail.com', 'clave2', '10000002', 1, '2025-07-01 06:53:43', 'USUARIO'),
(7, 'Usuario3', 'Apellido3', 'usuario3@gmail.com', 'clave3', '10000003', 1, '2025-07-01 06:53:43', 'USUARIO'),
(8, 'Usuario4', 'Apellido4', 'usuario4@gmail.com', 'clave4', '10000004', 0, '2025-07-01 06:53:43', 'USUARIO'),
(9, 'Usuario5', 'Apellido5', 'usuario5@gmail.com', 'clave5', '10000005', 0, '2025-07-01 06:53:43', 'USUARIO'),
(10, 'Usuario6', 'Apellido6', 'usuario6@gmail.com', 'clave6', '10000006', 0, '2025-07-01 06:53:43', 'USUARIO'),
(11, 'Usuario7', 'Apellido7', 'usuario7@gmail.com', 'clave7', '10000007', 0, '2025-07-01 06:53:43', 'USUARIO'),
(12, 'Usuario8', 'Apellido8', 'usuario8@gmail.com', 'clave8', '10000008', 0, '2025-07-01 06:53:43', 'USUARIO'),
(13, 'Usuario9', 'Apellido9', 'usuario9@gmail.com', 'clave9', '10000009', 0, '2025-07-01 06:53:43', 'USUARIO'),
(14, 'Usuario10', 'Apellido10', 'usuario10@gmail.com', 'clave10', '10000010', 0, '2025-07-01 06:53:43', 'USUARIO'),
(15, 'Usuario11', 'Apellido11', 'usuario11@gmail.com', 'clave11', '10000011', 0, '2025-07-01 06:53:43', 'USUARIO'),
(16, 'Usuario12', 'Apellido12', 'usuario12@gmail.com', 'clave12', '10000012', 0, '2025-07-01 06:53:43', 'USUARIO'),
(17, 'Usuario13', 'Apellido13', 'usuario13@gmail.com', 'clave13', '10000013', 0, '2025-07-01 06:53:43', 'USUARIO'),
(18, 'Usuario14', 'Apellido14', 'usuario14@gmail.com', 'clave14', '10000014', 0, '2025-07-01 06:53:43', 'USUARIO'),
(19, 'Usuario15', 'Apellido15', 'usuario15@gmail.com', 'clave15', '10000015', 0, '2025-07-01 06:53:43', 'USUARIO'),
(20, 'Usuario16', 'Apellido16', 'usuario16@gmail.com', 'clave16', '10000016', 0, '2025-07-01 13:21:55', 'USUARIO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `votos`
--

CREATE TABLE `votos` (
  `id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `candidato_id` int(11) NOT NULL,
  `fecha_voto` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `votos`
--

INSERT INTO `votos` (`id`, `usuario_id`, `candidato_id`, `fecha_voto`) VALUES
(5, 5, 2, '2025-07-01 07:04:22'),
(6, 6, 2, '2025-07-01 07:04:57'),
(7, 7, 4, '2025-07-01 07:05:31'),
(8, 5, 4, '2025-07-01 07:14:18'),
(9, 6, 4, '2025-07-01 07:14:39'),
(10, 7, 4, '2025-07-01 07:15:04'),
(11, 8, 4, '2025-07-01 07:15:27'),
(12, 9, 3, '2025-07-01 07:16:19'),
(13, 5, 4, '2025-07-01 07:20:36'),
(14, 6, 4, '2025-07-01 07:20:55'),
(15, 7, 3, '2025-07-01 07:21:18'),
(16, 5, 4, '2025-07-01 07:26:21'),
(17, 6, 4, '2025-07-01 07:26:41'),
(18, 7, 4, '2025-07-01 07:27:02'),
(19, 8, 3, '2025-07-01 07:27:23'),
(20, 5, 4, '2025-07-01 08:20:47'),
(21, 6, 4, '2025-07-01 08:21:09'),
(22, 7, 4, '2025-07-01 08:21:37'),
(23, 8, 3, '2025-07-01 08:22:24'),
(24, 5, 4, '2025-07-01 08:25:28'),
(25, 6, 4, '2025-07-01 08:25:52'),
(26, 7, 3, '2025-07-01 08:26:14'),
(27, 8, 3, '2025-07-01 08:26:41'),
(28, 5, 4, '2025-07-01 08:44:31'),
(29, 6, 4, '2025-07-01 08:44:55'),
(30, 7, 3, '2025-07-01 08:45:22'),
(31, 5, 5, '2025-07-01 08:51:35'),
(32, 6, 5, '2025-07-01 08:52:00'),
(33, 7, 4, '2025-07-01 08:52:19'),
(34, 8, 4, '2025-07-01 08:52:40'),
(35, 5, 5, '2025-07-01 11:09:13'),
(36, 5, 5, '2025-07-01 13:33:27'),
(37, 6, 5, '2025-07-01 13:33:54'),
(38, 7, 5, '2025-07-01 13:34:16'),
(39, 8, 4, '2025-07-01 13:35:06'),
(40, 5, 5, '2025-07-01 13:47:07'),
(41, 6, 5, '2025-07-01 13:47:42'),
(42, 7, 4, '2025-07-01 13:48:31');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `candidatos`
--
ALTER TABLE `candidatos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero` (`numero`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `dni` (`dni`);

--
-- Indices de la tabla `votos`
--
ALTER TABLE `votos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `usuario_id` (`usuario_id`),
  ADD KEY `candidato_id` (`candidato_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `candidatos`
--
ALTER TABLE `candidatos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `votos`
--
ALTER TABLE `votos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `votos`
--
ALTER TABLE `votos`
  ADD CONSTRAINT `votos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `votos_ibfk_2` FOREIGN KEY (`candidato_id`) REFERENCES `candidatos` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
