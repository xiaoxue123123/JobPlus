package com.laioffer.job.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.job.db.MySQLConnection;
import com.laioffer.job.entity.HistoryRequestBody;
import com.laioffer.job.entity.Item;
import com.laioffer.job.entity.ResultResponse;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Set;

// 对应favorite的内容

@WebServlet(name = "HistoryServlet", value = "/history")
public class HistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();

        String userId = request.getParameter("user_id");

        MySQLConnection connection = new MySQLConnection();
        Set<Item> items = connection.getFavoriteItems(userId);
        connection.close();
        mapper.writeValue(response.getWriter(), items);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        // map 成 HistoryRequestBody class
        ObjectMapper mapper = new ObjectMapper();
        HistoryRequestBody body = mapper.readValue(request.getReader(), HistoryRequestBody.class);

        MySQLConnection connection = new MySQLConnection();
        connection.setFavoriteItems(body.userId, body.favorite);
        connection.close();

        ResultResponse resultResponse = new ResultResponse("SUCCESS");
        mapper.writeValue(response.getWriter(), resultResponse);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // remove favorite item
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();

        HistoryRequestBody body = mapper.readValue(request.getReader(), HistoryRequestBody.class);


        MySQLConnection connection = new MySQLConnection();
        connection.unsetFavoriteItems(body.userId, body.favorite.getId());
        connection.close();

        ResultResponse resultResponse = new ResultResponse("SUCCESS");
        mapper.writeValue(response.getWriter(), resultResponse);
    }
}
