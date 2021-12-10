package com.laioffer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.job.entity.ExampleCoordinates;
import com.laioffer.job.entity.ExampleJob;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ExampleJobServelet", value = "/example_job")
public class ExampleJobServelet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();

        // read a Job from DB and convert to Job class

        // jackson example
        ExampleCoordinates coordinates = new ExampleCoordinates(37.485130, -122.148316);
        ExampleJob job = new ExampleJob("Software Engineer", 123456, "Aug 1, 2020", false, coordinates);
        String jsonResponse = mapper.writeValueAsString(job);
        response.getWriter().print(jsonResponse);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
