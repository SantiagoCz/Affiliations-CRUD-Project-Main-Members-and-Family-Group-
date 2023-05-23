<%@page import="logica.Empresa"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charsetarset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
    </head>
    <body>
        
        <section class="show-registers">
            <h1>Lista de Empresas registradas</h1>
            <form id="empresa-form">
                <table>
                    <tr>
                        <th>N°</th>
                        <th>CUIT</th>
                        <th>Nombre de la empresa</th>
                    </tr>
                    <%
                    List<Empresa> listaEmpresas = (List) request.getSession().getAttribute("listaEmpresas");
                    int contador = 1;
                    for (Empresa empresa : listaEmpresas) {
                %>
                <tr onclick="seleccionarFila(this)">
                  <td><%= contador%></td>
                  <td><%= empresa.getCuit()%></td>
                  <td><%= empresa.getNombre()%></td>
                </tr>
                <% contador = contador + 1; %>
                <% }%>
              </table>
            </form>
        </section>

    </body>
</html>