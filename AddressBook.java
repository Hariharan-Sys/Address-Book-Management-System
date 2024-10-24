package address;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBook {

    private static final String URL = "jdbc:mysql://localhost:3306/AddressBookDB";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	public void addContact(Contact contact) {
        String sql = "INSERT INTO contacts (name, phoneNumber, email, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getPhoneNumber());
            pstmt.setString(3, contact.getEmail());
            pstmt.setString(4, contact.getAddress());

            pstmt.executeUpdate();
            System.out.println("Contact added successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Contact contact = new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phoneNumber"),
                        rs.getString("email"),
                        rs.getString("address")
                );
                contacts.add(contact);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contacts;
    }

    public Contact getContactByName(String name) {
        String sql = "SELECT * FROM contacts WHERE name = ?";
        Contact contact = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                contact = new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phoneNumber"),
                        rs.getString("email"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contact;
    }

    public void updateContact(Contact contact) {
        String sql = "UPDATE contacts SET phoneNumber = ?, email = ?, address = ? WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, contact.getPhoneNumber());
            pstmt.setString(2, contact.getEmail());
            pstmt.setString(3, contact.getAddress());
            pstmt.setString(4, contact.getName());

            pstmt.executeUpdate();
            System.out.println("Contact updated successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteContact(String name) {
        String sql = "DELETE FROM contacts WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Contact deleted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public Contact searchContact(String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SELECT * FROM contacts WHERE name = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Assuming your Contact constructor is defined as: Contact(String name, String phoneNumber, String email, String address)
                return new Contact(
                    resultSet.getString("name"),
                    resultSet.getString("phoneNumber"),
                    resultSet.getString("email"),
                    resultSet.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources to prevent memory leaks
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null; // Return null if no contact was found
    }


}

