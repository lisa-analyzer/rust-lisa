package it.unipr.frontend.cfg;

import static it.unive.lisa.LiSAFactory.getDefaultFor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.heap.MonolithicHeap;

class CFGTestConfiguration {

	static LiSAConfiguration mkConf() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration();
		conf.abstractState = getDefaultFor(AbstractState.class,
						new MonolithicHeap(),
						new NoOpValues(),
						new NoOpTypes());
		conf.serializeInputs = true;	
		
		return conf;
	}
}
