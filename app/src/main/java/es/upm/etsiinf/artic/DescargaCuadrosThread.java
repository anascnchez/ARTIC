package es.upm.etsiinf.artic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DescargaCuadrosThread implements Runnable
{
    private Home home;

    public DescargaCuadrosThread( Home home )
    {
        this.home = home;
    }

    @Override
    public void run()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a");
        Gson gson = gsonBuilder.create();
        String response = null;
        try {
            String urlServerService = "https://api.artic.edu/api/v1/artworks?fields=id,title,image_id&query[term][is_public_domain]=true";

            response = NetUtils.getURLText( urlServerService );
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = gson.fromJson( response, JsonObject.class );
        Type listType = new TypeToken<Data>() {}.getType();
        Data cdrs = gson.fromJson(jsonObject, listType);

        if (home.isAdded() && home.getActivity() != null) {
            home.getActivity().runOnUiThread(() ->
                    home.finishDownload(cdrs)
            );
        }
    }
}