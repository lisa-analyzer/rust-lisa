package it.unipr.cfg.expression.unary;

import java.util.Collections;

import it.unipr.cfg.type.composite.RustReferenceType;
import it.unipr.cfg.type.primitive.RustPointerType;
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
import it.unive.lisa.program.cfg.statement.UnaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.HeapReference;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;

/**
 * Rust unary ref expression (e.g., &x).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustRefExpression extends UnaryExpression {

	/**
	 * Builds the unary ref expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param expr     the inner expression
	 * @param mutable  the mutability of the reference
	 */
	public RustRefExpression(CFG cfg, CodeLocation location, Expression expr, boolean mutable) {
		super(cfg, location, "&", Untyped.INSTANCE, expr);
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
					SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {		
		AnalysisState<A, H, V, T> result = state.bottom();
		for (Type type : expr.getRuntimeTypes(getProgram().getTypes())) {
			HeapReference ref = new HeapReference(new RustReferenceType(type, false), expr, getLocation());
			ref.setRuntimeTypes(Collections.singleton(new RustReferenceType(type, false)));
			result = result.lub(state.smallStepSemantics(ref, this));
		}
		
		return result;
	}

}
