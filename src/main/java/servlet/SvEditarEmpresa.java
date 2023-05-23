
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
import logica.Controladora;
import logica.Empresa;
import logica.Validacion;


@WebServlet(name = "SvEditarEmpresa", urlPatterns = {"/SvEditarEmpresa"})
public class SvEditarEmpresa extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        String cuit = request.getParameter("cuit");

        // Instanciamos la Controladora
        Controladora controladora = new Controladora();
        
        // Vamos a comprobar que exista la empresa y ya asignamos el objeto empresa
        Empresa empresaSeleccionada = controladora.traerEmpresaPorCuit(cuit);

        if (empresaSeleccionada != null) {
            HttpSession misesion = request.getSession();
            misesion.setAttribute("empresaSeleccionada", empresaSeleccionada);
            request.getRequestDispatcher("editarEmpresa.jsp").forward(request, response);
        } else {
            request.setAttribute("mensajeEmpresa", "Verifique el número de CUIT ingresado");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    processRequest(request, response);

    // Obtener los parámetros del formulario
    String nombre = request.getParameter("nombreEmpresa");
    String cuit = request.getParameter("cuitEmpresa");

    Controladora controladora = new Controladora();

    // Obtener la empresa seleccionada de la sesión
    Empresa empresaSeleccionada = (Empresa) request.getSession().getAttribute("empresaSeleccionada");

    // Obtener el CUIT anterior de la empresa
    String cuitAnterior = empresaSeleccionada.getCuit();

    // Comprobar si el CUIT ha cambiado y si es diferente al anterior
    if (!cuit.equals(cuitAnterior)) {
        // Verificar si el nuevo CUIT ya existe en la base de datos
        Validacion validar = new Validacion(controladora);
        String mensajeValidacion = validar.validarCuit(cuit);

        if (mensajeValidacion.equals("cuit no encontrado")) {
            // El nuevo CUIT no existe en la base de datos, se puede actualizar
            empresaSeleccionada.setCuit(cuit);
            empresaSeleccionada.setNombre(nombre);
            controladora.editarEmpresa(empresaSeleccionada);
            request.setAttribute("mensajeEmpresa", "La empresa se ha actualizado con éxito.");
        } else {
            // El nuevo CUIT ya existe en la base de datos, muestra un mensaje de error
            request.setAttribute("mensajeEmpresa", "La cuit ya esta asignado a otra empresa");
        }
    } else {
        // El CUIT no ha cambiado, se actualiza solo el nombre de la empresa
        empresaSeleccionada.setNombre(nombre);
        controladora.editarEmpresa(empresaSeleccionada);
        request.setAttribute("mensajeEmpresa", "El nombre de la empresa se ha actualizado con éxito.");
    }

    RequestDispatcher rd = request.getRequestDispatcher("editarEmpresa.jsp");
    rd.forward(request, response);
}



    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
