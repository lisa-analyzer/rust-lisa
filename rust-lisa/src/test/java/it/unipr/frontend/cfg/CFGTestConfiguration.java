package it.unipr.frontend.cfg;

import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.MonolithicHeap;
import it.unive.lisa.interprocedural.ModularWorstCaseAnalysis;
import it.unive.lisa.interprocedural.callgraph.RTACallGraph;

class CFGTestConfiguration {

	static LiSAConfiguration mkConf() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration();
		conf.abstractState = new SimpleAbstractState<>(
				new MonolithicHeap(),
				new NoOpValues(),
				new NoOpTypes());
		conf.callGraph = new RTACallGraph();
		conf.interproceduralAnalysis = new ModularWorstCaseAnalysis<>();
		conf.jsonOutput = true;
		conf.serializeInputs = true;

		return conf;
	}
}
