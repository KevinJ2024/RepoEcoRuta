package com.EcoRuta.Entrega;

import com.EcoRuta.Lote.Lote;

import java.time.LocalDate;
import java.util.ArrayList;

public class Entrega {
    private String codigo;
    private LocalDate fecha;
    private EstadoEntrega estadoEntrega;

    // LLaves de relacion con otras clases
    private Aportante theAportante;
    private Comprobante theComprobante;
    private ArrayList<Lote> listLotes;

    public Entrega(String codigo, LocalDate fecha, EstadoEntrega estadoEntrega, Aportante theAportante) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.estadoEntrega = estadoEntrega;
        this.theAportante = theAportante;

        this.theComprobante = null;
        this.listLotes = new ArrayList<Lote>();
    }

    public boolean agregarLote(Lote nuevoLote) {
        boolean res = false;
        if (puedeAgregarLote()) {
            if(nuevoLote != null){
                this.listLotes.add(nuevoLote);
                res = true;
            }
        }
        return res;
    }

    public boolean actualizarEstadoEntrega(EstadoEntrega nuevoEstado){
        boolean res = false;

        if (nuevoEstado == EstadoEntrega.ACEPTADA){
            if (puedePasarA(nuevoEstado)){
                this.setEstadoEntrega(nuevoEstado);
                res = true;
            }
        }else{
            this.setEstadoEntrega(nuevoEstado);
        }

        return res;
    }

    public boolean generarComprobante(){
        boolean res = false;

        if (this.estadoEntrega == EstadoEntrega.ACEPTADA){
            double pesoTotal = calcularPesoEntrega();
            double valorTotal = calcularPrecioEntrega();

            Comprobante comprobante = new Comprobante(this, this.theAportante.id(), pesoTotal, valorTotal);
            this.setTheComprobante(comprobante);
            res = true;
        }

        return res;
    }

    // ========== INICIO DE FUNCIONES AUXILIARES ========== //

    private double calcularPesoEntrega(){
        double res = 0.0;

        if(!this.listLotes.isEmpty()){
            for (Lote lote: listLotes){
                if (lote != null){
                    res += lote.getPeso();
                }
            }
        }

        return res;
    }

    private double calcularPrecioEntrega(){
        double res = 0.0;

        if(!this.listLotes.isEmpty()){
            for (Lote lote: listLotes){
                if (lote != null){
                    res += lote.calcularValorLote();
                }
            }
        }

        return res;
    }

    // ========== FIN DE FUNCIONES AUXILIARES ========== //

    // ========== INICIO DE FUNCIONES DE VALIDACION ========== //

    private boolean puedeAgregarLote(){
        boolean res = false;
        if (this.estadoEntrega != EstadoEntrega.ACEPTADA){
            res = true;
        }
        return res;
    }

    public boolean validarPesoEntero(double kilogramos){
        boolean res = false;
        if (kilogramos >= 0.0){
            res = true;
        }
        return res;
    }

    public boolean validarEquiposEntero(double cantEquipos){
        boolean res = false;
        if (cantEquipos >= 0.0){
            res = true;
        }
        return res;
    }

    private boolean puedePasarA(EstadoEntrega nuevoEstado){
        boolean res = false;

        if (this.codigo != null){
            if(nuevoEstado == EstadoEntrega.ACEPTADA){
                if(this.estadoEntrega != EstadoEntrega.ACEPTADA && !this.listLotes.isEmpty()){
                    res = true;
                }
            }else{
                res = true;
            }
        }

        return res;
    }

    // ========== FIN DE FUNCIONES DE VALIDACION ========== //

    // ========== INICIO DE GETS Y SETS ========== //

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoEntrega getEstadoEntrega() {
        return estadoEntrega;
    }

    public void setEstadoEntrega(EstadoEntrega estadoEntrega) {
        this.estadoEntrega = estadoEntrega;
    }

    public Aportante getTheAportante() {
        return theAportante;
    }

    public void setTheAportante(Aportante theAportante) {
        this.theAportante = theAportante;
    }

    public Comprobante getTheComprobante() {
        return theComprobante;
    }

    public void setTheComprobante(Comprobante theComprobante) {
        this.theComprobante = theComprobante;
    }

    public ArrayList<Lote> getListLotes() {
        return listLotes;
    }

    public void setListLotes(ArrayList<Lote> listLotes) {
        this.listLotes = listLotes;
    }


    // ========== FIN DE GETS Y SETS ========== //
}
