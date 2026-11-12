package com.EcoRuta;

import com.EcoRuta.Entrega.Aportante;
import com.EcoRuta.Entrega.Comprobante;
import com.EcoRuta.Entrega.Entrega;
import com.EcoRuta.Entrega.EstadoEntrega;
import com.EcoRuta.Lote.Lote;
import com.EcoRuta.Lote.Metal;
import com.EcoRuta.Lote.PapelCarton;
import com.EcoRuta.Lote.ResElectrico;
import com.EcoRuta.Lote.TipoMetal;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
  private static Empresa empresa;

  public static void main(String[] args) {
    empresa = new Empresa("900123456-1", "EcoRuta");
    precargarAportantes();
    mostrarMenuPrincipal();
  }

  // ==================== PRECARGA DE APORTANTES ====================
  private static void precargarAportantes() {
    empresa.getListAportantes().add(new Aportante("1001", "Maria Gomez", "maria@correo.com", new ArrayList<Entrega>()));
    empresa.getListAportantes().add(
        new Aportante("1002", "Papeleria Central", "contacto@papeleria.com", new ArrayList<Entrega>()));
    empresa.getListAportantes()
        .add(new Aportante("1003", "Colegio Verde", "info@colegioverde.edu", new ArrayList<Entrega>()));
  }

  // ==================== MENÚ PRINCIPAL ====================
  private static void mostrarMenuPrincipal() {
    boolean continuar = true;
    while (continuar) {
      String opciones = "=== SISTEMA ECORUTA ===\n\n"
          + "1. Registrar entrega\n"
          + "2. Consultar entrega por código\n"
          + "3. Consultar comprobante por código\n"
          + "4. Consultar acumulado de aportante por identificación\n"
          + "5. Registrar lote\n"
          + "6. Actualizar estado de entrega por código\n"
          + "7. Salir\n\n"
          + "Seleccione una opción:";

      String opcion = JOptionPane.showInputDialog(null, opciones);

      if (opcion == null) {
        continuar = false;
        break;
      }

      switch (opcion) {
        case "1":
          registrarEntrega();
          break;
        case "2":
          consultarEntregaPorCodigo();
          break;
        case "3":
          consultarComprobantePorCodigo();
          break;
        case "4":
          consultarAcumuladoAportantePorId();
          break;
        case "5":
          registrarLote();
          break;
        case "6":
          actualizarEstadoEntregaPorCodigo();
          break;
        case "7":
          JOptionPane.showMessageDialog(null, "¡Gracias por usar el sistema!");
          continuar = false;
          break;
        default:
          JOptionPane.showMessageDialog(null, "Opción inválida. Intente nuevamente.");
      }
    }
  }

  // ==================== OPCION 1: REGISTRAR ENTREGA ====================
  private static void registrarEntrega() {
    String idAportante = leerTexto("Ingrese la identificación del aportante:");
    if (idAportante == null)
      return;

    Aportante aportante = empresa.consultarAportante(idAportante);
    if (aportante == null) {
      JOptionPane.showMessageDialog(null, "No existe un aportante con la identificación: " + idAportante);
      return;
    }

    String codigo = leerTexto("Ingrese el código único de la entrega:");
    if (codigo == null)
      return;

    if (buscarEntregaGlobal(codigo) != null) {
      JOptionPane.showMessageDialog(null, "Ya existe una entrega con el código: " + codigo);
      return;
    }

    LocalDate fecha = leerFecha();
    if (fecha == null)
      return;

    Entrega nuevaEntrega = new Entrega(codigo, fecha, EstadoEntrega.ABIERTA, aportante);

    if (aportante.agregarEntrega(nuevaEntrega)) {
      JOptionPane.showMessageDialog(null, "Entrega registrada exitosamente\n\n"
          + "Código: " + codigo + "\n"
          + "Fecha: " + fecha + "\n"
          + "Aportante: " + aportante.nombre() + " (" + aportante.id() + ")\n"
          + "Estado: " + nuevaEntrega.getEstadoEntrega());
    } else {
      JOptionPane.showMessageDialog(null, "No fue posible registrar la entrega.");
    }
  }

  // ==================== OPCION 2: CONSULTAR ENTREGA POR CODIGO
  // ====================
  private static void consultarEntregaPorCodigo() {
    String codigo = leerTexto("Ingrese el código de la entrega a consultar:");
    if (codigo == null)
      return;

    Entrega entrega = buscarEntregaGlobal(codigo);
    if (entrega == null) {
      JOptionPane.showMessageDialog(null, "No existe una entrega con el código: " + codigo);
      return;
    }

    int pesoTotal = 0;
    double valorTotal = 0.0;
    String detalleLotes = "";

    ArrayList<Lote> lotes = entrega.getListLotes();
    if (!lotes.isEmpty()) {
      for (Lote lote : lotes) {
        pesoTotal += lote.getPeso();
        valorTotal += lote.calcularValorLote();
        detalleLotes += "  - " + lote.getCodigo() + " | " + lote.getDescripcion()
            + " | " + lote.getPeso() + " kg | $" + lote.calcularValorLote() + "\n";
      }
    } else {
      detalleLotes = "  (Sin lotes registrados)\n";
    }

    String infoComprobante;
    if (entrega.getTheComprobante() != null) {
      infoComprobante = "Comprobante: generado";
    } else {
      infoComprobante = "Comprobante: sin generar (entrega no aceptada)";
    }

    String mensaje = "=== DATOS DE LA ENTREGA ===\n\n"
        + "Código: " + entrega.getCodigo() + "\n"
        + "Fecha: " + entrega.getFecha() + "\n"
        + "Estado: " + entrega.getEstadoEntrega() + "\n"
        + "Aportante: " + entrega.getTheAportante().nombre() + " (" + entrega.getTheAportante().id() + ")\n"
        + infoComprobante + "\n\n"
        + "--- Lotes (" + lotes.size() + ") ---\n"
        + detalleLotes + "\n"
        + "Peso total: " + pesoTotal + " kg\n"
        + "Valor total: $" + valorTotal;

    JOptionPane.showMessageDialog(null, mensaje);
  }

  // ==================== OPCION 3: CONSULTAR COMPROBANTE POR CODIGO
  // ====================
  private static void consultarComprobantePorCodigo() {
    String codigo = leerTexto("Ingrese el código de la entrega del comprobante:");
    if (codigo == null)
      return;

    Entrega entrega = buscarEntregaGlobal(codigo);
    if (entrega == null) {
      JOptionPane.showMessageDialog(null, "No existe una entrega con el código: " + codigo);
      return;
    }

    Comprobante comprobante = entrega.getTheComprobante();
    if (comprobante == null) {
      JOptionPane.showMessageDialog(null, "La entrega " + codigo + " no tiene comprobante.\n"
          + "Solo las entregas ACEPTADAS generan comprobante.");
      return;
    }

    String etiquetas = comprobante.obtenerEtiquetas();
    if (etiquetas.isEmpty()) {
      etiquetas = "  (Sin etiquetas para esta entrega)";
    }

    String mensaje = "=== COMPROBANTE ===\n\n"
        + "Código de entrega: " + comprobante.theEntrega().getCodigo() + "\n"
        + "Identificación del aportante: " + comprobante.idAportante() + "\n"
        + "Peso total: " + comprobante.pesoTotal() + " kg\n"
        + "Valor total reconocido: $" + comprobante.valorTotal() + "\n\n"
        + "Etiquetas de control:\n" + etiquetas;

    JOptionPane.showMessageDialog(null, mensaje);
  }

  // ==================== OPCION 4: CONSULTAR ACUMULADO POR ID
  // ====================
  private static void consultarAcumuladoAportantePorId() {
    String id = leerTexto("Ingrese la identificación del aportante:");
    if (id == null)
      return;

    if (empresa.consultarAportante(id) == null) {
      JOptionPane.showMessageDialog(null, "No existe un aportante con la identificación: " + id);
      return;
    }

    double acumulado = empresa.consultarAcumuladoAportante(id);

    JOptionPane.showMessageDialog(null, "=== VALOR ACUMULADO ===\n\n"
        + "Aportante: " + id + "\n"
        + "Valor acumulado: $" + acumulado);
  }

  // ==================== OPCION 5: REGISTRAR LOTE ====================
  private static void registrarLote() {
    String codigoEntrega = leerTexto("Ingrese el código de la entrega a la que desea agregar el lote:");
    if (codigoEntrega == null)
      return;

    Entrega entrega = buscarEntregaGlobal(codigoEntrega);
    if (entrega == null) {
      JOptionPane.showMessageDialog(null, "No existe una entrega con el código: " + codigoEntrega);
      return;
    }

    if (entrega.getEstadoEntrega() == EstadoEntrega.ACEPTADA) {
      JOptionPane.showMessageDialog(null, "La entrega ya fue ACEPTADA y está cerrada a nuevos lotes.");
      return;
    }

    String tipos = "Tipos de lote:\n\n"
        + "1. Papel y cartón\n"
        + "2. Metal\n"
        + "3. Residuos electrónicos\n\n"
        + "Seleccione el tipo de lote:";

    String opcionTipo = JOptionPane.showInputDialog(null, tipos);
    if (opcionTipo == null)
      return;

    Lote nuevoLote = null;

    switch (opcionTipo) {
      case "1":
        nuevoLote = crearPapelCarton(entrega);
        break;
      case "2":
        nuevoLote = crearMetal(entrega);
        break;
      case "3":
        nuevoLote = crearResElectrico(entrega);
        break;
      default:
        JOptionPane.showMessageDialog(null, "Opción inválida.");
    }

    if (nuevoLote == null)
      return;

    if (entrega.agregarLote(nuevoLote)) {
      JOptionPane.showMessageDialog(null, "Lote agregado exitosamente a la entrega " + codigoEntrega);
    } else {
      JOptionPane.showMessageDialog(null, "No fue posible agregar el lote.\n"
          + "Verifique que el código del lote no esté repetido y que el peso sea válido.");
    }
  }

  // ==================== OPCION 6: ACTUALIZAR ESTADO POR CODIGO
  // ====================
  private static void actualizarEstadoEntregaPorCodigo() {
    String codigo = leerTexto("Ingrese el código de la entrega:");
    if (codigo == null)
      return;

    Entrega entrega = buscarEntregaGlobal(codigo);
    if (entrega == null) {
      JOptionPane.showMessageDialog(null, "No existe una entrega con el código: " + codigo);
      return;
    }

    if (entrega.getEstadoEntrega() == EstadoEntrega.ACEPTADA) {
      JOptionPane.showMessageDialog(null, "La entrega " + codigo + " ya está ACEPTADA y no admite cambios de estado.");
      return;
    }

    if (entrega.getListLotes().isEmpty()) {
      JOptionPane.showMessageDialog(null, "La entrega no tiene lotes.\n"
          + "Debe agregar al menos un lote antes de aceptarla.");
      return;
    }

    String confirmacion = leerTexto("¿Confirma aceptar la entrega " + codigo + "? (S/N):");
    if (confirmacion == null)
      return;

    if (confirmacion.equalsIgnoreCase("S")) {
      if (entrega.actualizarEstadoEntrega(EstadoEntrega.ACEPTADA)) {
        Comprobante comprobante = entrega.getTheComprobante();
        JOptionPane.showMessageDialog(null, "Entrega ACEPTADA exitosamente.\n\n"
            + "Comprobante generado:\n"
            + "Peso total: " + comprobante.pesoTotal() + " kg\n"
            + "Valor total: $" + comprobante.valorTotal());
      } else {
        JOptionPane.showMessageDialog(null, "No fue posible aceptar la entrega.");
      }
    } else if (confirmacion.equalsIgnoreCase("N")) {
      JOptionPane.showMessageDialog(null, "Operación cancelada.");
    } else {
      JOptionPane.showMessageDialog(null, "Respuesta inválida. Debe ser S o N.");
    }
  }

  // ========== INICIO DE FUNCIONES AUXILIARES ========== //

  private static PapelCarton crearPapelCarton(Entrega entrega) {
    PapelCarton lote = null;

    String codigo = leerTexto("Ingrese el código único del lote (dentro de la entrega):");
    if (codigo != null) {
      if (codigoLoteRepetido(entrega, codigo)) {
        JOptionPane.showMessageDialog(null, "Ya existe un lote con el código " + codigo + " en esta entrega.");
      } else {
        String descripcion = leerTexto("Ingrese la descripción del lote:");
        if (descripcion != null) {
          Integer peso = leerEntero("Ingrese el peso en kilogramos (entero positivo):");
          if (peso != null) {
            if (entrega.validarPesoEntero(peso)) {
              String respuesta = leerTexto("¿El material fue clasificado antes de entregarse? (S/N):");
              if (respuesta != null) {
                if (respuesta.equalsIgnoreCase("S")) {
                  lote = new PapelCarton(codigo, descripcion, peso, entrega, true);
                } else if (respuesta.equalsIgnoreCase("N")) {
                  lote = new PapelCarton(codigo, descripcion, peso, entrega, false);
                } else {
                  JOptionPane.showMessageDialog(null, "Respuesta inválida. Debe ser S o N.");
                }
              }
            } else {
              JOptionPane.showMessageDialog(null, "El peso debe ser un entero positivo.");
            }
          }
        }
      }
    }

    return lote;
  }

  private static Metal crearMetal(Entrega entrega) {
    Metal lote = null;

    String codigo = leerTexto("Ingrese el código único del lote (dentro de la entrega):");
    if (codigo != null) {
      if (codigoLoteRepetido(entrega, codigo)) {
        JOptionPane.showMessageDialog(null, "Ya existe un lote con el código " + codigo + " en esta entrega.");
      } else {
        String descripcion = leerTexto("Ingrese la descripción del lote:");
        if (descripcion != null) {
          Integer peso = leerEntero("Ingrese el peso en kilogramos (entero positivo):");
          if (peso != null) {
            if (entrega.validarPesoEntero(peso)) {
              String opciones = "Tipos de metal:\n\n1. ALUMINIO\n2. ACERO\n\nSeleccione el tipo de metal:";
              String opcionMetal = JOptionPane.showInputDialog(null, opciones);

              if (opcionMetal != null) {
                if (opcionMetal.equals("1")) {
                  lote = new Metal(codigo, descripcion, peso, entrega, TipoMetal.ALUMINIO);
                } else if (opcionMetal.equals("2")) {
                  lote = new Metal(codigo, descripcion, peso, entrega, TipoMetal.ACERO);
                } else {
                  JOptionPane.showMessageDialog(null, "Opción inválida.");
                }
              }
            } else {
              JOptionPane.showMessageDialog(null, "El peso debe ser un entero positivo.");
            }
          }
        }
      }
    }

    return lote;
  }

  private static ResElectrico crearResElectrico(Entrega entrega) {
    ResElectrico lote = null;

    String codigo = leerTexto("Ingrese el código único del lote (dentro de la entrega):");
    if (codigo != null) {
      if (codigoLoteRepetido(entrega, codigo)) {
        JOptionPane.showMessageDialog(null, "Ya existe un lote con el código " + codigo + " en esta entrega.");
      } else {
        String descripcion = leerTexto("Ingrese la descripción del lote:");
        if (descripcion != null) {
          Integer peso = leerEntero("Ingrese el peso en kilogramos (entero positivo):");
          if (peso != null) {
            if (entrega.validarPesoEntero(peso)) {
              Integer equipos = leerEntero("Ingrese la cantidad de equipos completos (entero mayor o igual a cero):");
              if (equipos != null) {
                if (entrega.validarEquiposEntero(equipos)) {
                  lote = new ResElectrico(codigo, descripcion, peso, entrega, equipos);
                } else {
                  JOptionPane.showMessageDialog(null, "La cantidad de equipos debe ser mayor o igual a cero.");
                }
              }
            } else {
              JOptionPane.showMessageDialog(null, "El peso debe ser un entero positivo.");
            }
          }
        }
      }
    }

    return lote;
  }

  private static boolean codigoLoteRepetido(Entrega entrega, String codigoLote) {
    boolean res = false;

    ArrayList<Lote> lotes = entrega.getListLotes();
    if (!lotes.isEmpty()) {
      for (Lote lote : lotes) {
        if (lote.getCodigo().equalsIgnoreCase(codigoLote)) {
          res = true;
          break;
        }
      }
    }

    return res;
  }

  // busca si hay una entrega ya existe en el global de la empresa
  private static Entrega buscarEntregaGlobal(String codigoEntrega) {
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

  // función para mostrar mensajes personalizados
  private static String leerTexto(String mensaje) {
    String res = null;
    boolean continuar = true;

    while (continuar) {
      String entrada = JOptionPane.showInputDialog(null, mensaje);

      if (entrada == null) {
        continuar = false;
      } else if (entrada.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "El valor no puede estar vacío. Intente nuevamente.");
      } else {
        res = entrada.trim();
        continuar = false;
      }
    }

    return res;
  }

  private static Integer leerEntero(String mensaje) {
    Integer res = null;
    boolean continuar = true;

    while (continuar) {
      String entrada = JOptionPane.showInputDialog(null, mensaje);

      if (entrada == null) {
        continuar = false;
      } else {
        try {
          res = Integer.parseInt(entrada.trim());
          continuar = false;
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog(null, "Debe ingresar un número entero válido. Intente nuevamente.");
        }
      }
    }

    return res;
  }

  private static LocalDate leerFecha() {
    LocalDate res = null;

    Integer anio = leerEntero("Ingrese el AÑO de la entrega (ej: 2025):");
    if (anio != null) {
      Integer mes = leerEntero("Ingrese el MES de la entrega (1-12):");
      if (mes != null) {
        Integer dia = leerEntero("Ingrese el DÍA de la entrega (1-31):");
        if (dia != null) {
          try {
            res = LocalDate.of(anio, mes, dia);
          } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La fecha ingresada no es válida.");
          }
        }
      }
    }

    return res;
  }

  // ========== FIN DE FUNCIONES AUXILIARES ========== //
}