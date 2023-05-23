
package persistencia;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.Afiliado;
import logica.Empresa;
import logica.Familiar;
import logica.Persona;
import logica.Titular;
import persistencia.exceptions.NonexistentEntityException;


public class ControladoraPersistencia {
    
    AfiliadoJpaController afiliadoJpa = new AfiliadoJpaController();
    EmpresaJpaController empresaJpa = new EmpresaJpaController();
    FamiliarJpaController familiarJpa = new FamiliarJpaController();
    TitularJpaController titularJpa = new TitularJpaController();
    
    
    //AFILIADOS-----------------------------------
    public void crearAfiliado(Afiliado afiliado) {
        afiliadoJpa.create(afiliado);
    }
    public Persona traerAfiliadoPorDni(String dni) {
        return afiliadoJpa.findAfiliadoDni(dni);
    }
    public List<Afiliado> traerAfiliados() {
        return afiliadoJpa.findAfiliadoEntities();
    }
    public void editarAfiliado(Afiliado afiliado) {
        try {
            afiliadoJpa.edit(afiliado);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //--------------------------------------------
    
    //EMPRESAS------------------------------------
    public void crearEmpresa(Empresa empresa) {
        empresaJpa.create(empresa);
    }
    public List<Empresa> traerEmpresas() {
        return empresaJpa.findEmpresaEntities();
    }
    public Empresa traerEmpresaPorCuit(String cuit){
        return empresaJpa.findEmpresaCuit(cuit);
    }
    public void editarEmpresa(Empresa empresa){
        try {
            empresaJpa.edit(empresa);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void eliminarEmpresa(int id) {
        try {
            empresaJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //--------------------------------------------    

    //FAMILIARES----------------------------------
    public void crearFamiliar(Familiar familiar) {
        familiarJpa.create(familiar);
    }
    public List<Familiar> traerFamiliares() {
        return familiarJpa.findFamiliarEntities();
    }  
    public void editarFamiliar(Familiar familiar) {
        try {
            familiarJpa.edit(familiar);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void eliminarFamiliar(int id) {
        try {
            familiarJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //--------------------------------------------
    
    //TITULAR-------------------------------------
    public void crearTitular(Titular titular) {
        titularJpa.create(titular);
    }
    public List<Titular> traerTitulares() {
        return titularJpa.findTitularEntities();
    }    
    public void editarTitular(Titular titular) {
        try {
            titularJpa.edit(titular);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void eliminarTitular(int id) {
        try {
            titularJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //--------------------------------------------

}