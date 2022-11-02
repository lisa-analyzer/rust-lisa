package it.unipr.frontend.cfg;

import org.junit.Test;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAConfiguration.GraphType;

public class RustCFGIf extends RustLiSATestExecutor {

	@Test
	public void testSimpleIf() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/simple-if", "simple-if.rs", conf);
	}

	@Test
	public void testIfElse() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/if-else", "if-else.rs", conf);
	}

	@Test
	public void testIfElseif() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/if-elseif", "if-elseif.rs", conf);
	}

	@Test
	public void testIfElseifElse() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/if-elseif-else", "if-elseif-else.rs", conf);
	}

	@Test
	public void testMultipleIf() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/multiple-if", "multiple-if.rs", conf);
	}

	@Test
	public void testMoreThanOneIf() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/more-than-one-if", "more-than-one-if.rs", conf);
	}

}
