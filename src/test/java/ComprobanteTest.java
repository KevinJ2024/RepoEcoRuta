import com.EcoRuta.Entrega.Aportante;
import com.EcoRuta.Entrega.Comprobante;
import com.EcoRuta.Entrega.Entrega;
import com.EcoRuta.Entrega.EstadoEntrega;
import com.EcoRuta.Lote.Metal;
import com.EcoRuta.Lote.PapelCarton;
import com.EcoRuta.Lote.ResElectrico;
import com.EcoRuta.Lote.TipoMetal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ComprobanteTest {

    @Test
    @DisplayName("Consulta de etiquetas de la entrega aceptada")
    public void testConsultaEtiquetasEntregaAceptada() {

        // Arrange (Mismo escenario de la entrega aceptada)
        Aportante aportante = new Aportante(
                "123",
                "Kevin",
                "kevin67@gmail.com",
                new ArrayList<Entrega>()
        );

        Entrega entrega = new Entrega(
                "E01",
                LocalDate.now(),
                EstadoEntrega.ABIERTA,
                aportante
        );

        PapelCarton papel = new PapelCarton(
                "L01", "Papel y carton", 10, entrega, true
        );

        Metal metal = new Metal(
                "L02", "Aluminio", 5, entrega, TipoMetal.ALUMINIO
        );

        ResElectrico electrico = new ResElectrico(
                "L03", "Residuos electronicos", 4, entrega, 2
        );

        entrega.agregarLote(papel);
        entrega.agregarLote(metal);
        entrega.agregarLote(electrico);
        entrega.actualizarEstadoEntrega(EstadoEntrega.ACEPTADA);

        // Act
        Comprobante comprobante = entrega.getTheComprobante();
        assertNotNull(comprobante, "El comprobante debe existir tras aceptar la entrega");

        String etiquetas = comprobante.obtenerEtiquetas();

        // Assert
        assertNotNull(etiquetas);

        // 1. Verificar que la etiqueta de metal incluya su código (L02) y el tipo (ALUMINIO)
        assertTrue(etiquetas.contains("L02"), "Debe contener el código del lote de metal");
        assertTrue(etiquetas.contains("ALUMINIO"), "Debe contener el tipo de metal ALUMINIO");

        // 2. Verificar que la etiqueta electrónica incluya su código (L03) y cantidad de equipos (2)
        assertTrue(etiquetas.contains("L03"), "Debe contener el código del lote electrónico");
        assertTrue(etiquetas.contains("2"), "Debe contener la cantidad de equipos completos");

        // 3. Verificar que NO aparezca el lote de papel y cartón (L01)
        assertFalse(etiquetas.contains("L01"), "No debe generar etiqueta para PapelCarton");

        // 4. Verificar que aparezcan exactamente dos etiquetas
        String[] listaEtiquetas = etiquetas.split(", ");
        assertEquals(2, listaEtiquetas.length, "Deben aparecer exactamente dos etiquetas");
    }

}
