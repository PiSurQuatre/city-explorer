package fr.hei.devweb.cityexplorer.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.hei.devweb.cityexplorer.services.CityService;

@WebServlet("/opinion")
public class OpinionServlet extends AbstractGenericServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idCityLike = req.getParameter("like");
		if(idCityLike != null) {
			try {
				Integer cityId = Integer.parseInt(idCityLike);
				CityService.getInstance().likeCity(cityId);
			} catch (NumberFormatException e) {
			}
		}
		
		String idCityDislike = req.getParameter("dislike");
		if(idCityDislike != null) {
			try {
				Integer cityId = Integer.parseInt(idCityDislike);
				CityService.getInstance().dislikeCity(cityId);
			} catch (NumberFormatException e) {
			}
		}
		
		resp.sendRedirect("home");
	}

	
}
