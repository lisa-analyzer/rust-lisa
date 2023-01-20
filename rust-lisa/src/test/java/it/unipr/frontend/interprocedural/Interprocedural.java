package it.unipr.frontend.interprocedural;

import org.junit.Test;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.pointbased.PointBasedHeap;
import it.unive.lisa.analysis.nonrelational.value.TypeEnvironment;
import it.unive.lisa.analysis.nonrelational.value.ValueEnvironment;
import it.unive.lisa.analysis.numeric.Interval;
import it.unive.lisa.analysis.types.InferredTypes;
import it.unive.lisa.interprocedural.ContextBasedAnalysis;
import it.unive.lisa.interprocedural.RecursionFreeToken;
import it.unive.lisa.interprocedural.callgraph.RTACallGraph;

public class Interprocedural extends RustLiSATestExecutor {
	private LiSAConfiguration mkConf() {
		LiSAConfiguration conf = new LiSAConfiguration();
		conf.abstractState = new SimpleAbstractState<>(
				new PointBasedHeap(),
				new ValueEnvironment<>(new Interval()),
				new TypeEnvironment<>(new InferredTypes()));
		conf.serializeResults = true;
		conf.callGraph = new RTACallGraph();
		conf.interproceduralAnalysis = new ContextBasedAnalysis<>(RecursionFreeToken.getSingleton());
		conf.jsonOutput = true;
		return conf;
	}

	@Test
	public void testSum() throws AnalysisSetupException {
		LiSAConfiguration conf = mkConf();

		perform("interprocedural/sum", "sum.rs", conf);
	}

	@Test
	public void testRectangle() throws AnalysisSetupException {
		LiSAConfiguration conf = mkConf();

		perform("interprocedural/rectangle", "rectangle.rs", conf);
	}

	@Test
	public void testGeometry() throws AnalysisSetupException {
		LiSAConfiguration conf = mkConf();

		perform("interprocedural/geometry", "geometry.rs", conf);
	}

	@Test
	public void testBookMethods() throws AnalysisSetupException {
		LiSAConfiguration conf = mkConf();

		perform("interprocedural/book-methods", "book-methods.rs", conf);
	}

	@Test
	public void testParameterPassage() throws AnalysisSetupException {
		LiSAConfiguration conf = mkConf();

		perform("interprocedural/parameter-passage", "parameter-passage.rs", conf);
	}
	
	@Test
	public void testBookEnums() throws AnalysisSetupException {
		LiSAConfiguration conf = mkConf();
		
		perform("interprocedural/book-enums", "book-enums.rs", conf);
	}
}
