package org.sysmob.biblivirti.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.enums.ENivelSimulado;
import org.sysmob.biblivirti.enums.EStatusGrupo;
import org.sysmob.biblivirti.enums.EStatusMaterial;
import org.sysmob.biblivirti.enums.ETipoGrupo;
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.enums.EUsuarioStatus;
import org.sysmob.biblivirti.model.Apresentacao;
import org.sysmob.biblivirti.model.AreaInteresse;
import org.sysmob.biblivirti.model.Conteudo;
import org.sysmob.biblivirti.model.Exercicio;
import org.sysmob.biblivirti.model.Formula;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Jogo;
import org.sysmob.biblivirti.model.Livro;
import org.sysmob.biblivirti.model.Material;
import org.sysmob.biblivirti.model.Simulado;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.model.Video;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public abstract class BiblivirtiParser {

    public static Usuario parseToUsuario(JSONObject json) throws JSONException {
        Usuario usuario = null;
        if (json != null) {
            usuario = new Usuario();
            usuario.setUsnid(json.getInt(Usuario.FIELD_USNID));
            usuario.setUscfbid(json.getString(Usuario.FIELD_USCFBID));
            usuario.setUscnome(json.getString(Usuario.FIELD_USCNOME));
            usuario.setUscmail(json.getString(Usuario.FIELD_USCMAIL));
            usuario.setUsclogn(json.getString(Usuario.FIELD_USCLOGN));
            usuario.setUscfoto(json.getString(Usuario.FIELD_USCFOTO));
            usuario.setUscsenh(json.opt(Usuario.FIELD_USCSENH) == null ? null : json.getString(Usuario.FIELD_USCSENH));
            usuario.setUscstat(EUsuarioStatus.ATIVO.getValue() == json.getString(Usuario.FIELD_USCSTAT).charAt(0) ? EUsuarioStatus.ATIVO : EUsuarioStatus.INATIVO);
            usuario.setUsdcadt(Timestamp.valueOf(json.getString(Usuario.FIELD_USDCADT)));
            usuario.setUsdaldt(Timestamp.valueOf(json.getString(Usuario.FIELD_USDALDT)));
            usuario.setGrupos(json.opt(Usuario.FIELD_USGROUPS) != null && json.optJSONArray(Usuario.FIELD_USGROUPS) != null ? parseToGrupos(json.getJSONArray(Usuario.FIELD_USGROUPS)) : null);
        }
        return usuario;
    }

    public static List<Usuario> parseToUsuarios(JSONArray json) throws JSONException {
        List<Usuario> usuarios = null;
        if (json != null) {
            usuarios = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                Usuario usuario = null;
                usuario = parseToUsuario(json.getJSONObject(i));
                usuarios.add(usuario);
            }
        }
        if (usuarios != null && usuarios.size() > 0)
            return usuarios;
        return null;
    }

    public static AreaInteresse parseToAreaInteresse(JSONObject json) throws JSONException {
        AreaInteresse areaInteresse = null;
        if (json != null) {
            areaInteresse = new AreaInteresse();
            areaInteresse.setAinid(json.getInt(AreaInteresse.FIELD_AINID));
            areaInteresse.setAicdesc(json.getString(AreaInteresse.FIELD_AICDESC));
            areaInteresse.setAidcadt(Timestamp.valueOf(json.getString(AreaInteresse.FIELD_AIDCADT)));
            areaInteresse.setAidaldt(Timestamp.valueOf(json.getString(AreaInteresse.FIELD_AIDALDT)));
        }
        return areaInteresse;

    }

    public static List<AreaInteresse> parseToAreasinteresse(JSONArray json) throws JSONException {
        List<AreaInteresse> areasInteresse = null;
        if (json != null) {
            areasInteresse = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                AreaInteresse areaInteresse = null;
                areaInteresse = parseToAreaInteresse(json.getJSONObject(i));
                areasInteresse.add(areaInteresse);
            }
        }
        if (areasInteresse != null && areasInteresse.size() > 0)
            return areasInteresse;
        return null;
    }

    public static Grupo parseToGrupo(JSONObject json) throws JSONException {
        Grupo grupo = null;
        if (json != null) {
            grupo = new Grupo();
            grupo.setGrnid(json.getInt(Grupo.FIELD_GRNID));
            grupo.setAreaInteresse(parseToAreaInteresse(json.getJSONObject(Grupo.FIELD_GRAREAOFINTEREST)));
            grupo.setGrcnome(json.getString(Grupo.FIELD_GRCNOME));
            grupo.setGrcfoto(json.getString(Grupo.FIELD_GRCFOTO));
            grupo.setGrctipo(ETipoGrupo.ABERTO.getValue() == json.getString(Grupo.FIELD_GRCTIPO).charAt(0) ? ETipoGrupo.ABERTO : ETipoGrupo.FECHADO);
            grupo.setGrcstat(EStatusGrupo.ATIVO.getValue() == json.getString(Grupo.FIELD_GRCSTAT).charAt(0) ? EStatusGrupo.ATIVO : EStatusGrupo.INATIVO);
            grupo.setGrdcadt(Timestamp.valueOf(json.getString(Grupo.FIELD_GRDCADT)));
            grupo.setGrdaldt(Timestamp.valueOf(json.getString(Grupo.FIELD_GRDALDT)));
            grupo.setAdmin(parseToUsuario(json.opt(Grupo.FIELD_GRADMIN) != null ? json.getJSONObject(Grupo.FIELD_GRADMIN) : null));
            grupo.setUsuarios(json.opt(Grupo.FIELD_GRUSERS) != null ? parseToUsuarios(json.getJSONArray(Grupo.FIELD_GRUSERS)) : null);
        }
        return grupo;
    }

    public static List<Grupo> parseToGrupos(JSONArray json) throws JSONException {
        List<Grupo> grupos = null;
        if (json != null && !json.equals("null")) {
            grupos = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                Grupo grupo = null;
                grupo = parseToGrupo(json.getJSONObject(i));
                grupos.add(grupo);
            }
        }
        if (grupos != null && grupos.size() > 0)
            return grupos;
        return null;
    }

    public static Material parseToMaterial(JSONObject json) throws JSONException {
        Material material = null;
        if (json != null) {
            if (json.getString(Material.FIELD_MACTIPO).charAt(0) == ETipoMaterial.APRESENTACAO.getValue()) {
                material = new Apresentacao();
            } else if (json.getString(Material.FIELD_MACTIPO).charAt(0) == ETipoMaterial.EXERCICIO.getValue()) {
                material = new Exercicio();
            } else if (json.getString(Material.FIELD_MACTIPO).charAt(0) == ETipoMaterial.FORMULA.getValue()) {
                material = new Formula();
            } else if (json.getString(Material.FIELD_MACTIPO).charAt(0) == ETipoMaterial.JOGO.getValue()) {
                material = new Jogo();
            } else if (json.getString(Material.FIELD_MACTIPO).charAt(0) == ETipoMaterial.LIVRO.getValue()) {
                material = new Livro();
            } else if (json.getString(Material.FIELD_MACTIPO).charAt(0) == ETipoMaterial.SIMULADO.getValue()) {
                material = new Simulado();
                if (json.getString(Material.FIELD_MACNIVL).charAt(0) == ENivelSimulado.BASICO.getValue()) {
                    ((Simulado) material).setMacnivl(ENivelSimulado.BASICO);
                } else if (json.getString(Material.FIELD_MACNIVL).charAt(0) == ENivelSimulado.INTERMEDIARIO.getValue()) {
                    ((Simulado) material).setMacnivl(ENivelSimulado.INTERMEDIARIO);
                } else if (json.getString(Material.FIELD_MACNIVL).charAt(0) == ENivelSimulado.AVANCADO.getValue()) {
                    ((Simulado) material).setMacnivl(ENivelSimulado.AVANCADO);
                } else if (json.getString(Material.FIELD_MACNIVL).charAt(0) == ENivelSimulado.PROFISSIONAL.getValue()) {
                    ((Simulado) material).setMacnivl(ENivelSimulado.PROFISSIONAL);
                }
            } else if (json.getString(Material.FIELD_MACTIPO).charAt(0) == ETipoMaterial.VIDEO.getValue()) {
                material = new Video();
            }

            material.setManid(json.getInt(Material.FIELD_MANID));
            material.setMacdesc(json.getString(Material.FIELD_MACDESC));
            material.setMacurl(json.getString(Material.FIELD_MACURL));
            material.setMacstat(EStatusMaterial.ATIVO.getValue() == json.getString(Material.FIELD_MACSTAT).charAt(0) ? EStatusMaterial.ATIVO : EStatusMaterial.INATIVO);
            material.setMadcadt(Timestamp.valueOf(json.getString(Material.FIELD_MADCADT)));
            material.setMadaldt(Timestamp.valueOf(json.getString(Material.FIELD_MADALDT)));
            material.setManqtdce(json.getInt(Material.FIELD_MANQTDCE));
            material.setManqtdha(json.getInt(Material.FIELD_MANQTDHA));
        }
        return material;
    }

    public static List<Material> parseToMateriais(JSONArray json) throws JSONException {
        List<Material> materiais = null;
        if (json != null) {
            materiais = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                Material material = null;
                material = parseToMaterial(json.getJSONObject(i));
                materiais.add(material);
            }
        }
        if (materiais != null && materiais.size() > 0)
            return materiais;
        return null;
    }

    public static Conteudo parseToConteudo(JSONObject json) throws JSONException {
        Conteudo conteudo = null;
        if (json != null) {
            conteudo = new Conteudo();
            conteudo.setConid(json.getInt(Conteudo.FIELD_CONID));
            conteudo.setCocdesc(json.getString(Conteudo.FIELD_COCDESC));
            conteudo.setCodcadt(Timestamp.valueOf(json.getString(Conteudo.FIELD_CODCADT)));
            conteudo.setCodaldt(Timestamp.valueOf(json.getString(Conteudo.FIELD_CODALDT)));
            conteudo.setGrupo(json.opt(Conteudo.FIELD_GROUP) != null ? parseToGrupo(json.getJSONObject(Conteudo.FIELD_GROUP)) : null);
        }
        return conteudo;
    }

    public static List<Conteudo> parseToConteudos(JSONArray json) throws JSONException {
        List<Conteudo> conteudos = null;
        if (json != null) {
            conteudos = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                Conteudo conteudo = null;
                conteudo = parseToConteudo(json.getJSONObject(i));
                conteudos.add(conteudo);
            }
        }
        if (conteudos != null && conteudos.size() > 0)
            return conteudos;
        return null;
    }
}
