package it.unipr.frontend.cfg;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import org.junit.Test;

public class RustCFGUnsafe extends RustLiSATestExecutor {

	@Test
	public void testUnsafeBlock() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/unsafe-block", "unsafe-block.rs", conf);
	}

	@Test
	public void testUnsafeFn() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/unsafe-fn", "unsafe-fn.rs", conf);
	}

	@Test
	public void testUnsafeMethod() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/unsafe-method", "unsafe-method.rs", conf);
	}

}
