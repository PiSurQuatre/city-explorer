package fr.hei.devweb.cityexplorer.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import fr.hei.devweb.cityexplorer.pojos.City;
import fr.hei.devweb.cityexplorer.services.CityService;

@WebServlet("/delete")
public class CityDeleteServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = -3497793006266174453L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TemplateEngine templateEngine = this.createTemplateEngine(req);
		
		WebContext context = new WebContext(req, resp, getServletContext());		
		templateEngine.process("citydelete", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	
}
