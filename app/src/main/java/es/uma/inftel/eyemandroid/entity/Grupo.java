package es.uma.inftel.eyemandroid.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Grupo implements Comparable<Grupo>, Parcelable{

    private String id;
    private Long idGrupo;
    private String nombreGrupo;
    private Usuario creador;
    private List<Usuario> listaUsuarios;
    private String imagenMiniatura;
    private String imagen;

    public static final Parcelable.Creator<Grupo> CREATOR = new Parcelable.Creator<Grupo>(){

        @Override
        public Grupo createFromParcel(Parcel in){
            return new Grupo(in);
        }

        @Override
        public Grupo[] newArray (int size){
            return new Grupo[size];
        }

    };

    public Grupo(Long idGrupo, String nombreGrupo, Usuario creador, List<Usuario> listaUsuarios, String imagenMiniatura, String imagen) {
        this.idGrupo = idGrupo;
        this.nombreGrupo = nombreGrupo;
        this.creador = creador;
        this.listaUsuarios = listaUsuarios;
        this.imagen = imagen;
        this.imagenMiniatura = imagenMiniatura;
    }

    public Grupo(){

    }

    public Grupo(Parcel in){
        this.idGrupo = in.readLong();
        this.nombreGrupo = in.readString();
        this.creador = in.readParcelable(Usuario.class.getClassLoader());
        this.listaUsuarios = in.readArrayList(Usuario.class.getClassLoader());
        this.imagen = in.readString();
        this.imagenMiniatura = in.readString();
    }

    public String getImagenMiniatura() {
        return imagenMiniatura;
    }

    public void setImagenMiniatura(String imagenMiniatura) {
        this.imagenMiniatura = imagenMiniatura;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Grupo other = (Grupo) obj;
        if (!this.idGrupo.equals(other.idGrupo)) {
            return false;
        }
        if (!this.nombreGrupo.equals(other.nombreGrupo)) {
            return false;
        }
        if (!this.creador.equals(other.creador)) {
            return false;
        }
        if (!this.listaUsuarios.equals(other.listaUsuarios)) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idGrupo);
        dest.writeString(nombreGrupo);
        dest.writeParcelable(creador,flags);
        dest.writeList(listaUsuarios);
        dest.writeString(imagen);
        dest.writeString(imagenMiniatura);
    }

    @Override
    public int compareTo(Grupo another) {
        return 0;
    }
}
