package it.unipr.frontend.cfg;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAConfiguration.GraphType;

import org.junit.Test;

public class RustCFGComplex extends RustLiSATestExecutor {

	@Test
	public void testRectangle() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/rectangle", "rectangle.rs", conf);
	}

}
