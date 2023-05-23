<%-- 
    Document   : index
    Created on : 22 may. 2023, 13:56:19
    Author     : Santiago Czarny
--%>
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
        
    <section class="form-register" id="registro">
        <form action="SvEmpresa" method="POST">
            <!--Formulario -->
            <h4>Registro de Empresa</h4>
            <input class="controls" type="text" name="nombre" id="nombre" placeholder="Ingrese nombre de la empresa">
            <input class="controls" type="text" name="cuit" id="cuit" placeholder="Ingrese CUIT de la empresa" inputmode="numeric" pattern="[0-9]{11}" title="El CUIT debe tener 11 dígitos numéricos sin guiones">
            <input class="botons" type="submit" value="Registrar">
        </form>  

        <div id="mensajeEmpresa">
            <% String mensajeEmpresa = (String) request.getAttribute("mensajeEmpresa"); %>
            <% if (mensajeEmpresa != null) {%>
            <p><%= mensajeEmpresa%></p>
            <% } %>
        </div>
        
        <form action="SvEmpresa" method="GET">
            <input class="botons" type="submit" value="Mostrar todos">
        </form> 
        
        <form action="SvEditarEmpresa" method="GET">
            <input class="controls" type="text" name="cuit" id="cuit" placeholder="Ingrese CUIT de la empresa" inputmode="numeric" pattern="[0-9]{11}" title="El CUIT debe tener 11 dígitos numéricos sin guiones">
            <input class="botons" type="submit" value="Editar">
        </form> 
        
        <form action="SvEliminarEmpresa" method="POST">
            <input class="controls" type="text" name="cuit" id="cuit" placeholder="Ingrese CUIT de la empresa" inputmode="numeric" pattern="[0-9]{11}" title="El CUIT debe tener 11 dígitos numéricos sin guiones">
            <input class="botons" type="submit" value="Eliminar">
        </form> 
    </section>
       
    <section class="form-register" id="registro-titular">
        <form action="SvTitular" method="POST">
            <h4>Formulario Registro de Afiliados Titulares</h4>
            <!--Formulario -->
            <input class="controls" type="text" name="dni" id="dni" placeholder="DNI" inputmode="numeric" pattern="[0-9]{8}" title="El DNI debe tener 8 dígitos numéricos sin puntos">
            <input class="controls" type="text" name="nombre" id="nombre" placeholder="Nombre">
            <input class="controls" type="text" name="apellido" id="apellido" placeholder="Apellido">
            <input class="controls" type="text" name="telefono" id="telefono" placeholder="Telefono" inputmode="numeric" pattern="[0-9]{10,11}"">
            <input class="controls" type="email" name="email" id="email" placeholder="Correo electrónico">
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
                <input class="botons" type="submit" value="Registrar">
        </form>
        <div id="mensajeTitular">
            <% String mensajeTitular = (String) request.getAttribute("mensajeTitular"); %>
            <% if (mensajeTitular != null) {%>
            <p><%= mensajeTitular%></p>
            <% }%>
        </div>
        
        <form action="SvTitular" method="GET">
            <input class="botons" type="submit" value="Mostrar todos">
        </form>
        
        <form action="SvEditarTitular" method="GET">
            <input class="controls" type="text" name="dni" id="dni" placeholder="DNI" inputmode="numeric" pattern="[0-9]{8}" title="El DNI debe tener 8 dígitos numéricos sin puntos">
            <input class="botons" type="submit" value="Editar">
        </form> 
        
        <form action="SvEliminarTitular" method="POST">
            <input class="controls" type="text" name="dni" id="dni" placeholder="DNI" inputmode="numeric" pattern="[0-9]{8}" title="El DNI debe tener 8 dígitos numéricos sin puntos">
            <input class="botons" type="submit" value="Eliminar">
        </form> 
        
    </section>
        
    <section class="form-register" id="registro-familiar">
        <form action="SvFamiliar" method="POST">
            <h4>Formulario Registro de Afiliados Adherentes</h4>
            <!--Formulario -->
            <input class="controls" type="text" name="dniTitular" id="dniTitular" placeholder="Ingrese el DNI del titular" inputmode="numeric" pattern="[0-9]{8}" title="El DNI debe tener 8 dígitos numéricos sin puntos">
            <input class="controls" type="text" name="dni" id="dni" placeholder="DNI" inputmode="numeric" pattern="[0-9]{8}" title="El DNI debe tener 8 dígitos numéricos sin puntos">
            <input class="controls" type="text" name="nombre" id="nombre" placeholder="Nombre">
            <input class="controls" type="text" name="apellido" id="apellido" placeholder="Apellido">
            <input class="controls" type="text" name="telefono" id="telefono" placeholder="Telefono" inputmode="numeric" pattern="[0-9]{10,11}"">
            <input class="controls" type="email" name="email" id="email" placeholder="Correo electrónico">
            <select class="controls" name="relacion" id="relacion">
                <option value="">Seleccione una opción</option>
                <option value="hijo">Hijo/a</option>
                <option value="concubino">Conyugue</option>
            </select>

            <input class="botons" type="submit" value="Registrar">
        </form>
        <div id="mensajeTitular">
            <% String mensajeFamiliar = (String) request.getAttribute("mensajeFamiliar"); %>
            <% if (mensajeFamiliar != null) {%>
            <p><%= mensajeFamiliar%></p>
            <% }%>
        </div>
        
        <form action="SvFamiliar" method="GET">
            <input class="botons" type="submit" value="Mostrar todos">
        </form>
        
        <form action="SvEditarFamiliar" method="GET">
            <input class="controls" type="text" name="dni" id="dni" placeholder="DNI" inputmode="numeric" pattern="[0-9]{8}" title="El DNI debe tener 8 dígitos numéricos sin puntos">
            <input class="botons" type="submit" value="Editar">
        </form> 
        
        <form action="SvEliminarFamiliar" method="POST">
            <input class="controls" type="text" name="dni" id="dni" placeholder="DNI" inputmode="numeric" pattern="[0-9]{8}" title="El DNI debe tener 8 dígitos numéricos sin puntos">
            <input class="botons" type="submit" value="Eliminar">
        </form> 
        
    </section>
        

    </body>
</html>