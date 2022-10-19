package it.unipr.cfg;

import it.unipr.cfg.expression.RustReturnExpression;
import it.unipr.cfg.type.RustUnitType;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CFGDescriptor;
import it.unive.lisa.program.cfg.edge.Edge;
import it.unive.lisa.program.cfg.edge.FalseEdge;
import it.unive.lisa.program.cfg.edge.SequentialEdge;
import it.unive.lisa.program.cfg.edge.TrueEdge;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NoOp;
import it.unive.lisa.program.cfg.statement.Ret;
import it.unive.lisa.program.cfg.statement.Return;
import it.unive.lisa.program.cfg.statement.Statement;
import it.unive.lisa.util.datastructures.graph.AdjacencyMatrix;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Implementation of a CFG of Rust.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustCFG extends CFG {
	private final boolean unsafe;
	private final SourceCodeLocation location;

	/**
	 * Deletes a node that is a leaf node (e.g. a terminating node) in the CFG,
	 * putting a different node in the same place.
	 * 
	 * @param rustCFG the CFG that needs to be updated
	 * @param oldNode the node that needs to be deleted from the tree
	 * @param newNode the new node that need to be inserted
	 */
	private void switchLeafNodes(Statement oldNode, Statement newNode) {
		AdjacencyMatrix<Statement, Edge, CFG> adj = getAdjacencyMatrix();

		Collection<Edge> edges = adj.getIngoingEdges(oldNode);
		for (Edge e : edges) {

			Edge newEdge = new SequentialEdge(e.getSource(), newNode);
			if (e instanceof TrueEdge)
				newEdge = new TrueEdge(e.getSource(), newNode);
			else if (e instanceof FalseEdge)
				newEdge = new FalseEdge(e.getSource(), newNode);

			adj.removeEdge(e);
			addEdge(newEdge);
		}

		adj.removeNode(oldNode);
	}

	/**
	 * Perform a lot of operation needed in order to prepare the CFG for the
	 * use.
	 * 
	 * @param rustCFG    the CFG that needs to be updated
	 * @param ctx        the context in which to operate
	 * @param returnType the return type of this CFG (e.g. return type of a
	 *                       function)
	 * 
	 * @return the ready-to-use CFG
	 */
	private void prepareCFG(ParserRuleContext ctx) {
		Collection<Statement> nodes = getNodes();

		// Substitute exit points wit
		if (getDescriptor().getReturnType() instanceof RustUnitType) {
			Ret ret = new Ret(this, location);

			// Add possible missing ret as final instruction
			if (getAllExitpoints().isEmpty()) {
				Set<Statement> exitNodes = nodes
						.stream()
						.filter(n -> getAdjacencyMatrix().followersOf(n).isEmpty())
						.collect(Collectors.toSet());

				for (Statement exit : exitNodes) {
					addNode(ret);
					addEdge(new SequentialEdge(exit, ret));
				}
			} else
				addNode(ret);

			// Substitute return with ret nodes
			for (Statement node : nodes) {
				if (node instanceof RustReturnExpression) {
					NoOp noOp = new NoOp(this, location);
					addNode(noOp);

					switchLeafNodes(node, noOp);
					addEdge(new SequentialEdge(noOp, ret));
				}
			}
		}
		// Substitute inner RustExplicitReturn with return statements
		else {
			List<Statement> nonNoOpNodes = nodes.stream().filter(n -> !(n instanceof NoOp))
					.collect(Collectors.toList());
			if (nonNoOpNodes.size() == 1) {
				AdjacencyMatrix<Statement, Edge, CFG> adj = getAdjacencyMatrix();
				Statement node = nonNoOpNodes.get(0);
				adj.getEdges().forEach(e -> adj.removeEdge(e));
				adj.getNodes().forEach(n -> adj.removeNode(n));
				addNode(node);
			}

			// Add return to exit node if it's the only stmt
			if (getNodes().size() == 1) {
				Statement onlyNode = nodes.stream().findFirst().get();

				NoOp noOp = new NoOp(this, location);
				addNode(noOp, true);
				getEntrypoints().remove(onlyNode);

				addEdge(new SequentialEdge(noOp, onlyNode));
				getAllExitpoints().add(onlyNode);
			}

			for (Statement stmt : nodes)
				if (stmt instanceof RustReturnExpression) {
					Expression value = ((RustReturnExpression) stmt).getSubExpression();

					Return ret = new Return(this, location, value);
					addNode(ret);

					switchLeafNodes(stmt, ret);
				}
		}

		simplify();

		Set<Edge> toRemove = new HashSet<>();
		Set<Edge> toAdd = new HashSet<>();

		AdjacencyMatrix<Statement, Edge, CFG> adj = getAdjacencyMatrix();
		for (Edge e : adj.getEdges())
			if (e instanceof TrueEdge) {
				Collection<Edge> ingoinEdges = adj.getIngoingEdges(e.getDestination());
				for (Edge ingoing : ingoinEdges) {
					if (ingoing instanceof FalseEdge && ingoing.getSource().equals(e.getSource())) {
						toRemove.add(e);
						toRemove.add(ingoing);

						toAdd.add(new SequentialEdge(e.getSource(), e.getDestination()));
					}
				}
			}

		toRemove.forEach(r -> adj.removeEdge(r));
		toAdd.forEach(a -> adj.addEdge(a));
	}

	/**
	 * Yields a CFG from a descriptor.
	 * 
	 * @param descriptor the descriptor of this CFG
	 * @param unsafe     the decorator unsafe
	 * @param location   the source code location where this CFG is
	 */
	public RustCFG(CFGDescriptor descriptor, boolean unsafe, SourceCodeLocation location) {
		super(descriptor);
		this.unsafe = unsafe;
		this.location = location;
	}

	/**
	 * Yields the decorator unsafe of this function.
	 * 
	 * @return true if the function is unsafe
	 */
	public boolean isUnsafe() {
		return unsafe;
	}

	/**
	 * Perform the operations needed for preparing the CFG for the use.
	 * 
	 * @param ctx the context in which to operate
	 */
	public void finalize(ParserRuleContext ctx) {
		prepareCFG(ctx);
	}
}
