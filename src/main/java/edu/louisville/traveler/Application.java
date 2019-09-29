package edu.louisville.traveler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
  scanBasePackages = {
    "edu.louisville.traveler.graphs",
    "edu.louisville.traveler.maps",
    "edu.louisville.traveler.genetics",
  }
)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
