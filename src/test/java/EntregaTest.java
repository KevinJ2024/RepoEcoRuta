import com.EcoRuta.Empresa;
import com.EcoRuta.Entrega.Aportante;
import com.EcoRuta.Entrega.Entrega;
import com.EcoRuta.Entrega.EstadoEntrega;
import com.EcoRuta.Lote.Metal;
import com.EcoRuta.Lote.PapelCarton;
import com.EcoRuta.Lote.ResElectrico;
import com.EcoRuta.Lote.TipoMetal;

import org.junit.jupiter.api.Assertions;
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
        aportante);

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
        aportante);

    // Act
    boolean resultado = entrega.actualizarEstadoEntrega(
        EstadoEntrega.ACEPTADA);

    // Assert
    assertFalse(resultado, "No debe aceptarse una entrega sin lotes");

    assertEquals(
        EstadoEntrega.ABIERTA,
        entrega.getEstadoEntrega());

    assertNull(
        entrega.getTheComprobante(),
        "La entrega no debe tener comprobante");

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
        new ArrayList<Entrega>());

    Entrega entrega = new Entrega(
        "E01",
        LocalDate.now(),
        EstadoEntrega.ABIERTA,
        aportante);

    PapelCarton papel = new PapelCarton(
        "L01", "Papel y carton", 10, entrega, true);

    Metal metal = new Metal(
        "L02", "Aluminio", 5, entrega, TipoMetal.ALUMINIO);

    ResElectrico electrico = new ResElectrico(
        "L03", "Residuos electronicos", 4, entrega, 2);

    // Act
    boolean resultado1 = entrega.agregarLote(papel);
    boolean resultado2 = entrega.agregarLote(metal);
    boolean resultado3 = entrega.agregarLote(electrico);

    boolean resultadoAceptacion = entrega.actualizarEstadoEntrega(EstadoEntrega.ACEPTADA);

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

  @Test
  @DisplayName("Prueba #1 ")
  public void testPrueba1() {

    // Registro de una entrega. Con un aportante registrado, cree la entrega E01.
    // Debe quedar en estado ABIERTA, sin lotes y sin comprobante. Intente registrar
    // otra entrega con el código E01 y luego una con un código nuevo y un aportante
    // desconocido. Ambas operaciones deben rechazarse y E01 debe conservarse.

    Empresa empresaMock = new Empresa("900123456-1", "EcoRuta");

    Aportante aportanteMock = new Aportante("1094938205", "Manuel Pinzón", "manuelPinzon@gmail.com",
        new ArrayList<Entrega>());

    String codigoMock = "E01";

    Entrega entregaMock = new Entrega(
        "E01",
        LocalDate.now(),
        EstadoEntrega.ABIERTA,
        aportanteMock);

    Entrega entregaRepetidaMock = new Entrega(
        "E01",
        LocalDate.now(),
        EstadoEntrega.ABIERTA,
        aportanteMock);

    empresaMock.getListAportantes().add(aportanteMock);
    // Con un aportante registrado, cree la entrega E01.
    aportanteMock.agregarEntrega(entregaMock);

    // Debe quedar en estado ABIERTA, sin lotes y sin comprobante.
    Entrega entregaInicialReal = buscarEntregaGlobal(empresaMock, codigoMock);

    assertEquals(entregaInicialReal.getEstadoEntrega(), EstadoEntrega.ABIERTA);
    assertEquals(0, entregaInicialReal.getListLotes().size());
    assertEquals(false, entregaInicialReal.generarComprobante());

    // Intente registrar otra entrega con el código E01

    // al agregar una entrega con codigo repetido el método devuelve false, se
    // rechaza la operación.
    assertEquals(false, aportanteMock.agregarEntrega(entregaRepetidaMock));

    // luego una con un código nuevo y un aportante
    // desconocido.
    String codigoNoRepetidoMock = "CodigoNoRepetido";

    Entrega entregaNoRepetidaMock = new Entrega(
        "CodigoNoRepetido",
        LocalDate.now(),
        EstadoEntrega.ABIERTA,
        aportanteMock);

    // aportanteNoRegistradoMock no se registra con la clase Empresa
    Aportante aportanteNoRegistradoMock = new Aportante("1094938205", "Manuel Pinzón", "manuelPinzon@gmail.com",
        new ArrayList<Entrega>());

    aportanteNoRegistradoMock.agregarEntrega(entregaNoRepetidaMock);

    // No se encuentra la entrega en la entidad empresa porque la entrega no está
    // asociada a un aportante vinculado a la misma. Por lo tanto se rechaza un
    // registro de una entrega asociada a un aportante no vinculado a la empresa.
    assertEquals(null, buscarEntregaGlobal(empresaMock, codigoNoRepetidoMock));
  }

  // Métodos auiliares

  // busca si hay una entrega ya existe en el global de la empresa
  private static Entrega buscarEntregaGlobal(Empresa empresa, String codigoEntrega) {
    Entrega res = null;

    if (!empresa.getListAportantes().isEmpty()) {
      for (Aportante aportante : empresa.getListAportantes()) {
        if (!aportante.listEntregas().isEmpty()) {
          for (Entrega entrega : aportante.listEntregas()) {
            if (entrega.getCodigo().equalsIgnoreCase(codigoEntrega)) {
              res = entrega;
              break;
            }
          }
        }
        if (res != null) {
          break;
        }
      }
    }

    return res;
  }
}
