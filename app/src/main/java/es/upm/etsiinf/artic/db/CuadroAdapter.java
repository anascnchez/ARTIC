package es.upm.etsiinf.artic.db;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import es.upm.etsiinf.artic.Cuadro;
import es.upm.etsiinf.artic.R;

public class CuadroAdapter extends BaseAdapter {

    private Context context;
    private List<Cuadro> cuadros;  // Lista de objetos Cuadro
    private LayoutInflater inflater; // Para inflar nuestro layout personalizado

    // Constructor para inicializar el contexto y la lista de datos
    public CuadroAdapter(Context context, List<Cuadro> cuadros) {
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

        // Obtenemos el objeto Planeta actual basado en la posición
        Cuadro cuadro = cuadros.get(position);

        // Configuramos el nombre y la imagen del cuadro
        // Encontramos las vistas de nuestro layout personalizado
        TextView nombreTextView = convertView.findViewById(R.id.nombre_cuadro);
        nombreTextView.setText(cuadro.getTitle());

        ImageView imagenImageView = convertView.findViewById(R.id.imagen_cuadro);

        Uri uri = Uri.parse(cuadro.getImage());
        Glide.with(context)
                .load(cuadro.getImage()) // URL o Uri
                .into(imagenImageView);

        // Devolvemos la vista del ítem completamente poblada
        return convertView;
    }}