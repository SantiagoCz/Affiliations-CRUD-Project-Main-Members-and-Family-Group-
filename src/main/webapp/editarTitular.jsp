<%@page import="logica.Afiliado"%>
<%@page import="logica.Empresa"%>
<%@page import="java.util.List"%>
<%@page import="logica.Controladora"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charsetarset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
    </head>
    <body>
        <h1>Editar Titular</h1>
            <form action="SvEditarTitular" method="POST">
                <% Afiliado afiliadoTitular = (Afiliado) request.getSession().getAttribute("afiliadoTitular");%> 
                <label>Id del afiliado:</label>
                <input type="text" name="idTitular" value="<%=afiliadoTitular.getId()%>" readonly>
                <br>
                <input class="controls" type="text" name="dni" id="dni" placeholder="DNI" inputmode="numeric" pattern="[0-9]{8}" title="El DNI debe tener 8 dígitos numéricos sin puntos" value="<%=afiliadoTitular.getDni()%>">
                <input class="controls" type="text" name="nombre" id="nombre" placeholder="Nombre" value="<%=afiliadoTitular.getNombre()%>">
                <input class="controls" type="text" name="apellido" id="apellido" placeholder="Apellido" value="<%=afiliadoTitular.getApellido()%>">
                <input class="controls" type="text" name="telefono" id="telefono" placeholder="Telefono" inputmode="numeric" pattern="[0-9]{10,11}" value="<%=afiliadoTitular.getTelefono()%>">
                <input class="controls" type="email" name="email" id="email" placeholder="Correo electrónico" value="<%=afiliadoTitular.getEmail()%>">
                <!--Se carga el combobox con las empresas registradas -->
                <select class="controls" name="empresa" id="empresa">
                    <option value="">Elija la empresa</option>
                    <%
                        Controladora controladora = new Controladora();
                        List<Empresa> listaEmpresa = controladora.traerEmpresas();
                        for (Empresa empresa : listaEmpresa) {
                    %>
                    <option value="<%= empresa.getId()%>"><%= empresa.getNombre()%></option>
                    <% }%>
                </select>
                <input type="submit" value="Guardar">
            </form>
            <form action="index.jsp">
                <input type="submit" value="Volver">
            </form>
    
            <div id="mensajeTitular">
                <% String mensajeTitular = (String) request.getAttribute("mensajeTitular"); %>
                <% if (mensajeTitular != null) {%>
                <p><%= mensajeTitular%></p>
                <% } %>
            </div> 
    </body>
</html>
