package es.upm.etsiinf.artic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class CuadroAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Cuadro> cuadros;
    private Cuadro cuadroSeleccionado;
    private LayoutInflater inflater;
    private int layoutId;

    public CuadroAdapter(FragmentManager fragmentManager, Context context, ArrayList<Cuadro> cuadros, int layoutId) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.cuadros = cuadros;
        this.layoutId = layoutId;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cuadros.size();
    }

    @Override
    public Object getItem(int position) {
        return cuadros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void abrirDetalle(Cuadro cuadroSeleccionado) {
        InformacionExtendida dialogFragment = InformacionExtendida.newInstance(cuadroSeleccionado.getId());
        dialogFragment.show(fragmentManager, "cuadro_seleccionado");
    }

    private void compartirCuadro(Cuadro cuadro) {
        String url = cuadro.getImageUrl();
        if (url == null || url.isEmpty()) {
            Toast.makeText(context, "No link to share", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share picture");
        context.startActivity(shareIntent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false);
        }

        cuadroSeleccionado = cuadros.get(position);
        TextView nombreTextView = convertView.findViewById(R.id.nombre_cuadro);
        if (nombreTextView != null) {
            nombreTextView.setText(cuadroSeleccionado.getTitle());
        }

        View vistaImagen = convertView.findViewById(R.id.imagen_cuadro);
        if (vistaImagen == null) return convertView;

        String url = cuadroSeleccionado.getImageUrl();
        if (url == null) return convertView;

        url = url.trim();

        if (vistaImagen instanceof WebView) {
            WebView webView = (WebView) vistaImagen;
            webView.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    abrirDetalle(cuadroSeleccionado);
                }
                return false;
            });
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webView.loadUrl(url);
        } else if (vistaImagen instanceof ImageView) {
            ImageView imageView = (ImageView) vistaImagen;
            imageView.setImageDrawable(null);

            // Distingue uso de lista en local y de la api
            if (url.startsWith("content://") || url.startsWith("file://")) {
                imageView.setImageURI(Uri.parse(url));
            } else if (url.startsWith("http")) {
                WebView webView = new WebView(context);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);
            }
        }

        ImageButton btnShare = convertView.findViewById(R.id.btn_share);
        if(btnShare != null){
            btnShare.setOnClickListener(v -> {
                compartirCuadro(cuadroSeleccionado);
            });
        }


        return convertView;
    }
}
