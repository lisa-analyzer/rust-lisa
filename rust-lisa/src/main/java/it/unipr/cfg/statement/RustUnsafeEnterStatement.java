package it.unipr.cfg.statement;

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
import it.unive.lisa.program.cfg.edge.Edge;
import it.unive.lisa.program.cfg.statement.Statement;
import it.unive.lisa.util.datastructures.graph.GraphVisitor;

/**
 * Rust unsafe entering statement.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustUnsafeEnterStatement extends Statement {

	/**
	 * Constructs a {@link RustUnsafeEnterStatement}.
	 * 
	 * @param cfg      the cfg in which this statement will be put
	 * @param location the location in the program
	 */
	public RustUnsafeEnterStatement(CFG cfg, CodeLocation location) {
		super(cfg, location);
	}

	@Override
	public int setOffset(int offset) {
		int oldOffset = this.offset;
		this.offset = offset;
		return oldOffset;
	}

	@Override
	public <V> boolean accept(GraphVisitor<CFG, Statement, Edge, V> visitor, V tool) {
		// TODO too coarse for now
		return false;
	}

	@Override
	public String toString() {
		return "Unsafe enter";
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> semantics(
					AnalysisState<A, H, V, T> entryState, InterproceduralAnalysis<A, H, V, T> interprocedural,
					StatementStore<A, H, V, T> expressions) throws SemanticException {
		// TODO Too coarse for now
		return entryState.top();
	}
}
