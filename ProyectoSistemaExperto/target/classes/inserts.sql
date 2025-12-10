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