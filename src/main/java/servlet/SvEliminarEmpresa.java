
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
import logica.Familiar;
import logica.Titular;
import logica.Validacion;


@WebServlet(name = "SvEliminarEmpresa", urlPatterns = {"/SvEliminarEmpresa"})
public class SvEliminarEmpresa extends HttpServlet {


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

        // Primero vamos a verificar que exista una empresa con el mismo CUIT:
        String cuit = request.getParameter("cuit");

        // Instanciamos la Controladora
        Controladora controladora = new Controladora();

        // Vamos a comprobar que exista la empresa y ya asignamos el objeto empresa
        Empresa empresaSeleccionada = controladora.traerEmpresaPorCuit(cuit);

        if (empresaSeleccionada != null) {
            List<Titular> listaTitulares = controladora.traerTitulares();
            for (Titular titular : listaTitulares) {
                Empresa empresaTitular = titular.getEmpresa();
                if (empresaTitular != null && empresaSeleccionada.getId() == empresaTitular.getId()) {
                    Afiliado afiliadoTitular = titular.getAfiliadoTitular();
                    afiliadoTitular.setStatus("inactivo");

                    List<Familiar> listaFamiliares = titular.getFamiliarTitular();
                    for (Familiar familiar : listaFamiliares) {
                        Afiliado afiliadoFamiliar = familiar.getAfiliadoFamiliar();
                        afiliadoFamiliar.setStatus("inactivo");
                        controladora.editarAfiliado(afiliadoFamiliar); // Guardar cambios en el familiar
                    }

                    controladora.editarAfiliado(afiliadoTitular); // Guardar cambios en el titular
                }
            }

            // Elimina Empresa
            controladora.eliminarEmpresa(empresaSeleccionada.getId());
            request.setAttribute("mensajeEmpresa", "Empresa eliminada de manera exitosa");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        } else {
            request.setAttribute("mensajeEmpresa", "Verifique el número de CUIT ingresado");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
