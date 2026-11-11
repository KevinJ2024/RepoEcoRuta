package com.EcoRuta.Entrega;

import java.util.ArrayList;

public record Aportante(String id, String nombre, String correo, ArrayList<Entrega> listEntregas) {

    public boolean agregarEntrega(Entrega entrega) {
        boolean res = false;
        if (entrega != null) {
            if (evitarDuplicarEntrega(entrega.getCodigo())) {
                listEntregas.add(entrega);
                res = true;
            }
        }

        return res;
    }

    public Entrega consultarEntrega(String codigoEntrega) {
        Entrega res = null;
        if (!this.listEntregas.isEmpty()) {
            for (Entrega entrega : listEntregas) {
                if (entrega.getCodigo().equalsIgnoreCase(codigoEntrega)) {
                    res = entrega;
                    break;
                }
            }
        }
        return res;
    }

    public Comprobante consultarComprobante(String codigoEntrega) {
        Comprobante res = null;
        if (!this.listEntregas.isEmpty()) {
            for (Entrega entrega : listEntregas) {
                if (entrega.getCodigo().equalsIgnoreCase(codigoEntrega)) {
                    res = entrega.getTheComprobante();
                    break;
                }
            }
        }
        return res;
    }

    private boolean evitarDuplicarEntrega(String codigoEntrega) {
        boolean res = true;

        if (!this.listEntregas.isEmpty()) {
            if (!codigoEntrega.isEmpty()) {
                for (Entrega entrega : listEntregas) {
                    if (entrega.getCodigo().equalsIgnoreCase(codigoEntrega)) {
                        res = false;
                    }
                }
            }
        }

        return res;
    }
}
