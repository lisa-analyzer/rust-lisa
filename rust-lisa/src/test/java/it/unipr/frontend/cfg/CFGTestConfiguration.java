package it.unipr.frontend.cfg;

import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAFactory;
import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.heap.MonolithicHeap;

class CFGTestConfiguration {
	
	static LiSAConfiguration mkConf() throws AnalysisSetupException {
		return new LiSAConfiguration()
				.setSerializeResults(true)
				.setAbstractState(LiSAFactory.getDefaultFor(AbstractState.class,
						new MonolithicHeap(),
						new NoOpValues(),
						new NoOpTypes()));
	}
}
