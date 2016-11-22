package fr.hei.devweb.cityexplorer.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.hei.devweb.cityexplorer.exceptions.CityExplorerRuntimeException;
import fr.hei.devweb.cityexplorer.pojos.City;
import fr.hei.devweb.cityexplorer.pojos.Country;

public class CityDao {

	public List<City> listCities() {
		List<City> cities = new ArrayList<City>();

		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM city WHERE deleted=false ORDER BY name")) {
			while (resultSet.next()) {
				Country country = Country.valueOf(resultSet.getString("country"));
				cities.add(new City(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("summary"),
						country, resultSet.getInt("likes"), resultSet.getInt("dislikes")));
			}
		} catch (SQLException e) {
			throw new CityExplorerRuntimeException("Error when getting cities", e);
		}

		return cities;
	}

	public List<City> listCitiesByCountry(Country country) {
		List<City> cities = new ArrayList<City>();

		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM city WHERE country = ? ORDER BY name")) {
			statement.setString(1, country.name());
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					cities.add(new City(resultSet.getInt("id"), resultSet.getString("name"),
							resultSet.getString("summary"), Country.valueOf(resultSet.getString("country")), 
									resultSet.getInt("likes"), resultSet.getInt("dislikes")));
				}
			}
		} catch (SQLException e) {
			throw new CityExplorerRuntimeException("Error when getting cities", e);
		}

		return cities;
	}

	public City getCity(Integer id) {
		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM city WHERE id = ? AND deleted=false")) {
			statement.setInt(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Country country = Country.valueOf(resultSet.getString("country"));
					return new City(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("summary"),
							country, resultSet.getInt("likes"), resultSet.getInt("dislikes"));
				}
			}
		} catch (SQLException e) {
			throw new CityExplorerRuntimeException("Error when getting cities", e);
		}
		return null;
	}

	public void addLike(Integer cityId) {
		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				PreparedStatement statement = connection.prepareStatement("UPDATE city SET likes=likes+1 WHERE id=?")) {
			statement.setInt(1, cityId);
			statement.executeUpdate();			
		} catch (SQLException e) {
			throw new CityExplorerRuntimeException("Error when liking city", e);
		}
	}
	
	public void addDislike(Integer cityId) {
		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				PreparedStatement statement = connection.prepareStatement("UPDATE city SET dislikes=dislikes+1 WHERE id=?")) {
			statement.setInt(1, cityId);
			statement.executeUpdate();			
		} catch (SQLException e) {
			throw new CityExplorerRuntimeException("Error when disliking city", e);
		}
	}
	
	
	
	public void addCity(City newCity) {
		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT INTO city(name, summary, likes, dislikes, country) VALUES (?, ?, ?, ?, ?)")) {
			statement.setString(1, newCity.getName());
			statement.setString(2, newCity.getSummary());
			statement.setInt(3, newCity.getLikes());
			statement.setInt(4, newCity.getDislikes());
			statement.setString(5, newCity.getCountry().name());

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CityExplorerRuntimeException("Error when getting cities", e);
		}
	}
	
	public void deleteCity(Integer cityId) {
		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				PreparedStatement statement = connection.prepareStatement("UPDATE city SET deleted=true WHERE id=?")) {
			statement.setInt(1, cityId);
			statement.executeUpdate();	
		} catch (SQLException e) {
			throw new CityExplorerRuntimeException("Error when deleting cities", e);
		}
	}
}
