package es.upm.etsiinf.artic;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class DataCuadro
{
    @SerializedName("data")
    private Map<String,String> data;
    @SerializedName("config")
    private Map<String,String> config;

    public DataCuadro( Map<String,String> data, Map<String,String> config )
    {
        this.data = data;
        this.config = config;
    }
    public Cuadro getCuadro()
    {
        String id = data.get("id");
        String title = data.get("title");
        String image_id = data.get("image_id");
        String iiif_url = config.get("iiif_url");
        String description = data.get("description");
        return new Cuadro( id, title, image_id, iiif_url, description );
    }
}