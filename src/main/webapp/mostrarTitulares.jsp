<%@page import="logica.Familiar"%>
<%@page import="java.util.List"%>
<%@page import="logica.Titular"%>
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
            <h1>Lista de Afiliados Titulares:</h1>
            <% 
            List<Titular> listaTitulares = (List) request.getSession().getAttribute("listaTitulares");
            int contador = 1;
            for (Titular titular : listaTitulares) {  
            %>
                <p><b>Titular N°<%=contador%></p></b>
                 <% if (titular.getAfiliadoTitular() != null) { %>
                        <p>DNI: <%=titular.getAfiliadoTitular().getDni()%></p>
                        <p>Nombre y apellido: <%=titular.getAfiliadoTitular().getNombre()%> <%=titular.getAfiliadoTitular().getApellido()%></p>
                        <p>Telefono: <%=titular.getAfiliadoTitular().getTelefono()%>, Email: <%=titular.getAfiliadoTitular().getEmail()%></p>
                        <p>Status: <%=titular.getAfiliadoTitular().getStatus()%></p>
                 <% } else { %>
                        <p>No hay información de afiliado para este titular.</p>
                 <% } %>
                 <% if (titular.getEmpresa() != null) { %>
                        <p>Empresa: <%=titular.getEmpresa().getNombre()%></p>
                 <% } else { %>
                        <p>No hay información de empresa para este titular.</p>
                 <% } %>

                    <% 
                    List<Familiar> listaFamiliares = titular.getFamiliarTitular();
                    if (listaFamiliares != null) {
                    %><p>FAMILIARES A CARGO:</p><%
                        for (Familiar familiar : listaFamiliares) {  
                    %>
                            <p>Nombre y apellido: <%=familiar.getAfiliadoFamiliar().getNombre()%> <%=familiar.getAfiliadoFamiliar().getApellido()%>, Relacion: <%=familiar.getRelacion()%> </p>
                            <p>Status: <%=familiar.getAfiliadoFamiliar().getStatus()%></p>
                     <% } %>
                 <% } else { %>
                        <p>El afiliado no tiene familiares asociados.</p>
                 <% } %>

                <p>-------------------------------------</p>
                <% contador++; %>

         <% } %>
    </section>
</body>
</html>
