/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.OrderDAO;
import dao.ProductDAO;
import dao.ProductOrderDetailDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.Item;
import model.Order;
import model.Product;
import model.ProductOrderDetail;

/**
 *
 * @author ADMIN
 */
public class BuyAgainServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BuyAgainServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BuyAgainServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            List<ProductOrderDetail> orderDetails = new ProductOrderDetailDAO().getAllProductOrderDetailByOrderID(id);
            HttpSession session = request.getSession();
            Cart cart = null;
            if ((Cart) session.getAttribute("cart") == null) {
                cart = new Cart();
            } else {
                cart = (Cart) session.getAttribute("cart");
            }
            
            for(ProductOrderDetail pro : orderDetails){
                int quantity = pro.getProductQuantity();
                int productID = pro.getProductID();
                Product product = new ProductDAO().getProductByID(productID);
                Item item = new Item(product, quantity);
                cart.addItemToCart(item);
            }
            
            session.setAttribute("cart", cart);
            session.setAttribute("subtotal", cart.getTotalMoney());
            session.setAttribute("count", cart.getItems().size());
            session.setMaxInactiveInterval(-1);
            List<Item> items = cart.getItems();
            request.setAttribute("items", items);

            request.getRequestDispatcher("cart.jsp").forward(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(BuyAgainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
