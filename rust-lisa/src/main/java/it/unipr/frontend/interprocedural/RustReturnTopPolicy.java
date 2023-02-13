package it.unipr.frontend.interprocedural;

import it.unipr.cfg.type.primitive.RustUnitType;
import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.OpenCallPolicy;
import it.unive.lisa.program.AbstractClassUnit;
import it.unive.lisa.program.CompilationUnit;
import it.unive.lisa.program.cfg.CodeMember;
import it.unive.lisa.program.cfg.statement.call.OpenCall;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.PushAny;
import it.unive.lisa.symbolic.value.Variable;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import java.util.Collection;
import java.util.Optional;

/**
 * An {@link OpenCallPolicy}, where the post state is exactly the entry state,
 * with the only difference of having a synthetic variable named
 * {@value OpenCallPolicy#RETURNED_VARIABLE_NAME} assigned to top. This
 * variable, that is also stored as computed expression, represent the unknown
 * result of the call.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustReturnTopPolicy implements OpenCallPolicy {

	/**
	 * The singleton instance of this class.
	 */
	public static final RustReturnTopPolicy INSTANCE = new RustReturnTopPolicy();

	private RustReturnTopPolicy() {
		// Blank
	}

	/**
	 * Applies the policy to the given macro call.
	 * 
	 * @param <A>        the type of {@link AbstractState} contained into the
	 *                       analysis state
	 * @param <H>        the type of {@link HeapDomain} contained into the
	 *                       computed abstract state
	 * @param <V>        the type of {@link ValueDomain} contained into the
	 *                       computed abstract state
	 * @param <T>        the type of {@link TypeDomain} contained into the
	 *                       computed abstract state
	 * @param call       the macro call (which is a {@link OpenCall}) under
	 *                       evaluation
	 * @param entryState the state when the call is executed
	 * @param params     the symbolic expressions representing the computed
	 *                       values of the parameters of the call
	 * 
	 * @return the {@link AnalysisState} representing the abstract result of the
	 *             execution of this call
	 * 
	 * @throws SemanticException if something goes wrong during the computation
	 */
	private <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> applyOnMacroCall(
					OpenCall call,
					AnalysisState<A, H, V, T> entryState,
					ExpressionSet<SymbolicExpression>[] params)
					throws SemanticException {

		Type type;
		switch (call.getConstructName()) {
		case "println!":
			type = RustUnitType.getInstance();
			break;

		default:
			type = Untyped.INSTANCE;
			break;
		}

		PushAny pushAny = new PushAny(type, call.getLocation());
		Variable var = new Variable(type, RETURNED_VARIABLE_NAME, call.getLocation());
		return entryState.assign(var, pushAny, call);
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> apply(
					OpenCall call,
					AnalysisState<A, H, V, T> entryState,
					ExpressionSet<SymbolicExpression>[] params)
					throws SemanticException {

		if (call.getConstructName().endsWith("!"))
			return applyOnMacroCall(call, entryState, params);

		// get every ancestors of the units
		Optional<CompilationUnit> externCompilationUnit = call.getProgram().getUnits().stream()
				.filter(unit -> unit instanceof CompilationUnit)
				.flatMap(unit -> ((CompilationUnit) unit).getImmediateAncestors().stream())
				.filter(ancestor -> ancestor.getName().equals("extern"))
				.findFirst();

		Type returnType = Untyped.INSTANCE;
		if (externCompilationUnit.isPresent()) {
			AbstractClassUnit externUnit = (AbstractClassUnit) externCompilationUnit.get();
			Collection<CodeMember> externCodeMembers = externUnit.getCodeMembersByName(call.getConstructName());

			if (externCodeMembers.size() == 0)
				throw new SemanticException(
						"Found more than one function that matches the declaration of " + call + " in extern module");

			// There is no overloading in Rust, so there should be only one
			// extern function with a specific name
			CodeMember externFn = externCodeMembers.stream().findFirst().get();

			returnType = externFn.getDescriptor().getReturnType();
		}

		PushAny pushAny = new PushAny(returnType, call.getLocation());
		Variable var = new Variable(returnType, RETURNED_VARIABLE_NAME, call.getLocation());
		return entryState.assign(var, pushAny, call);
	}
}
