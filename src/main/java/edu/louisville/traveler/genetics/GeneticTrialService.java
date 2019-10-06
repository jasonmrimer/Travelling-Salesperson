package edu.louisville.traveler.genetics;

import edu.louisville.traveler.maps.Map;
import org.springframework.stereotype.Service;


@Service
public class GeneticTrialService {
  Trial trialFromMap(Map map) {
    ParentSelector parentSelector = new RandomParentSelector();
    TrialGenerator geneticTrialGenerator = new TrialGenerator(
      new RandomParentsBreeder(parentSelector, map, 16, 0),
      32,
      64,
      256
    );
    return geneticTrialGenerator.runTrial();
  }
}
