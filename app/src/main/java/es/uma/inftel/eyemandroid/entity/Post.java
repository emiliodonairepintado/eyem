package es.uma.inftel.eyemandroid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Post implements Comparable<Post>, Parcelable {

    private Long idPost;
    private String tipo;
    private String contenido;
    private String imagen;
    private String video;
    private List<String> mostradoPor;
    private Usuario creador;
    private String localizacion;


    public Post(Long idPost, String tipo, String contenido, String imagen, String video, List<String> mostradoPor, Usuario creador, String localizacion) {
        this.idPost = idPost;
        this.tipo = tipo;
        this.contenido = contenido;
        this.imagen = imagen;
        this.video = video;
        this.mostradoPor = mostradoPor;
        this.creador = creador;
        this.localizacion = localizacion;
    }

    public Post(Post post) {
        this.idPost = post.idPost;
        this.tipo = post.tipo;
        this.contenido = post.contenido;
        this.imagen = post.imagen;
        this.video = post.video;
        this.mostradoPor = new ArrayList<>(post.mostradoPor);
        this.creador = post.creador;
        this.localizacion = post.localizacion;
    }

    private Post (Parcel in) {
        mostradoPor = new ArrayList<>();
        idPost = in.readLong();
        tipo = in.readString();
        imagen = in.readString();
        contenido = in.readString();
        video = in.readString();
        in.readList(mostradoPor,null);
        localizacion = in.readString();
        creador = in.readParcelable(Usuario.class.getClassLoader());
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>(){

        @Override
        public Post createFromParcel(Parcel in){
            return new Post(in);
        }

        @Override
        public Post[] newArray (int size){
            return new Post[size];
        }

    };

    public Post(){

    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<String> getMostradoPor() {
        return mostradoPor;
    }

    public void setMostradoPor(List<String> mostradoPor) {
        this.mostradoPor = mostradoPor;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    @Override
    public int compareTo(Post o) {
        return o.getIdPost().compareTo(this.getIdPost());
    }

    @Override
         public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(idPost);
        dest.writeString(tipo);
        dest.writeString(imagen);
        dest.writeString(contenido);
        dest.writeString(video);
        dest.writeList(mostradoPor);
        dest.writeString(localizacion);
        dest.writeParcelable(creador,flags);

    }
}
