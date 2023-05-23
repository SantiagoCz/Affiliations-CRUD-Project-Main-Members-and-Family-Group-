
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


@WebServlet(name = "SvEmpresa", urlPatterns = {"/SvEmpresa"})
public class SvEmpresa extends HttpServlet {


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

        List<Empresa> listaEmpresas = controladora.traerEmpresas();

        if (listaEmpresas != null && !listaEmpresas.isEmpty()) {
            HttpSession misesion = request.getSession();
            misesion.setAttribute("listaEmpresas", listaEmpresas);
            response.sendRedirect("mostrarEmpresas.jsp");
        } else {
            request.setAttribute("mensajeEmpresa", "No hay empresas cargadas en el sistema");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }  
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        //Primero vamos a verificar que no exista una empresa con el mismo cuit:
        String cuit = request.getParameter("cuit");
        
        //instanciamos...
        Controladora controladora = new Controladora();
        Validacion validar = new Validacion(controladora);

        String mensajeValidacion = validar.validarCuit(cuit);

        request.setAttribute("mensajeEmpresa", "");
        
        if (mensajeValidacion.equals("cuit no encontrado")) {
            String nombre = request.getParameter("nombre");
            
            Empresa empresa = new Empresa();
            empresa.setCuit(cuit);
            empresa.setNombre(nombre);
            controladora.crearEmpresa(empresa);

            request.setAttribute("mensajeEmpresa", "La empresa se ha guardado con éxito.");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        } else {
        request.setAttribute("mensajeEmpresa", mensajeValidacion);
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
