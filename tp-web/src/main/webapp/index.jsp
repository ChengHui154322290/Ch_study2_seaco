<%
//RequestDispatcher rd = request.getRequestDispatcher( "/index.html");
response.sendRedirect(request.getContextPath() + "/index.html");
//rd.forward(request, response);
%>