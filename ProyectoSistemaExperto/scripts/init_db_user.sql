-- Script: init_db_user.sql
-- Crea usuario de aplicación y otorga permisos sobre la BD sistema_experto

CREATE DATABASE IF NOT EXISTS sistema_experto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'AppPassword123!';
GRANT SELECT, INSERT, UPDATE, DELETE ON sistema_experto.* TO 'appuser'@'localhost';

FLUSH PRIVILEGES;

-- Opcional: muestra conteo de tablas (ejecutar después):
-- USE sistema_experto;
-- SHOW TABLES;