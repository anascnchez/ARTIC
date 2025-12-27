package es.upm.etsiinf.artic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class DescargaCuadrosThread implements Runnable
{
    private MainActivity mainActivity;

    public DescargaCuadrosThread( MainActivity mainActivity )
    {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a");
        Gson gson = gsonBuilder.create();
        String response = null;
        try {
            String urlServerService = "https://api.artic.edu/api/v1/artworks";

            response = NetUtils.getURLText( urlServerService );
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Cuadro> pls = Arrays.asList(gson.fromJson(response, Cuadro[].class));
    }
}
