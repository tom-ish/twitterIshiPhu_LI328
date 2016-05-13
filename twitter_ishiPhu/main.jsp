<script type = "text/javascript">
function go(){
	<%
		String id = request.getParameter("id");
		String id = request.getParameter("login");
		String id = request.getParameter("session_key");
		if(id != null)
			out.println("main(" + id + "," + login + "," + session_key + ")");
		else
			out.println("main()");
	%>
}
$(go);
</script>