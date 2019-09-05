package edu.louisville.project1;

import edu.louisville.project1.graphs.Graph;
import edu.louisville.project1.graphs.Node;
import edu.louisville.project1.graphs.NodeComparator;

import java.util.*;

public class DepthFirstSearcher {
  List<List<Node>> allPathsFromStartToEnd = new ArrayList<>();

  List<Node> traverseGraphToEnd(Graph graph, Node start, Node end) {
    Node rootNode = start;
    List<Node> traveledPath = new ArrayList<>();
//    List<List<Node>> traveledPaths = new ArrayList<>(new ArrayList<>());

    traveledPath.add(start);
    recursiveExploration(graph, rootNode, end, traveledPath);

//    traveledPath = exploreDepth(graph, rootNode, end);
//    if (traveledPath.contains(end)) {
//      traveledPaths.add(traveledPath);
//    }
//    find connected nodes
//    break ties, take first node
//    repeat until no children
//    return one step, repeat until no children
//    mark as discovered and visited
    int bestPathLength = Integer.MAX_VALUE;
    for (List<Node> path : this.allPathsFromStartToEnd) {
      bestPathLength = Math.min(path.size(), bestPathLength);
    }

    Iterator<List<Node>> iterator = this.allPathsFromStartToEnd.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().size() != bestPathLength) {
        iterator.remove();
      }
    }

    for (int i = 0; i < bestPathLength; i++) {
      int lesserNodeValue = Integer.MAX_VALUE;
      iterator = this.allPathsFromStartToEnd.iterator();
      while (iterator.hasNext()) {
        lesserNodeValue = Math.min(Integer.parseInt(iterator.next().get(i).getName()), lesserNodeValue);
      }

      iterator = this.allPathsFromStartToEnd.iterator();
      while (iterator.hasNext()) {
        if (Integer.parseInt(iterator.next().get(i).getName()) != lesserNodeValue) {
          iterator.remove();
        }
      }
    }

    System.out.println(this.allPathsFromStartToEnd);
    return this.allPathsFromStartToEnd.get(0);
  }

  private void recursiveExploration(Graph graph, Node rootNode, Node end, List<Node> traveledPath) {
    List<Node> children = discoverChildren(graph, rootNode);
    if (!exists(children)) {
      rootNode.setVisited(true);
      if (traveledPath.get(traveledPath.size() - 1).equals(end)) {
        this.allPathsFromStartToEnd.add(traveledPath);
      }
      return;
    }
    for (Node child : children) {
      List<Node> pathForChild = new ArrayList<>(traveledPath);
      pathForChild.add(child);
      this.recursiveExploration(graph, child, end, pathForChild);
    }
  }

  private List<Node> exploreDepth(Graph graph, Node rootNode, Node endNode) {
    List<Node> traveledPath = new ArrayList<>();
    traveledPath.add(rootNode);
    while (exists(discoverChildren(graph, rootNode))) {
      List<Node> adjacentNodes = discoverChildren(graph, rootNode);
      adjacentNodes.sort(new NodeComparator());
      rootNode = adjacentNodes.get(0);
      traveledPath.add(rootNode);
      if (rootNode.equals(endNode)) {
        break;
      }
    }
    return traveledPath;
  }

  private List<Node> discoverChildren(
    Graph graph,
    Node rootNode
  ) {
    @SuppressWarnings("unchecked")
    List<Node> adjacentNodes = graph.getEdges().get(rootNode);
    if (exists(adjacentNodes)) {
      for (Node discoveredNode : adjacentNodes) {
        if (!discoveredNode.isDiscovered()) {
          discoveredNode.setDiscovered(true);
          discoveredNode.setDepth(rootNode.getDepth() + 1);
          discoveredNode.setParentNode(rootNode);
        }
      }
    }
    return adjacentNodes;
  }

  private boolean exists(List<Node> nodes) {
    return nodes != null;
  }

  Deque<Node> findShortestPath(Graph graph, Node start, Node end) {
    return new LinkedList<>();
  }
}
