%ProyectoSistemaExperto\src\main\resources reglas.pl
% Reglas que esperan: enfermedad(nombre, categoria) + sintoma(sintoma, enfermedad)

% 1. coincide_sintomas: verifica que Todos los síntomas coincidan
coincide_sintomas([], _).
coincide_sintomas([H|Resto], Enfermedad) :-
    sintoma(H, Enfermedad),
    coincide_sintomas(Resto, Enfermedad).

% 2. diagnostico_categoria busca por categoría
diagnostico_categoria(Categoria, Enfermedad) :-
    enfermedad(Enfermedad, Categoria).

% 3. enfermedades_cronicas  busca enfermedades crónicas
enfermedades_cronicas(Enfermedad) :-
    enfermedad(Enfermedad, cronica).

% 4. enfermedades_por_sintoma busca enfermedades por un síntoma
enfermedades_por_sintoma(Sintoma, Enfermedad) :-
    sintoma(Sintoma, Enfermedad).

