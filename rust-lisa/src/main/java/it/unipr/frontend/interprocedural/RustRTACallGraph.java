package it.unipr.frontend.interprocedural;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import it.unipr.cfg.type.composite.RustReferenceType;
import it.unive.lisa.interprocedural.callgraph.RTACallGraph;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.Type;

public class RustRTACallGraph extends RTACallGraph {

	@Override
	public Collection<Type> getPossibleTypesOfReceiver(Expression receiver, Set<Type> types) {
		Set<Type> result = new HashSet<>();
		
		// Remove all fake-references
		for (Type recType : types) {
			Type type = recType;
			while (type.isReferenceType() && !(type instanceof RustReferenceType))
				type = type.asReferenceType().getInnerType();
			
			result.add(type);
		}
		
		return result;
	}
}
