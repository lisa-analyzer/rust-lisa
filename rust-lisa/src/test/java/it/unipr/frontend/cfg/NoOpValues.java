package it.unipr.frontend.cfg;

import java.util.function.Predicate;

import it.unive.lisa.analysis.ScopeToken;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.representation.DomainRepresentation;
import it.unive.lisa.analysis.representation.StringRepresentation;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.program.cfg.ProgramPoint;
import it.unive.lisa.symbolic.value.Identifier;
import it.unive.lisa.symbolic.value.ValueExpression;

class NoOpValues implements ValueDomain<NoOpValues> {

	@Override
	public NoOpValues assign(Identifier id, ValueExpression expression, ProgramPoint pp)
			throws SemanticException {
		return this;
	}

	@Override
	public NoOpValues smallStepSemantics(ValueExpression expression, ProgramPoint pp)
			throws SemanticException {
		return this;
	}

	@Override
	public NoOpValues assume(ValueExpression expression, ProgramPoint pp) throws SemanticException {
		return this;
	}

	@Override
	public NoOpValues forgetIdentifier(Identifier id) throws SemanticException {
		return this;
	}

	@Override
	public NoOpValues forgetIdentifiersIf(Predicate<Identifier> test) throws SemanticException {
		return this;
	}

	@Override
	public Satisfiability satisfies(ValueExpression expression, ProgramPoint pp) throws SemanticException {
		return Satisfiability.UNKNOWN;
	}

	@Override
	public NoOpValues pushScope(ScopeToken token) throws SemanticException {
		return this;
	}

	@Override
	public NoOpValues popScope(ScopeToken token) throws SemanticException {
		return this;
	}

	@Override
	public DomainRepresentation representation() {
		return new StringRepresentation("noop");
	}

	@Override
	public NoOpValues lub(NoOpValues other) throws SemanticException {
		return this;
	}

	@Override
	public NoOpValues widening(NoOpValues other) throws SemanticException {
		return this;
	}

	@Override
	public boolean lessOrEqual(NoOpValues other) throws SemanticException {
		return true;
	}

	@Override
	public NoOpValues top() {
		return this;
	}

	@Override
	public NoOpValues bottom() {
		return this;
	}

}
