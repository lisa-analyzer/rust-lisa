package it.unipr.frontend.cfg;

import org.junit.Test;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;

public class RustCFGTypes extends RustLiSATestExecutor {

	@Test
	public void testTypeParsing() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/type-parsing", "type-parsing.rs", conf);
	}

	@Test
	public void testStructParsing() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/struct-parsing", "struct-parsing.rs", conf);
	}

	@Test
	public void testEnumParsing() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/enum", "enum.rs", conf);
	}

}
