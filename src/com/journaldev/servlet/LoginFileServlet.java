package com.journaldev.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/GetFile")
public class LoginFileServlet extends HttpServlet {

    private String fileName;

    public void init() throws ServletException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        fileName = request.getParameter("filename");

        request.setAttribute("fileName",fileName);

        String nextHTML = "/GetFile.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextHTML);
        dispatcher.forward(request, response);
    }

    public void destroy() {
    }

}
