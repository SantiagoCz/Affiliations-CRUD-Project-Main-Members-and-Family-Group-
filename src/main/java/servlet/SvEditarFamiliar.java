
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
import logica.Validacion;


@WebServlet(name = "SvEditarFamiliar", urlPatterns = {"/SvEditarFamiliar"})
public class SvEditarFamiliar extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        String dni = request.getParameter("dni");

        // Instanciamos la Controladora
        Controladora controladora = new Controladora();

        // Vamos a comprobar que exista y que sea de tipo familiar
        Afiliado afiliadoFamiliar = null;
        Afiliado afiliado = (Afiliado) controladora.traerAfiliadoPorDni(dni);
        if (afiliado != null && afiliado.getTipoAfiliado() != null && afiliado.getTipoAfiliado().equals("familiar")) {
            afiliadoFamiliar = afiliado;
        }

        if (afiliadoFamiliar != null) {
            HttpSession misesion = request.getSession();
            misesion.setAttribute("afiliadoFamiliar", afiliadoFamiliar);
            request.getRequestDispatcher("editarFamiliar.jsp").forward(request, response);
        } else {
            request.setAttribute("mensajeFamiliar", "Verifique el número de DNI ingresado");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        // Obtener los parámetros del formulario
        String dni = request.getParameter("dni");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String relacion = request.getParameter("relacion");

        Controladora controladora = new Controladora();

        Afiliado afiliadoFamiliar = (Afiliado) request.getSession().getAttribute("afiliadoFamiliar");

        // Obtener el DNI anterior del afiliado familiar antes de actualizarlo
        String dniAnterior = afiliadoFamiliar.getDni();

        // Comprobar si el DNI ha cambiado y si es diferente al anterior
        if (!dni.equals(dniAnterior)) {
            // El DNI ha cambiado, se debe comprobar si ya existe en la base de datos
            Validacion validar = new Validacion(controladora);
            String mensajeValidacion = validar.validarBusqueda(dni);
            if (mensajeValidacion.equals("encontrado")) {
                // El nuevo DNI ya existe en la base de datos, muestra un mensaje de error
                request.setAttribute("mensajeFamiliar", "El DNI ingresado ya existe para otro afiliado.");
                RequestDispatcher rd = request.getRequestDispatcher("editarFamiliar.jsp");
                rd.forward(request, response);
                return; // Detener la ejecución para evitar continuar con la actualización
            }
        }

        // Actualizar los datos del afiliado familiar
        afiliadoFamiliar.setDni(dni);
        afiliadoFamiliar.setNombre(nombre);
        afiliadoFamiliar.setApellido(apellido);
        afiliadoFamiliar.setTelefono(telefono);
        afiliadoFamiliar.setEmail(email);

        Familiar familiar = new Familiar();
        familiar.setAfiliadoFamiliar(afiliadoFamiliar);
        familiar.setRelacion(relacion);

        // Guardar los cambios en la base de datos
        controladora.editarAfiliado(afiliadoFamiliar);
        controladora.editarFamiliar(familiar);

        request.setAttribute("mensajeFamiliar", "El familiar se ha actualizado con éxito.");
        RequestDispatcher rd = request.getRequestDispatcher("editarFamiliar.jsp");
        rd.forward(request, response);
    }


    

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
