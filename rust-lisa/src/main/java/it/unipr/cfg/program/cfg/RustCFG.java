package it.unipr.cfg.program.cfg;

import it.unipr.cfg.expression.literal.RustUnitLiteral;
import it.unipr.cfg.expression.utils.RustReturnExpression;
import it.unipr.cfg.type.primitive.RustUnitType;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeMemberDescriptor;
import it.unive.lisa.program.cfg.edge.Edge;
import it.unive.lisa.program.cfg.edge.FalseEdge;
import it.unive.lisa.program.cfg.edge.SequentialEdge;
import it.unive.lisa.program.cfg.edge.TrueEdge;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NoOp;
import it.unive.lisa.program.cfg.statement.Ret;
import it.unive.lisa.program.cfg.statement.Return;
import it.unive.lisa.program.cfg.statement.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Implementation of a CFG of Rust.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustCFG extends CFG {
	private final boolean unsafe;

	/**
	 * Yields a CFG from a descriptor.
	 * 
	 * @param descriptor the descriptor of this CFG
	 * @param unsafe     the decorator unsafe
	 */
	public RustCFG(CodeMemberDescriptor descriptor, boolean unsafe) {
		super(descriptor);
		this.unsafe = unsafe;
	}

	/**
	 * Deletes a node that is a leaf node (e.g. a terminating node) in the CFG,
	 * putting a different node in the same place.
	 * 
	 * @param rustCFG the CFG that needs to be updated
	 * @param oldNode the node that needs to be deleted from the tree
	 * @param newNode the new node that need to be inserted
	 */
	private void switchLeafNodes(Statement oldNode, Statement newNode) {
		for (Edge e : getNodeList().getIngoingEdges(oldNode)) {
			Edge newEdge = e.newInstance(e.getSource(), newNode);
			Edge edgeReference = getEdgeConnecting(e.getSource(), e.getDestination());
			getNodeList().removeEdge(edgeReference);
			addEdge(newEdge);
		}

		getOutgoingEdges(oldNode).forEach(edge -> getNodeList().removeEdge(edge));
		getNodeList().removeNode(oldNode);
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
			Ret ret = new Ret(this, getDescriptor().getLocation());

			// Add possible missing ret as final instruction
			if (getAllExitpoints().isEmpty()) {
				Set<Statement> exitNodes = nodes
						.stream()
						.filter(n -> followersOf(n).isEmpty())
						.collect(Collectors.toSet());

				for (Statement exit : exitNodes) {
					addNode(ret);
					addEdge(new SequentialEdge(exit, ret));
				}
			} else
				addNode(ret);

			// Substitute return with ret nodes
			List<Pair<Statement, Statement>> toSwitchList = new ArrayList<>();
			for (Statement node : nodes) {
				if (node instanceof RustReturnExpression) {
					RustReturnExpression rustReturn = (RustReturnExpression) node;

					// The value inside the return is null iff the return was
					// empty or has a RustUnitLiteral
					if (rustReturn.getSubExpression() instanceof RustUnitLiteral) {
						NoOp noOp = new NoOp(this, node.getLocation());
						toSwitchList.add(Pair.of(node, noOp));
					}
					// Here the return type of the expression is void but the
					// expression actually do something, so we add a last node
					// that evaluates the inner expression (in case of
					// side-effects)
					else {
						Expression expr = rustReturn.getSubExpression();
						toSwitchList.add(Pair.of(node, expr));
					}
				}
			}

			for (Pair<Statement, Statement> toSwitch : toSwitchList) {
				addNode(toSwitch.getRight());
				switchLeafNodes(toSwitch.getLeft(), toSwitch.getRight());
				addEdge(new SequentialEdge(toSwitch.getRight(), ret));
			}

		}
		// Substitute inner RustExplicitReturn with return statements
		else {
			List<Statement> nonNoOpNodes = nodes.stream().filter(n -> !(n instanceof NoOp))
					.collect(Collectors.toList());
			if (nonNoOpNodes.size() == 1) {
				// AdjacencyMatrix<Statement, Edge, CFG> adj =
				// getAdjacencyMatrix();
				Statement node = nonNoOpNodes.get(0);
				getEdges().forEach(e -> getNodeList().removeEdge(e));
				getNodes().forEach(n -> getNodeList().removeNode(n));
				addNode(node);
			}

			// Add return to exit node if it's the only stmt
			if (getNodes().size() == 1) {
				Statement onlyNode = nodes.stream().findFirst().get();

				NoOp noOp = new NoOp(this, getDescriptor().getLocation());
				addNode(noOp, true);
				getEntrypoints().remove(onlyNode);

				addEdge(new SequentialEdge(noOp, onlyNode));
				getAllExitpoints().add(onlyNode);
			}

			List<Pair<Statement, Return>> toSwitchList = new ArrayList<>();
			for (Statement stmt : nodes)
				if (stmt instanceof RustReturnExpression) {
					Expression value = ((RustReturnExpression) stmt).getSubExpression();

					Return ret = new Return(this, getDescriptor().getLocation(), value);

					toSwitchList.add(Pair.of(stmt, ret));
				}

			for (Pair<Statement, Return> toSwitch : toSwitchList) {
				addNode(toSwitch.getRight());
				switchLeafNodes(toSwitch.getLeft(), toSwitch.getRight());
				getNodeList().removeNode(toSwitch.getLeft());
			}
		}

		simplify();

		Set<Edge> toRemove = new HashSet<>();
		Set<Edge> toAdd = new HashSet<>();

		for (Edge e : getEdges())
			if (e instanceof TrueEdge) {
				Collection<Edge> outgoingEdges = getOutgoingEdges(e.getSource());
				for (Edge outgoing : outgoingEdges) {
					if (outgoing instanceof FalseEdge && outgoing.getSource().equals(e.getSource())
							&& outgoing.getDestination().equals(e.getDestination())) {
						toRemove.add(e);
						toRemove.add(outgoing);

						toAdd.add(new SequentialEdge(e.getSource(), e.getDestination()));
					}
				}
			}

		toRemove.forEach(r -> getNodeList().removeEdge(r));
		toAdd.forEach(a -> getNodeList().addEdge(a));

		// It can happen sometimes that there are no nodes with parent, adding
		// them
		getEntrypoints().addAll(
				getNodes().stream().filter(node -> getIngoingEdges(node).isEmpty()).collect(Collectors.toSet()));

		// Removing nodes that are not in the graph
		getEntrypoints().removeIf(entry -> !containsNode(entry));
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
