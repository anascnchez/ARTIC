package es.upm.etsiinf.artic;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.artic.db.CuadroAdapter;

public class MainActivity extends AppCompatActivity {
    private ListView listViewCuadros;
    private ProgressBar progressBar;
    private CuadroAdapter adapter;
    private List<Cuadro> listaCuadros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Lista de Cuadros
        listViewCuadros = findViewById(R.id.main_list_cuadros);
        listaCuadros = new ArrayList<>();
        // Crea el adapter usando el layout simple de Android
        adapter = new CuadroAdapter(
                this, // Context
                listaCuadros // Lista de datos
        );
        // Asocia el adapter al ListView
        listViewCuadros.setAdapter(adapter);
        // Barra de progreso
        progressBar = findViewById(R.id.progressBar);

        //Barra de navegación inferior
        BottomNavigationView bottomNav = findViewById(R.id.navigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNav, navController);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void startDownload()
    {
        progressBar.setVisibility(View.VISIBLE);
    }
    void finishDownload(ArrayList<Cuadro> result)
    {
        progressBar.setVisibility(View.GONE);
    }
}