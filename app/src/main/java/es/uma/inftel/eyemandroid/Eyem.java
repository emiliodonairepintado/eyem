package es.uma.inftel.eyemandroid;

/**
 * Created by miguel on 19/03/15.
 */
public class Eyem {

    private static final String SCHEME = "http://";
     //192.168.183.98:8080 fac
    //192.168.1.66:8080 casa
    public static final String AUTHORITY = "192.168.183.98:8080/socialEyem-web/webresources";

    public static final String USUARIO_PATH = "/usuario";

    public static final String CREARGRUPO_PATH = "/grupo/crearGrupo/";

    public static final String GRUPO_PATH = "/grupo";

    public static final String CREAR_GRUPO_REST_URI = SCHEME + AUTHORITY + CREARGRUPO_PATH;

    public static final String USUARIO_EMAIL_PATH = USUARIO_PATH + "/email";

    public static final String POST_PATH = "/post";

    public static final String USUARIO_REST_URI = SCHEME + AUTHORITY + USUARIO_PATH;

    public static final String CREARPOST_REST_URI = SCHEME + AUTHORITY + POST_PATH;

    public static final String FINDALLPOST_REST_URI = SCHEME + AUTHORITY + POST_PATH;

    public static final String FIND_USER_POSTS_REST_URI = SCHEME + AUTHORITY + POST_PATH + "/email";

    public static final String FIND_USER_REPLICATED_POSTS_REST_URI = SCHEME + AUTHORITY + POST_PATH + "/replicados";

    public static final String REPLICAR_POST_REST_URI = SCHEME + AUTHORITY + POST_PATH + "/replicar";

    public static final String USUARIO_EMAIL_REST_URI = SCHEME + AUTHORITY + USUARIO_EMAIL_PATH;

    public static final String BORRARPOST_REST_URI = SCHEME + AUTHORITY + POST_PATH + "/borrar";

    public static final String BORRARGRUPO_REST_URI = SCHEME + AUTHORITY + GRUPO_PATH + "/eliminarGrupo";

    public static final String FIND_USER_GROUPS = SCHEME + AUTHORITY + GRUPO_PATH + "/email";

    public static final String USUARIO_NAME_PATH= "/nombre";

    public static final String Usuario_REST_NAME_URI = SCHEME + AUTHORITY + USUARIO_PATH + USUARIO_NAME_PATH;

    public static final String FIND_POST_GROUP_REST_URI = SCHEME + AUTHORITY + POST_PATH + "/idGrupo";

}
