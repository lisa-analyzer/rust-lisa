package it.unipr.frontend.cfg;

import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import org.junit.Test;

public class RustCFGTrait extends RustLiSATestExecutor {

	@Test
	public void testTraitDefinition() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/trait-definition", "trait-definition.rs", conf);
	}

	@Test
	public void testTraitImplementation() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/trait-implementation", "trait-implementation.rs", conf);
	}

	@Test
	public void testTrait() throws AnalysisSetupException {
		LiSAConfiguration conf = CFGTestConfiguration.mkConf();
		perform("cfg/trait", "trait.rs", conf);
	}
}
