package com.EcoRuta.Entrega;

import java.util.ArrayList;

public record Aportante(String id, String nombre, String correo, ArrayList<Entrega> listEntregas) {

    public boolean agregarEntrega(Entrega entrega){
        boolean res = false;
        if (entrega != null){
            listEntregas.add(entrega);
            res =  true;
        }

        return res;
    }

}
