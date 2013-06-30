package DButil;

import java.sql.*;

public class Sql {

	private Statement state;

	public Sql() {
		state = DataBase.getStatement();
	}

	public ResultSet executeQuery(String sqlString) {
		ResultSet rs = null;
		try {
			System.out.println(sqlString);
			rs = state.executeQuery(sqlString);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return rs;
	}

	public boolean executeUpdate(String sqlString) {
		int returnInt = 0;
		try {
			System.out.println(sqlString);
			returnInt = state.executeUpdate(sqlString);
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return (returnInt > 0 ? true : false);
	}

	public int executeForAutoID(String sqlString) {
		int returnvalue = 0;

		try {
			System.out.println(sqlString);
			state.executeQuery(sqlString);
			ResultSet rs = state.getGeneratedKeys();
			returnvalue = rs.getInt(1);
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return returnvalue;

	}

}
