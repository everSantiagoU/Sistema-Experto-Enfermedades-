package ProyectoSistemaExperto.inferencia;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/* PROJECT: Sistema Experto
 * MODULE: Inference Engine
 * 
 * CLASS: MotorInferencia
 * DESCRIPTION: Core inference engine class that implements the expert system's reasoning
 *              capabilities. Integrates SWI-Prolog via JPL to perform logical inference
 *              over medical knowledge rules and dynamic facts loaded from MySQL database.
 *              Provides diagnostic capabilities symptom matching and disease categorization
 *              through Prolog based rule evaluation
 * 
 * 
 * - Dynamic loading of Prolog rules from resources
 * - Integration with MySQL data via dynamic fact assertion
 * - Multiple diagnostic query methods
 * - Symptom-based disease matching
 * - Category-based disease filtering
 * - Chronic disease identification
 * - Recommendation retrieval
 * 
 * ARCHITECTURE
 * - Uses JPL Java Prolog Interface for Prolog integration
 * - Maintains dynamic knowledge base separate from static rules
 * - Supports real-time fact addition/removal
 * - Thread-safe query execution
 * 
 * @author Ever Santiago Uribe
 * @version 1.5.0
 * @since 2025-12-10
 * @lastModified 2025-12-11
 * @dependencies JPL 7.2.0, SWI-Prolog 8.4.0, MySQL Connector/J 8.0
*/

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

    // constructor
    public MotorInferencia() {
        cargarReglas();
    }

    // cargamos las reglas desde reglas.pl que esta en resourcs
    public void cargarReglas() {
        try {
            URL rulesURL = getClass().getClassLoader().getResource("reglas.pl");

            if (rulesURL == null) {
                System.err.println("Error reglas.pl no encontrado en resources");
                return;
            }

            rutaReglas = new File(rulesURL.toURI()).getAbsolutePath().replace("\\", "/");

            Query q = new Query("consult('" + rutaReglas + "')");

            cargadoConsulta = q.hasSolution();

            if (cargadoConsulta) {
                System.out.println("MotorInferencia: reglas.pl cargado correctamente");
            } else {
                System.err.println("MotorInferencia: No se pudo cargar reglas.pl");
            }

            q.close();

        } catch (Exception e) {
            System.err.println("ERROR cargarReglas(): " + e.getMessage());
        }
    }

    // cargar los hechos de forma dinamica desde mysql
    public void agregarEnfermedad(Enfermedad e) {
        try {
            // agg enfermedad(nombre, categoria)
            String consulta1 = String.format(
                    "assert(enfermedad('%s','%s')).",
                    e.getNombre(), e.getCategoria()
            );
            new Query(consulta1).hasSolution();

            // agg sintoma(nombre, enfermedad)
            for (String s : e.getSintomas()) {
                String consulta2 = String.format(
                        "assert(sintoma('%s','%s')).",
                        s, e.getNombre()
                );
                new Query(consulta2).hasSolution();
            }

            // add recomendacion(nombre, ListaRecomendaciones)
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

    // convierte lista de strings a lista de prolog
    private String convertirListaProlog(List<String> lista) {
        List<String> quoted = new ArrayList<>();
        for (String s : lista) quoted.add("'" + s.trim() + "'");
        return "[" + String.join(",", quoted) + "]";
    }

     
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

    
    public List<String> diagnosticoPorCategoria(String categoria) {
        List<String> resultado = new ArrayList<>();

        Query q = new Query("diagnostico_categoria('" + categoria + "', Enfermedad)");

        while (q.hasMoreSolutions()) {
            resultado.add(q.nextSolution().get("Enfermedad").name());
        }

        q.close();
        return resultado;
    }


    public List<String> enfermedadesCronicas() {
        List<String> lista = new ArrayList<>();

        Query q = new Query("enfermedades_cronicas(Enfermedad)");

        while (q.hasMoreSolutions()) {
            lista.add(q.nextSolution().get("Enfermedad").name());
        }

        q.close();
        return lista;
    }


    public List<String> enfermedadesPorSintoma(String sintoma) {
        List<String> lista = new ArrayList<>();

        Query q = new Query("enfermedades_por_sintoma('" + sintoma + "', Enfermedad)");

        while (q.hasMoreSolutions()) {
            lista.add(q.nextSolution().get("Enfermedad").name());
        }

        q.close();
        return lista;
    }

    
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

    // eliminar los hechos de la memoria persistente de prolog 
    public void limpiarBD() {
        new Query("retractall(enfermedad(_, _)).").hasSolution();
        new Query("retractall(sintoma(_, _)).").hasSolution();
        new Query("retractall(recomendacion(_, _)).").hasSolution();

        System.out.println("✓ Base de hechos dinámica limpiada");
    }
}


