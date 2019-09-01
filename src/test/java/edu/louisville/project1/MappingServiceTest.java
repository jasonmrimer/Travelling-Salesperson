package edu.louisville.project1;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MappingServiceTest {
  @Test
  public void routeFromMapReturnsRoute() {
    City city1 = new City(1, 0.0d, 0.0d);
    City city2 = new City(2, 3.0d, 4.0d);
    City city3 = new City(3, 0d, 8.0d);
    City city4 = new City(4, -3.0d, 4.0d);
    Map map = new Map(List.of(
      city1,
      city2,
      city3,
      city4
    ));
    List<City> route = List.of(city1, city4, city3, city2, city1);

    MappingService mappingService = new MappingService();

    assertEquals(
      new WeightedRoute(route, 20f),
      mappingService.routeFromMap(map)
    );

  }

}