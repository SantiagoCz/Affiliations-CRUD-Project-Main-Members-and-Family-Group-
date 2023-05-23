
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
import logica.Afiliado;
import logica.Controladora;
import logica.Empresa;
import logica.Familiar;
import logica.Titular;


@WebServlet(name = "SvEliminarFamiliar", urlPatterns = {"/SvEliminarFamiliar"})
public class SvEliminarFamiliar extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        // Primero vamos a verificar que exista:
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
            // Elimina Empresa
            controladora.eliminarFamiliar(afiliadoFamiliar.getId());
            request.setAttribute("mensajeFamiliar", "Afiliado eliminado de manera exitosa");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        } else {
            request.setAttribute("mensajeFamiliar", "Verifique el número de DNI ingresado");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
        
    }

  
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
