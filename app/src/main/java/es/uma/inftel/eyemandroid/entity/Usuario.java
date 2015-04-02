package es.uma.inftel.eyemandroid.entity;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {

    private String email;
    private String nombre;
    private String imagen;
    private String imagenCover;
    private String pass;

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>(){

        @Override
        public Usuario createFromParcel(Parcel in){
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray (int size){
            return new Usuario[size];
        }

    };

    private Usuario (Parcel in) {
        email = in.readString();
        nombre = in.readString();
        imagen = in.readString();
        imagenCover = in.readString();
        pass = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(email);
        out.writeString(nombre);
        out.writeString(imagen);
        out.writeString(imagenCover);
        out.writeString(pass);
    }

    public Usuario() {
    }

    public Usuario(String email, String imagen, String nombre,String pass, String imagenCover) {
        this.email = email;
        this.imagen = imagen;
        this.nombre = nombre;
        this.pass = pass;
        this.imagenCover=imagenCover;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagenCover() {
        return imagenCover;
    }

    public void setImagenCover(String imagenCover) {
        this.imagenCover = imagenCover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;
        return email.equals(usuario.email);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        return result;
    }
}
