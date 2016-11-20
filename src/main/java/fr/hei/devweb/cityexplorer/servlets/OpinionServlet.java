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
		String idCityDislike = req.getParameter("dislike");

		try {
			if (idCityLike != null) {
				Integer cityId = Integer.parseInt(idCityLike);
				CityService.getInstance().likeCity(cityId);
				this.redirectToPage(cityId, req, resp);
			} else if (idCityDislike != null) {
				Integer cityId = Integer.parseInt(idCityDislike);
				CityService.getInstance().dislikeCity(cityId);
				this.redirectToPage(cityId, req, resp);
			} else {
				resp.sendRedirect("home");
			}
		} catch (NumberFormatException e) {
			resp.sendRedirect("home");
		}
	}

	private void redirectToPage(Integer cityId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String returnPage = req.getParameter("return");
		if("detail".equals(returnPage)) {
			resp.sendRedirect(String.format("detail?id=%d", cityId));
		} else {
			resp.sendRedirect("home");
		}
	}
	
}
