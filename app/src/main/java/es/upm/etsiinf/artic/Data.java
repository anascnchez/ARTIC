package es.upm.etsiinf.artic;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class Data
{
    @SerializedName("data")
    private ArrayList<Map<String,String>> data;
    @SerializedName("config")
    private Map<String,String> config;

    public Data(ArrayList<Map<String,String>> data, Map<String,String> config )
    {
        this.data = data;
        this.config = config;
    }
    public Cuadro getCuadro( int position )
    {
        String title = data.get(position).get("title");
        String image_id = data.get(position).get("image_id");
        String iiif_url = config.get("iiif_url");
        return new Cuadro( title, image_id, iiif_url );
    }

    public ArrayList<Cuadro> getCuadros()
    {
        ArrayList<Cuadro> ret = new ArrayList<Cuadro>();
        for ( int i = 0; i < data.size(); i++ )
        {
            ret.add( getCuadro( i ) );
        }
        return ret;
    }
}
