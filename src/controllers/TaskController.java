package controllers;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import dbutil.DBHelper;

import model.Record;

public class TaskController {

	public static void addTasks(List<Record> records, int userID)
			throws SQLException {

		String query = "INSERT INTO task (date, user, record, resource, type) VALUES ";
		int j = 1;

		for (Record r : records) {
			for (String res : r.getWebResources()) {
				query += "(?,?,?,?,?)";

				if (records.indexOf(r) == records.size() - 1
						&& r.getWebResources().indexOf(res) == r
								.getWebResources().size() - 1)
					query += ";";
				else
					query += ",";
			}

		}

		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(query);
		for (Record r : records) {
			int id = DBHelper.getRecordID(r);
			if (id != -1) {

				for (String s : r.getWebResources()) {
					statement.setDate(j,
							new java.sql.Date(((new Date()).getTime())));
					j++;
					statement.setInt(j, userID);
					j++;
					statement.setInt(j, id);
					j++;
					statement.setString(j, s);
					j++;
					statement.setString(j, r.getType());
					j++;

				}

			}
		}

		statement.executeUpdate();

	}

}
