package it.unipr.frontend.cfg;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import org.junit.Test;

public class RustCFGFieldExistance extends RustLiSATestExecutor {

	@Test
	public void testFieldExistance() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setDumpCFGs(true).setJsonOutput(true);
		perform("cfg/field-existance", "field-existance.rs", conf);
	}

	@Test
	public void testErrorMissingField() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setDumpCFGs(true).setJsonOutput(true);
		try {
			perform("cfg/error-missing-field", "error-missing-field.rs", conf);
		} catch (UnsupportedOperationException e) {
			// We want to catch exactly this type of error, all good here
			if (!(e.getMessage().contains("Use of undeclared struct field in"))) {
				throw new IllegalArgumentException();
			}
		}
	}

}
