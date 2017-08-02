<%
//RequestDispatcher rd = request.getRequestDispatcher( "/index.html");
//rd.forward(request, response);
response.sendRedirect(request.getContextPath() + "/index.html");
%>