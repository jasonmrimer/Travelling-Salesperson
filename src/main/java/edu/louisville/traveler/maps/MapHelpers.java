package edu.louisville.traveler.maps;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

class MapHelpers {
  City findNearestCity(City start, List<City> cities) {
    City nearestCity = null;
    float bestDistance = Float.MAX_VALUE;
    List<City> citiesWithoutStartingCity = new ArrayList<>(cities);
    citiesWithoutStartingCity.remove(start);
    for (City city : citiesWithoutStartingCity) {
      double distance = calculateDistance(start, city);
      if (distance < bestDistance) {
        bestDistance = (float) distance;
        nearestCity = city;
      }
    }
    return nearestCity;
  }

  CityAndEdge findNearestCity(HashSet<Edge> edges, List<City> cities) {
    City nearestCity = null;
    float bestDistance = Float.MAX_VALUE;
    List<Edge> closestEdges = new ArrayList<>();
    for (City city : cities) {
      for (Edge edge : edges) {
        double currDistance = calculateDistance(city, edge);
        System.out.println((edge + " " + city + " " + currDistance));
        if (currDistance <= bestDistance) {
          bestDistance = (float) currDistance;
          nearestCity = city;
          closestEdges.add(edge);
        }
      }
    }
    Edge closestEdge = breakTies(closestEdges);
    System.out.println(edges);
    System.out.println(nearestCity + " " + closestEdges);
    return new CityAndEdge(nearestCity, closestEdge);
  }

  private Edge breakTies(List<Edge> edges) {
    Iterator<Edge> iterator = edges.iterator();
    while (iterator.hasNext()) {
      Edge edge = iterator.next();
      for (Edge checkEdge : edges) {
        if (edge.getStart().equals(checkEdge.getEnd())) {
          iterator.remove();
        }
      }
    }
    System.out.println(edges);
    return edges.get(0);
  }

  Point2D generateNewPointOnLine(Edge edge, double radius) {
    double x1 = edge.getEnd().getLatitude();
    double x2 = edge.getStart().getLatitude();
    double y1 = edge.getEnd().getLongitude();
    double y2 = edge.getStart().getLongitude();
    Point2D.Double vector = new Point2D.Double((x2 - x1), (y2 - y1));
    double length = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    Point2D.Double normalizedVector = new Point2D.Double(
      vector.x / length,
      vector.y / length
    );
    Point2D newPoint = new Point2D.Double(
      x1 + (normalizedVector.x),
      y1 + (normalizedVector.y)
    );
    return newPoint;
  }

  double calculateDistance(City start, City end) {
    return Point2D.distance(
      start.latitude,
      start.longitude,
      end.latitude,
      end.longitude
    );
  }

  /* Credit for the algorithm to calculate distance from a point to an edge goes to:
  Joshua's answer at
  https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
   */
  double calculateDistance(City city, Edge edge) {
    double x0 = city.latitude;
    double y0 = city.longitude;
    double x1 = edge.getStart().latitude;
    double y1 = edge.getStart().longitude;
    double x2 = edge.getEnd().latitude;
    double y2 = edge.getEnd().longitude;

    double A = x0 - x1;
    double B = y0 - y1;
    double C = x2 - x1;
    double D = y2 - y1;

    double dot = A * C + B * D;
    double lengthSquared = C * C + D * D;
    double param = -1;
    if (lengthSquared != 0) {
      param = dot / lengthSquared;
    }

    double xx, yy;

    if (param < 0) {
      xx = x1;
      yy = y1;
    } else if (param > 1) {
      xx = x2;
      yy = y2;
    } else {
      xx = x1 + param * C;
      yy = y1 + param * D;
    }

    double dx = x0 - xx;
    double dy = y0 - yy;
    return Math.sqrt(dx * dx + dy * dy);
  }
}
