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
		Country filterCountry = (Country) req.getSession().getAttribute("filterCountry");
		context.setVariable("cities", CityService.getInstance().listCities(filterCountry));
		context.setVariable("countries", Country.values());
		context.setVariable("filterCountry", filterCountry);
		
		templateEngine.process("home", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String selectedCountry = req.getParameter("country");
		
		if(selectedCountry == null || "".equals(selectedCountry)) {
			req.getSession().removeAttribute("filterCountry");
		} else {
			try {
				req.getSession().setAttribute("filterCountry", Country.valueOf(selectedCountry));
			} catch (IllegalArgumentException e) {
				req.getSession().removeAttribute("filterCountry");
			}
		}
		
		resp.sendRedirect("home");
	}

	
	
}
