package fr.hei.devweb.cityexplorer.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import fr.hei.devweb.cityexplorer.pojos.City;
import fr.hei.devweb.cityexplorer.services.CityService;

@WebServlet("/addcity")
@MultipartConfig
public class CityAddServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = -3497793006266174453L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TemplateEngine templateEngine = this.createTemplateEngine(req);

		WebContext context = new WebContext(req, resp, getServletContext());
		if (req.getSession().getAttribute("cityCreationError") != null) {
			context.setVariable("errorMessage", req.getSession().getAttribute("cityCreationError"));
			context.setVariable("city", (City) req.getSession().getAttribute("cityCreationData"));

			req.getSession().removeAttribute("cityCreationError");
			req.getSession().removeAttribute("cityCreationData");
		} else {
			context.setVariable("city", new City());
		}

		templateEngine.process("cityadd", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String summary = req.getParameter("summary");

		City newCity = new City(null, name, summary);
		Part image = req.getPart("image");

		try {
			String fileName = "/home/julien/image/city-explorer/" + name + "/" + image.getSubmittedFileName();
			File f = new File(fileName);
			f.getParentFile().mkdirs();
			f.createNewFile();
			image.write(fileName);
			CityService.getInstance().addCity(newCity, fileName);
			resp.sendRedirect("home");
		} catch (IllegalArgumentException e) {
			req.getSession().setAttribute("cityCreationError", e.getMessage());
			req.getSession().setAttribute("cityCreationData", newCity);
			resp.sendRedirect("addcity");
		}

	}

}
