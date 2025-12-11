
package ProyectoSistemaExperto.views;
import com.formdev.flatlaf.FlatLightLaf;
/**
 *
 * @author EVER URIBE
 */
public class ProyectoSistemaExperto {
    public static void main(String[] args) {
        FlatLightLaf.setup(); // Activar apariencia moderna FlatLaf

        java.awt.EventQueue.invokeLater(() -> {
            new VentanaBienvenida().setVisible(true);
        });
    }
}
