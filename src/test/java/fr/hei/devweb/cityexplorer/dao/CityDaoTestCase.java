package fr.hei.devweb.cityexplorer.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import fr.hei.devweb.cityexplorer.daos.CityDao;
import fr.hei.devweb.cityexplorer.daos.DataSourceProvider;
import fr.hei.devweb.cityexplorer.pojos.City;
import fr.hei.devweb.cityexplorer.pojos.Country;

public class CityDaoTestCase extends AbstractDaoTestCase {
	
	private CityDao cityDao = new CityDao();

	@Override
	public void insertDataSet(Statement statement) throws Exception {
		statement.executeUpdate("INSERT INTO city(id, name, summary, likes, dislikes, country, deleted, picture) VALUES(1, 'City 1', 'Summary 1', 1, 2, 'FRANCE', false, '/path/to/image1.png')");
		statement.executeUpdate("INSERT INTO city(id, name, summary, likes, dislikes, country, deleted, picture) VALUES(2, 'City 2', 'Summary 2', 3, 4, 'UK', false, '/path/to/image2.png')");
		statement.executeUpdate("INSERT INTO city(id, name, summary, likes, dislikes, country, deleted, picture) VALUES(3, 'City 3', 'Summary 3', 5, 6, 'FRANCE', true, '/path/to/image3.png')");
	}

	@Test
	public void shouldListCities() throws Exception {
		// WHEN
		List<City> cities = cityDao.listCities();
		// THEN
		Assertions.assertThat(cities).hasSize(2);
		Assertions.assertThat(cities).extracting("id", "name", "summary", "likes", "dislikes", "country").containsOnly(
				Assertions.tuple(1, "City 1", "Summary 1", 1, 2, Country.FRANCE),
				Assertions.tuple(2, "City 2", "Summary 2", 3, 4, Country.UK)
		);
	}
	
	@Test
	public void shouldListCitiesByCountry() throws Exception {
		// WHEN
		List<City> cities = cityDao.listCitiesByCountry(Country.FRANCE);
		// THEN
		Assertions.assertThat(cities).hasSize(1);
		Assertions.assertThat(cities).extracting("id", "name", "summary", "country").containsOnly(
				Assertions.tuple(1, "City 1", "Summary 1", Country.FRANCE)
		);
	}

	@Test
	public void shouldGetCity() throws Exception {
		// WHEN
		City city = cityDao.getCity(1);
		// THEN
		Assertions.assertThat(city).isNotNull();
		Assertions.assertThat(city.getId()).isEqualTo(1);
		Assertions.assertThat(city.getName()).isEqualTo("City 1");
		Assertions.assertThat(city.getSummary()).isEqualTo("Summary 1");
		Assertions.assertThat(city.getLikes()).isEqualTo(1);
		Assertions.assertThat(city.getCountry()).isEqualTo(Country.FRANCE);
		Assertions.assertThat(city.getDislikes()).isEqualTo(2);
		
	}
	
	@Test
	public void shouldNotGetDeletedCity() throws Exception {
		// WHEN
		City city = cityDao.getCity(3);
		// THEN
		Assertions.assertThat(city).isNull();
		
	}

	@Test
	public void shouldAddLike() throws Exception {
		// WHEN
		cityDao.addLike(1);
		// THEN
		try(Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM city WHERE id=1")){
			Assertions.assertThat(resultSet.next()).isTrue();
			Assertions.assertThat(resultSet.getInt("id")).isEqualTo(1);
			Assertions.assertThat(resultSet.getString("name")).isEqualTo("City 1");
			Assertions.assertThat(resultSet.getString("summary")).isEqualTo("Summary 1");
			Assertions.assertThat(resultSet.getInt("likes")).isEqualTo(2);
			Assertions.assertThat(resultSet.getInt("dislikes")).isEqualTo(2);
			Assertions.assertThat(resultSet.next()).isFalse();
		}
	}
	
	@Test
	public void shouldAddDislike() throws Exception {
		// WHEN
		cityDao.addDislike(1);
		// THEN
		try(Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM city WHERE id=1")){
			Assertions.assertThat(resultSet.next()).isTrue();
			Assertions.assertThat(resultSet.getInt("id")).isEqualTo(1);
			Assertions.assertThat(resultSet.getString("name")).isEqualTo("City 1");
			Assertions.assertThat(resultSet.getString("summary")).isEqualTo("Summary 1");
			Assertions.assertThat(resultSet.getInt("likes")).isEqualTo(1);
			Assertions.assertThat(resultSet.getInt("dislikes")).isEqualTo(3);
			Assertions.assertThat(resultSet.next()).isFalse();
		}
	}

	@Test
	public void shouldGetPicturePath() throws Exception {
		// WHEN
		String picturePath = cityDao.getPicturePath(1);
		// THEN
		Assertions.assertThat(picturePath).isEqualTo("/path/to/image1.png");
		
	}
	
	@Test
	public void shouldAddCity() throws Exception {
		// GIVEN 
		City newCity = new City(null, "My new city", "Summary for my new city", Country.FRANCE, 11, 12);
		// WHEN
		cityDao.addCity(newCity, "/path/to/picture.png");
		// THEN
		try(Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM city WHERE name='My new city'")){
			Assertions.assertThat(resultSet.next()).isTrue();
			Assertions.assertThat(resultSet.getInt("id")).isNotNull();
			Assertions.assertThat(resultSet.getString("name")).isEqualTo("My new city");
			Assertions.assertThat(resultSet.getString("summary")).isEqualTo("Summary for my new city");
			Assertions.assertThat(resultSet.getInt("likes")).isEqualTo(11);
			Assertions.assertThat(resultSet.getString("picture")).isEqualTo("/path/to/picture.png");
			Assertions.assertThat(resultSet.getString("country")).isEqualTo("FRANCE");
			Assertions.assertThat(resultSet.getInt("dislikes")).isEqualTo(12);
			Assertions.assertThat(resultSet.next()).isFalse();
		}
	}
	
	@Test
	public void shouldDeleteCity() throws Exception {
		// WHEN
		cityDao.deleteCity(1);
		// THEN
		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM city WHERE id=1")) {
			Assertions.assertThat(resultSet.next()).isTrue();
			Assertions.assertThat(resultSet.getInt("id")).isNotNull();
			Assertions.assertThat(resultSet.getString("name")).isEqualTo("City 1");
			Assertions.assertThat(resultSet.getString("summary")).isEqualTo("Summary 1");
			Assertions.assertThat(resultSet.getBoolean("deleted")).isTrue();
			Assertions.assertThat(resultSet.next()).isFalse();
		}
	}
}
