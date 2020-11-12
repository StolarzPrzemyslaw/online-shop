package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.UserDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.model.AddressDetail;
import com.codecool.shop.model.user.Address;
import com.codecool.shop.model.user.User;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/registration"}, loadOnStartup = 6)
public class RegistrationController extends HttpServlet {
	UserDao userDao = DatabaseManager.getInstance().userDao;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
		WebContext context = new WebContext(req, resp, req.getServletContext());
		engine.process("product/userRegistration.html", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(req);
		User user = getUser(req);
		userDao.add(user);
		resp.sendRedirect("/");
	}

	private User getUser(HttpServletRequest request) {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String password = request.getParameter("password");//TODO hash password
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		Address billingAddress = getBillingAddress(request);
		Address shippingAddress;
		if (request.getParameter("sameAddress").equals("on")) {
			shippingAddress = billingAddress;
		} else shippingAddress = getShippingAddress(request);
		AddressDetail addressDetail = new AddressDetail(billingAddress, shippingAddress);

		return new User(firstName, lastName, email, phone, addressDetail);
	}


	private Address getBillingAddress(HttpServletRequest request) {
		String billingCountry = request.getParameter("billingCountry");
		String billingCity = request.getParameter("billingCity");
		String billingZipCode = request.getParameter("billingZip");
		String billingAddress = request.getParameter("billingAddress");
		return new Address(billingCountry, billingCity, billingZipCode, billingAddress);
	}

	private Address getShippingAddress(HttpServletRequest request) {
		String shippingCountry = request.getParameter("shippingCountry");
		String shippingCity = request.getParameter("shippingCity");
		String shippingZip = request.getParameter("shippingZip");
		String shippingAddress = request.getParameter("shippingAddress");
		return new Address(shippingCountry, shippingCity, shippingZip, shippingAddress);
	}

}