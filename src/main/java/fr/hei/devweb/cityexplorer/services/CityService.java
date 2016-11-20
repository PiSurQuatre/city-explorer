package fr.hei.devweb.cityexplorer.services;

import java.util.List;

import fr.hei.devweb.cityexplorer.daos.CityDao;
import fr.hei.devweb.cityexplorer.daos.CommentDao;
import fr.hei.devweb.cityexplorer.pojos.City;
import fr.hei.devweb.cityexplorer.pojos.Comment;

public class CityService {
	
	private CityDao cityDao = new CityDao();
	private CommentDao commentDao = new CommentDao();
	
	private static class CityServiceHolder {
		private static CityService instance = new CityService();
	}
	
	public static CityService getInstance() {
		return CityServiceHolder.instance;
	}

	private CityService() {
	}
	
	public List<City> listAllCities() {
		return cityDao.listCities();
	}
	
	public City getCity(Integer id) {
		if(id == null) {
			throw new IllegalArgumentException("City id must be provided.");
		}
		return cityDao.getCity(id);
	}
	
	public void addCity(City newCity) {
		if(newCity == null){
			throw new IllegalArgumentException("A city must be provided.");
		}
		if(newCity.getName() == null || "".equals(newCity.getName())) {
			throw new IllegalArgumentException("A city must have a name.");
		}
		if(newCity.getSummary() == null || "".equals(newCity.getSummary())) {
			throw new IllegalArgumentException("A city must have a summary.");
		}
		cityDao.addCity(newCity);
	}
	
	public List<Comment> listCommentsByCity(Integer cityId) {
		if(cityId == null) {
			throw new IllegalArgumentException("City id must be provided.");
		}
		return commentDao.listComments(cityId);
	}
	
	public void addComment(Integer cityId, Comment newComment) {
		if(cityId == null) {
			throw new IllegalArgumentException("City id must be provided.");
		}
		if(newComment == null){
			throw new IllegalArgumentException("A comment must be provided.");
		}
		if(newComment.getPseudo() == null || "".equals(newComment.getPseudo())) {
			throw new IllegalArgumentException("A comment must have a pseudo.");
		}
		if(newComment.getMessage() == null || "".equals(newComment.getMessage())) {
			throw new IllegalArgumentException("A comment must have a message.");
		}
		if(newComment.getCreationDate()== null) {
			throw new IllegalArgumentException("A comment must have a creation date.");
		}
		
		commentDao.addComment(newComment, cityId);
	}

}
