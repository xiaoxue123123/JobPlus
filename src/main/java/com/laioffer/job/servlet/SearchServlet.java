package com.laioffer.job.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.job.db.MySQLConnection;
import com.laioffer.job.entity.Item;
import com.laioffer.job.external.GitHubClient;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("user_id");
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));



        MySQLConnection connection = new MySQLConnection();
        Set<String> favoritedItemIds = connection.getFavoriteItemIds(userId);
        connection.close();


        GitHubClient client = new GitHubClient();
        response.setContentType("application/json");

        // search API returns a list of items
        List<Item> items = client.search(lat, lon, null);

        for (Item item : items) {
            boolean favorate = favoritedItemIds.contains(item.getId());
            item.setFavorite(favorate);
        }


        /**
         *  中间可以加处理逻辑
         * **/

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.getWriter().print(mapper.writeValueAsString(items));
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
