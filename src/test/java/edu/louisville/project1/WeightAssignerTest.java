package edu.louisville.project1;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class WeightAssignerTest extends BaseTest {
  @Test
  public void assignsWeightsToAllRoutes() {
    WeightAssigner subject = new WeightAssigner();
    HashSet<List<City>> routes = new HashSet<>();
    routes.add(route1);
    routes.add(route2);
    routes.add(route3);

    HashMap<List<City>, Float> expectedWeightedMap = new HashMap<>();
    expectedWeightedMap.put(route1, 20f);
    expectedWeightedMap.put(route2, 24f);
    expectedWeightedMap.put(route3, 24f);

    assertEquals(
      expectedWeightedMap,
      subject.assignWeightsToRoutes(routes)
    );
  }

}