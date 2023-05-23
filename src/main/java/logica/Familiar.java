
package logica;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "familiar")
public class Familiar implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_familiar")
    private int id;
    
    @Column(name = "relacion")
    private String relacion;
    
    //RELACION (N-1) CON AFILIADO    
    @ManyToOne
    @JoinColumn(name = "id_afiliado", referencedColumnName = "id")
    private Afiliado afiliadoFamiliar;
    
    //RELACION (N-1) CON TITULAR    
    @ManyToOne
    @JoinColumn(name = "id_titular")
    private Titular titular;

    public Familiar() {
    }

    public Familiar(int id, String relacion, Afiliado afiliadoFamiliar, Titular titular) {
        this.id = id;
        this.relacion = relacion;
        this.afiliadoFamiliar = afiliadoFamiliar;
        this.titular = titular;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelacion() {
        return relacion;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    public Afiliado getAfiliadoFamiliar() {
        return afiliadoFamiliar;
    }

    public void setAfiliadoFamiliar(Afiliado afiliadoFamiliar) {
        this.afiliadoFamiliar = afiliadoFamiliar;
    }

    public Titular getTitular() {
        return titular;
    }

    public void setTitular(Titular titular) {
        this.titular = titular;
    }

}