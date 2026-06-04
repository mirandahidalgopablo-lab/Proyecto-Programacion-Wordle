package modelo;

public class Palabra {

    private int id;
    private String palabra;
    private int categoriaId;
    private String categoriaNombre;
    private int dificultad;
    private int longitud;

    public Palabra() {
        this.id = 0;
        this.palabra = "";
        this.categoriaId = 1;
        this.categoriaNombre = "";
        this.dificultad = 3;
        this.longitud = 5;
    }

    public Palabra(int id, String palabra, int categoriaId, String categoriaNombre, int dificultad, int longitud) {
        this.id = id;
        this.palabra = palabra;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.dificultad = dificultad;
        this.longitud = longitud;
    }

    public int getId() { return id; }
    public String getPalabra() { return palabra; }
    public int getCategoriaId() { return categoriaId; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public int getDificultad() { return dificultad; }
    public int getLongitud() { return longitud; }

    public void setId(int id) { this.id = id; }
    public void setPalabra(String palabra) { this.palabra = palabra; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
    public void setDificultad(int dificultad) { this.dificultad = dificultad; }
    public void setLongitud(int longitud) { this.longitud = longitud; }
}
