package org.smu.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.smu.connection.JDBC;
import org.smu.model.Customer;

public class JDBCCustomerDAO implements CustomerDAO{

	@Override
	public List<Customer> getAllCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		try {
			Statement statement = JDBC.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM test.Customer");

			Customer customer = null;
			while(resultSet.next()){
				customer = new Customer();
				customer.setId(resultSet.getInt("id"));
				customer.setFirstName(resultSet.getString("firstname"));
				customer.setLastName(resultSet.getString("lastName"));
				customer.setCity(resultSet.getString("city"));
				customer.setProvince(resultSet.getString("province"));
				customer.setCountry(resultSet.getString("country"));
				customer.setPostal(resultSet.getString("postal"));
				customer.setPhone(resultSet.getString("phone"));
				customer.setEmail(resultSet.getString("email"));
				customer.setAddress(resultSet.getString("address"));

				customers.add(customer);
			}
			resultSet.close();
			statement.close();

		} catch (SQLException e) {
			System.out.println(e);
		}
		return customers;
	}

	@Override
	public Customer addCustomer(Customer cust) throws SQLException {

		PreparedStatement preparedStatement = null;
		String insertCustomerSQL = "INSERT INTO test.Customer"
				+ "(firstName, lastName, city,province,country,postal,phone,email,address) VALUES"
				+ "(?,?,?,?,?,?,?,?,?)";
		try {
			preparedStatement = JDBC.getConnection().prepareStatement(insertCustomerSQL,PreparedStatement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1,cust.getFirstName());
			preparedStatement.setString(2,cust.getLastName());
			preparedStatement.setString(3,cust.getCity());
			preparedStatement.setString(4,cust.getProvince());
			preparedStatement.setString(5,cust.getCountry());
			preparedStatement.setString(6,cust.getPostal());
			preparedStatement.setString(7,cust.getPhone());
			preparedStatement.setString(8,cust.getEmail());
			preparedStatement.setString(9,cust.getAddress());

			int insertcount = preparedStatement.executeUpdate();
			if(insertcount>0) {
				ResultSet rs =preparedStatement.getGeneratedKeys();
				if (rs != null && rs.next()) {
				   int  customerId = rs.getInt(1);
				   cust.setId(customerId);
				}
			}
			
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
		return cust;
	}

	@Override
	public Customer updateCustomer(Customer cust) throws SQLException {
		PreparedStatement preparedStatement = null;
		String updateCustomerSQL = "UPDATE test.Customer SET firstName =?, lastName=?, city=?,province=?,country=?,postal=?,phone=?,email=?,address=? WHERE id=?";
		try {
				preparedStatement = JDBC.getConnection().prepareStatement(updateCustomerSQL);
				
				preparedStatement.setString(1,cust.getFirstName());
				preparedStatement.setString(2,cust.getLastName());
				preparedStatement.setString(3,cust.getCity());
				preparedStatement.setString(4,cust.getProvince());
				preparedStatement.setString(5,cust.getCountry());
				preparedStatement.setString(6,cust.getPostal());
				preparedStatement.setString(7,cust.getPhone());
				preparedStatement.setString(8,cust.getEmail());
				preparedStatement.setString(9,cust.getAddress());
				preparedStatement.setInt(10, cust.getId());
				
				preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
		return cust;
	}

	@Override
	public void deleteCustomer(int id) throws SQLException {
		PreparedStatement preparedStatement = null;
		String updateCustomerSQL = "DELETE from test.Customer WHERE id=?";
		try {
				preparedStatement = JDBC.getConnection().prepareStatement(updateCustomerSQL);
				preparedStatement.setInt(1, id);
				preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}
}
