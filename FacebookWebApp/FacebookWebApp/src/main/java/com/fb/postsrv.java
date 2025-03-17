package com.fb;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class postsrv extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String postContent = request.getParameter("postContent");
        Part filePart = request.getPart("postImage"); // Image file
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        // Save image to "uploads" folder
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();
        
        if (!fileName.isEmpty()) {
            filePart.write(uploadPath + File.separator + fileName);
        }
        
        try {
            // Database Connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/facebook", "root", "password");

            // Insert Post into Database
            String sql = "INSERT INTO posts (content, image) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, postContent);
            pst.setString(2, fileName);
            pst.executeUpdate();
            
            con.close();
            
            // Redirect back to post page
            response.sendRedirect("post.html");
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
