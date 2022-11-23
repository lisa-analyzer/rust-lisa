package it.unipr.frontend.cfg;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import org.junit.Test;

public class RustCFGFieldExistance extends RustLiSATestExecutor {

	@Test
	public void testFieldExistance() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/field-existance", "field-existance.rs", conf);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testErrorMissingField() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/error-missing-field", "error-missing-field.rs", conf);
	}

}
