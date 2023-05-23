<%@page import="logica.Afiliado"%>
<%@page import="logica.Familiar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charsetarset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
    </head>
    <body>
        <h1>Editar Familiar</h1>
            <form action="SvEditarFamiliar" method="POST">
                <% Afiliado afiliadoFamiliar = (Afiliado) request.getSession().getAttribute("afiliadoFamiliar");%>    
                <label>Id del afiliado:</label>
                <input type="text" name="idFamiliar" value="<%=afiliadoFamiliar.getId()%>" readonly>
                <br>
                <input class="controls" type="text" name="dni" id="dni" placeholder="DNI" inputmode="numeric" pattern="[0-9]{8}" title="El DNI debe tener 8 dígitos numéricos sin puntos" value="<%=afiliadoFamiliar.getDni()%>">
                <input class="controls" type="text" name="nombre" id="nombre" placeholder="Nombre" value="<%=afiliadoFamiliar.getNombre()%>">
                <input class="controls" type="text" name="apellido" id="apellido" placeholder="Apellido" value="<%=afiliadoFamiliar.getApellido()%>">
                <input class="controls" type="text" name="telefono" id="telefono" placeholder="Telefono" inputmode="numeric" pattern="[0-9]{10,11}" value="<%=afiliadoFamiliar.getTelefono()%>">
                <input class="controls" type="email" name="email" id="email" placeholder="Correo electrónico" value="<%=afiliadoFamiliar.getEmail()%>">
                <select class="controls" name="relacion" id="relacion">
                    <option value="">Elija una opcion</option>
                    <option value="hijo">Hijo/a</option>
                    <option value="concubino">Conyugue</option>
                </select>
                <input type="submit" value="Guardar">
            </form>

                
            <form action="index.jsp">
                <input type="submit" value="Volver">
            </form>
    
            <div id="mensajeFamiliar">
                <% String mensajeFamiliar = (String) request.getAttribute("mensajeFamiliar"); %>
                <% if (mensajeFamiliar != null) {%>
                <p><%= mensajeFamiliar%></p>
                <% } %>
            </div> 
    </body>
</html>
