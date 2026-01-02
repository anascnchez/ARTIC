package es.upm.etsiinf.artic;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CuadroAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Cuadro> cuadros;
    private LayoutInflater inflater;
    private int layoutId; 
    public ArrayList<Cuadro> cuadros_favoritos = new ArrayList<>(); 

    public CuadroAdapter(Context context, ArrayList<Cuadro> cuadros, int layoutId) {
        this.context = context;
        this.cuadros = cuadros;
        this.layoutId = layoutId;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return cuadros.size(); }

    @Override
    public Object getItem(int position) { return cuadros.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false);
        }

        Cuadro cuadro = cuadros.get(position);
        TextView nombreTextView = convertView.findViewById(R.id.nombre_cuadro);
        if (nombreTextView != null) {
            nombreTextView.setText(cuadro.getTitle());
        }

        View vistaImagen = convertView.findViewById(R.id.imagen_cuadro);
        String url = cuadro.getImageUrl();

        if (url != null && vistaImagen != null) {
            if (vistaImagen instanceof WebView) {
                WebView webView = (WebView) vistaImagen;
                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                }
                webView.loadUrl(url.trim());
            } else if (vistaImagen instanceof ImageView) {
                ImageView imageView = (ImageView) vistaImagen;
                // Cargamos la URI de forma nativa
                imageView.setImageURI(Uri.parse(url.trim()));
            }
        }






        return convertView;
    }
}