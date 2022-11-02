package it.unipr.frontend.cfg;

import org.junit.Test;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;

public class RustCFGMatch extends RustLiSATestExecutor {

	@Test
	public void testExprMatch() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/expr-match", "expr-match.rs", conf);
	}

	@Test
	public void testBlockyMatch() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/blocky-match", "blocky-match.rs", conf);
	}

	@Test
	public void testMixAndMatch() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/mix-and-match", "mix-and-match.rs", conf);
	}

	@Test
	public void testMatchIf() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/match-if", "match-if.rs", conf);
	}

	@Test
	public void testMatchOr() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/match-or", "match-or.rs", conf);
	}

	@Test
	public void testMatchCatchAll() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/match-catch-all", "match-catch-all.rs", conf);
	}

}
