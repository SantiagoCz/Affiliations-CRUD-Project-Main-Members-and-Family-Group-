<%@page import="logica.Familiar"%>
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
            <h1>Lista de Afiliados Adherentes:</h1>
            <% 
            List<Familiar> listaFamiliares = (List) request.getSession().getAttribute("listaFamiliares");
            int contador = 1;
            for (Familiar familiar : listaFamiliares) {  
            %>
                <p><b>Adherente N°<%=contador%></p></b>
                 <% if (familiar.getAfiliadoFamiliar() != null) { %>
                        <p>DNI: <%=familiar.getAfiliadoFamiliar().getDni()%></p>
                        <p>Nombre y apellido: <%=familiar.getAfiliadoFamiliar().getNombre()%> <%=familiar.getAfiliadoFamiliar().getApellido()%></p>
                        <p>Status: <%=familiar.getAfiliadoFamiliar().getStatus()%></p>
                        <p>Es <%=familiar.getRelacion()%> de <%=familiar.getTitular().getAfiliadoTitular().getNombre()%> <%=familiar.getTitular().getAfiliadoTitular().getApellido()%></p>
                 <% } else { %>
                        <p>No hay información de afiliado para este familiar.</p>
                 <% } %>
                <p>-------------------------------------</p>
                <% contador++; %>

         <% } %>
        </section>
    </body>
</html>
