package service;

import model.Address;
import model.Instructor;
import model.identity.ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static service.PersistenceService.closeStatement;

public class InstructorsService {

	private PersistenceService connectionService;

	public InstructorsService(PersistenceService connectionService) {
		this.connectionService = connectionService;
	}

	public List<Instructor> getAll() {
		List<Instructor> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM instructors ";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					result.add(createInstructor(resultSet));
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public Instructor getById(ID id) {
		Instructor result = null;
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM instructors WHERE id = ? ";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				statement.setString(1, id.toString());
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					result = createInstructor(resultSet);
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public boolean add(Instructor instructor) {
		if (getById(instructor.getId()) != null) {
			return true;
		}
		boolean result = false;
		PreparedStatement statement = null;
		try {
			String insert = "INSERT INTO instructors (id,firstName,lastName,houseNumber,streetName,zipCode,phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
			statement = connectionService.prepareStatment(insert);
			statement.setString(1, instructor.getId().toString());
			statement.setString(2, instructor.getFirstName());
			statement.setString(3, instructor.getLastName());
			statement.setInt(4, instructor.getAddress().getHouseNumber());
			statement.setString(5, instructor.getAddress().getStreetName());
			statement.setString(6, instructor.getAddress().getZipCode());
			statement.setString(7, instructor.getPhoneNumber());

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
			String sql = "DELETE FROM instructors";
			statement = connectionService.prepareStatment(sql);
			statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
	}

	private Instructor createInstructor(ResultSet resultSet)
			throws SQLException {
		String instructorId = resultSet.getString("id");
		String firstName = resultSet.getString("firstName");
		String lastName = resultSet.getString("lastName");
		int houseNumber = resultSet.getInt("houseNumber");
		String streetName = resultSet.getString("streetName");
		String zipCode = resultSet.getString("zipCode");
		String phoneNumber = resultSet.getString("phoneNumber");
		return new Instructor(new ID(instructorId), firstName, lastName,
				new Address(houseNumber, streetName, zipCode), phoneNumber);
	}

}
