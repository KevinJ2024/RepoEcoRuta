package com.EcoRuta.Lote;

import com.EcoRuta.Entrega.Entrega;

public class ResElectricos extends Lote{

    private final double PRECIO_RESELECTRICOS = 1400;
    private final double PRECIO_EQUIPO_COMPLETO = 800;

    private int equiposCompletos;

    public ResElectricos(String codigo, String descripcion, double peso, Entrega theEntrega, int equiposCompletos) {
        super(codigo, descripcion, peso, theEntrega);
        this.equiposCompletos = equiposCompletos;
    }

    @Override
    public double calcularValorLote() {
        return 0;
    }

    // ========== INICIO DE GETS Y SETS ========== //

    public int getEquiposCompletos() {
        return equiposCompletos;
    }

    public void setEquiposCompletos(int equiposCompletos) {
        this.equiposCompletos = equiposCompletos;
    }

    // ========== FIN DE GETS Y SETS ========== //
}
