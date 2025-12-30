package es.upm.etsiinf.artic;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.artic.db.CuadroRepository;

public class Profile extends Fragment {

    private CuadroRepository repository;
    private ListView listViewProfile;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new CuadroRepository(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewProfile = view.findViewById(R.id.profile_listview);

        repository.open();
        List<Cuadro> cuadrosFavoritos = repository.getTodosLosCuadros();
        repository.close();

        // Depuración: Vamos a ver qué URLs se están generando
        for (Cuadro c : cuadrosFavoritos) {
            Log.d("DEBUG_ARTIC", "Título: " + c.getTitle());
            Log.d("DEBUG_ARTIC", "Image ID: " + c.getImageId());
            Log.d("DEBUG_ARTIC", "URL Generada: " + c.getImageUrl());
        }

        // Creamos el adaptador con la lista obtenida
        CuadroAdapter adapter = new CuadroAdapter(getContext(), (ArrayList<Cuadro>) cuadrosFavoritos);
        listViewProfile.setAdapter(adapter);
    }
}
