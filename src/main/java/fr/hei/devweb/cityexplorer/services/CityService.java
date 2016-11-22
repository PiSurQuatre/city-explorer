package fr.hei.devweb.cityexplorer.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.Part;

import fr.hei.devweb.cityexplorer.daos.CityDao;
import fr.hei.devweb.cityexplorer.daos.CommentDao;
import fr.hei.devweb.cityexplorer.pojos.City;
import fr.hei.devweb.cityexplorer.pojos.Comment;
import fr.hei.devweb.cityexplorer.pojos.Country;

public class CityService {
	
	private CityDao cityDao = new CityDao();
	private CommentDao commentDao = new CommentDao();
	
	private static final String PICTURE_MAIN_DIRECTORY = "C:/HEI/data";
	
	private static class CityServiceHolder {
		private static CityService instance = new CityService();
	}
	
	public static CityService getInstance() {
		return CityServiceHolder.instance;
	}

	private CityService() {
	}
	
	public List<City> listAllCities(Country country) {
		if(country == null) {
			return cityDao.listCities();
		} else {
			return cityDao.listCitiesByCountry(country);
		}
	}
	
	public City getCity(Integer id) {
		if(id == null) {
			throw new IllegalArgumentException("City id must be provided.");
		}
		return cityDao.getCity(id);
	}
	
	public void likeCity(Integer id) {
		if(id == null) {
			throw new IllegalArgumentException("City id must be provided.");
		}
		cityDao.addLike(id);
	}
	
	public void dislikeCity(Integer id) {
		if(id == null) {
			throw new IllegalArgumentException("City id must be provided.");
		}
		cityDao.addDislike(id);
	}
	
	public void addCity(City newCity, Part picture) throws IOException {
		if(newCity == null){
			throw new IllegalArgumentException("A city must be provided.");
		}
		if(newCity.getName() == null || "".equals(newCity.getName())) {
			throw new IllegalArgumentException("A city must have a name.");
		}
		if(newCity.getSummary() == null || "".equals(newCity.getSummary())) {
			throw new IllegalArgumentException("A city must have a summary.");
		}
		if(newCity.getCountry() == null) {
			throw new IllegalArgumentException("A city must have a country.");
		}
		if(picture == null) {
			throw new IllegalArgumentException("A city must have a picture.");
		}
		
		Path picturePath = Paths.get(PICTURE_MAIN_DIRECTORY, picture.getSubmittedFileName());
		
		cityDao.addCity(newCity, picturePath.toString());
		
		Files.copy(picture.getInputStream(), picturePath);
	}
	
	public Path getPicturePatch(Integer cityId) {
		String picturePathString = cityDao.getPicturePath(cityId);
		if(picturePathString == null) {
			return getDefaultPicturePath();
		} else {
			Path picturePath = Paths.get(cityDao.getPicturePath(cityId));
			if(Files.exists(picturePath)) {
				return picturePath;
			} else {
				return getDefaultPicturePath();
			}
		}
		
	}
	
	private Path getDefaultPicturePath() {
		try {
			return Paths.get(this.getClass().getClassLoader().getResource("city-no-photo.png").toURI());
		} catch (URISyntaxException e) {
			return null;
		}
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
	
	public void deleteCity(Integer cityId) {
		if(cityId == null) {
			throw new IllegalArgumentException("City id must be provided.");
		}
		cityDao.deleteCity(cityId);
	}

}
