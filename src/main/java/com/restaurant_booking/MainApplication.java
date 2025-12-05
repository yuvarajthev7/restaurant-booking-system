package com.restaurant_booking;

import com.restaurant_booking.model.Restaurant;
import com.restaurant_booking.model.RestaurantTable;
import com.restaurant_booking.model.User;
import com.restaurant_booking.repository.RestaurantRepository;
import com.restaurant_booking.repository.RestaurantTableRepository;
import com.restaurant_booking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataSeeder(
            UserRepository userRepo,
            PasswordEncoder encoder,
            RestaurantRepository restaurantRepo,
            RestaurantTableRepository tableRepo
    ) {
        return args -> {
            // 1. Create Admin
            if (userRepo.findByEmail("admin@test.com").isEmpty()) {
                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEmail("admin@test.com");
                admin.setGovernmentId("ADMIN001");
                admin.setPassword(encoder.encode("password123"));
                admin.setRole("ADMIN");
                userRepo.save(admin);
                System.out.println("-> ADMIN CREATED: admin@test.com");
            }

            // 2. Create Regular User
            if (userRepo.findByEmail("user@test.com").isEmpty()) {
                User user = new User();
                user.setFirstName("John");
                user.setLastName("Doe");
                user.setEmail("user@test.com");
                user.setGovernmentId("USER123");
                user.setPassword(encoder.encode("password123"));
                user.setRole("USER");
                userRepo.save(user);
                System.out.println("-> USER CREATED: user@test.com");
            }

            // 3. Create Default Restaurant (Fix for "No Restaurants Showing")
            if (restaurantRepo.count() == 0) {
                Restaurant place = new Restaurant();
                place.setName("Pizza Palace");
                place.setAddress("123 Cheese St");
                place.setPhoneNumber("555-0199");
                restaurantRepo.save(place);

                // Add a Table to it
                RestaurantTable table = new RestaurantTable();
                table.setTableNumber(1);
                table.setSeatCount(4);
                table.setRestaurant(place);
                tableRepo.save(table);

                System.out.println("-> RESTAURANT CREATED: Pizza Palace with Table #1");
            }
        };
    }

    @RestController
    class TestController {
        @GetMapping("/test")
        public String test() {
            return "Server is working!";
        }
    }
}
