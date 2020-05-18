<jsp:useBean id="managehaHatest" scope="session" class="fr.paris.lutece.plugins.hatest.web.HatestJspBean" />
<% String strContent = managehaHatest.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
