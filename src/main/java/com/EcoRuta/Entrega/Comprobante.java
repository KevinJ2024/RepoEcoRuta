package com.EcoRuta.Entrega;

import com.EcoRuta.Lote.Lote;
import com.EcoRuta.Lote.Metal;
import com.EcoRuta.Lote.ResElectrico;

import java.util.ArrayList;

public record Comprobante(Entrega theEntrega, String idAportante, int pesoTotal, double valorTotal) {

    public String obtenerEtiquetas() {
        String res = "";
        ArrayList<Lote> lotes = theEntrega.getListLotes();

        if (lotes != null && !lotes.isEmpty()) {
            for (Lote lote : lotes) {
                if (lote instanceof Metal || lote instanceof ResElectrico) {
                    if (!res.isEmpty()) {
                        res += ", ";
                    }

                    if (lote instanceof Metal) {
                        res += ((Metal) lote).getEtiqueta();
                    } else {
                        res += ((ResElectrico) lote).getEtiqueta();
                    }
                }
            }
        }
        return res;
    }

}
