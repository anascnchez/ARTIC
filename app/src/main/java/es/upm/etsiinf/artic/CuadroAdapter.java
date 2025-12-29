package es.upm.etsiinf.artic;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CuadroAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Cuadro> cuadros;  // Lista de objetos Cuadro
    private LayoutInflater inflater; // Para inflar nuestro layout personalizado

    // Constructor para inicializar el contexto y la lista de datos
    public CuadroAdapter(Context context, ArrayList<Cuadro> cuadros) {
        this.context = context;
        this.cuadros = cuadros;
        this.inflater = LayoutInflater.from(context);
    }

    // Devuelve la cantidad de elementos en la lista
    @Override
    public int getCount() {
        return cuadros.size();
    }

    // Devuelve un ítem específico de la lista basado en su posición
    @Override
    public Object getItem(int position) {
        return cuadros.get(position);
    }

    // Devuelve el id del ítem, aquí usamos la posición como id
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Crea y devuelve una vista para cada ítem de la lista
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Verificamos si la vista reciclada está disponible; si no, inflamos una nueva
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_cuadro, parent, false);
        }

        // Obtenemos el objeto Cuadro actual basado en la posición
        Cuadro cuadro = cuadros.get( position );

        // Configuramos el nombre y la imagen del cuadro
        // Encontramos las vistas de nuestro layout personalizado
        TextView nombreTextView = convertView.findViewById(R.id.nombre_cuadro);
        nombreTextView.setText(cuadro.getTitle());

        WebView imagenWebView = convertView.findViewById(R.id.imagen_cuadro);
        System.out.println(cuadro.getImageUrl());
        String url = cuadro.getImageUrl();
        if (url != null)
        {
            url = url.trim();

            WebSettings settings = imagenWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(imagenWebView, true);

            imagenWebView.clearHistory();
            imagenWebView.loadUrl(url);
        }
        // Devolvemos la vista del ítem completamente poblada
        return convertView;
    }
}