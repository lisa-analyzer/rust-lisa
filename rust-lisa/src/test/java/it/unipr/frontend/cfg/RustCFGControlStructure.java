package it.unipr.frontend.cfg;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import org.junit.Test;

public class RustCFGControlStructure extends RustLiSATestExecutor {

	@Test
	public void testLoop() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/loop", "loop.rs", conf);
	}

	@Test
	public void testWhile() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/while", "while.rs", conf);
	}

	@Test
	public void testFor() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/for", "for.rs", conf);
	}

	@Test
	public void testNestedLoops() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/nested-loops", "nested-loops.rs", conf);
	}

	@Test
	public void testEuclidianGCD() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/euclidian-gcd", "euclidian-gcd.rs", conf);
	}
}
