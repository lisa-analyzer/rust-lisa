package it.unipr.frontend.cfg;

import org.junit.Test;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;

public class RustCFGIfLet extends RustLiSATestExecutor {

	@Test
	public void testSimpleIfLet() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/simple-if-let", "simple-if-let.rs", conf);
	}

}
