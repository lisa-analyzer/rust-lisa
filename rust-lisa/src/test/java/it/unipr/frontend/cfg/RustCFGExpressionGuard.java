package it.unipr.frontend.cfg;

import org.junit.Test;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;

public class RustCFGExpressionGuard extends RustLiSATestExecutor {

	@Test
	public void testExpressionGuard() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/expression-guard", "expression-guard.rs", conf);
	}

}
