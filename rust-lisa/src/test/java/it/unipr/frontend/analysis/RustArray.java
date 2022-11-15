package it.unipr.frontend.analysis;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAFactory;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.pointbased.FieldSensitivePointBasedHeap;
import it.unive.lisa.analysis.nonrelational.value.ValueEnvironment;
import it.unive.lisa.analysis.numeric.Interval;
import it.unive.lisa.analysis.value.TypeDomain;
import org.junit.Test;

public class RustArray extends RustLiSATestExecutor {

	@Test
	public void testArray() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration()
				.setAbstractState(new SimpleAbstractState<>(
						new FieldSensitivePointBasedHeap(),
						new ValueEnvironment<>(new Interval()),
						LiSAFactory.getDefaultFor(TypeDomain.class)))
				.setSerializeResults(true)
				.setJsonOutput(true);

		perform("analysis/array", "array.rs", conf);
	}
}
