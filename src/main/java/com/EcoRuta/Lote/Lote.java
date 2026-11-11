package com.EcoRuta.Lote;

import com.EcoRuta.Entrega.Entrega;

public abstract class Lote {
    protected String codigo;
    protected String descripcion;
    protected double peso;

    //LLaves de relacion con otras clases
    protected Entrega theEntrega;

    public Lote(String codigo, String descripcion, double peso, Entrega theEntrega) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.peso = peso;
        this.theEntrega = theEntrega;
    }

    public abstract double calcularValorLote();

    // ========== INICIO DE GETS Y SETS ========== //

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Entrega getTheEntrega() {
        return theEntrega;
    }

    public void setTheEntrega(Entrega theEntrega) {
        this.theEntrega = theEntrega;
    }


    // ========== FIN DE GETS Y SETS ========== //
}
