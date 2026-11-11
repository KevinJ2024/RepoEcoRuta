package com.EcoRuta.Lote;

import com.EcoRuta.Entrega.Entrega;

public class Metales extends Lote implements IEtiquetar{
    private final double PRECIO_ALUMINIO = 2200;
    private final double PRECIO_ACERO = 900;

    private TipoMetal tipoMetal;

    public Metales(String codigo, String descripcion, double peso, Entrega theEntrega, TipoMetal tipoMetal) {
        super(codigo, descripcion, peso, theEntrega);
        this.tipoMetal = tipoMetal;
    }

    @Override
    public String generarEtiqueta(){

        return "";
    }

    @Override
    public double calcularValorLote() {
        return 0;
    }

    // ========== INICIO DE GETS Y SETS ========== //

    public TipoMetal getTipoMetal() {
        return tipoMetal;
    }

    public void setTipoMetal(TipoMetal tipoMetal) {
        this.tipoMetal = tipoMetal;
    }

    // ========== FIN DE GETS Y SETS ========== //
}
