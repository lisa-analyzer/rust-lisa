package it.unipr.cfg.expression.literal.enums;

import it.unipr.cfg.expression.literal.RustTupleLiteral;
import it.unipr.cfg.program.unit.RustEnumUnit;
import it.unipr.cfg.type.composite.RustTupleType;
import it.unipr.cfg.type.composite.enums.RustEnumType;
import it.unipr.cfg.type.composite.enums.RustEnumVariant;
import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.type.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Rust enum tuple literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustEnumTupleLiteral extends RustEnumLiteral<NaryExpression> {

	private String variantName;

	/**
	 * Build the enum tuple literal.
	 * 
	 * @param cfg         the {@link CFG} where this literal lies
	 * @param location    the location where this literal is defined
	 * @param expressions the values to be used inside the literal. During
	 *                        destructuring they can be variables, in (almost)
	 *                        any other case they can be other literals
	 * @param variantName the name of this variant
	 * @param enumType    the enum type of this literal
	 */
	public RustEnumTupleLiteral(CFG cfg, CodeLocation location, Expression[] expressions, String variantName,
			RustEnumType enumType) {
		super(cfg, location, new RustMultipleExpression(cfg, location, expressions), enumType);
		this.variantName = variantName;
	}

	@Override
	public String toString() {
		return getStaticType() + "::" + variantName + "(" + getValue() + ")";
	}

	@Override
	public boolean isInstanceOf(RustEnumVariant variant) {
		if (variant instanceof RustTupleType) {
			List<Type> types = ((RustTupleType) variant).getTypes();
			Expression[] multiple = getValue().getSubExpressions();

			if (multiple.length != types.size())
				return false;

			return true;
		}
		return false;
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> semantics(
					AnalysisState<A, H, V, T> entryState, InterproceduralAnalysis<A, H, V, T> interprocedural,
					StatementStore<A, H, V, T> expressions) throws SemanticException {

		// Find a collection of valid tuples inside the unit
		RustEnumUnit unit = RustEnumType.get(getStaticType().toString()).getUnit();
		Collection<RustEnumVariant> validTuples = new HashSet<>();
		for (RustEnumVariant variant : unit.getVariants()) {
			if (variant instanceof RustTupleType) {
				RustTupleType candidate = (RustTupleType) variant;

				// Check that each type in the expression canBeAssignedTo the
				// corresponding type in the tuple
				for (int i = 0; i < candidate.getTypes().size(); ++i) {
					Type t = candidate.getTypes().get(i);
					Expression expr = getValue().getSubExpressions()[i];
					if (!(expr.getStaticType().canBeAssignedTo(t)))
						validTuples.add(candidate);
				}
			}
		}

		RustMultipleExpression inner = (RustMultipleExpression) getValue();
		AnalysisState<A, H, V, T> result = entryState.lub(inner.semantics(entryState, interprocedural, expressions));
		for (RustEnumVariant tuple : validTuples) {
			AnalysisState<A, H, V, T> tupleState = RustTupleLiteral.semantic(getLocation(), this,
					(RustTupleType) tuple, interprocedural, entryState,
					getValue().getSubExpressions(), inner.getSymbolicExpression(),
					expressions);

			result = result.lub(tupleState);
		}

		return result;
	}
}
