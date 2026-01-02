package es.upm.etsiinf.artic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DescargaInformacionCuadro implements Runnable
{
    private InformacionExtendida informacionExtendida;
    private String id_cuadro;

    public DescargaInformacionCuadro( InformacionExtendida informacionExtendida, String id_cuadro )
    {
        this.informacionExtendida = informacionExtendida;
        this.id_cuadro = id_cuadro;
    }

    @Override
    public void run()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a");
        Gson gson = gsonBuilder.create();
        String response = null;
        try {
            String urlServerService = "https://api.artic.edu/api/v1/artworks/" + this.id_cuadro + "?fields=id,title,image_id,description";
            response = NetUtils.getURLText( urlServerService );
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = gson.fromJson( response, JsonObject.class );
        Type listType = new TypeToken<DataCuadro>() {}.getType();
        System.out.println( jsonObject.toString() );
        DataCuadro cdr = gson.fromJson(jsonObject, listType);

        if (informacionExtendida.isAdded() && informacionExtendida.getActivity() != null) {
            informacionExtendida.getActivity().runOnUiThread(() ->
                    informacionExtendida.finishDownload(cdr)
            );
        }
    }
}