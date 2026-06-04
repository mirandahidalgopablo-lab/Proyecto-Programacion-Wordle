package modelo;

public class Partida {

    private int id;
    private int usuarioId;
    private String palabraTexto;
    private int longitud;
    private int intentos;
    private boolean victoria;
    private int tiempoSegundos;
    private String fecha;
    private String nombreUsuario;

    public Partida() { }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public String getPalabraTexto() { return palabraTexto; }
    public int getLongitud() { return longitud; }
    public int getIntentos() { return intentos; }
    public boolean isVictoria() { return victoria; }
    public int getTiempoSegundos() { return tiempoSegundos; }
    public String getFecha() { return fecha; }
    public String getNombreUsuario() { return nombreUsuario; }

    public void setId(int id) { this.id = id; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setPalabraTexto(String palabraTexto) { this.palabraTexto = palabraTexto; }
    public void setLongitud(int longitud) { this.longitud = longitud; }
    public void setIntentos(int intentos) { this.intentos = intentos; }
    public void setVictoria(boolean victoria) { this.victoria = victoria; }
    public void setTiempoSegundos(int tiempoSegundos) { this.tiempoSegundos = tiempoSegundos; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}
