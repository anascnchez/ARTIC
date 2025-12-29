package es.upm.etsiinf.artic.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.io.InputStream;
import java.util.ArrayList;

import es.upm.etsiinf.artic.Cuadro;
import es.upm.etsiinf.artic.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;

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

        ImageView imagenImageView = convertView.findViewById(R.id.imagen_cuadro);
        System.out.println(cuadro.getImageUrl());
        String url = cuadro.getImageUrl();
        if (url != null) {
            GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                    .build());

            Glide.with(context)
                    .load(glideUrl)
                    .into(imagenImageView);
        }
        // Devolvemos la vista del ítem completamente poblada
        return convertView;
    }
}