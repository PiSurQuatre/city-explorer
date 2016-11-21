package fr.hei.devweb.cityexplorer.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.Part;

import fr.hei.devweb.cityexplorer.daos.CityDao;
import fr.hei.devweb.cityexplorer.pojos.City;

public class CityService {
	
	private CityDao cityDao = new CityDao();
	
	private static final String PICTURE_MAIN_DIRECTORY = "C:/HEI/data";
	
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

}
