package com.example.demo.controllers;

import com.example.demo.TestUnits;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUnits.injectObjects(orderController,"orderRepository",orderRepository);
        TestUnits.injectObjects(orderController,"userRepository",userRepository);
    }

    @Test
    public void submitOrder_happy_path(){
        User user = generateTestUser();
        Item item = generateItem();
        user.setCart(generateCart(user,item));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        UserOrder order = response.getBody();
        assertEquals(order.getItems(),user.getCart().getItems());
        assertEquals(order.getUser(),user);
    }

    @Test
    public void submitOrder_sad_path(){
        User user = generateTestUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertNotEquals(null,response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_happy_Path(){
        User user = generateTestUser();
        Item item = generateItem();
        UserOrder userOrder = UserOrder.createFromCart(generateCart(user,item));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrder));
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotEquals(null,response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> orders = response.getBody();
        assertEquals(userOrder,orders.get(0));
    }

    @Test
    public void getOrders_sad_path(){
        User user = generateTestUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotEquals(null,response);
        assertEquals(404, response.getStatusCodeValue());
    }

    public User generateTestUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("ohk");
        return user;
    }

    public Item generateItem(){
        Item item = new Item();
        item.setId((long) 1);
        item.setName("book");
        item.setPrice(BigDecimal.valueOf(2.99));
        return item;
    }

    public Cart generateCart(User user, Item item){
        Cart cart = new Cart();
        cart.setId((long)0);
        cart.setUser(user);
        cart.addItem(item);
        return cart;
    }
}
