CREATE DATABASE IF NOT EXISTS sistema_experto;
USE sistema_experto;

-- Tabla enfermedades
CREATE TABLE enfermedad (
    id_enfermedad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    recomendacion TEXT NOT NULL
);

-- Tabla síntomas
CREATE TABLE sintoma (
    id_sintoma INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Relacion enfermedad - sintoma N:M
CREATE TABLE enfermedad_sintoma (
    id_enfermedad INT,
    id_sintoma INT,
    PRIMARY KEY (id_enfermedad, id_sintoma),
    FOREIGN KEY (id_enfermedad) REFERENCES enfermedad(id_enfermedad),
    FOREIGN KEY (id_sintoma) REFERENCES sintoma(id_sintoma)
);

-- Pacientes
CREATE TABLE paciente (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    edad INT
);

-- Diagnosticos
CREATE TABLE diagnostico (
    id_diag INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT,
    id_enfermedad INT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente),
    FOREIGN KEY (id_enfermedad) REFERENCES enfermedad(id_enfermedad)
);
