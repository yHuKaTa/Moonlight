package com.aacdemy.moonlight.runner;

import com.aacdemy.moonlight.entity.restaurant.TableRestaurant;
import com.aacdemy.moonlight.entity.restaurant.TableZone;
import com.aacdemy.moonlight.repository.restaurant.TableRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TableRestaurantRunner implements CommandLineRunner {
    @Autowired
    TableRestaurantRepository tableRestaurantRepository;

    @Override
    public void run(String... args) throws Exception {
        if (tableRestaurantRepository.count() == 0) {

            List<TableRestaurant> tables = new ArrayList<TableRestaurant>();

            tables.add(new TableRestaurant(1L,1, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(2L,2, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(3L,3, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(4L,4, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(5L,5, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(6L,6, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(7L,7, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(8L,8, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(9L,9, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(10L,10, TableZone.SALON, false, 4));
            tables.add(new TableRestaurant(11L,11, TableZone.TERRACE, true, 4));
            tables.add(new TableRestaurant(12L,12, TableZone.TERRACE, true, 4));
            tables.add(new TableRestaurant(13L,13, TableZone.TERRACE, true, 4));
            tables.add(new TableRestaurant(14L,14, TableZone.TERRACE, true, 4));
            tables.add(new TableRestaurant(15L,15, TableZone.TERRACE, true, 4));
            tables.add(new TableRestaurant(16L,16, TableZone.TERRACE, true, 4));
            tables.add(new TableRestaurant(17L,1, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(18L,2, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(19L,3, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(20L,4, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(21L,5, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(22L,6, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(23L,7, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(24L,8, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(25L,9, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(26L,10, TableZone.BAR, false, 1));
            tables.add(new TableRestaurant(27L,11, TableZone.BAR, false, 1));

            tableRestaurantRepository.saveAll(tables);
        }
    }
}
