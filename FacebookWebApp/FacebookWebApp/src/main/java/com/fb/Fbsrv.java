package com.fb;

import java.io.IOException;
import java.io.PrintWriter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt; // ✅ Import BCrypt
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/fburl")
public class Fbsrv extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Fbsrv() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        // ✅ Get form data correctly
        String username = request.getParameter("username");
        String firstName = request.getParameter("firstname");
        String surname = request.getParameter("surname");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // ✅ Debugging - Check if data is received properly
        System.out.println("Username: " + username);
        System.out.println("DOB: " + dob);

        // ✅ Validate input (Ensure no null or empty values)
        if (username == null || firstName == null || surname == null || email == null || password == null || dob == null ||
            username.isEmpty() || firstName.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty()) {
            pw.println("Error: All fields are required.");
            return;
        }

        // ✅ Encrypt the password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12)); // ✅ Secure hashing

        // ✅ Hibernate session
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        try {
            User user = new User();
            user.setUsername(username);
            user.setFname(firstName);
            user.setLname(surname);
            user.setEmail(email);
            user.setPassword(hashedPassword); // ✅ Store the hashed password
            user.setGender(gender);
            user.setDob(dob);

            session.persist(user);
            tx.commit();

            // ✅ Redirect to login page
            RequestDispatcher dispatcher = request.getRequestDispatcher("Login.html");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            tx.rollback();
            pw.println("Error: " + e.getMessage());
        } finally {
            session.close();
        }
    }
}
