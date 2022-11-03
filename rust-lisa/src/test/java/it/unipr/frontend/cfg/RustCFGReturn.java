package it.unipr.frontend.cfg;

import org.junit.Test;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;

public class RustCFGReturn extends RustLiSATestExecutor {

	@Test
	public void testReturnVoid() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/return-void", "return-void.rs", conf);
	}

	@Test
	public void testReturn() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/return", "return.rs", conf);
	}

	@Test
	public void testImplicitReturn() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/implicit-return", "implicit-return.rs", conf);
	}

}
