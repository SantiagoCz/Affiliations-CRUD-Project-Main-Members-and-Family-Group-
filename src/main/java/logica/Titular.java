
package logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "titular")
public class Titular implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_titular")
    private int id;
    
    //RELACION (N-1) CON AFILIADO    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_afiliado")
    private Afiliado afiliadoTitular;
    
    //RELACION (N-1) CON EMPRESA    
    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
    
    //RELACION (1-N) CON FAMILIAR
    @OneToMany(mappedBy = "titular")
    private List<Familiar> familiarTitular;

    public Titular() {
    }

    public Titular(int id, Afiliado afiliadoTitular, Empresa empresa, List<Familiar> familiarTitular) {
        this.id = id;
        this.afiliadoTitular = afiliadoTitular;
        this.empresa = empresa;
        this.familiarTitular = familiarTitular;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Afiliado getAfiliadoTitular() {
        return afiliadoTitular;
    }

    public void setAfiliadoTitular(Afiliado afiliadoTitular) {
        this.afiliadoTitular = afiliadoTitular;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Familiar> getFamiliarTitular() {
        return familiarTitular;
    }

    public void setFamiliarTitular(List<Familiar> familiarTitular) {
        this.familiarTitular = familiarTitular;
    }

}