package ProyectoSistemaExperto.inferencia;

import ProyectoSistemaExperto.models.Enfermedad;
import org.jpl7.Query;
import org.jpl7.Term;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MotorInferencia {

    private String rutaReglas;
    private boolean cargadoConsulta = false;

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public MotorInferencia() {
        cargarReglas();
    }

    // ============================================
    // Cargar reglas.pl desde resources
    // ============================================
    public void cargarReglas() {
        try {
            URL rulesURL = getClass().getClassLoader().getResource("reglas.pl");

            if (rulesURL == null) {
                System.err.println("ERROR: reglas.pl no encontrado en resources");
                return;
            }

            rutaReglas = new File(rulesURL.toURI()).getAbsolutePath().replace("\\", "/");

            Query q = new Query("consult('" + rutaReglas + "')");

            cargadoConsulta = q.hasSolution();

            if (cargadoConsulta) {
                System.out.println("✓ MotorInferencia: reglas.pl cargado correctamente");
            } else {
                System.err.println("✗ ERROR MotorInferencia: No se pudo cargar reglas.pl");
            }

            q.close();

        } catch (Exception e) {
            System.err.println("ERROR cargarReglas(): " + e.getMessage());
        }
    }

    // ============================================
    // Carga dinámica de hechos desde MySQL
    // ============================================
    public void agregarEnfermedad(Enfermedad e) {
        try {
            // 1. agregar enfermedad(nombre, categoria).
            String consulta1 = String.format(
                    "assert(enfermedad('%s','%s')).",
                    e.getNombre(), e.getCategoria()
            );
            new Query(consulta1).hasSolution();

            // 2. agregar sintoma(nombre, enfermedad).
            for (String s : e.getSintomas()) {
                String consulta2 = String.format(
                        "assert(sintoma('%s','%s')).",
                        s, e.getNombre()
                );
                new Query(consulta2).hasSolution();
            }

            // 3. agregar recomendacion(nombre, ListaRecomendaciones).
            String listR = convertirListaProlog(e.getRecomendaciones());
            String consulta3 = String.format(
                    "assert(recomendacion('%s', %s)).",
                    e.getNombre(), listR
            );
            new Query(consulta3).hasSolution();

        } catch (Exception ex) {
            System.err.println("ERROR agregarEnfermedad(): " + ex.getMessage());
        }
    }

    // Convierte List<String> a forma Prolog: ['a','b','c']
    private String convertirListaProlog(List<String> lista) {
        List<String> quoted = new ArrayList<>();
        for (String s : lista) quoted.add("'" + s.trim() + "'");
        return "[" + String.join(",", quoted) + "]";
    }

    // ============================================
    // CONSULTAS PRINCIPALES DEL MOTOR
    // ============================================

    /**
     * Retorna enfermedades que coinciden con TODOS los síntomas del usuario.
     * coincide_sintomas(Sintomas, Enfermedad)
     */
    public List<String> diagnosticar(List<String> sintomas) {
        List<String> resultado = new ArrayList<>();

        String listaProlog = convertirListaProlog(sintomas);

        Query q = new Query("coincide_sintomas(" + listaProlog + ", Enfermedad)");

        while (q.hasMoreSolutions()) {
            Map<String, Term> sol = q.nextSolution();
            resultado.add(sol.get("Enfermedad").name());
        }

        q.close();
        return resultado;
    }

    /**
     * Retorna enfermedades que pertenecen a una categoría.
     */
    public List<String> diagnosticoPorCategoria(String categoria) {
        List<String> resultado = new ArrayList<>();

        Query q = new Query("diagnostico_categoria('" + categoria + "', Enfermedad)");

        while (q.hasMoreSolutions()) {
            resultado.add(q.nextSolution().get("Enfermedad").name());
        }

        q.close();
        return resultado;
    }

    /**
     * Retorna todas las enfermedades crónicas.
     */
    public List<String> enfermedadesCronicas() {
        List<String> lista = new ArrayList<>();

        Query q = new Query("enfermedades_cronicas(Enfermedad)");

        while (q.hasMoreSolutions()) {
            lista.add(q.nextSolution().get("Enfermedad").name());
        }

        q.close();
        return lista;
    }

    /**
     * Retorna todas las enfermedades asociadas a un síntoma.
     */
    public List<String> enfermedadesPorSintoma(String sintoma) {
        List<String> lista = new ArrayList<>();

        Query q = new Query("enfermedades_por_sintoma('" + sintoma + "', Enfermedad)");

        while (q.hasMoreSolutions()) {
            lista.add(q.nextSolution().get("Enfermedad").name());
        }

        q.close();
        return lista;
    }

    /**
     * Retorna las recomendaciones de una enfermedad
     */
    public List<String> recomendacion(String enfermedad) {
        List<String> lista = new ArrayList<>();

        Query q = new Query("recomendacion('" + enfermedad + "', R)");

        if (q.hasSolution()) {
            Term term = q.oneSolution().get("R");

            for (Term t : term.toTermArray()) {
                lista.add(t.name());
            }
        }

        q.close();
        return lista;
    }

    // ============================================
    // BORRAR BASE DE HECHOS DINÁMICOS
    // ============================================
    public void limpiarBD() {
        new Query("retractall(enfermedad(_, _)).").hasSolution();
        new Query("retractall(sintoma(_, _)).").hasSolution();
        new Query("retractall(recomendacion(_, _)).").hasSolution();

        System.out.println("✓ Base de hechos dinámica limpiada");
    }
}


