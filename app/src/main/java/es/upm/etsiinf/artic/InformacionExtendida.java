package es.upm.etsiinf.artic;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class InformacionExtendida extends DialogFragment
{
    private static final String ARG_ID = "id";
    private String mId;
    private WebView webViewImagen;
    private TextView textViewTitulo;
    private TextView textViewDescripcion;
    private View view;
    private ProgressBar progressBar;

    public InformacionExtendida()
    {
    }

    public static InformacionExtendida newInstance( String id )
    {
        InformacionExtendida fragment = new InformacionExtendida();
        Bundle args = new Bundle();
        args.putString( ARG_ID, id );
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null )
        {
            mId = getArguments().getString( ARG_ID );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState )
    {
        this.view = inflater.inflate( R.layout.fragment_informacion, container, false );
        // Inicializamos vistas usando la vista raíz
        progressBar = this.view.findViewById( R.id.progressBar2 );
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated( view, savedInstanceState );
        startDownload();
    }

    void startDownload()
    {
        progressBar.setVisibility( View.VISIBLE );
        DescargaInformacionCuadro descarga = new DescargaInformacionCuadro(this, mId );
        new Thread( descarga ).start();
    }
    void finishDownload( DataCuadro cdr )
    {
        progressBar.setVisibility( View.GONE );
        Cuadro cuadro = cdr.getCuadro();

        textViewTitulo = view.findViewById( R.id.titulo_cuadro );
        if ( textViewTitulo != null )
        {
            textViewTitulo.setText( cuadro.getTitle() );
        }

        textViewDescripcion = view.findViewById( R.id.descripcion_cuadro );
        if ( textViewDescripcion != null )
        {
            textViewDescripcion.setText( cuadro.getDescription() );
        }

        View vistaImagen = view.findViewById( R.id.imagen_cuadro_info );
        String url = cuadro.getImageUrl();

        if ( url != null && vistaImagen != null )
        {
            if (vistaImagen instanceof WebView)
            {
                WebView webView = (WebView) vistaImagen;
                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
                {
                    settings.setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
                }
                webView.loadUrl( url.trim() );
            } else if ( vistaImagen instanceof ImageView) {
                ImageView imageView = ( ImageView ) vistaImagen;
                // Cargamos la URI de forma nativa
                imageView.setImageURI( Uri.parse( url.trim() ) );
            }
        }
    }
}
