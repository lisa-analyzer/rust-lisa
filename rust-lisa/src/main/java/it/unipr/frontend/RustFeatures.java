package it.unipr.frontend;

import it.unipr.frontend.interprocedural.RustParameterAssigningStrategy;
import it.unipr.frontend.interprocedural.RustParameterMatchingStrategy;
import it.unive.lisa.program.language.LanguageFeatures;
import it.unive.lisa.program.language.hierarchytraversal.HierarcyTraversalStrategy;
import it.unive.lisa.program.language.hierarchytraversal.SingleInheritanceTraversalStrategy;
import it.unive.lisa.program.language.parameterassignment.ParameterAssigningStrategy;
import it.unive.lisa.program.language.resolution.ParameterMatchingStrategy;
import it.unive.lisa.program.language.validation.BaseValidationLogic;
import it.unive.lisa.program.language.validation.ProgramValidationLogic;

/**
 * Language features for rust.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustFeatures extends LanguageFeatures {

	@Override
	public ParameterMatchingStrategy getMatchingStrategy() {
		return RustParameterMatchingStrategy.INSTANCE;
	}

	@Override
	public HierarcyTraversalStrategy getTraversalStrategy() {
		return SingleInheritanceTraversalStrategy.INSTANCE;
	}

	@Override
	public ParameterAssigningStrategy getAssigningStrategy() {
		return RustParameterAssigningStrategy.INSTANCE;
	}

	@Override
	public ProgramValidationLogic getProgramValidationLogic() {
		return new BaseValidationLogic();
	}
}
