package com.example.datastructureproject_groupb.entidades.pagina_inicio;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.datastructureproject_groupb.Bocu;
import com.example.datastructureproject_groupb.R;
import com.example.datastructureproject_groupb.fragments.CuentaFragment;
import com.example.datastructureproject_groupb.fragments.DescubrirFragment;
import com.example.datastructureproject_groupb.fragments.EventosFragment;
import com.example.datastructureproject_groupb.fragments.PaginaPrincipalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PaginaInicio extends AppCompatActivity{

    public static final int PAGINA_PRINCIPAL = 0;
    public static final int DESCUBRIR = 1;
    public static final int EVENTOS = 2;
    public static final int CUENTA = 3;
    public static int intensionInicializacion = PAGINA_PRINCIPAL;
    private BottomNavigationView menu;
    private Fragment fragmento;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicio);

        initView();
        initValues();
        initListener();

        switch (intensionInicializacion) {
            case PAGINA_PRINCIPAL:
                menu.setSelectedItemId(R.id.menuPaginaPrincipal);
                fragmento = PaginaPrincipalFragment.newInstance();
                openFragment(fragmento);
                break;
            case DESCUBRIR:
                menu.setSelectedItemId(R.id.menuDescubrir);
                fragmento = DescubrirFragment.newInstance();
                openFragment(fragmento);
                break;
            case EVENTOS:
                menu.setSelectedItemId(R.id.menuEventos);
                fragmento = EventosFragment.newInstance();
                openFragment(fragmento);
                break;
            case CUENTA:
                menu.setSelectedItemId(R.id.menuCuenta);
                fragmento = CuentaFragment.newInstance();
                openFragment(fragmento);
                break;
        }

        if(Bocu.estadoUsuario == Bocu.SIN_REGISTRAR)
            menu.getMenu().removeItem(R.id.menuEventos);

        if(Bocu.estadoUsuario == Bocu.USUARIO_COMUN)
            menu.getMenu().findItem(R.id.menuEventos).setIcon(getDrawable(R.drawable.icono_no_favorito_boton));

        if(Bocu.estadoUsuario == Bocu.ARTISTA)
            menu.getMenu().findItem(R.id.menuEventos).setIcon(getDrawable(R.drawable.icono_campana));
    }

    private void initView(){
        menu = findViewById(R.id.menu);
    }

    private void initValues(){
        manager = getSupportFragmentManager();
    }

    private void initListener(){
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int idMenu = menuItem.getItemId();

                switch (idMenu) {
                    case R.id.menuPaginaPrincipal:
                        fragmento = PaginaPrincipalFragment.newInstance();
                        openFragment(fragmento);
                        return true;
                    case R.id.menuDescubrir:
                        fragmento = DescubrirFragment.newInstance();
                        openFragment(fragmento);
                        return true;
                    case R.id.menuEventos:
                        fragmento = EventosFragment.newInstance();
                        openFragment(fragmento);
                        return true;
                    case R.id.menuCuenta:
                        fragmento = CuentaFragment.newInstance();
                        openFragment(fragmento);
                        return true;
                }

                return false;
            }
        });
    }

    private void openFragment(Fragment fragmento){
        manager.beginTransaction()
                .replace(R.id.frameContenedor, fragmento)
                .commit();
    }

    public BottomNavigationView getMenu(){
        return menu;
    }

}