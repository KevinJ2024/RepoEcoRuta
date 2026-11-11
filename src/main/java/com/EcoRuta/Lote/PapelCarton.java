package com.EcoRuta.Lote;

import com.EcoRuta.Entrega.Entrega;

public class PapelCarton extends Lote implements IEtiquetar{

    private final double PRECIO_PAPELCARTON = 500;
    private final double PRECIO_ADICIONAL_CLASIFICADO = 100;

    private boolean clasificado;

    public PapelCarton(String codigo, String descripcion, double peso, Entrega theEntrega, boolean clasificado) {
        super(codigo, descripcion, peso, theEntrega);
        this.clasificado = clasificado;
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

    public boolean isClasificado() {
        return clasificado;
    }

    public void setClasificado(boolean clasificado) {
        this.clasificado = clasificado;
    }


    // ========== FIN DE GETS Y SETS ========== //
}
