package edu.louisville.project1.graphs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class GraphRequest {
  private boolean[][] adjacencyMatrix;
  private Node start;
}
