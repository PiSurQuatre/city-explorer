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

public class CityDao {

	public List<City> listCities() {
		List<City> cities = new ArrayList<City>();

		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM city WHERE deleted=false ORDER BY name")) {
			while (resultSet.next()) {
				cities.add(
						new City(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("summary")));
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
					return new City(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("summary"));
				}
			}
		} catch (SQLException e) {
			throw new CityExplorerRuntimeException("Error when getting cities", e);
		}
		return null;
	}
	
	public void addCity(City newCity) {
		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT INTO city(name, summary) VALUES (?, ?)")) {
			statement.setString(1, newCity.getName());
			statement.setString(2, newCity.getSummary());
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
