package Content;
/**
 * Author - Manan Shah
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ClientSide
 */
@WebServlet("/ClientSide")
public class ClientSide extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientSide() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		}

	/**
	 * This function authenticates the user at its server and gives permission to access mails if successful
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String username=request.getParameter("username");
		String password=request.getParameter("pass");
		EmailClient ec=new EmailClient();
		HttpSession session_name=request.getSession();
		if(ec.authenticateUser(username, password))
		{

			session_name.setAttribute("client", ec);
			response.sendRedirect("loggedIn.jsp");
		}
		else
		{
			session_name.setAttribute("Error", "Invalid login information..Try again");
			response.sendRedirect("Homepage.jsp");
		}
		
		
		
	    
	}

}
