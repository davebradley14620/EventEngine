package eventengine.server;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InitializerServlet
 */
public class InitializerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			System.out.println("*****************************************");
			System.out.println("*** Initializing EventEngine globals. ***");
			System.out.println("*****************************************");
			//
			// Put our context-param variables (web.xml) into the System.properties so anyone can get at them.
			//
			for (Enumeration e = config.getServletContext().getInitParameterNames() ; e.hasMoreElements() ;) {
				String name = (String)e.nextElement();
				String value = (String)config.getServletContext().getInitParameter(name);
				System.setProperty(name,value);
			}
/*
			//
			// Create the Model.
			//
			Model model = (Model)config.getServletContext().getAttribute("model");
			if ( model == null ) {
				try {
					model = new Model( "jdbc/hoorateDB" );
				} catch ( Exception e ) {
					e.printStackTrace(System.err);
					throw new ServletException("InitializerServlet.init: ERROR - Unable to instantiate HooRate Model object."+e.getMessage());

				}
				config.getServletContext().setAttribute("model",model);
			}
*/
		} catch( Exception e ) {
			e.printStackTrace(System.err);
			//
			// Rethrow.
			//
			throw new ServletException( e );
		}
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
