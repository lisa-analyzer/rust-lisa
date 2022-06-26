package it.unipr.frontend;

import org.junit.Test;

import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;

public class RustCFGExpressionGuard extends RustLiSATestExecutor {

	@Test
	public void testExpressionGuards() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setDumpCFGs(true).setJsonOutput(true);
		perform("cfg/expression-guard", "expression-guard.rs", conf);
	}

}
