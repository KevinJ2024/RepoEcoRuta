import com.EcoRuta.Entrega.Aportante;
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

public class EntregaTest {

    @Test
    @DisplayName("Prueba de registro de lote en entrega")
    public void testValidacionLotes() {

        // Arrange
        Aportante aportante = new Aportante("123", "Kevin", "kevin67@gmail.com", new ArrayList<Entrega>());
        Entrega entrega = new Entrega(
                "E01",
                LocalDate.now(),
                EstadoEntrega.ABIERTA,
                aportante
        );

        Metal lote1 = new Metal("L01", "descripcion 1", 5, entrega, TipoMetal.ACERO);
        Metal lote2 = new Metal("L01", "descripcion 2", 3, entrega, TipoMetal.ACERO);
        Metal lote3 = new Metal("L02", "descripcion 3", 0, entrega, TipoMetal.ALUMINIO);

        // Act
        boolean resultado1 = entrega.agregarLote(lote1);
        boolean resultado2 = entrega.agregarLote(lote2);
        boolean resultado3 = entrega.agregarLote(lote3);

        // Assert
        assertTrue(resultado1, "El primer lote debería agregarse");
        assertFalse(resultado2, "No debe permitir códigos duplicados");
        assertFalse(resultado3, "No debe permitir lotes con peso cero");

        assertEquals(1, entrega.getListLotes().size());
        assertEquals("L01", entrega.getListLotes().get(0).getCodigo());
    }

    @Test
    @DisplayName("Prueba para evaluar la negacion de intentar aceptar una entrega sin lotes")
    public void testRechazarEntregaVacia() {
        Aportante aportante = new Aportante("123", "Kevin", "kevin67@gmail.com", new ArrayList<Entrega>());

        // Arrange
        Entrega entrega = new Entrega(
                "E01",
                LocalDate.now(),
                EstadoEntrega.ABIERTA,
                aportante
        );

        // Act
        boolean resultado = entrega.actualizarEstadoEntrega(
                EstadoEntrega.ACEPTADA
        );

        // Assert
        assertFalse(resultado, "No debe aceptarse una entrega sin lotes");

        assertEquals(
                EstadoEntrega.ABIERTA,
                entrega.getEstadoEntrega()
        );

        assertNull(
                entrega.getTheComprobante(),
                "La entrega no debe tener comprobante"
        );

        assertTrue(entrega.getListLotes().isEmpty());
    }

    @Test
    @DisplayName("Prueba de los calculos de los distintos hijos de Lote")
    public void testAceptacionCalculoTresTiposLote() {

        // Arrange
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

        // Act
        boolean resultado1 = entrega.agregarLote(papel);
        boolean resultado2 = entrega.agregarLote(metal);
        boolean resultado3 = entrega.agregarLote(electrico);

        boolean resultadoAceptacion =
                entrega.actualizarEstadoEntrega(EstadoEntrega.ACEPTADA);

        // Assert
        assertTrue(resultado1);
        assertTrue(resultado2);
        assertTrue(resultado3);
        assertTrue(resultadoAceptacion);

        assertEquals(EstadoEntrega.ACEPTADA, entrega.getEstadoEntrega());

        assertEquals(3, entrega.getListLotes().size());

        assertNotNull(entrega.getTheComprobante());

        // Validar cálculo individual de cada lote
        assertEquals(6000.0, papel.calcularValorLote(), 0.01);
        assertEquals(11000.0, metal.calcularValorLote(), 0.01);
        assertEquals(7200.0, electrico.calcularValorLote(), 0.01);

        // Validar totales del comprobante
        assertEquals(19, entrega.getTheComprobante().pesoTotal());
        assertEquals(24200.0, entrega.getTheComprobante().valorTotal());
    }
}


