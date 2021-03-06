package org.sysmob.biblivirti.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.model.Apresentacao;
import org.sysmob.biblivirti.model.Conteudo;
import org.sysmob.biblivirti.model.Exercicio;
import org.sysmob.biblivirti.model.Formula;
import org.sysmob.biblivirti.model.Jogo;
import org.sysmob.biblivirti.model.Livro;
import org.sysmob.biblivirti.model.Material;
import org.sysmob.biblivirti.model.Simulado;
import org.sysmob.biblivirti.model.Video;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by djalmocruzjr on 24/01/2017.
 */

public abstract class BiblivirtiUtils {

    public static boolean isNetworkConnected() {
        Context context = BiblivirtiApplication.getInstance().getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static String getMimeType(String type) {
        String mimeType = "";
        switch (type) {
            case "application/pdf":
                mimeType = "pdf";
                break;
            case "image/jpeg":
                mimeType = "jpeg";
                break;
            case "image/png":
                mimeType = "png";
                break;
            case "image/gif":
                mimeType = "gif";
                break;
            case "image/*":
                mimeType = "jpeg";
                break;
            case "video/mp4":
                mimeType = "mp4";
                break;
            case "video/*":
                mimeType = "mp4";
                break;
        }
        return mimeType;
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static String encondImage(Bitmap bitmap, String mimeType) {
        String result = null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            result = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT) + "." + getMimeType(mimeType);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Material createMaterialByTipo(ETipoMaterial tipoMaterial) {
        Material material = null;

        if (tipoMaterial == ETipoMaterial.APRESENTACAO) {
            material = new Apresentacao();
        } else if (tipoMaterial == ETipoMaterial.EXERCICIO) {
            material = new Exercicio();
        } else if (tipoMaterial == ETipoMaterial.FORMULA) {
            material = new Formula();
        } else if (tipoMaterial == ETipoMaterial.JOGO) {
            material = new Jogo();
        } else if (tipoMaterial == ETipoMaterial.LIVRO) {
            material = new Livro();
        } else if (tipoMaterial == ETipoMaterial.SIMULADO) {
            material = new Simulado();
        } else if (tipoMaterial == ETipoMaterial.VIDEO) {
            material = new Video();
        }

        return material;
    }

    @Nullable
    public static JSONArray createContentsJson(List<Conteudo> conteudosSelecionados) throws JSONException {
        JSONArray jsonContents = null;
        if (conteudosSelecionados != null && conteudosSelecionados.size() > 0) {
            jsonContents = new JSONArray();
            for (Conteudo conteudo : conteudosSelecionados) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Conteudo.FIELD_CONID, conteudo.getConid());
                jsonContents.put(jsonObject);
            }
        }
        return jsonContents != null ? jsonContents : null;
    }

    public static String encondFile(Context context, Uri uri, String mimeType) {
        String result = null;
        try {
            InputStream stream = context.getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            result = Base64.encodeToString(bytes, Base64.DEFAULT) + "." + getMimeType(mimeType);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static final String createStringErrors(JSONObject errors) {
        String result = "";
        if (errors != null) {
            Iterator<String> keys = errors.keys();
            while (keys.hasNext()) {
                try {
                    String key = keys.next();
                    result += errors.opt(key) != null ? "\n*) " + errors.getString(key) + "\n" : "";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
