package it.unipr.frontend.analysis;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAFactory;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.MonolithicHeap;
import it.unive.lisa.analysis.nonrelational.value.ValueEnvironment;
import it.unive.lisa.analysis.numeric.Interval;
import it.unive.lisa.analysis.value.TypeDomain;
import org.junit.Test;

public class RustLogical extends RustLiSATestExecutor {

	@Test
	public void testLogical() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration();
		conf.abstractState = new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Interval()),
				LiSAFactory.getDefaultFor(TypeDomain.class));
		conf.serializeResults = true;
		conf.jsonOutput = true;

		perform("analysis/logical", "logical.rs", conf);
	}

}
