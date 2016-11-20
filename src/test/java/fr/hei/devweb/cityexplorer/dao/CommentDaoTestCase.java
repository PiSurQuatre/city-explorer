package fr.hei.devweb.cityexplorer.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.internal.cglib.core.Local;
import org.junit.Test;

import fr.hei.devweb.cityexplorer.daos.CommentDao;
import fr.hei.devweb.cityexplorer.daos.DataSourceProvider;
import fr.hei.devweb.cityexplorer.pojos.City;
import fr.hei.devweb.cityexplorer.pojos.Comment;

public class CommentDaoTestCase extends AbstractDaoTestCase {
	
	private CommentDao commentDao = new CommentDao();

	@Override
	public void insertDataSet(Statement statement) throws Exception {
		statement.executeUpdate("INSERT INTO city(id, name, summary) VALUES(1, 'City 1', 'Summary 1')");
		statement.executeUpdate("INSERT INTO city(id, name, summary) VALUES(2, 'City 2', 'Summary 2')");
		statement.executeUpdate("INSERT INTO comment(id, pseudo, creationdate, message, city) VALUES(11, 'Pseudo 11', '2016-11-20', 'Message 11', 1)");
		statement.executeUpdate("INSERT INTO comment(id, pseudo, creationdate, message, city) VALUES(12, 'Pseudo 12', '2016-11-25', 'Message 12', 1)");
		statement.executeUpdate("INSERT INTO comment(id, pseudo, creationdate, message, city) VALUES(21, 'Pseudo 21', '2016-11-22', 'Message 21', 2)");
	}
	
	@Test
	public void shouldListCommentsByCity() {
		// WHEN
		List<Comment> comments = commentDao.listComments(1);
		// THEN
		Assertions.assertThat(comments).hasSize(2);
		Assertions.assertThat(comments).extracting("id", "pseudo", "creationDate", "message").containsExactly(
				Assertions.tuple(12, "Pseudo 12", LocalDate.of(2016, 11, 25), "Message 12"),
				Assertions.tuple(11, "Pseudo 11", LocalDate.of(2016, 11, 20), "Message 11")
				);
	}
	
	@Test
	public void shouldAddComment() throws Exception {
		// GIVEN 
		Comment newComment = new Comment(null, "New pseudo", LocalDate.of(2016, 11, 30), "New message");
		// WHEN
		commentDao.addComment(newComment, 1);
		// THEN
		try(Connection connection = DataSourceProvider.getInstance().getDataSource().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM comment WHERE pseudo='New pseudo'")){
			Assertions.assertThat(resultSet.next()).isTrue();
			Assertions.assertThat(resultSet.getInt("id")).isNotNull();
			Assertions.assertThat(resultSet.getString("pseudo")).isEqualTo("New pseudo");
			Assertions.assertThat(resultSet.getString("message")).isEqualTo("New message");
			Assertions.assertThat(resultSet.getDate("creationdate").toLocalDate()).isEqualTo(LocalDate.of(2016, 11, 30));
			Assertions.assertThat(resultSet.getInt("city")).isEqualTo(1);
			Assertions.assertThat(resultSet.next()).isFalse();
		}
	}

}
