package es.upm.etsiinf.artic;

public class Cuadro
{
    private String title;
    private String image_id;
    private String iiif_url;

    public Cuadro( String title, String image_id, String iiif_url )
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
        if (image_id == null || image_id.isEmpty()) {
            return null;
        }

        // Si es una URI local (galería o cámara), devolvemos solo la URI
        if (image_id.startsWith("content://") || image_id.startsWith("file://")) {
            return image_id;
        }
        
        // Si es un ID del museo, construimos la URL de Artic
        String baseUrl = iiif_url;
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "https://www.artic.edu/iiif/2";
        }
        
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        
        return baseUrl + "/" + image_id + "/full/200,/0/default.jpg";
    }
}
