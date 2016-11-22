package fr.hei.devweb.cityexplorer.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import fr.hei.devweb.cityexplorer.services.CityService;

@WebServlet("/deletecity")
public class CityDeleteServlet extends AbstractGenericServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String confirm = req.getParameter("confirm");
		Integer idCity = Integer.parseInt(req.getParameter("id"));
		if("true".equals(confirm)) {
			CityService.getInstance().deleteCity(idCity);
			resp.sendRedirect("home");
		} else {
			TemplateEngine templateEngine = this.createTemplateEngine(req);
			
			WebContext context = new WebContext(req, resp, req.getServletContext());			
			context.setVariable("city", CityService.getInstance().getCity(idCity));
			
			templateEngine.process("confirmdelete", context, resp.getWriter());
		}
	}

	
}
