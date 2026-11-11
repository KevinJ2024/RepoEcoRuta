package com.EcoRuta.Lote;

import com.EcoRuta.Entrega.Entrega;

public class ResElectrico extends Lote implements IEtiquetar{

    private final double PRECIO_RESELECTRICOS = 1400;
    private final double PRECIO_EQUIPO_COMPLETO = 800;

    private int equiposCompletos;
    private String etiqueta;

    public ResElectrico(String codigo, String descripcion, int peso, Entrega theEntrega, int equiposCompletos) {
        super(codigo, descripcion, peso, theEntrega);
        this.equiposCompletos = equiposCompletos;
        this.etiqueta = generarEtiqueta();
    }

    @Override
    public String generarEtiqueta(){
        String res = "Codigo Lote: " + codigo + " Equipos Completos: " + equiposCompletos;
        return res;
    }

    @Override
    public double calcularValorLote() {
        double res = 0.0;

        res += peso * PRECIO_RESELECTRICOS;
        if (equiposCompletos > 0){
            res += equiposCompletos * PRECIO_EQUIPO_COMPLETO;
        }

        return res;
    }

    // ========== INICIO DE GETS Y SETS ========== //

    public int getEquiposCompletos() {
        return equiposCompletos;
    }

    public void setEquiposCompletos(int equiposCompletos) {
        this.equiposCompletos = equiposCompletos;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    // ========== FIN DE GETS Y SETS ========== //
}
