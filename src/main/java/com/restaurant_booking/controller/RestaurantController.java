package com.restaurant_booking.controller;

import com.restaurant_booking.model.Restaurant;
import com.restaurant_booking.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.restaurant_booking.model.RestaurantTable;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.addRestaurant(restaurant);
    }
    @PostMapping("/{id}/tables")
    public RestaurantTable addTable(@PathVariable Long id, @RequestBody RestaurantTable table) {
        return restaurantService.addTable(id, table);
    }
    
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }
}
