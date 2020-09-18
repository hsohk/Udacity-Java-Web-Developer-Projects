package com.example.demo.controllers;

import com.example.demo.TestUnits;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUnits.injectObjects(cartController,"userRepository",userRepository);
        TestUnits.injectObjects(cartController,"cartRepository", cartRepository);
        TestUnits.injectObjects(cartController,"itemRepository", itemRepository);
    }

    @Test
    public void addToCart_happy_path(){
        User user = generateTestUser();
        ModifyCartRequest modifyCartRequest = generateModifyCartRequest();
        Item item = generateItem();

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(java.util.Optional.ofNullable(item));

        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertNotEquals(null, response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertEquals(cart.getItems().get(0),item);
        assertEquals(cart.getTotal(),item.getPrice().multiply( BigDecimal.valueOf(modifyCartRequest.getQuantity()) ));
    }

    @Test
    public void removeFromCart_happy_path(){
        User user = generateTestUser();
        ModifyCartRequest modifyCartRequest = generateModifyCartRequest();
        Item item = generateItem();

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(java.util.Optional.ofNullable(item));

        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertNotEquals(null, response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertEquals(cart.getItems().get(0),item);
        assertEquals(cart.getTotal(),item.getPrice().multiply( BigDecimal.valueOf(modifyCartRequest.getQuantity()) ));

        response = cartController.removeFromCart(modifyCartRequest);
        assertNotEquals(null, response);
        assertEquals(200, response.getStatusCodeValue());
        cart = response.getBody();
        assertEquals(cart.getItems().size(),0);
    }

    public User generateTestUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("ohk");
        user.setCart(new Cart());
        return user;
    }

    public Item generateItem(){
        Item item = new Item();
        item.setId((long) 1);
        item.setName("book");
        item.setPrice(BigDecimal.valueOf(2.99));
        return item;
    }

    public ModifyCartRequest generateModifyCartRequest(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("ohk");
        return modifyCartRequest;
    }
}
