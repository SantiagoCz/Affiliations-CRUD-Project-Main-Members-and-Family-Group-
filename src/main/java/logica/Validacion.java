package logica;

import java.util.List;

public class Validacion {
    
    Controladora controladora;
   
    public Validacion(Controladora controladora) {
        this.controladora = controladora;
    }
    //ESTE METODO SE UTILIZARIA EN CASO DE TENER QUE LOGUEARSE
    /*
    public String validarUsuario(String dni, String contrasenia) {
        String mensaje = null;

        if (dni == null || dni.trim().isEmpty() || contrasenia == null || contrasenia.trim().isEmpty()) {
            mensaje = "Ingrese el DNI y la contraseña";
        } else {
            Persona persona = controladora.traerUsuarioPorDni(dni);
            Usuario usuario = (Usuario) persona;
            if (persona == null) {
                mensaje = "Usuario no encontrado";
            } else if (!usuario.getContrasenia().equals(contrasenia)) {
                mensaje = "Contraseña incorrecta";
            } else {
                mensaje = "Inicio de sesión exitoso";
            }
        }
        return mensaje;
    }*/

    // METODO DE VALIDACION POR BUSQUEDA DE DNI
    public String validarBusqueda(String dni) {
        String mensaje;
        // trim().isEmpty() verifica si una cadena está vacía o si solo contiene espacios en blanco   
        if (dni == null || dni.trim().isEmpty()) {
            mensaje = "Ingrese el DNI";
        } else {
            Persona persona = controladora.traerAfiliadoPorDni(dni);
            if (persona == null) {
                mensaje = "no encontrado";
            } else {
                mensaje = "encontrado";
            }
        }
        return mensaje;
    }
    
    // METODO DE VALIDACION DE TITULAR POR BUSQUEDA DE DNI
    public String validarTitular(String dni) {
        String mensaje;
        if (dni == null || dni.trim().isEmpty()) {
            mensaje = "Ingrese el DNI del Titular";
        } else {
            Persona persona = controladora.traerAfiliadoPorDni(dni);
            if (persona == null) {
                mensaje = "DNI no registrado";
            } else {
                List<Titular> listaTitulares = controladora.traerTitulares();
                boolean titularEncontrado = false;

                for (Titular titular : listaTitulares) {
                    Afiliado afiliadoTitular = titular.getAfiliadoTitular();

                    if (afiliadoTitular != null && afiliadoTitular.getDni() != null && afiliadoTitular.getDni().equals(dni)) {
                        titularEncontrado = true;
                        break;
                    }
                }
                if (titularEncontrado) {
                    mensaje = "titular encontrado";
                } else {
                    mensaje = "El DNI ingresado no corresponde al de un titular";
                }
            }
        }

        return mensaje;
    }

    // METODO DE VALIDACION POR BUSQUEDA DE CUIT
    public String validarCuit(String cuit){
        String mensaje;
        // trim().isEmpty() verifica si una cadena está vacía o si solo contiene espacios en blanco   
        if (cuit == null || cuit.trim().isEmpty()) {
            mensaje = "Ingrese el CUIT";
        } else {
            List<Empresa> listaEmpresas = controladora.traerEmpresas();
            for (Empresa empresa : listaEmpresas) {
                if (empresa.getCuit().equalsIgnoreCase(cuit)) {
                    mensaje = "empresa encontrado";
                    return mensaje;
                }
            }
        }
        mensaje = "cuit no encontrado";
        return mensaje;
    }
}

