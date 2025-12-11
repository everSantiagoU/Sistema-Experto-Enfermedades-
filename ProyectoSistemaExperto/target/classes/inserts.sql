-- ProyectoSistemaExperto\src\main\resources
USE sistema_experto;


INSERT INTO enfermedad (nombre, categoria, recomendacion) VALUES
('Gripe', 'viral', 'Descansar, hidratar, consultar médico'),
('Resfriado', 'viral', 'Descansar, hidratar'),
('Diabetes', 'crónica', 'Controlar dieta, consultar especialista'),
('COVID-19', 'viral', 'Aislamiento, consultar médico'),
('Varicela', 'viral', 'Descansar, evitar rascar lesiones'),
('Migraña', 'crónica', 'Descansar, evitar luz intensa'),
('Alergia', 'alergia', 'Evitar alérgenos, tomar antihistamínicos'),
('Hipotiroidismo', 'crónica', 'Control médico y medicación'),
('Gastroenteritis', 'viral', 'Hidratación, dieta ligera'),
('Faringitis', 'viral/bacteriana', 'Consultar médico');

INSERT INTO sintoma (nombre) VALUES
('fiebre'),
('tos'),
('dolor_cabeza'),
('dolor_muscular'),
('estornudos'),
('dolor_garganta'),
('sed'),
('cansancio'),
('perdida_peso'),
('perdida_gusto_olfato'),
('erupcion'),
('picazon'),
('nausea'),
('sensibilidad_luz'),
('ojos_lagrimosos'),
('aumento_peso'),
('piel_seca'),
('vomito'),
('diarrea'),
('dolor_abdominal');

-- Gripe
INSERT INTO enfermedad_sintoma VALUES
(1, 1), -- fiebre
(1, 2), -- tos
(1, 3), -- dolor_cabeza
(1, 4); -- dolor_muscular

-- Resfriado 
INSERT INTO enfermedad_sintoma VALUES
(2, 2), -- tos
(2, 5), -- estornudos
(2, 6); -- dolor_garganta

-- Diabetes
INSERT INTO enfermedad_sintoma VALUES
(3, 7), -- sed
(3, 8), -- cansancio
(3, 9); -- perdida_peso

-- COVID-19 
INSERT INTO enfermedad_sintoma VALUES
(4, 1), -- fiebre
(4, 2), -- tos
(4, 8), -- cansancio
(4, 10); -- perdida_gusto_olfato

-- Varicela 
INSERT INTO enfermedad_sintoma VALUES
(5, 1),  -- fiebre
(5, 11), -- erupcion
(5, 12); -- picazon

-- Migraña 
INSERT INTO enfermedad_sintoma VALUES
(6, 3),  -- dolor_cabeza
(6, 13), -- nausea
(6, 14); -- sensibilidad_luz

-- Alergia 
INSERT INTO enfermedad_sintoma VALUES
(7, 5),  -- estornudos
(7, 12), -- picazon
(7, 15); -- ojos_lagrimosos

-- Hipotiroidismo 
INSERT INTO enfermedad_sintoma VALUES
(8, 8),  -- cansancio
(8, 16), -- aumento_peso
(8, 17); -- piel_seca

-- Gastroenteritis 
INSERT INTO enfermedad_sintoma VALUES
(9, 18), -- vomito
(9, 19), -- diarrea
(9, 20), -- dolor_abdominal
(9, 1);  -- fiebre

-- Faringitis 
INSERT INTO enfermedad_sintoma VALUES
(10, 6), -- dolor_garganta
(10, 1), -- fiebre
(10, 2); -- tos
