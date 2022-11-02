package it.unipr.frontend.cfg;

import java.util.Set;
import java.util.function.Predicate;

import it.unipr.frontend.RustTypeSystem;
import it.unive.lisa.analysis.ScopeToken;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.representation.DomainRepresentation;
import it.unive.lisa.analysis.representation.StringRepresentation;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.program.cfg.ProgramPoint;
import it.unive.lisa.symbolic.value.Identifier;
import it.unive.lisa.symbolic.value.ValueExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;

class NoOpTypes implements TypeDomain<NoOpTypes> {
	
	private final TypeSystem types = new RustTypeSystem();
	
	@Override
	public NoOpTypes assign(Identifier id, ValueExpression expression, ProgramPoint pp)
			throws SemanticException {
		return this;
	}

	@Override
	public NoOpTypes smallStepSemantics(ValueExpression expression, ProgramPoint pp)
			throws SemanticException {
		return this;
	}

	@Override
	public NoOpTypes assume(ValueExpression expression, ProgramPoint pp) throws SemanticException {
		return this;
	}

	@Override
	public NoOpTypes forgetIdentifier(Identifier id) throws SemanticException {
		return this;
	}

	@Override
	public NoOpTypes forgetIdentifiersIf(Predicate<Identifier> test) throws SemanticException {
		return this;
	}

	@Override
	public Satisfiability satisfies(ValueExpression expression, ProgramPoint pp) throws SemanticException {
		return Satisfiability.UNKNOWN;
	}

	@Override
	public NoOpTypes pushScope(ScopeToken token) throws SemanticException {
		return this;
	}

	@Override
	public NoOpTypes popScope(ScopeToken token) throws SemanticException {
		return this;
	}

	@Override
	public DomainRepresentation representation() {
		return new StringRepresentation("noop");
	}

	@Override
	public NoOpTypes lub(NoOpTypes other) throws SemanticException {
		return this;
	}

	@Override
	public NoOpTypes widening(NoOpTypes other) throws SemanticException {
		return this;
	}

	@Override
	public boolean lessOrEqual(NoOpTypes other) throws SemanticException {
		return true;
	}

	@Override
	public NoOpTypes top() {
		return this;
	}

	@Override
	public NoOpTypes bottom() {
		return this;
	}

	@Override
	public Set<Type> getInferredRuntimeTypes() {
		return types.getTypes();
	}

	@Override
	public Type getInferredDynamicType() {
		return Untyped.INSTANCE;
	}
}