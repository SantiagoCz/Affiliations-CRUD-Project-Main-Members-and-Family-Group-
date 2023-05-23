package logica;
import java.util.List;
import persistencia.ControladoraPersistencia;

public class Controladora {
    
    ControladoraPersistencia controlPersistencia = new ControladoraPersistencia();

    //AFILIADOS----------------------------------
    public void crearAfiliado(Afiliado afiliado) {
        controlPersistencia.crearAfiliado(afiliado);
    }
    public Persona traerAfiliadoPorDni(String dni) {
        return controlPersistencia.traerAfiliadoPorDni(dni);
    }
    public List<Afiliado> traerAfiliados() {
        return controlPersistencia.traerAfiliados();
    }
    public void editarAfiliado(Afiliado afiliado) {
        controlPersistencia.editarAfiliado(afiliado);
    }
    //-------------------------------------------
    
    //EMPRESAS-----------------------------------
    public void crearEmpresa(Empresa empresa) {
        controlPersistencia.crearEmpresa(empresa);
    }
    public List<Empresa> traerEmpresas() {
        return controlPersistencia.traerEmpresas();
    }
    public Empresa traerEmpresaPorCuit(String cuit) {
        return controlPersistencia.traerEmpresaPorCuit(cuit);
    }
    public void editarEmpresa(Empresa empresa){
        controlPersistencia.editarEmpresa(empresa);
    }
    public void eliminarEmpresa(int id) {
        controlPersistencia.eliminarEmpresa(id);
    }
    //-------------------------------------------
    
    //FAMILIARES---------------------------------
    public void crearFamiliar(Familiar familiar) {
        controlPersistencia.crearFamiliar(familiar);
    }
    public List<Familiar> traerFamiliares() {
        return controlPersistencia.traerFamiliares();
    }
    public void editarFamiliar(Familiar familiar) {
        controlPersistencia.editarFamiliar(familiar);
    }
    public void eliminarFamiliar(int id) {
        controlPersistencia.eliminarFamiliar(id);
    }
    //-------------------------------------------
    
    //TITULAR------------------------------------
    public void crearTitular(Titular titular) {
        controlPersistencia.crearTitular(titular);
    }
    public List<Titular> traerTitulares(){
        return controlPersistencia.traerTitulares();
    }
    public void editarTitular(Titular titular) {
        controlPersistencia.editarTitular(titular);
    }
    public void eliminarTitular(int id) {
        controlPersistencia.eliminarTitular(id);
    }
    //-------------------------------------------

    //USUARIOS-----------------------------------
    public Persona traerUsuarioPorDni(String dni) {
        return controlPersistencia.traerAfiliadoPorDni(dni);
    }
    //-------------------------------------------

}