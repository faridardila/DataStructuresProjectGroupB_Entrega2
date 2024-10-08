package com.example.datastructureproject_groupb.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.datastructureproject_groupb.Bocu;
import com.example.datastructureproject_groupb.entidades.pagina_inicio.PaginaInicio;
import com.example.datastructureproject_groupb.entidades.artista.Artista;
import com.example.datastructureproject_groupb.entidades.info_sesion.InfoSesion;
import com.example.datastructureproject_groupb.entidades.info_sesion.UsuarioRegistrado;
import com.example.datastructureproject_groupb.fragments.CuentaFragment;

public class DbSesion extends DbArt{
    Context context;

    public DbSesion(Context context) {
        super(context);
        this.context=context;
    }
    public void mantenerSesionIniciada(int tipoSesion, String correoSesion){
        InfoSesion infoSesion = new InfoSesion(0, tipoSesion, correoSesion);
        infoSesion.setTipoSesion(tipoSesion);
        infoSesion.setCorreoSesion(correoSesion);
        insertarInfoSesion(tipoSesion, correoSesion);
    }

    public void cerrarSesion(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INFO_SESION, null, null);
        db.close();
        Bocu.usuario = null;
        Bocu.estadoUsuario = Bocu.SIN_REGISTRAR;
        this.getWritableDatabase().close();
        PaginaInicio.intensionInicializacion = PaginaInicio.CUENTA;
        Intent intent = new Intent(context, PaginaInicio.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    public void insertarInfoSesion(int tipoSesion, String correoSesion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tipoSesion", tipoSesion);
        values.put("CorreoSesion", correoSesion);

        long resultado = db.insert(TABLE_INFO_SESION, null, values);



        db.close();
    }

    public boolean verificarSesionActiva(){
        DbArt dbHelper = new DbArt(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursorInfo = db.rawQuery("SELECT * FROM " + TABLE_INFO_SESION, null);
        boolean sesionActiva = false;
        if (cursorInfo.moveToFirst()) {
            InfoSesion infoSesion = new InfoSesion(0, cursorInfo.getInt(1), cursorInfo.getString(2));
            sesionActiva = (infoSesion.getTipoSesion() != 0);
        }
        cursorInfo.close();
        return sesionActiva;
    }

    public void sesionActiva(){
        String correo="";
        DbArt dbHelper = new DbArt(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursorInfo = db.rawQuery("SELECT * FROM " + TABLE_INFO_SESION, null);
        if (cursorInfo.moveToFirst()) {
            InfoSesion infoSesion = new InfoSesion(0, cursorInfo.getInt(1), cursorInfo.getString(2));
            DbExpositor dbExpositor= new DbExpositor(context);
            DbUsuariosComunes dbUsuariosComunes= new DbUsuariosComunes(context);

            correo=cursorInfo.getString(2);

            if (verificarSesionActiva()) {
                if (infoSesion.getTipoSesion() == Bocu.USUARIO_COMUN) {
                    
                    // NO OLVIDAR ACEDER MEDIANTE LA ESTRUCTURA
                    UsuarioRegistrado usuarioRegistrado = dbUsuariosComunes.verUsuario(correo);
                    Bocu.usuario = usuarioRegistrado;
                    Bocu.estadoUsuario = Bocu.USUARIO_COMUN;

                    CuentaFragment.establecerEventosFavoritos(context);

                } else {
                    
                    // NO OLVIDAR ACCEDER MEDIANTE LA ESTRUCTURA
                    Artista artista = dbExpositor.verUsuarioExpositor(correo);
                    Bocu.usuario = artista;
                    Bocu.estadoUsuario = Bocu.ARTISTA;

                    CuentaFragment.establecerEventosExpositor();

                }
            }
        }
        cursorInfo.close();
    }
    }

