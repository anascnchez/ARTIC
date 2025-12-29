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

import es.upm.etsiinf.artic.db.CuadroAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView listViewCuadros;
    private ProgressBar progressBar;
    private CuadroAdapter adapter;
    private ArrayList<Cuadro> cuadros;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializamos lista y adapter
        cuadros = new ArrayList<Cuadro>();
        adapter = new CuadroAdapter(getActivity(), cuadros); // Context del Activity
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializamos vistas usando la vista raíz
        listViewCuadros = view.findViewById(R.id.main_list_cuadros);
        progressBar = view.findViewById(R.id.progressBar);
        listViewCuadros.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startDownload();
    }

    void startDownload()
    {
        progressBar.setVisibility(View.VISIBLE);
        DescargaCuadrosThread descarga = new DescargaCuadrosThread(this);
        new Thread(new DescargaCuadrosThread(this)).start();
    }

    void finishDownload(Data result)
    {
        progressBar.setVisibility(View.GONE);
        if (result != null) {
            cuadros.clear();
            cuadros.addAll(result.getCuadros());
            adapter.notifyDataSetChanged(); // Para que se refresque la ListView
        }
    }
}