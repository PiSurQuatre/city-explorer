package fr.hei.devweb.cityexplorer.servlets;

import java.io.IOException;
import java.util.function.Consumer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.hei.devweb.cityexplorer.services.CityService;

@WebServlet("/avis")
public class AvisServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = 5126466295502907058L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idLike = req.getParameter("like");
		if (idLike != null && !"".equals(idLike)) {
			try {
				Integer idCity = Integer.parseInt(idLike);
				CityService.getInstance().addLike(idCity);
				redirectToPage(req, resp, idCity);
			} catch (NumberFormatException e) {
				resp.sendRedirect("home");
			}
		}
				
		String idDislike = req.getParameter("dislike");
		if (idDislike != null && !"".equals(idDislike)) {
			try {
				Integer idCity = Integer.parseInt(idDislike);
				CityService.getInstance().addDislike(idCity);
				redirectToPage(req, resp, idCity);
			} catch (NumberFormatException e) {
				resp.sendRedirect("home");
			}
		}
		
		
	}
	
	private void redirectToPage(HttpServletRequest req, HttpServletResponse resp, Integer idCity) throws IOException {
		String pageRetour = req.getParameter("retour");
		if("detail".equals(pageRetour)) {
			resp.sendRedirect(String.format("detail?id=%d", idCity));
		} else {
			resp.sendRedirect("home");
		}
	}
}
