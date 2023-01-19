package it.unipr.frontend;

import it.unipr.cfg.type.composite.RustReferenceType;
import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.cfg.Parameter;
import it.unive.lisa.program.cfg.statement.call.Call;
import it.unive.lisa.program.cfg.statement.call.Call.CallType;
import it.unive.lisa.program.language.parameterassignment.ParameterAssigningStrategy;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.HeapReference;
import it.unive.lisa.symbolic.value.OutOfScopeIdentifier;
import it.unive.lisa.symbolic.value.Variable;
import it.unive.lisa.type.ReferenceType;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Implementation of the parameter assigning strategy for Rust.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustParameterAssigningStrategy implements ParameterAssigningStrategy {

	/**
	 * Singleton instance for {@link RustParameterAssigningStrategy}.
	 */
	public static final RustParameterAssigningStrategy INSTANCE = new RustParameterAssigningStrategy();

	private RustParameterAssigningStrategy() {
		// Blank
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> Pair<AnalysisState<A, H, V, T>, ExpressionSet<SymbolicExpression>[]> prepare(
					Call call, AnalysisState<A, H, V, T> callState, InterproceduralAnalysis<A, H, V, T> interprocedural,
					StatementStore<A, H, V, T> expressions, Parameter[] formals,
					ExpressionSet<SymbolicExpression>[] actuals)
					throws SemanticException {

		// forget actuals that are Variables and does not have RustReferenceType
		for (int i = 0; i < formals.length; ++i)
			if (!(formals[i].getStaticType() instanceof RustReferenceType))
				for (ExpressionSet<SymbolicExpression> actualSet : actuals)
					for (SymbolicExpression actualElement : actualSet)
						if (actualElement instanceof OutOfScopeIdentifier)
							callState = callState.lub(callState.forgetIdentifier((OutOfScopeIdentifier) actualElement));

		// if it is an instance call, we need check the first parameter
		// that corresponds to the callee of the instance call
		if (call.getCallType() == CallType.INSTANCE) {

			Parameter formal = formals[0];
			ExpressionSet<SymbolicExpression> actual = actuals[0];
			AnalysisState<A, H, V, T> prepared = callState.bottom();

			for (SymbolicExpression exp : actual) {
				HeapReference ref = new HeapReference(new ReferenceType(formal.getStaticType()), exp,
						call.getLocation());
				AnalysisState<A, H, V, T> referenceState = callState.smallStepSemantics(ref, call);

				for (SymbolicExpression e : referenceState.getComputedExpressions()) {
					Variable formalId = new Variable(new ReferenceType(formal.getStaticType()), formal.getName(),
							formal.getAnnotations(), formal.getLocation());
					prepared = prepared.lub(callState.assign(formalId, e, call));
				}
			}

			// all the others formals
			for (int i = 1; i < formals.length; i++) {
				AnalysisState<A, H, V, T> temp = prepared.bottom();

				for (SymbolicExpression exp : actuals[i])
					if (formals[i].getStaticType().isInMemoryType()) {
						Variable formalVariable = new Variable(new ReferenceType(formals[i].getStaticType()),
								formals[i].getName(), formals[i].getAnnotations(), formals[i].getLocation());

						temp = temp.lub(prepared.assign(formalVariable, exp, call));
					} else
						temp = temp.lub(prepared.assign(formals[i].toSymbolicVariable(), exp, call));

				prepared = temp;
			}

			return Pair.of(prepared, actuals);
		} else {
			// prepare the state for the call: assign the value to each
			// parameter
			AnalysisState<A, H, V, T> prepared = callState;

			for (int i = 0; i < formals.length; i++) {
				AnalysisState<A, H, V, T> temp = prepared.bottom();

				for (SymbolicExpression exp : actuals[i])
					if (formals[i].getStaticType().isInMemoryType()) {
						Variable formalVariable = new Variable(new ReferenceType(formals[i].getStaticType()),
								formals[i].getName(), formals[i].getAnnotations(), formals[i].getLocation());
						temp = temp.lub(prepared.assign(formalVariable, exp, call));
					} else
						temp = temp.lub(prepared.assign(formals[i].toSymbolicVariable(), exp, call));

				prepared = temp;
			}

			return Pair.of(prepared, actuals);
		}
	}
}
