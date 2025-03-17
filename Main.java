import java.sql.*;
import java.util.Scanner;

public class Main {
    public static final String URL = "jdbc:postgresql://localhost:5432/mybd";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "PASSWORD";

    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Students");
            System.out.println("2. Edit Marks");
            System.out.println("3. Delete Students");
            System.out.println("4. Show Data");
            System.out.println("5. Exit");
            System.out.print("Enter A Choice: ");
            int a = in.nextInt();
            switch (a) {
                case 1 -> addStudent(conn);
                case 2 -> editMarks(conn);
                case 3 -> deleteStudent(conn);
                case 4 -> showData(conn);
                case 5 -> {
                    return;
                }
                default -> {
                    System.out.println("Invalid choice. Exiting...");
                    return;
                }
            }
        }
    }

    public static void addStudent(Connection conn) throws SQLException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the Student Name: ");
        String name = in.nextLine();
        System.out.println("Enter the Student Roll No.");
        int roll = in.nextInt();
        System.out.println("Enter DSA Marks: ");
        float dsa = in.nextFloat();
        System.out.println("Enter DBMS Marks: ");
        float dbms = in.nextFloat();
        System.out.println("Enter Computer Networks Marks: ");
        float cn = in.nextFloat();
        System.out.println("Enter OOP Marks: ");
        float oop = in.nextFloat();
        System.out.println("Enter Math Marks: ");
        float math = in.nextFloat();

        String sql = "INSERT INTO students (name, roll, dsa, dbms, cn, oop, math) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement smt = conn.prepareStatement(sql);
        smt.setString(1, name);
        smt.setInt(2, roll);
        smt.setFloat(3, dsa);
        smt.setFloat(4, dbms);
        smt.setFloat(5, cn);
        smt.setFloat(6, oop);
        smt.setFloat(7, math);
        int status = smt.executeUpdate();
        if (status > 0) {
            System.out.println("Student Added Successfully!");
        } else {
            System.out.println("Not Added");
        }
    }

    public static void editMarks(Connection conn) throws SQLException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Roll No. of Student Whose Marks You Want to Edit: ");
        int roll = in.nextInt();

        System.out.println("Subjects: ");
        System.out.println("1. DSA");
        System.out.println("2. DBMS");
        System.out.println("3. CN");
        System.out.println("4. OOP");
        System.out.println("5. Math");

        System.out.print("Enter the Name of Subject In Which You Want to Edit Marks: ");
        in.nextLine(); // consume the newline character left by nextInt()
        String subject = in.nextLine();

        System.out.print("Enter The New Marks: ");
        float marks = in.nextFloat();

        String sql = "UPDATE students SET " + subject.toLowerCase() + " = ? WHERE roll = ?";
        PreparedStatement smt = conn.prepareStatement(sql);
        smt.setFloat(1, marks);
        smt.setInt(2, roll);
        int status = smt.executeUpdate();
        if (status > 0) {
            System.out.println("Marks Edited Successfully!");
        } else {
            System.out.println("Error Occurred");
        }
    }

    public static void deleteStudent(Connection conn) throws SQLException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Roll No. of Student To Delete: ");
        int roll = in.nextInt();

        String sql = "DELETE FROM students WHERE roll = ?";
        PreparedStatement smt = conn.prepareStatement(sql);
        smt.setInt(1, roll);

        int status = smt.executeUpdate();
        if (status > 0) {
            System.out.println("Student Deleted Successfully!");
        } else {
            System.out.println("Student Not Found!");
        }
    }

    public static void showData(Connection conn) throws SQLException {
        String sql = "SELECT * FROM students";
        PreparedStatement smt = conn.prepareStatement(sql);
        ResultSet rs = smt.executeQuery();
        while (rs.next()) {
            System.out.println("---------------------------------------");
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Roll: " + rs.getInt("roll"));
            System.out.println("DSA: " + rs.getFloat("dsa"));
            System.out.println("DBMS: " + rs.getFloat("dbms"));
            System.out.println("CN: " + rs.getFloat("cn"));
            System.out.println("OOP: " + rs.getFloat("oop"));
            System.out.println("MATH: " + rs.getFloat("math"));
            System.out.println("---------------------------------------");
        }
    }
}
