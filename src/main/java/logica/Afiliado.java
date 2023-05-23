
package logica;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "afiliado")
public class Afiliado extends Persona {
    
    @Column(name = "tipo_afiliado")
    private String tipoAfiliado;
    
    @Column(name = "status")
    private String status;
    
    //RELACION BIDIRECCIONAL (1-n) CON TITULAR
    @OneToMany(mappedBy = "afiliadoTitular")
    private List<Titular> titulares;
    
    //RELACION BIDIRECCIONAL (1-n) CON FAMILIAR
    @OneToMany(mappedBy = "afiliadoFamiliar")
    private List<Familiar> familiares;
    
    public Afiliado() {
    }

    public Afiliado(String tipoAfiliado, String status, List<Titular> titulares, List<Familiar> familiares, int id, String dni, String nombre, String apellido, String telefono, String email) {
        super(id, dni, nombre, apellido, telefono, email);
        this.tipoAfiliado = tipoAfiliado;
        this.status = status;
        this.titulares = titulares;
        this.familiares = familiares;
    }
    
    public String getTipoAfiliado() {
        return tipoAfiliado;
    }

    public void setTipoAfiliado(String tipoAfiliado) {
        this.tipoAfiliado = tipoAfiliado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Titular> getTitulares() {
        return titulares;
    }

    public void setTitulares(List<Titular> titulares) {
        this.titulares = titulares;
    }

    public List<Familiar> getFamiliares() {
        return familiares;
    }

    public void setFamiliares(List<Familiar> familiares) {
        this.familiares = familiares;
    }

}