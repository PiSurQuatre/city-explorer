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
import fr.hei.devweb.cityexplorer.pojos.Comment;

public class CommentDao {

	public List<Comment> listComments(Integer cityId) {
		List<Comment> comments = new ArrayList<>();

		try (Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM comment WHERE city = ? ORDER BY creationDate DESC")) {
			statement.setInt(1, cityId);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					comments.add(new Comment(resultSet.getInt("id"), resultSet.getString("pseudo"),
							resultSet.getDate("creationdate").toLocalDate(), resultSet.getString("message")));
				}
			}
		} catch (SQLException e) {
			throw new CityExplorerRuntimeException("Error when getting cities", e);
		}

		return comments;
	}
}
