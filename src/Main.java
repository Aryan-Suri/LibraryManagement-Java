import java.util.Scanner;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;

import connect.Connector;

public class Main {
	static boolean run = true;

	public static void main(String[] args) throws ParseException {
		Scanner scan = new Scanner(System.in);
		while (run) {
			System.out.println("Select one of the options: " + "\n" + "1. Student 2. Librarian");
			int userInfo = scan.nextInt();

			// user is a student
			if (userInfo == 1) {
				System.out.println("Enter the Student ID: ");
				int studentId = scan.nextInt();
				// String studentPassword = "STUDENT" + studentId;

				System.out.println("Enter your password");
				String getPassword = scan.next();

				try {
					Connection con = Connector.getCon();
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(
							"select * from person where id = '" + studentId + "'and password = '" + getPassword + "'");
					if (rs.next()) {
						System.out.println("Successfully Logged In" + "\n");
						System.out.println("What would you like to do : " + "\n"
								+ "1. Add Book   2. Return Book   3. History   4. Exit");
						int studentInfo = scan.nextInt();

						if (studentInfo == 1) {
							System.out.println("Enter the Book ID: ");
							int bookId = scan.nextInt();
							long now = System.currentTimeMillis();
							Date dateNow = new Date(now);
							// use 1 month after the current date for the return date
							LocalDate ld = LocalDate.now();
							LocalDate monthLater = ld.plusMonths(1);
							java.sql.Date returnDate = java.sql.Date.valueOf(monthLater);
							String returned = "no";

							try {
								// Connection con = Connector.getCon();
								// Statement st = con.createStatement();
								ResultSet rst = st.executeQuery("Select * from book where id = '" + bookId + "'");
								if (rst.next()) {
									st.executeUpdate("insert into issuebook values('" + bookId + "', '" + studentId
											+ "', '" + dateNow + "', '" + returnDate + "', '" + returned + "')");
									System.out.println("Book successfully added");
									System.out.println();
								} else {
									System.out.println("No Book with this ID exists.");
									run = false;
								}
							} catch (Exception e) {
								System.out.println(e);
							}
						} else if (studentInfo == 2) {
							System.out.println("Enter the Book ID:");
							String bookId = scan.next();
							try {
								// Connection con = Connector.getCon();
								// Statement st = con.createStatement();
								ResultSet rs1 = st.executeQuery("select * from issuebook where bookId = '" + bookId
										+ "'and studentId =  '" + studentId + "'");

								if (rs1.next()) {
									st.executeUpdate("update issuebook set returned = 'Yes' where bookId = '" + bookId
											+ "'and studentId ='" + studentId + "'");
									System.out.println("Book Successfully Returned");
									System.out.println();
								} else {
									System.out.println("No Book ID cooresponds with the Student ID");
									run = false;
								}
							} catch (Exception e) {
								System.out.println(e);
							}

						}

						// User selects 3
						else if (studentInfo == 3) {
							try {
								// Connection con = Connector.getCon();
								// Statement st = con.createStatement();

								// finds all the books that havent been returned
								String no = "no";
								String yes = "Yes";
								ResultSet rst2 = st.executeQuery("select * from issuebook where studentId = '"
										+ studentId + "'and returned = '" + no + "'");
								ResultSetMetaData rsmd = rst2.getMetaData();
								int columnNumber = rsmd.getColumnCount();

								System.out.println("Books Taken Out: ");
								System.out.println(
										"---------------------------------------------------------------------------");
								System.out.println(
										"Book ID   Student ID      Issue Date           Return Date       Returned");
								System.out.println(
										"---------------------------------------------------------------------------");

								// Loops through all the books that havent been returned and prints their info
								while (rst2.next()) {
									for (int i = 1; i <= columnNumber; i++) {
										System.out.print("   " + rst2.getString(i) + "        ");
									}
									System.out.println();
								}
								System.out.println(
										"---------------------------------------------------------------------------");
								System.out.println();

								// Finds all the books that have been returned
								ResultSet rs1 = st.executeQuery("select * from issuebook where studentId = '"
										+ studentId + "'and returned = '" + yes + "'");
								ResultSetMetaData rsmd1 = rs1.getMetaData();
								int columnNumber1 = rsmd1.getColumnCount();

								System.out.println("Books Returned: ");
								System.out.println(
										"---------------------------------------------------------------------------");
								System.out.println(
										"Book ID   Student ID      Issue Date           Return Date       Returned");
								System.out.println(
										"---------------------------------------------------------------------------");

								// Loops through all the books that have been returned and prints out their info
								while (rs1.next()) {
									for (int i = 1; i <= columnNumber1; i++) {
										System.out.print("   " + rs1.getString(i) + "        ");
									}
									System.out.println();
								}
								System.out.println(
										"---------------------------------------------------------------------------");

							} catch (Exception e) {
								System.out.println(e);
							}
						}

						// User selects any other number
						else {
							System.out.println("Successfully Logged Out");
							run = false;
						}
					} else {
						System.out.println("Wrong Username or Password");
						run = false;
					}
				} catch (Exception e) {
					System.out.println(e);
				}

			} else

			// user chooses the librarian
			if (userInfo == 2) {

				// Asks the user for the username and password
				System.out.println("Enter the username(Case Sensitive)");
				String librarianUser = scan.next();
				System.out.println("Enter the password(Case Sensitive)");
				String librarianPass = scan.next();

				// If password is wrong, it prints the error message
				if (librarianUser.compareTo("Admin") < 0 || librarianPass.compareTo("ADMIN") < 0) {
					System.out.println("Wrong Username or Password" + "\n" + "Please Try Again");
					run = false;
				} else {

					// gives user the prompts if the passsword is correct
					System.out.println("What would you like to do: " + "\n"
							+ "1. Add Student  2. Add Book  3. Assign Book  4. Return Book  5. All Information  6. Logout");
					int librarianInfo = scan.nextInt();

					// ADDS A STUDENT TO DATABASE IF USER SELECTS 1
					if (librarianInfo == 1) {
						System.out.println("Enter id: ");
						int id = scan.nextInt();
						System.out.println("Enter name: ");
						String name = scan.next();
						System.out.println("Enter age: ");
						int age = scan.nextInt();
						System.out.println("Enter the password: ");
						String password = scan.next();

						try {
							Connection con = Connector.getCon();
							Statement st = con.createStatement();
							st.executeUpdate("insert into person values('" + id + "', '" + name + "', '" + age + "', '"
									+ password + "')");
							System.out.println("Successfully Updated");
						} catch (Exception e) {
							System.out.println(e);
						}
					}

					// ADDS BOOK TO DATABSE IF USER SELECTS 2
					else if (librarianInfo == 2) {
						System.out.println("Enter Book id: ");
						int bookId = scan.nextInt();
						System.out.println("Enter Book name: ");
						String bookName = scan.next();
						System.out.println("Enter Book author: ");
						String bookAuthor = scan.next();
						System.out.println("Enter Book subject: ");
						String Subject = scan.next();

						try {
							Connection con = Connector.getCon();
							Statement st = con.createStatement();
							st.executeUpdate("insert into Book values('" + bookId + "', '" + bookName + "', '"
									+ bookAuthor + "', '" + Subject + "')");
							System.out.println("Successfully Updated");
						} catch (Exception e) {
							System.out.println(e);
						}
					}

					// ASSIGNS A BOOK TO THE STUDENT IF USER SELECTS 3
					else if (librarianInfo == 3) {
						System.out.println("Enter Book ID: ");
						int issueBookID = scan.nextInt();
						System.out.println("Enter User ID: ");
						int issuePersonID = scan.nextInt();
						// uses the current date for the issue date
						long now = System.currentTimeMillis();
						Date dateNow = new Date(now);
						// use 1 month after the current date for the return date
						LocalDate ld = LocalDate.now();
						LocalDate monthLater = ld.plusMonths(1);
						java.sql.Date returnDate = java.sql.Date.valueOf(monthLater);
						String returned = "no";
						try {
							Connection con = Connector.getCon();
							Statement st = con.createStatement();
							ResultSet rs = st.executeQuery("select * from book where id = '" + issueBookID + "'");
							// makes sure that the book id entered matches a book id in table
							if (rs.next()) {
								ResultSet rs1 = st
										.executeQuery("select * from person where id = '" + issuePersonID + "'");
								// makes sure the student id entered matches the student id in table
								if (rs1.next()) {
									// Assigns the book to the student
									st.executeUpdate("insert into issuebook values('" + issueBookID + "', '"
											+ issuePersonID + "', '" + dateNow + "', '" + returnDate + "', '" + returned
											+ "')");
									System.out.println("Successfully Updated");
								} else {
									System.out.println("Incorrect Student ID");
									run = false;
								}
							} else {
								System.out.println("Incorrect Book ID ");
								run = false;
							}
						} catch (Exception e) {
							System.out.println(e);
						}

					}

					// RETURNS A BOOK RENTED BY THE STUDENT IF USER SELECTS 4
					else if (librarianInfo == 4) {
						System.out.println("Enter the book ID: ");
						int returnBookID = scan.nextInt();
						System.out.println("Enter the student ID: ");
						int returnStudentID = scan.nextInt();
						try {
							Connection con = Connector.getCon();
							Statement st = con.createStatement();

							// finds the book that the user wants to return
							ResultSet rs = st.executeQuery("select * from issuebook where bookId = '" + returnBookID
									+ "' and studentId = '" + returnStudentID + "'");

							// checks if the book id and student id match and changes the returned to "yes"
							if (rs.next()) {
								st.executeUpdate("update issuebook set returned = 'Yes' where studentId = '"
										+ returnStudentID + "'and bookId = '" + returnBookID + "'");
								System.out.println("Succesfully Updated");
							} else {
								System.out.println("incorrect student or book ID");
								run = false;
							}
						} catch (Exception e) {
							System.out.println(e);
						}
					}

					// PRINTS OUT ALL THE INFORMSTION IF USER SELECTS 5
					else if (librarianInfo == 5) {
						try {
							Connection con = Connector.getCon();
							Statement st = con.createStatement();

							// finds all the books that havent been returned
							ResultSet rs = st.executeQuery("select * from issuebook where issuebook.returned = 'no'");
							ResultSetMetaData rsmd = rs.getMetaData();
							int columnNumber = rsmd.getColumnCount();

							System.out.println("Books Taken Out: ");
							System.out.println(
									"---------------------------------------------------------------------------");
							System.out.println(
									"Book ID   Student ID      Issue Date           Return Date       Returned");
							System.out.println(
									"---------------------------------------------------------------------------");

							// Loops through all the books that havent been returned and prints their info
							while (rs.next()) {
								for (int i = 1; i <= columnNumber; i++) {
									System.out.print("   " + rs.getString(i) + "        ");
								}
								System.out.println();
							}
							System.out.println(
									"---------------------------------------------------------------------------");
							System.out.println();

							// Finds all the books that have been returned
							ResultSet rs1 = st.executeQuery("select * from issuebook where issuebook.returned = 'Yes'");
							ResultSetMetaData rsmd1 = rs1.getMetaData();
							int columnNumber1 = rsmd1.getColumnCount();

							System.out.println("Books Returned: ");
							System.out.println(
									"---------------------------------------------------------------------------");
							System.out.println(
									"Book ID   Student ID      Issue Date           Return Date       Returned");
							System.out.println(
									"---------------------------------------------------------------------------");

							// Loops through all the books that have been returned and prints out their info
							while (rs1.next()) {
								for (int i = 1; i <= columnNumber1; i++) {
									System.out.print("   " + rs1.getString(i) + "        ");
								}
								System.out.println();
							}
							System.out.println(
									"---------------------------------------------------------------------------");

						} catch (Exception e) {
							System.out.println(e);
						}

					}

					// SUCCESSFULLY LOGS THE USER OUT IF ANY OTHER KEY IS PRESSED
					else {
						System.out.println("Successfully Logged Out.");
						run = false;
					}

				}
			} else {
				System.out.println("Wrong prompt chosen");
				run = false;
			}
		}
	}
}
