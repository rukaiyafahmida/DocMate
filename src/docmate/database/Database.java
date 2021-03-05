package docmate.database;

import java.sql.*;

public class Database {

    private static Database database;

    private Database() {
    }

    public static Database getInstance() {

        if (database == null) {
            database = new Database();
        }

        return database;
    }

    public Connection getConnection() {
        Connection connection = null;
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=DOCMATE;user=sa;password=12345678";

        try {
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return connection;
    }

    public void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        closeResultSet(resultSet);
        closePreparedStatement(preparedStatement);
        closeConnection(connection);
    }

    public void close(Connection connection, PreparedStatement preparedStatement) {
        closePreparedStatement(preparedStatement);
        closeConnection(connection);
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
