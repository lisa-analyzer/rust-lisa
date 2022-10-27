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

public class RustNumerical extends RustLiSATestExecutor {

	@Test
	public void testLet() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration()
				.setAbstractState(new SimpleAbstractState<>(
						new MonolithicHeap(),
						new ValueEnvironment<>(new Interval()),
						LiSAFactory.getDefaultFor(TypeDomain.class)))
				.setDumpTypeInference(true)
				.setDumpAnalysis(true)
				.setJsonOutput(true);

		perform("analysis/let", "let.rs", conf);
	}

	@Test
	public void testNumerical() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration()
				.setAbstractState(new SimpleAbstractState<>(
						new MonolithicHeap(),
						new ValueEnvironment<>(new Interval()),
						LiSAFactory.getDefaultFor(TypeDomain.class)))
				.setDumpTypeInference(true)
				.setDumpAnalysis(true)
				.setJsonOutput(true);

		perform("analysis/numerical", "numerical.rs", conf);
	}

	@Test
	public void testBitwise() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration()
				.setAbstractState(new SimpleAbstractState<>(
						new MonolithicHeap(),
						new ValueEnvironment<>(new Interval()),
						LiSAFactory.getDefaultFor(TypeDomain.class)))
				.setDumpTypeInference(true)
				.setDumpAnalysis(true)
				.setJsonOutput(true);

		perform("analysis/bitwise", "bitwise.rs", conf);
	}

}
