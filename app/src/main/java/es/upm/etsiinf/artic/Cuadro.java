package es.upm.etsiinf.artic;
import com.google.gson.annotations.SerializedName;
public class Cuadro
{
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("lqip")
    private String lqip;

    public Cuadro ( String id, String title, String lqip )
    {
        this.id = id;
        this.title = title;
        this.lqip = lqip;
    }

}
