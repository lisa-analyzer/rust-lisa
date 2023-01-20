package it.unipr.cfg.expression.literal.enums;

import it.unipr.cfg.expression.literal.RustStructLiteral;
import it.unipr.cfg.program.unit.RustEnumUnit;
import it.unipr.cfg.type.composite.RustStructType;
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
import it.unive.lisa.program.CompilationUnit;
import it.unive.lisa.program.Global;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Rust enum struct literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustEnumStructLiteral extends RustEnumLiteral<NaryExpression> {

	private String variantName;
	private String[] names;

	/**
	 * Build the enum struct literal.
	 * 
	 * @param cfg         the {@link CFG} where this literal lies
	 * @param location    the location where this literal is defined
	 * @param names       the names of the field in this struct
	 * @param expressions the values to be used inside the literal. During
	 *                        destructuring they can be variables, in (almost)
	 *                        any other case they can be other literals
	 * @param variantName the name of this variant
	 * @param enumType    the enum type of this literal
	 */
	public RustEnumStructLiteral(CFG cfg, CodeLocation location, String[] names, Expression[] expressions,
			String variantName,
			RustEnumType enumType) {
		super(cfg, location, new RustMultipleExpression(cfg, location, expressions), enumType);
		this.names = names;
		this.variantName = variantName;
	}

	@Override
	public String toString() {
		return getStaticType() + "::" + variantName + "{" + getValue() + "}";
	}

	@Override
	public boolean isInstanceOf(RustEnumVariant variant) {
		if (variant instanceof RustStructType) {
			CompilationUnit structUnit = ((RustStructType) variant).getUnit();

			Set<String> globalSet = structUnit.getInstanceGlobals(false).stream().map(g -> g.getName())
					.collect(Collectors.toSet());

			return globalSet.equals(new HashSet<String>(Arrays.asList(names)));
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

		// Find a collection of valid struct inside the unit
		RustEnumUnit enumUnit = RustEnumType.get(getStaticType().toString()).getUnit();
		Collection<RustEnumVariant> validStructs = new HashSet<>();
		for (RustEnumVariant variant : enumUnit.getVariants()) {
			if (variant instanceof RustStructType) {
				RustStructType structType = (RustStructType) variant;
				Collection<Global> structGlobals = structType.getUnit().getInstanceGlobals(false);

				// Check that each type in the expression canBeAssignedTo the
				// corresponding type in the struct
				for (int i = 0; i < names.length; ++i) {
					Expression subExpr = getValue().getSubExpressions()[i];
					String key = names[i];
					if (!structGlobals.stream().anyMatch(
							g -> subExpr.getStaticType().canBeAssignedTo(g.getStaticType()) && key.equals(g.getName())))
						validStructs.add(variant);
				}
			}
		}

		RustMultipleExpression inner = (RustMultipleExpression) getValue();
		AnalysisState<A, H, V, T> result = entryState.lub(inner.semantics(entryState, interprocedural, expressions));
		for (RustEnumVariant struct : validStructs) {
			AnalysisState<A, H, V, T> structState = RustStructLiteral.semantic(getLocation(), this,
					(RustStructType) struct, interprocedural, entryState, names, inner.getSubExpressions(),
					inner.getSymbolicExpression(), expressions);

			result = result.lub(structState);
		}

		return result;
	}
}
