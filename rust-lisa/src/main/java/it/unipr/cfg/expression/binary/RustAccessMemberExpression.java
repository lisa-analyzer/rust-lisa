package it.unipr.cfg.expression.binary;

import java.util.Collections;

import com.ibm.icu.impl.CollectionSet;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.Global;
import it.unive.lisa.program.Unit;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.BinaryExpression;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.AccessChild;
import it.unive.lisa.symbolic.heap.HeapDereference;
import it.unive.lisa.symbolic.heap.HeapReference;
import it.unive.lisa.type.ReferenceType;
import it.unive.lisa.type.Type;

/**
 * Rust access to member expression (e.g., x.y).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustAccessMemberExpression extends BinaryExpression {

	/**
	 * Builds the access to member expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param left     the left-hand side of this expression
	 * @param right    the right-hand side of this expression
	 */
	public RustAccessMemberExpression(CFG cfg, CodeLocation location, Expression left, Expression right) {
		super(cfg, location, ".", right.getStaticType(), left, right);
	}

	@Override
	public String toString() {
		return getSubExpressions()[0] + "." + getSubExpressions()[1];
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
					SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
					throws SemanticException {
		
		AnalysisState<A, H, V, T> result = state.bottom();
	
		SymbolicExpression deref = left;

		for (Type runtimeType : left.getRuntimeTypes(getProgram().getTypes())) {
			Type refType = runtimeType;
			
			// TODO: check whether using a counter is appropriate. Probably it is not.
			//
			// There are two test involving this check: testStruct and testRectangle.
			// The first one does not have parameter passage, which means that the
			// number of references used is even (one for LiSA memory model and one for
			// Andersen's lift). The second one has parameter passage: when a function
			// with a reference as parameter is called, there are three references for
			// the same memory zone (the inner most for the LiSA memory model, then one
			// for the Andersen's lift and the latter which represents a rust
			// reference).
			//
			// The assumption here is that if there are two pointers, than the
			// "left" SymbolicExpression is declared in the same function as where it
			// is used (or in a function that has its ownership), otherwise it will be
			// under a third reference also.
			int counter = 0;
			while (refType.isPointerType()) {
				deref = new HeapDereference(refType, deref, getLocation());
				refType = refType.asReferenceType().getInnerType();
				counter++;
			}
			
			if (counter % 2 == 1)
				deref = new HeapDereference(refType, deref, getLocation());
		
			Type accessChildType = getStaticType();
			if (getStaticType().isUntyped()) {
				// Check weather we can do better than assign Untyped to the expression
				while (refType.isReferenceType())
					refType = refType.asReferenceType().getInnerType();
				
				Global g = refType.asUnitType().getUnit().getInstanceGlobal(right.toString(), true);
				
				if (g != null)
					accessChildType = g.getStaticType();
			}
			
			AccessChild accessChild = new AccessChild(accessChildType, deref, right, getLocation());
			accessChild.setRuntimeTypes(Collections.singleton(accessChildType));
			
			result = result.lub(state.smallStepSemantics(accessChild, this));
		}
		
		return result;
	}
}
