package com.restaurant_booking.service;

import com.restaurant_booking.model.Restaurant;
import com.restaurant_booking.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.restaurant_booking.model.RestaurantTable;
import com.restaurant_booking.repository.RestaurantTableRepository;
import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantTableRepository tableRepository;

    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
    public RestaurantTable addTable(Long restaurantId, RestaurantTable table){
      //finding the restaurant
      Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found"));
      table.setRestaurant(restaurant); //linking table
      return tableRepository.save(table); //saving the table
    }
}
