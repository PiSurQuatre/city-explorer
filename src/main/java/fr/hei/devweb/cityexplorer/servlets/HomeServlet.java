package fr.hei.devweb.cityexplorer.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import fr.hei.devweb.cityexplorer.pojos.Country;
import fr.hei.devweb.cityexplorer.services.CityService;

@WebServlet("/home")
public class HomeServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = 5402133218271984030L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TemplateEngine templateEngine = this.createTemplateEngine(req);
		
		WebContext context = new WebContext(req, resp, getServletContext());
		Country countryFilter = (Country) req.getSession().getAttribute("countryFilter");
		context.setVariable("cities", CityService.getInstance().listAllCities(countryFilter));
		context.setVariable("countries", Country.values());
		
		context.setVariable("countryFilterSelected", countryFilter);
		
		templateEngine.process("home", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String countryString = req.getParameter("countryFilter");
		
		try {
			Country country = Country.valueOf(countryString);
			req.getSession().setAttribute("countryFilter", country);
		} catch (IllegalArgumentException e) {
			req.getSession().removeAttribute("countryFilter");
		}
		
		resp.sendRedirect("home");
		
	}

	
	
}
