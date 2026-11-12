package com.EcoRuta.Lote;

import com.EcoRuta.Entrega.Entrega;

public class PapelCarton extends Lote {

  private final double PRECIO_PAPELCARTON = 500;
  private final double PRECIO_ADICIONAL_CLASIFICADO = 100;

  private boolean clasificado;

  public PapelCarton(String codigo, String descripcion, int peso, Entrega theEntrega, boolean clasificado) {
    super(codigo, descripcion, peso, theEntrega);
    this.clasificado = clasificado;
  }

  @Override
  public double calcularValorLote() {
    double res = 0.0;
    res += peso * PRECIO_PAPELCARTON;
    if (clasificado) {
      res += peso * PRECIO_ADICIONAL_CLASIFICADO;
    }

    return res;
  }

  // ========== INICIO DE GETS Y SETS ========== //

  public boolean isClasificado() {
    return clasificado;
  }

  public void setClasificado(boolean clasificado) {
    this.clasificado = clasificado;
  }

  // ========== FIN DE GETS Y SETS ========== //
}
