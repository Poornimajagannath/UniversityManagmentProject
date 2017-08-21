package service;

import model.Course;
import model.Prerequisite;
import model.identity.ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static service.PersistenceService.closeStatement;

public class CoursesService {

	private PersistenceService connectionService;

	public CoursesService(PersistenceService connectionService) {
		this.connectionService = connectionService;
	}

	public List<Course> getAll() {
		List<Course> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM courses ";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					String courseId = resultSet.getString("id");
					String name = resultSet.getString("name");
					String description = resultSet.getString("description");
					result.add(new Course(new ID(courseId), name, description));
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public Course getById(ID courseId) {
		Course result = null;
		PreparedStatement statement = null;
		try {
			String str = "SELECT * FROM courses WHERE id = ?";
			statement = connectionService.prepareStatment(str);
			if (statement != null) {
				statement.setString(1, courseId.toString());
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					String name = resultSet.getString("name");
					String description = resultSet.getString("description");
					result = new Course(courseId, name, description);
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public boolean addPrerequisite(Prerequisite prerequisite) {
		boolean result = false;
		PreparedStatement statement = null;
		try {
			String insert = "INSERT INTO prerequisites (courseId,prerequisiteId) VALUES (?, ?)";
			statement = connectionService.prepareStatment(insert);
			statement.setString(1, prerequisite.getClassId().toString());
			statement.setString(2, prerequisite.getPrerequisiteId().toString());

			result = statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public List<ID> getPrerequisites(ID courseId) {
		List<ID> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String str = "SELECT  pre.prerequisiteId FROM prerequisites pre JOIN courses cur ON (pre.courseId = cur.id) WHERE pre.courseId = ?";
			statement = connectionService.prepareStatment(str);
			if (statement != null) {
				statement.setString(1, courseId.toString());
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					String id = resultSet.getString("prerequisiteId");
					result.add(new ID(id));
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public boolean add(Course course) {
		boolean result = false;
		PreparedStatement statement = null;
		try {
			String insert = "INSERT INTO courses (id,name,description) VALUES (?, ?, ?)";
			statement = connectionService.prepareStatment(insert);
			statement.setString(1, course.getId().toString());
			statement.setString(2, course.getName());
			statement.setString(3, course.getDescription());

			int rows = statement.executeUpdate();
			result = rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;

	}

	/**
	 * Clears all entries from prerequisites and courses tables
	 */
	public void clearAll() {
		PreparedStatement statement = null;
		try {
			String prereSql = "DELETE FROM prerequisites";
			String coursesSql = "DELETE FROM courses";
			statement = connectionService.prepareStatment(prereSql);
			statement.execute();
			statement = connectionService.prepareStatment(coursesSql);
			statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
	}

}
