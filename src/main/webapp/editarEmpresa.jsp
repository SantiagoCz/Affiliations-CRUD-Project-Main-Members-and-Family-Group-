
<%@page import="logica.Empresa"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <head>
        <meta charsetarset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
    </head>
    
    <body>
        <section>
        <h1>Editar Empresa</h1>
            <form action="SvEditarEmpresa" method="POST">
            <% Empresa empresaSeleccionada = (Empresa) request.getSession().getAttribute("empresaSeleccionada");%>
                <label>Id de la empresa:</label>
                <input type="text" name="idEmpresa" value="<%= empresaSeleccionada.getId() %>" readonly>
                <br>
                <label>Nombre de la empresa:</label>
                <input type="text" name="nombreEmpresa" value="<%= empresaSeleccionada.getNombre() %>">
                <br>
                <label>CUIT de la empresa:</label>
                <input type="text" name="cuitEmpresa" value="<%= empresaSeleccionada.getCuit() %>">
                <br>
                
                <input type="submit" value="Guardar">
            </form>
            <form action="index.jsp">
                <input type="submit" value="Volver">
            </form>
    
            <div id="mensajeEmpresa">
                <% String mensajeEmpresa = (String) request.getAttribute("mensajeEmpresa"); %>
                <% if (mensajeEmpresa != null) {%>
                <p><%= mensajeEmpresa%></p>
                <% } %>
            </div>    
        </section>     
    </body>
</html>
