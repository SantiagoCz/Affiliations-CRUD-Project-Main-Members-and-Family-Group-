
package servlet;

import java.io.IOException;

import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.Afiliado;
import logica.Controladora;
import logica.Familiar;
import logica.Titular;
import logica.Validacion;


@WebServlet(name = "SvFamiliar", urlPatterns = {"/SvFamiliar"})
public class SvFamiliar extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        //instanciamos...
        Controladora controladora = new Controladora();

        List<Familiar> listaFamiliares = controladora.traerFamiliares();
        
        if (listaFamiliares != null && !listaFamiliares.isEmpty()) {
            HttpSession misesion = request.getSession();
            misesion.setAttribute("listaFamiliares", listaFamiliares);
            response.sendRedirect("mostrarFamiliares.jsp");
        } else {
            request.setAttribute("mensajeFamiliar", "No hay afiliados cargados en el sistema");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
        
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        // Primero vamos a verificar que el dni del titular sea de tipo titular
        String dniTitular = request.getParameter("dniTitular");

        // Instanciamos la Controladora y la clase de validación
        Controladora controladora = new Controladora();
        Validacion validar = new Validacion(controladora);

        String validarTitular = validar.validarTitular(dniTitular);

        request.setAttribute("mensajeFamiliar", "");

        if (validarTitular.equals("titular encontrado")) {

            // Ahora vamos a comprobar si ya existe un dni igual
            String dni = request.getParameter("dni");

            String mensajeValidacion = validar.validarBusqueda(dni);

            request.setAttribute("mensajeTitular", "");

            if (mensajeValidacion.equals("no encontrado")) {
                String nombre = request.getParameter("nombre");
                String apellido = request.getParameter("apellido");
                String telefono = request.getParameter("telefono");
                String email = request.getParameter("email");
                String relacion = request.getParameter("relacion");

                // --------------Inicio de Busqueda--------------
                // Buscar el objeto Titular
                Titular titularAsignado = null;
                List<Titular> listaTitulares = controladora.traerTitulares();
                for (Titular titular : listaTitulares) {
                    Afiliado afiliadoTitular = titular.getAfiliadoTitular();
                    if (afiliadoTitular != null && afiliadoTitular.getDni().equals(dniTitular)) {
                        titularAsignado = titular;
                        break;
                    }
                }
                // --------------Fin de Busqueda-------------

                // --------------Inicio del Guardado-------------
                if (titularAsignado != null) {
                    // Instanciamos los objetos
                    Familiar familiar = new Familiar();
                    Afiliado afiliado = new Afiliado();
                    Titular titular = new Titular();

                    // Asignamos los valores al objeto afiliado
                    afiliado.setDni(dni);
                    afiliado.setNombre(nombre);
                    afiliado.setApellido(apellido);
                    afiliado.setTelefono(telefono);
                    afiliado.setEmail(email);
                    afiliado.setTipoAfiliado("familiar");
                    afiliado.setStatus("activo");
                    // Guardamos el afiliado en la base de datos
                    controladora.crearAfiliado(afiliado);
                    // Asignamos los datos a familiar
                    familiar.setRelacion(relacion);
                    familiar.setAfiliadoFamiliar(afiliado);
                    familiar.setTitular(titularAsignado);
                    // Guardamos el familiar en la base de datos
                    controladora.crearFamiliar(familiar);

                    // Redirigimos a la pagina principal
                    request.setAttribute("mensajeFamiliar", "Afiliado registrado con éxito");
                    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                    rd.forward(request, response);
                } else {
                    request.setAttribute("mensajeFamiliar", mensajeValidacion);
                    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                    rd.forward(request, response);
                }
            } else {
                request.setAttribute("mensajeFamiliar", mensajeValidacion);
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
            }
        } else {
            request.setAttribute("mensajeFamiliar", validarTitular);
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
