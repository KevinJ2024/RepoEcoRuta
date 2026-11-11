package com.EcoRuta.Entrega;

import com.EcoRuta.Lote.Lote;
import com.EcoRuta.Lote.Metales;
import com.EcoRuta.Lote.ResElectricos;

import java.util.ArrayList;

public record Comprobante(Entrega theEntrega, String idAportante, double pesoTotal, double valorTotal) {

    public String obtenerEtiquetas(){
        String res = "";
        ArrayList<Lote> lotes = theEntrega.getListLotes();

        if(lotes != null){
            if(!lotes.isEmpty()){
                for (Lote lote: lotes){
                    if(lote instanceof Metales || lote instanceof ResElectricos){
                        if (lote instanceof Metales){
                            res += ", " + ((Metales) lote).getEtiqueta();
                        }else {
                            res += ", " + ((ResElectricos) lote).getEtiqueta();
                        }
                    }
                }
            }

        }
        return res;
    }

}
