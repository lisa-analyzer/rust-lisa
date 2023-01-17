package it.unipr.frontend;

import java.util.Set;

import it.unive.lisa.program.cfg.Parameter;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.call.Call;
import it.unive.lisa.program.cfg.statement.call.Call.CallType;
import it.unive.lisa.program.language.resolution.FixedOrderMatchingStrategy;
import it.unive.lisa.type.Type;

public class RustParameterMatchingStrategy extends FixedOrderMatchingStrategy {

	/**
	 * Singleton instance for {@link RustParameterMatchingStrategy}.
	 */
	public static final RustParameterMatchingStrategy INSTANCE = new RustParameterMatchingStrategy(); 
	
	private RustParameterMatchingStrategy() {
		//Blank
	}
	
	@Override
	public boolean matches(Call call, int pos, Parameter formal, Expression actual, Set<Type> types) {
		if (pos == 0 && call.getCallType() == CallType.INSTANCE) { // Instance call case
			// Formal is a reference
			if (formal.getStaticType().isPointerType()) {
				Type inner = formal.getStaticType().asPointerType().getInnerType();

				// any of the runtime type canBeAssignedTo inner type or runtime type can be
				// assigned to the formal static type (in case of passing reference / pointers)
				return types.stream()
						.anyMatch(rt -> rt.canBeAssignedTo(inner) || rt.canBeAssignedTo(formal.getStaticType()));
			} else if (types.stream().anyMatch(rt -> rt.isPointerType()
					&& rt.asPointerType().getInnerType().canBeAssignedTo(formal.getStaticType()))) {
				// the runtime type is a pointer type and its inner type canBeAssignedTo the
				// formal type

				// TODO verify that's correct
				return true;
			}

			// simply check if any runtime type canBeAssignedTo the formal static type
			return types.stream().anyMatch(rt -> rt.canBeAssignedTo(formal.getStaticType()));
			
		} else if (types.stream().anyMatch(rt -> rt.isPointerType()
				&& rt.asPointerType().getInnerType().canBeAssignedTo(formal.getStaticType()))) {
			// other call cases: runtime types is a pointer but 
			
			// TODO verify that's correct
			return true;
		}

		// simply check if the types canBeAssignedTo
		return types.stream().anyMatch(rt -> rt.canBeAssignedTo(formal.getStaticType()));
	}

}
