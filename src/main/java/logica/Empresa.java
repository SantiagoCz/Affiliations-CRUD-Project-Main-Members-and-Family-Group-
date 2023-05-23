package logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "cuit", length = 11)
    private String cuit;

    //RELACION (1-N) CON TITULAR
    @OneToMany(mappedBy = "empresa")
    private List<Titular> titualares;

    public Empresa() {
    }

    public Empresa(int id, String nombre, String cuit, List<Titular> titualares) {
        this.id = id;
        this.nombre = nombre;
        this.cuit = cuit;
        this.titualares = titualares;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public List<Titular> getTitualares() {
        return titualares;
    }

    public void setTitualares(List<Titular> titualares) {
        this.titualares = titualares;
    }
    
}
