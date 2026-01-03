package es.upm.etsiinf.artic;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class Home extends Fragment {

    private ListView listViewCuadros;
    private ProgressBar progressBar;
    private FrameLayout emptyContainer;

    private CuadroAdapter adapter;
    private ArrayList<Cuadro> cuadros;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializamos datos y adapter
        cuadros = new ArrayList<>();
        adapter = new CuadroAdapter(
                getParentFragmentManager(),
                getActivity(),
                cuadros,
                R.layout.item_cuadro_home
        );
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Referencias a vistas
        listViewCuadros = view.findViewById(R.id.main_list_cuadros);
        progressBar = view.findViewById(R.id.progressBar);
        emptyContainer = view.findViewById(R.id.empty_message_container);

        listViewCuadros.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        actualizarEstadoVista();

        // Si hay internet, empezamos descarga
        if (isConnectedToInternet()) {
            startDownload();
        }
    }

    void startDownload() {
        progressBar.setVisibility(View.VISIBLE);
        DescargaCuadrosThread descarga = new DescargaCuadrosThread(this);
        new Thread(descarga).start();
    }

    void finishDownload(Data cdrs) {
        progressBar.setVisibility(View.GONE);

        if (cdrs != null && cdrs.getCuadros() != null) {
            cuadros.clear();
            cuadros.addAll(cdrs.getCuadros());
            adapter.notifyDataSetChanged();
        }
        actualizarEstadoVista();
    }

    private void actualizarEstadoVista() {
        if (cuadros == null || cuadros.isEmpty()) {
            emptyContainer.setVisibility(View.VISIBLE);
            listViewCuadros.setVisibility(View.GONE);
        } else {
            emptyContainer.setVisibility(View.GONE);
            listViewCuadros.setVisibility(View.VISIBLE);
        }
    }

    //verifica q el telefono este conectado a internet
    private boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) requireContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}
