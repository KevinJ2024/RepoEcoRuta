package com.EcoRuta;

import com.EcoRuta.Entrega.Aportante;
import com.EcoRuta.Entrega.Entrega;
import com.EcoRuta.Entrega.EstadoEntrega;

import java.util.ArrayList;

public class Empresa {
    private String nit;
    private String nombre;

    private ArrayList<Aportante> listAportantes;

    public Empresa(String nit, String nombre) {
        this.nit = nit;
        this.nombre = nombre;
        this.listAportantes = new ArrayList<Aportante>();
    }

    public Aportante consultarAportante(String idAportante){
        Aportante res = null;

        if (!idAportante.isEmpty()){
            if (!listAportantes.isEmpty()){
                for (Aportante aportante: listAportantes){
                    if (aportante.id().equalsIgnoreCase(idAportante)){
                        res = aportante;
                        break;
                    }
                }
            }
        }

        return res;
    }

    public double consultarAcumuladoAportante(String idAportante){
        double res = 0.0;

        Aportante aportanteEncontrado = consultarAportante(idAportante);

        if (aportanteEncontrado != null){
            ArrayList<Entrega> listEntregas = aportanteEncontrado.listEntregas();
            if (!listEntregas.isEmpty()){
                for (Entrega entrega: listEntregas){
                    if (entrega.getEstadoEntrega() == EstadoEntrega.ACEPTADA){
                        res += entrega.getTheComprobante().valorTotal();
                    }
                }
            }
        }
        return res;
    }

    // ========== INICIO DE GETS Y SETS ========== //

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Aportante> getListAportantes() {
        return listAportantes;
    }

    public void setListAportantes(ArrayList<Aportante> listAportantes) {
        this.listAportantes = listAportantes;
    }

    // ========== FIN DE GETS Y SETS ========== //
}
