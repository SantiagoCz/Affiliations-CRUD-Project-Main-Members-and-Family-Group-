
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import logica.Titular;
import logica.Validacion;


@WebServlet(name = "SvEditarTitular", urlPatterns = {"/SvEditarTitular"})
public class SvEditarTitular extends HttpServlet {


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
        Afiliado afiliadoTitular = null;
        Afiliado afiliado = (Afiliado) controladora.traerAfiliadoPorDni(dni);
        if (afiliado != null && afiliado.getTipoAfiliado() != null && afiliado.getTipoAfiliado().equals("titular")) {
            afiliadoTitular = afiliado;
        }

        if (afiliadoTitular != null) {
            HttpSession misesion = request.getSession();
            misesion.setAttribute("afiliadoTitular", afiliadoTitular);
            request.getRequestDispatcher("editarTitular.jsp").forward(request, response);
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
        String empresaId = request.getParameter("empresa");
        int idEmpresa = Integer.parseInt(empresaId);

        Controladora controladora = new Controladora();

        Afiliado afiliadoTitular = (Afiliado) request.getSession().getAttribute("afiliadoTitular");

        // Obtener el DNI anterior del afiliado titular antes de actualizarlo
        String dniAnterior = afiliadoTitular.getDni();

        // Comprobar si el DNI ha cambiado y si es diferente al anterior
        if (!dni.equals(dniAnterior)) {
            // El DNI ha cambiado, se debe comprobar si ya existe en la base de datos
            Validacion validar = new Validacion(controladora);
            String mensajeValidacion = validar.validarBusqueda(dni);
            if (mensajeValidacion.equals("encontrado")) {
                // El nuevo DNI ya existe en la base de datos, muestra un mensaje de error
                request.setAttribute("mensajeTitular", "El DNI ingresado ya existe para otro afiliado.");
                RequestDispatcher rd = request.getRequestDispatcher("editarTitular.jsp");
                rd.forward(request, response);
                return; // Detener la ejecución para evitar continuar con la actualización
            }

        }

        Empresa empresaSeleccionada = null;
        if (empresaSeleccionada == null) {
            request.setAttribute("mensajeTitular", "Debe seleccionar una empresa");
            RequestDispatcher rd = request.getRequestDispatcher("editarTitular.jsp");
            rd.forward(request, response);
            return; // Detener la ejecución para evitar continuar con la actualización
        }

        List<Empresa> listaEmpresas = controladora.traerEmpresas();
        for (Empresa empresa : listaEmpresas) {
            if (empresa.getId() == idEmpresa) {
                empresaSeleccionada = empresa;
                break;
            }
        }

        // Comprobar si se ha seleccionado una empresa
        if (empresaSeleccionada != null) {
            // Actualizar los datos del afiliado titular
            afiliadoTitular.setDni(dni);
            afiliadoTitular.setNombre(nombre);
            afiliadoTitular.setApellido(apellido);
            afiliadoTitular.setTelefono(telefono);
            afiliadoTitular.setEmail(email);
            // Instanciamos empresa
            Empresa empresa = new Empresa();
            // Asignamos el id de la empresa seleccionada al objeto empresa
            empresa.setId(idEmpresa);
            // Instanciamos titular
            Titular titular = new Titular();
            // Actualizar la empresa del afiliado titular
            titular.setEmpresa(empresa);
            // Asignamos el objeto afiliado al objeto titular
            titular.setAfiliadoTitular(afiliadoTitular);

            // Guardar los cambios en la base de datos
            controladora.editarAfiliado(afiliadoTitular);
            controladora.editarTitular(titular);
        } else {
            request.setAttribute("mensajeTitular", "Debe seleccionar una empresa");
            RequestDispatcher rd = request.getRequestDispatcher("editarTitular.jsp");
            rd.forward(request, response);
            return; // Detener la ejecución para evitar continuar con la actualización
        }

        request.setAttribute("mensajeTitular", "El titular se ha actualizado con éxito.");
        RequestDispatcher rd = request.getRequestDispatcher("editarTitular.jsp");
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
