% Reglas estaticas
% 1. sirve para comprobar si unos sintomas coinciden con todos los de la enfermedad 
% caso base
coincide_sintomas([], _).
coincide_sintomas([H|Resto], Enfermedad) :-
    enfermedad(Enfermedad, ListaEnfermedad, _, _),
    member(H, ListaEnfermedad),
    coincide_sintomas(Resto, Enfermedad).

% 2. sirve para comprobar si una enfermedad coincide con al menos un sintoma
diagnostico(SintomasUsuario, Enfermedad) :-
    enfermedad(Enfermedad, ListaSintomas, _, _),
    member(S, SintomasUsuario),
    member(S, ListaSintomas).

% 3. sirve para encontrar enfermedades por una categoria
diagnostico_categoria(Categoria, Enfermedad) :-
    enfermedad(Enfermedad, _, Categoria, _).

% 4. sirve para ver la recomiendacion de una enfermedad
recomendacion(Enfermedad, Recomendaciones) :-
    enfermedad(Enfermedad, _, _, Recomendaciones).

% 5. sirve para ver enfermedades que son cronicas
enfermedades_cronicas(Enfermedad) :-
    enfermedad(Enfermedad, _, cronica, _).

% 6. sirve para ver todas las enfermedades de un sintoma
enfermedades_por_sintoma(Sintoma, Enfermedad) :-
    enfermedad(Enfermedad, ListaSintomas, _, _), 
    member(Sintoma, ListaSintomas).

