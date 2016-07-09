package Content;
/**
 * Author - Manan Shah, Gilford Fernandes
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class emailResponse
 */
@WebServlet("/emailResponse")
public class emailResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public emailResponse() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * This function composes the messages and sends message to sender's mail server where
	 * it is stored on server's queue.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session=request.getSession(true);
		EmailClient ec=(EmailClient)session.getAttribute("client");
		PrintWriter out = response.getWriter();
		String to=request.getParameter("toSend");
		String cc_trim=request.getParameter("cc");
		
		ArrayList<String> list_cc=new ArrayList<>();
		if(!cc_trim.trim().isEmpty())
		{
			String[] cc= cc_trim.split(",");
			List<String> ccList=(List)Arrays.asList(cc);
			for(String s:ccList)
			{
				list_cc.add(s);
			}
		}
		String subject=request.getParameter("subject");
		String message=request.getParameter("message");
		String attachmentPath=request.getParameter("attachment");
		
		list_cc.add(to);
		System.out.println(attachmentPath);
		for(int i=0;i<list_cc.size();i++){
			if(attachmentPath.trim().isEmpty())
			{
				Mail mail=ec.composeMail(list_cc.get(i), message.getBytes(), false, subject,"");
				System.out.println("composed");
				ec.sendMail(mail);
				System.out.println("sent");
			}
			else
			{
				Mail mail=ec.composeMail(list_cc.get(i), message.getBytes(), true, subject, attachmentPath);
				ec.sendMail(mail);
			}
		}
		
		response.sendRedirect("loggedIn.jsp");
	}
	

}
