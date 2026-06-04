package modelo;

public class Usuario {

    private int id;
    private String nombre;
    private int victorias;
    private int partidasJugadas;
    private int rachaActual;
    private int mejorRacha;
    private String fechaRegistro;

    public Usuario() {
        this.id = 0;
        this.nombre = "";
        this.victorias = 0;
        this.partidasJugadas = 0;
        this.rachaActual = 0;
        this.mejorRacha = 0;
        this.fechaRegistro = "";
    }

    public Usuario(int id, String nombre, int victorias, int partidasJugadas, int rachaActual, int mejorRacha, String fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.victorias = victorias;
        this.partidasJugadas = partidasJugadas;
        this.rachaActual = rachaActual;
        this.mejorRacha = mejorRacha;
        this.fechaRegistro = fechaRegistro;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getVictorias() { return victorias; }
    public int getPartidasJugadas() { return partidasJugadas; }
    public int getRachaActual() { return rachaActual; }
    public int getMejorRacha() { return mejorRacha; }
    public String getFechaRegistro() { return fechaRegistro; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setVictorias(int victorias) { this.victorias = victorias; }
    public void setPartidasJugadas(int partidasJugadas) { this.partidasJugadas = partidasJugadas; }
    public void setRachaActual(int rachaActual) { this.rachaActual = rachaActual; }
    public void setMejorRacha(int mejorRacha) { this.mejorRacha = mejorRacha; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
