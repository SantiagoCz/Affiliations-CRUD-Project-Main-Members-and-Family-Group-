
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
import logica.Familiar;


@WebServlet(name = "SvEliminarTitular", urlPatterns = {"/SvEliminarTitular"})
public class SvEliminarTitular extends HttpServlet {


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
        Afiliado afiliadoTitular = null;
        Afiliado afiliado = (Afiliado) controladora.traerAfiliadoPorDni(dni);
        if (afiliado != null && afiliado.getTipoAfiliado() != null && afiliado.getTipoAfiliado().equals("titular")) {
            afiliadoTitular = afiliado;
        }
        
        if (afiliadoTitular != null) {
            // Cambiamos el status del grupo familiar a inactivo
            List<Familiar> listaFamiliares = afiliadoTitular.getFamiliares();
            for (Familiar familiar : listaFamiliares) {
                Afiliado afiliadoFamiliar = familiar.getAfiliadoFamiliar();
                afiliadoFamiliar.setStatus("inactivo");
                controladora.editarAfiliado(afiliadoFamiliar); // Guardar cambios en el familiar
            }
            //ELIMINAMOS AL TITULAR:
            controladora.eliminarTitular(afiliadoTitular.getId());
            request.setAttribute("mensajeTitular", "Afiliado eliminado de manera exitosa");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        } else {
            request.setAttribute("mensajeTituar", "Verifique el número de DNI ingresado");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
        
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
