/*
 * Ivory: A Hadoop toolkit for Web-scale information retrieval
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package ivory.smrf.model.score;

import ivory.smrf.model.GlobalEvidence;
import ivory.smrf.model.GlobalTermEvidence;

/**
 * @author Don Metzler
 *
 */
public class BinaryTFScoringFunction extends ScoringFunction {

	/* (non-Javadoc)
	 * @see edu.umass.cs.SMRF.model.scoringfunction.ScoringFunction#getScore(double, int, int, double, long, long)
	 */
	@Override
	public double getScore(double tf, int docLen) {
		if( tf > 0.0 ) {
			return 1.0;
		}
		
		return 0.0;
	}

	/* (non-Javadoc)
	 * @see ivory.smrf.model.score.ScoringFunction#initialize(ivory.smrf.model.GlobalTermEvidence, ivory.smrf.model.GlobalEvidence)
	 */
	@Override
	public void initialize(GlobalTermEvidence termEvidence,
			GlobalEvidence globalEvidence) {
		// do nothing
	}

}
