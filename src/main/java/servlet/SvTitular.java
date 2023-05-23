
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
import logica.Empresa;
import logica.Familiar;
import logica.Titular;
import logica.Validacion;


@WebServlet(name = "SvTitular", urlPatterns = {"/SvTitular"})
public class SvTitular extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
        //instanciamos...
        Controladora controladora = new Controladora();

        List<Titular> listaTitulares = controladora.traerTitulares();

        if (listaTitulares != null && !listaTitulares.isEmpty()) {
            HttpSession misesion = request.getSession();
            misesion.setAttribute("listaTitulares", listaTitulares);
            response.sendRedirect("mostrarTitulares.jsp");
        } else {
            request.setAttribute("mensajeTitular", "No hay afiliados cargados en el sistema");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        //Primero vamos a verificar que no exista una persona con el mismo dni:
        String dni = request.getParameter("dni");
        
        //instanciamos...
        Controladora controladora = new Controladora();
        Validacion validar = new Validacion(controladora);

        String mensajeValidacion = validar.validarBusqueda(dni);

        request.setAttribute("mensajeTitular", "");
        
        if (mensajeValidacion.equals("no encontrado")) {
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");
            String empresaId = request.getParameter("empresa");
            int idEmpresa = Integer.parseInt(empresaId); 
            
            
            //------------Inicio de Busqueda---------------
            // Buscar el objeto Empresa en la lista por ID
            Empresa empresaSeleccionada = null;
            List<Empresa> listaEmpresas = controladora.traerEmpresas();
            for (Empresa empresa : listaEmpresas) {
                if (empresa.getId() == idEmpresa) {
                    empresaSeleccionada = empresa;
                    break;
                }
            }
            //--------------Fin de Busqueda-------------
            
            //--------------Inicio del Guardado-------------
            // Utilizar el objeto Empresa seleccionada
            if (empresaSeleccionada != null) {

            // Instanciamos los objetos
            Titular titular = new Titular();
            Afiliado afiliado = new Afiliado();
            Empresa empresa = new Empresa();

            // Asignamos los valores al objeto afiliado
            afiliado.setDni(dni);
            afiliado.setNombre(nombre);
            afiliado.setApellido(apellido);
            afiliado.setTelefono(telefono);
            afiliado.setEmail(email);
            afiliado.setTipoAfiliado("titular");
            afiliado.setStatus("activo");
            //Guardamos el afiliado en la base de datos
            controladora.crearAfiliado(afiliado);
            //Asignamos el id de la empresa seleccionada al objeto empresa
            empresa.setId(idEmpresa);
            //Asignamos el objeto Empresa al objeto Titular
            titular.setEmpresa(empresa);
            // Asignamos el objeto afiliado al objeto titular
            titular.setAfiliadoTitular(afiliado);
            // Guardamos el titular en la base de datos
            controladora.crearTitular(titular);

            } else {
                request.setAttribute("mensajeTitular", "Debe seleccionar una empresa");
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
            }

            //redirigimos a la pagina principal
            request.setAttribute("mensajeTitular", "Afiliado registrado con éxito");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        } else {
            request.setAttribute("mensajeTitular", mensajeValidacion);
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
