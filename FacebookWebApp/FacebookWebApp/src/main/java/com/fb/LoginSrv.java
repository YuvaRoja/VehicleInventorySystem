package com.fb;

import java.io.IOException;
import java.io.PrintWriter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt; // ✅ Import BCrypt
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.query.Query;

@WebServlet("/login")
public class LoginSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginSrv() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        // ✅ Get login data
        String uname = request.getParameter("uname");
        String enteredPassword = request.getParameter("password");

        // ✅ Validate input
        if (uname == null || enteredPassword == null || uname.isEmpty() || enteredPassword.isEmpty()) {
            pw.println("Error: Username and password are required.");
            return;
        }

        // ✅ Hibernate session
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            // ✅ Query to fetch user by username only
            Query<User> query = session.createQuery("FROM User WHERE username = :uname", User.class);
            query.setParameter("uname", uname);
            User user = query.uniqueResult();

            if (user != null) {
                String storedHashedPassword = user.getPassword(); // ✅ Get hashed password from DB

                // ✅ Check if entered password matches the stored hashed password
                if (BCrypt.checkpw(enteredPassword, storedHashedPassword)) {
                    // ✅ Store user in session
                    HttpSession httpSession = request.getSession();
                    httpSession.setAttribute("username", uname);

                    // ✅ Redirect to post page
                    response.sendRedirect("Post.html");
                } else {
                    pw.println("Invalid password.");
                }
            } else {
                pw.println("Invalid username.");
            }
        } catch (Exception e) {
            pw.println("Error: " + e.getMessage());
        } finally {
            session.close();
        }
    }
}
