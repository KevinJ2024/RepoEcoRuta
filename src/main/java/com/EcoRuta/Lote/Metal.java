package com.EcoRuta.Lote;

import com.EcoRuta.Entrega.Entrega;

public class Metal extends Lote implements IEtiquetar{
    private final double PRECIO_ALUMINIO = 2200;
    private final double PRECIO_ACERO = 900;

    private TipoMetal tipoMetal;
    private String etiqueta;

    public Metal(String codigo, String descripcion, int peso, Entrega theEntrega, TipoMetal tipoMetal) {
        super(codigo, descripcion, peso, theEntrega);
        this.tipoMetal = tipoMetal;
        this.etiqueta = generarEtiqueta();
    }

    @Override
    public String generarEtiqueta(){
        String res = "Codigo Lote: " + codigo + " Tipo de metal: " + tipoMetal;
        return res;
    }

    @Override
    public double calcularValorLote() {
        double res = 0.0;
        if (tipoMetal == TipoMetal.ACERO){
            res += peso * PRECIO_ACERO;
        }else{
            res += peso * PRECIO_ALUMINIO;
        }

        return res;
    }

    // ========== INICIO DE GETS Y SETS ========== //

    public TipoMetal getTipoMetal() {
        return tipoMetal;
    }

    public void setTipoMetal(TipoMetal tipoMetal) {
        this.tipoMetal = tipoMetal;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    // ========== FIN DE GETS Y SETS ========== //
}
