package address;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddressBookGUI {
    private AddressBook addressBook;
    private JFrame frame;
    private JTextField nameField, phoneField, emailField, addressField;
    private JTable contactTable;
    private String selectedContactName; // To keep track of the contact being edited

    public AddressBookGUI() {
        addressBook = new AddressBook();
        initialize();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddressBookGUI window = new AddressBookGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        // Main Frame
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Labels
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(10, 10, 80, 25);
        frame.getContentPane().add(nameLabel);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(10, 40, 80, 25);
        frame.getContentPane().add(phoneLabel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 70, 80, 25);
        frame.getContentPane().add(emailLabel);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(10, 100, 80, 25);
        frame.getContentPane().add(addressLabel);

        // Text fields for user input
        nameField = new JTextField();
        nameField.setBounds(100, 10, 160, 25);
        frame.getContentPane().add(nameField);

        phoneField = new JTextField();
        phoneField.setBounds(100, 40, 160, 25);
        frame.getContentPane().add(phoneField);

        emailField = new JTextField();
        emailField.setBounds(100, 70, 160, 25);
        frame.getContentPane().add(emailField);

        addressField = new JTextField();
        addressField.setBounds(100, 100, 160, 25);
        frame.getContentPane().add(addressField);

        // Buttons for actions
        JButton addButton = new JButton("Add Contact");
        addButton.setBounds(10, 140, 120, 25);
        frame.getContentPane().add(addButton);

        JButton viewButton = new JButton("View Contacts");
        viewButton.setBounds(150, 140, 120, 25);
        frame.getContentPane().add(viewButton);

        JButton loadEditButton = new JButton("Load for Edit");
        loadEditButton.setBounds(10, 170, 120, 25);
        frame.getContentPane().add(loadEditButton);

        JButton updateButton = new JButton("Update Contact");
        updateButton.setBounds(150, 170, 150, 25);
        frame.getContentPane().add(updateButton);
        
        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.setBounds(290, 140, 150, 25);
        frame.getContentPane().add(deleteButton);

        // Contact Table to display contacts
        contactTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setBounds(10, 210, 560, 150);
        frame.getContentPane().add(scrollPane);

        // Button action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewContacts();
            }
        });

        loadEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadContactForEdit();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateContact();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });
    }

    private void addContact() {
        // Fetch data from input fields
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();

        // Add the contact to the database
        Contact contact = new Contact(name, phone, email, address);
        addressBook.addContact(contact);

        // Clear input fields
        clearInputFields();

        JOptionPane.showMessageDialog(frame, "Contact added successfully!");
    }

    private void viewContacts() {
        // Fetch all contacts from the database
        List<Contact> contacts = addressBook.getAllContacts();

        // Create table model from contacts
        String[] columns = {"Name", "Phone", "Email", "Address"};
        String[][] data = new String[contacts.size()][4];
        for (int i = 0; i < contacts.size(); i++) {
            data[i][0] = contacts.get(i).getName();
            data[i][1] = contacts.get(i).getPhoneNumber();
            data[i][2] = contacts.get(i).getEmail();
            data[i][3] = contacts.get(i).getAddress();
        }

        contactTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }

    private void loadContactForEdit() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow != -1) {
            // Get the selected contact's details from the table
            String name = (String) contactTable.getValueAt(selectedRow, 0);
            String phone = (String) contactTable.getValueAt(selectedRow, 1);
            String email = (String) contactTable.getValueAt(selectedRow, 2);
            String address = (String) contactTable.getValueAt(selectedRow, 3);

            // Load the details into the input fields
            nameField.setText(name);
            phoneField.setText(phone);
            emailField.setText(email);
            addressField.setText(address);

            // Store the selected contact's name to identify it for updating
            selectedContactName = name;
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a contact to edit.");
        }
    }

    private void updateContact() {
        // Fetch updated data from input fields
        String newName = nameField.getText();
        String newPhone = phoneField.getText();
        String newEmail = emailField.getText();
        String newAddress = addressField.getText();

        if (selectedContactName != null) {
            // Get the contact from the database using the original name
            Contact contact = addressBook.searchContact(selectedContactName);

            if (contact != null) {
                // Update the contact details
                contact.setName(newName);
                contact.setPhoneNumber(newPhone);
                contact.setEmail(newEmail);
                contact.setAddress(newAddress);

                // Save the updated contact in the database
                addressBook.updateContact(contact);

                // Clear input fields
                clearInputFields();

                // Refresh contact list
                viewContacts();

                JOptionPane.showMessageDialog(frame, "Contact updated successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No contact selected for editing.");
        }
    }
    
    private void deleteContact() {
        // Fetch updated data from input fields
    	 int selectedRow = contactTable.getSelectedRow();
         if (selectedRow != -1) {
             // Get the selected contact's details from the table
             String name = (String) contactTable.getValueAt(selectedRow, 0);

             selectedContactName = name;

             if (selectedContactName != null) {
                 // Get the contact from the database using the original name
                 Contact contact = addressBook.searchContact(selectedContactName);

                 if (contact != null) {
                     // Update the contact details
                     String n1=contact.getName();

                     // Save the updated contact in the database
                     addressBook.deleteContact(n1);

                     // Clear input fields
                     clearInputFields();

                     // Refresh contact list
                     viewContacts();

                     JOptionPane.showMessageDialog(frame, "Contact deleted successfully!");
                 }
             } else {
                 JOptionPane.showMessageDialog(frame, "No contact selected for deleting.");
             }
         } else {
             JOptionPane.showMessageDialog(frame, "Please select a contact to delete.");
         }
//        String newName = nameField.getText();
//        selectedContactName=newName;
//
//        if (selectedContactName != null) {
//            // Get the contact from the database using the original name
//            Contact contact = addressBook.searchContact(selectedContactName);
//
//            if (contact != null) {
//                // Update the contact details
//                String n1=contact.getName();
//
//                // Save the updated contact in the database
//                addressBook.deleteContact(n1);
//
//                // Clear input fields
//                clearInputFields();
//
//                // Refresh contact list
//                viewContacts();
//
//                JOptionPane.showMessageDialog(frame, "Contact deleted successfully!");
//            }
//        } else {
//            JOptionPane.showMessageDialog(frame, "No contact selected for deleting.");
//        }
    }
    



    private void clearInputFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        selectedContactName = null;
    }
}
