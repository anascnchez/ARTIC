package es.upm.etsiinf.artic;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

public class Cuadro {
    private String title;
    private String image_id;
    private String iiif_url;

    Cuadro( String title, String image_id, String iiif_url )
    {
        this.title = title;
        this.image_id = image_id;
        this.iiif_url = iiif_url;
    }

    public String getTitle()
    {
        return title;
    }

    public String getImageId()
    {
        return image_id;
    }

    public String getImageUrl()
    {
        return iiif_url + "/" + image_id + "/full/200,/0/default.jpg";
    }
}
