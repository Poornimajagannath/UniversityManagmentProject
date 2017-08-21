package service;

import model.Address;
import model.Student;
import model.identity.ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static service.PersistenceService.closeStatement;

public class StudentsService {

	private PersistenceService connectionService;

	public StudentsService(PersistenceService connectionService) {
		this.connectionService = connectionService;
	}

	public List<Student> getAll() {
		List<Student> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM students";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Student toAdd = createStudent(resultSet);
					result.add(toAdd);

				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public Student getById(ID studentId) {
		Student result = null;
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM students WHERE id = ?";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				statement.setString(1, studentId.toString());
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					result = createStudent(resultSet);
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public boolean add(Student student) {
		if(getById(student.getId()) != null) {
			return true;
		}
		boolean result = false;
		PreparedStatement statement = null;
		try {
			String insert = "INSERT INTO students (id,firstName,lastName,houseNumber,streetName,zipCode,phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
			statement = connectionService.prepareStatment(insert);
			statement.setString(1, student.getId().toString());
			statement.setString(2, student.getFirstName());
			statement.setString(3, student.getLastName());
			statement.setInt(4, student.getAddress().getHouseNumber());
			statement.setString(5, student.getAddress().getStreetName());
			statement.setString(6, student.getAddress().getZipCode());
			statement.setString(7, student.getPhoneNumber());

			int rows = statement.executeUpdate();
			result = rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;

	}

	public void clearAll() {
		PreparedStatement statement = null;
		try {
			String sql = "DELETE FROM students";
			statement = connectionService.prepareStatment(sql);
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
	}

	private Student createStudent(ResultSet resultSet) throws SQLException {
		String id = resultSet.getString("id");
		String firstName = resultSet.getString("firstName");
		String lastName = resultSet.getString("lastName");
		int houseNumber = resultSet.getInt("houseNumber");
		String streetName = resultSet.getString("streetName");
		String zipCode = resultSet.getString("zipCode");
		String phoneNumber = resultSet.getString("phoneNumber");
		return new Student(new ID(id), firstName, lastName,
				new Address(houseNumber, streetName, zipCode),
				phoneNumber);
	}

}
