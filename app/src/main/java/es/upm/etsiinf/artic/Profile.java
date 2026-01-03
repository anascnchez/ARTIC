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

        CuadroAdapter adapter = new CuadroAdapter(
                null,
                getContext(),
                new ArrayList<>(cuadrosFavoritos),
                R.layout.item_cuadro_profile
        );

        listViewProfile.setAdapter(adapter);

        listViewProfile.setOnItemClickListener((parent, view1, position, id) -> {

            Cuadro cuadro = (Cuadro) parent.getItemAtPosition(position);

            Bundle bundle = new Bundle();
            bundle.putString("image_url", cuadro.getImageUrl());
            bundle.putString("title", cuadro.getTitle());

            FullImageFragment fragment = new FullImageFragment();
            fragment.setArguments(bundle);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
