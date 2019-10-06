package edu.louisville.traveler.genetics;

import edu.louisville.traveler.maps.City;
import edu.louisville.traveler.maps.Map;

import java.util.*;

public class RandomParentsBreeder implements Breeder {
  private List<LivingTour> unbredParents = new ArrayList<>();
  private List<LivingTour> currentChildren;
  private double mutationChance = 0.0;
  private int maxGeneSequenceLength;
  private int bornChildren;


  RandomParentsBreeder(int maxGeneSequenceLength, double mutationChance) {
    this.maxGeneSequenceLength = maxGeneSequenceLength;
    this.mutationChance = mutationChance;
  }

  @Override
  public LivingTour breedParents(LivingTour parent1, LivingTour parent2, int maxGeneSequenceLength) {
    Map map = new Map(new ArrayList<>(parent1.getCycle()));
    City[] emptyRoute = new City[map.getCities().size()];
    LivingTour child = new LivingTour(Arrays.asList(emptyRoute));

    Breeder.firstGene(
      Breeder.selectRandomParent(parent1, parent2),
      child,
      map,
      maxGeneSequenceLength,
      0
    );

    while (Breeder.childRouteIsNotComplete(child)) {
      int geneSequenceLength = maxGeneSequenceLength <= map.getCities().size() ?
        (int) (Math.random() * maxGeneSequenceLength) + 1 :
        (int) (Math.random() * map.getCities().size()) + 1;

      if (Breeder.shouldInheritGenes(this.mutationChance)) {
        LivingTour selectedParent = Breeder.selectRandomParent(parent1, parent2);
        LivingTour backupParent = selectedParent.equals(parent1) ? parent2 : parent1;
        addSequenceToChild(map, child, selectedParent, backupParent, geneSequenceLength);
      } else {
//        for (int i = 0; i < geneSequenceLength; i++) {
        Breeder.mutateSingleGene(map, child);
//        }
      }
    }
    return child;
  }

  @Override
  public Generation breedGeneration(Map map, List<LivingTour> currentParents, int gen) {
    unbredParents.clear();
    unbredParents.addAll(currentParents);
    unbredParents.sort(Comparator.comparingDouble(LivingTour::getWeight));
    currentChildren = new ArrayList<>();
    bornChildren = 0;
    int deceasedParents = 0;

    while (parentsAvailableForMating(currentParents)) {
      breedMates();
    }

    return new Generation(
      gen,
      currentParents,
      currentChildren,
      bornChildren,
      deceasedParents
    );
  }

  private static void addSequenceToChild(
    Map map, LivingTour child,
    LivingTour selectedParent,
    LivingTour backupParent,
    int geneSequenceLength
  ) {
    int currentSequenceLength = geneSequenceLength;
    LivingTour breedingParent = selectedParent;
    boolean bred = false;
    int childFirstOpenGene = child.getCycle().indexOf(null);
    boolean triedBothParents = false;

    while (!bred) {
      if (triedBothParents && currentSequenceLength == 0) {
        Breeder.mutateSingleGene(
          map,
          child
        );
      }

      if (!triedBothParents && currentSequenceLength == 0) {
        breedingParent = backupParent;
        triedBothParents = true;
        currentSequenceLength = geneSequenceLength;
      }

      int childAvailableStartIndex =
        indexOfAvailableOpening(
          child,
          currentSequenceLength,
          childFirstOpenGene
        );

      if (sequenceAvailable(childAvailableStartIndex)) {
        boolean suitable = checkSuitabilityOfParentGenesForChild(
          breedingParent,
          child,
          currentSequenceLength,
          childAvailableStartIndex
        );

        if (suitable) {
          transposeGenesToChild(child, currentSequenceLength, breedingParent, childAvailableStartIndex);
          bred = true;
        } else {
          childFirstOpenGene++;
        }
      } else {
        currentSequenceLength--;
      }
    }
  }

  private static int indexOfAvailableOpening(LivingTour child, int currentSequenceLength, int childFirstOpenGene) {
    int indexOfOpening = -1;
    for (int i = childFirstOpenGene; i <= child.getCycle().size() - currentSequenceLength; i++) {
      boolean availableSequence = true;
      for (int j = 0; j < currentSequenceLength; j++) {
        if (child.getCycle().get(i + j) != null) {
          availableSequence = false;
          break;
        }
      }
      if (availableSequence) {
        indexOfOpening = i;
        break;
      }
    }
    return indexOfOpening;
  }

  private static boolean sequenceAvailable(int childAvailableStartIndex) {
    return childAvailableStartIndex > -1;
  }

  private static boolean checkSuitabilityOfParentGenesForChild(
    LivingTour parent,
    LivingTour child,
    int geneSequenceLength,
    int geneStartingIndex
  ) {
    boolean suitable = true;
    for (int i = 0; i < geneSequenceLength; i++) {
      City parentGene = parent.getCycle().get(geneStartingIndex + i);
      if (child.getCycle().contains(parentGene)) {
        suitable = false;
        break;
      }
    }
    return suitable;
  }

  private static void transposeGenesToChild(LivingTour child, int currentSequenceLength, LivingTour breedingParent, int childAvailableStartIndex) {
    for (int i = 0; i < currentSequenceLength; i++) {
      int geneIndex = childAvailableStartIndex + i;
      City parentGene = breedingParent.getCycle().get(geneIndex);
      child.getCycle().set(geneIndex, parentGene);
    }
  }

  private boolean parentsAvailableForMating(List<LivingTour> currentParents) {
    currentParents.removeAll(Collections.singleton(null));
    int availableParentCount = 0;
    for (LivingTour parent : currentParents) {
      if (!parent.didBreed()) {
        availableParentCount++;
      }
    }
    return availableParentCount > 2;
  }

  private void breedMates() {
//    LivingTour parentSeekingMate = unbredParents.get((int) (Math.random() * unbredParents.size()));
//    LivingTour randomMate = findRandomMate(parentSeekingMate);
    LivingTour parentSeekingMate = unbredParents.get(0);
    LivingTour randomMate = unbredParents.get(1);
    for (int i = 0; i < 4; i++) {
      LivingTour child = breedParents(parentSeekingMate, randomMate, this.maxGeneSequenceLength);
      this.currentChildren.add(child);
        bornChildren++;
//      if (child.getWeight() < parentSeekingMate.getWeight() && child.getWeight() < randomMate.getWeight()) {
//        this.currentChildren.add(child);
//        bornChildren++;
//      }
    }
//    if (currentChildren.size() == 0) {
//      this.currentChildren.add(child);
//    } else if (isFit(child)) {
//      this.currentChildren.add(child);
//    }
    parentSeekingMate.setBred(true);
    randomMate.setBred(true);
    unbredParents.remove(parentSeekingMate);
    unbredParents.remove(randomMate);
  }

  private LivingTour findRandomMate(LivingTour parentSeekingMate) {
    int randomIndex = (int) (Math.random() * this.unbredParents.size());
    while (randomIndex == this.unbredParents.indexOf(parentSeekingMate) && this.unbredParents.get(randomIndex).didBreed()) {
      randomIndex = (int) (Math.random() * this.unbredParents.size());
    }
    return this.unbredParents.get(randomIndex);
  }

  private boolean isFit(LivingTour newborn) {
    double averageFitness = 0;
    for (LivingTour child : currentChildren) {
      if (child != null) {
        averageFitness += child.getWeight();
      }
    }
    averageFitness /= currentChildren.size();
    return averageFitness == 0 || newborn.getWeight() < averageFitness;
  }
}
