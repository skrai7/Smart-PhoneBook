package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/phonebook";
        String user = "root";
        String password = "sumitiitian12"; // <-- Yahan apna password zaroor daalna

        Scanner scanner = new Scanner(System.in);

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database Connected!\n");

            boolean isRunning = true;

            while (isRunning) {
                System.out.println("===============================");
                System.out.println("📱 SMART PHONEBOOK MENU 📱");
                System.out.println("===============================");
                System.out.println("1. Add New Contacts");
                System.out.println("2. View All Contacts");
                System.out.println("3. Seaching Contacts (Smart Search)");
                System.out.println("4. Delete Contact ");
                System.out.println("5. Update Contact(Edit)");
                System.out.println("6. Exit ");
                System.out.print("Apni choice batao (1-6): ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Enter key ko consume karne ke liye

                switch (choice) {
                    case 1: // ADD CONTACT
                        System.out.print("\nName: ");
                        String name = scanner.nextLine();
                        System.out.print("Phone number: ");
                        String phone = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Address: ");
                        String address = scanner.nextLine();

                        String insertSql = "INSERT INTO contacts (name, phone, email, address) VALUES (?, ?, ?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setString(1, name);
                        insertStmt.setString(2, phone);
                        insertStmt.setString(3, email);
                        insertStmt.setString(4, address);

                        insertStmt.executeUpdate();
                        System.out.println("✅ " + name + " ka contact save ho gaya!\n");
                        break;

                    case 2: // VIEW ALL
                        System.out.println("\n--- 📞 YOUR CONTACTS LIST ---");
                        String selectSql = "SELECT * FROM contacts";
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(selectSql);

                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id") +
                                    " | Name: " + rs.getString("name") +
                                    " | Phone: " + rs.getString("phone") +
                                    " | Email: " + rs.getString("email"));
                        }
                        System.out.println("---------------------------------\n");
                        break;

                    case 3: // SMART SEARCH
                        System.out.print("\nName you want to search: ");
                        String searchName = scanner.nextLine();

                        String searchSql = "SELECT * FROM contacts WHERE name LIKE ?";
                        PreparedStatement searchStmt = conn.prepareStatement(searchSql);
                        searchStmt.setString(1, "%" + searchName + "%");

                        ResultSet searchRs = searchStmt.executeQuery();
                        System.out.println("--- 🔍 Search Results ---");
                        boolean found = false;
                        while (searchRs.next()) {
                            found = true;
                            System.out.println("ID: " + searchRs.getInt("id") +
                                    " | Name: " + searchRs.getString("name") +
                                    " | Phone: " + searchRs.getString("phone"));
                        }
                        if (!found) {
                            System.out.println("NO contact found with this name.");
                        }
                        System.out.println("-------------------------\n");
                        break;

                    case 4: // DELETE CONTACT
                        System.out.print("\nTell id of contact you want to delete: ");
                        int deleteId = scanner.nextInt();
                        scanner.nextLine();

                        String deleteSql = "DELETE FROM contacts WHERE id = ?";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                        deleteStmt.setInt(1, deleteId);

                        int rowsDeleted = deleteStmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            System.out.println("🗑️ Contact successfully deleted!\n");
                        } else {
                            System.out.println(" No contact found with this ID.\n");
                        }
                        break;

                    case 5: // UPDATE CONTACT (NEW FEATURE)
                        System.out.print("\nEnter ID you want to Update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Enter key consume karne ke liye

                        System.out.print("Enter New Name: ");
                        String newName = scanner.nextLine();
                        System.out.print("New Phone number: ");
                        String newPhone = scanner.nextLine();
                        System.out.print("New Email: ");
                        String newEmail = scanner.nextLine();
                        System.out.print("New Address: ");
                        String newAddress = scanner.nextLine();

                        // SQL Update Query
                        String updateSql = "UPDATE contacts SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);

                        updateStmt.setString(1, newName);
                        updateStmt.setString(2, newPhone);
                        updateStmt.setString(3, newEmail);
                        updateStmt.setString(4, newAddress);
                        updateStmt.setInt(5, updateId); // ID sabse aakhiri (?) ki jagah jayegi

                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("✅ Contact successfully updated!\n");
                        } else {
                            System.out.println("❌ No contact found with this ID.\n");
                        }
                        break;

                    case 6: // EXIT
                        System.out.println("Closing Phonebook. Bye!");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("❌ Wrong choice! Please choose only between 1-6.\n");
                }
            }

            conn.close();
            scanner.close();

        } catch (Exception e) {
            System.out.println("Bhai error aa gaya:");
            e.printStackTrace();
        }
    }
}