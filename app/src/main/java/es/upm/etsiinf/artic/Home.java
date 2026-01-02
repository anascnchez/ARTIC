package es.upm.etsiinf.artic;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.util.ArrayList;

public class Home extends Fragment
{
    /*
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2"; */
    private ListView listViewCuadros;
    private ProgressBar progressBar;
    private CuadroAdapter adapter;
    private ArrayList<Cuadro> cuadros;
    /*
    private String mParam1;
    private String mParam2;
     */

    public Home()
    {
    }

    /*
    public static Home newInstance(String param1, String param2)
    {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    } */

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        // Inicializamos lista y adapter
        cuadros = new ArrayList<Cuadro>();
        adapter = new CuadroAdapter( this.getParentFragmentManager(), getActivity(), cuadros, R.layout.item_cuadro_home );
        /*
        if ( getArguments() != null ) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }*/
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.fragment_home, container, false );

        // Inicializamos vistas usando la vista raíz
        listViewCuadros = view.findViewById( R.id.main_list_cuadros );
        progressBar = view.findViewById( R.id.progressBar );
        listViewCuadros.setAdapter( adapter );
        return view;
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated( view, savedInstanceState );
        startDownload();
    }

    void startDownload()
    {
        progressBar.setVisibility( View.VISIBLE );
        DescargaCuadrosThread descarga = new DescargaCuadrosThread(this );
        new Thread( descarga ).start();
    }

    void finishDownload( Data cdrs )
    {
        progressBar.setVisibility( View.GONE );
        if ( cdrs != null )
        {
            cuadros.clear();
            cuadros.addAll( cdrs.getCuadros() );
            adapter.notifyDataSetChanged(); // Para que se refresque la ListView
        }
    }
}