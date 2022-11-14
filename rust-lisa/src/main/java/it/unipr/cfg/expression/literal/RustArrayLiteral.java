package it.unipr.cfg.expression.literal;

import java.util.Arrays;

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
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.AccessChild;
import it.unive.lisa.symbolic.heap.HeapAllocation;
import it.unive.lisa.symbolic.heap.HeapDereference;
import it.unive.lisa.symbolic.heap.HeapReference;
import it.unive.lisa.symbolic.value.Variable;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;

/**
 * Rust array literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustArrayLiteral extends NaryExpression {

	/**
	 * Build the array literal.
	 * 
	 * @param cfg        the {@link CFG} where this literal lies
	 * @param location   the location where this literal is defined
	 * @param staticType the static type of the array
	 * @param values     the values inside the literal
	 */
	public RustArrayLiteral(CFG cfg, CodeLocation location, Type staticType, Expression... values) {
		super(cfg, location, "[]", staticType, values);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(getSubExpressions());
	}

	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> expressionSemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			ExpressionSet<SymbolicExpression>[] params, StatementStore<A, H, V, T> expressions)
			throws SemanticException {

//		Type arrayType = new RustArrayType(getSubExpressions()[0].getStaticType(), getSubExpressions().length);
		
		// Mi faccio dare l'allocazione
		HeapAllocation allocation = new HeapAllocation(getStaticType(), getLocation());
		// Modifico (nel caso) il dominio dell'analisi
		AnalysisState<A, H, V, T> allocationState = state.smallStepSemantics(allocation, this);

		// Lisa a questo punto riscrive le espressioni e mi ritorna una qualche tipo di
		// Expression dentro un Set di Expression
		ExpressionSet<SymbolicExpression> containerExprs = allocationState.getComputedExpressions();

		// Nota: non posso usare direttamente il container perchè è una zona di memoria
		// intesa come fisica. Per usarla prima devo referenziarla in qualche modo,
		// anche in modo tale che l'uso sia trasparente
		AnalysisState<A, H, V, T> result = state.bottom();
		for (SymbolicExpression container : containerExprs) {
			HeapReference ref = new HeapReference(new RustReferenceType(getStaticType(), false), container, getLocation());
			HeapDereference deref = new HeapDereference(getStaticType(), ref, getLocation());

			AnalysisState<A, H, V, T> startingState = allocationState;
			// Scorro ciascuna subExpression
			for (int i = 0; i < getSubExpressions().length; ++i) {
				// Uso una variable per andare ad accedere tramite la accessChild
				Variable variable = new Variable(Untyped.INSTANCE, i + "", getLocation());
				AccessChild child = new AccessChild(getSubExpressions()[0].getStaticType(), deref, variable, getLocation());
				
				// Ci serve accumulare dentro un tmp per questioni particolari che non abbiamo tempo di vefere
				AnalysisState<A, H, V, T> tmp = result.bottom();

				// aggiorno lo stato del child all'interno dell'allocazione
				AnalysisState<A, H, V, T> accessedChildState = startingState.smallStepSemantics(child, this);
				for (SymbolicExpression childIdentifier : accessedChildState.getComputedExpressions()) {
					
					for (SymbolicExpression exprParam : params[i]) {
						tmp = tmp.lub(startingState.assign(childIdentifier, exprParam, this));
					}
				}
				
				startingState = tmp;				
			}
			
			result = result.lub(startingState.smallStepSemantics(ref, this));
		}

		return result;
	}

}
