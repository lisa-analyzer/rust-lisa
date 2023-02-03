package it.unipr.cfg.statement.assignment;

import java.util.Collections;

import it.unipr.cfg.type.composite.RustReferenceType;
import it.unipr.cfg.type.primitive.RustType;
import it.unipr.cfg.type.primitive.RustUnitType;
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
import it.unive.lisa.program.cfg.statement.BinaryExpression;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.Variable;
import it.unive.lisa.type.ReferenceType;

/**
 * Rust assignment expression (e.g., let x = y).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustLetAssignment extends BinaryExpression {

	/**
	 * Builds the let assignment expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param left     the left-hand side of this expression
	 * @param right    the right-hand side of this expression
	 */
	public RustLetAssignment(CFG cfg, CodeLocation location, Expression left, Expression right) {
		super(cfg, location, "=", RustUnitType.getInstance(), left, right);
	}

	@Override
	public String toString() {
		return "let " + getLeft().toString() + " : " + getLeft().getStaticType().toString() + " = " + getRight();
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
					SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> statementStore)
					throws SemanticException {

		if (left instanceof Variable && left.getStaticType().isInMemoryType())
			if (left.hasRuntimeTypes())
				left.setRuntimeTypes(Collections.singleton(new ReferenceType(left.getStaticType())));
		
		// forget identifiers that are Variables, are not have RustReferenceType
		// and are the rhs of an assignment
		if (!(right.getStaticType() instanceof RustReferenceType) && right instanceof Variable) {
			boolean noneCopiable = right.getRuntimeTypes(getProgram().getTypes()).stream()
				.map(t -> t instanceof ReferenceType && !(t instanceof RustReferenceType) ? ((ReferenceType) t).getInnerType() : t)
				.allMatch(t -> t instanceof RustType && !((RustType) t).isCopiable());
		
			if (noneCopiable)
				return state.assign(left, right, this).forgetIdentifier((Variable) right);
		}
		return state.assign(left, right, this);
	}
}
