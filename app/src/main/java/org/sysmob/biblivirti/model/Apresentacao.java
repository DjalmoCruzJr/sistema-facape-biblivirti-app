package org.sysmob.biblivirti.model;

import org.sysmob.biblivirti.enums.EStatusMaterial;
import org.sysmob.biblivirti.enums.ETipoMaterial;

import java.util.Date;
import java.util.List;

/**
 * Created by micro99 on 23/02/2017.
 */

public class Apresentacao extends Material {

    public Apresentacao() {
        super();
        this.setMactipo(ETipoMaterial.APRESENTACAO);
    }

    public Apresentacao(int manid, String macdesc, String macurl, EStatusMaterial macstat, Date madcadt, Date madaldt, int manqtdce, int manqtdha, List<Conteudo> conteudos, List<Comentario> comentarios) {
        super(manid, macdesc, macurl, ETipoMaterial.APRESENTACAO, macstat, madcadt, madaldt, manqtdce, manqtdha, conteudos, comentarios);
    }
}
