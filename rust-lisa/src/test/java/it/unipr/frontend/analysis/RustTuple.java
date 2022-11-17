package it.unipr.frontend.analysis;

import org.junit.Test;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAFactory;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.pointbased.PointBasedHeap;
import it.unive.lisa.analysis.nonrelational.value.ValueEnvironment;
import it.unive.lisa.analysis.numeric.Interval;
import it.unive.lisa.analysis.value.TypeDomain;

public class RustTuple extends RustLiSATestExecutor {

	@Test
	public void testTuple() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration()
				.setAbstractState(new SimpleAbstractState<>(
						new PointBasedHeap(),
						new ValueEnvironment<>(new Interval()),
						LiSAFactory.getDefaultFor(TypeDomain.class)))
				.setSerializeResults(true)
				.setJsonOutput(true);

		perform("analysis/tuple", "tuple.rs", conf);
	}
}