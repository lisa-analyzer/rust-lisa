package it.unipr.frontend;

import it.unipr.cfg.type.RustUnitType;
import it.unipr.cfg.type.composite.RustArrayType;
import it.unipr.cfg.type.composite.RustTupleType;
import it.unipr.cfg.type.numeric.signed.RustI32Type;
import it.unipr.rust.antlr.RustBaseVisitor;
import it.unipr.rust.antlr.RustParser.Assign_exprContext;
import it.unipr.rust.antlr.RustParser.ExprContext;
import it.unipr.rust.antlr.RustParser.IdentContext;
import it.unipr.rust.antlr.RustParser.Shift_exprContext;
import it.unipr.rust.antlr.RustParser.TyContext;
import it.unipr.rust.antlr.RustParser.Ty_pathContext;
import it.unipr.rust.antlr.RustParser.Ty_path_mainContext;
import it.unipr.rust.antlr.RustParser.Ty_path_segment_no_superContext;
import it.unipr.rust.antlr.RustParser.Ty_path_tailContext;
import it.unipr.rust.antlr.RustParser.Ty_sumContext;
import it.unipr.rust.antlr.RustParser.Ty_sum_listContext;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import java.util.LinkedList;
import java.util.List;

/**
 * Type visitor for Rust, managing the parsing of Rust types.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustTypeVisitor extends RustBaseVisitor<Object> {
	
	private final RustCodeMemberVisitor codeVisitor;
	
	public RustTypeVisitor(RustCodeMemberVisitor codeVisitor) {
		this.codeVisitor = codeVisitor;
	}

	@Override
	public Type visitTy(TyContext ctx) {
		if (ctx.ty_path() != null) {
			// TODO Skipping macro_tail? part
			return visitTy_path(ctx.ty_path());
		}

		switch (ctx.getChild(0).getText()) {
		case "_":
			return Untyped.INSTANCE;
		case "(":
			if (ctx.ty_sum() != null) {
				Type type = (Type) visitTy_sum(ctx.ty_sum());

				if (ctx.ty_sum_list() != null) {
					List<Type> remainingTypes = visitTy_sum_list(ctx.ty_sum_list());
					remainingTypes.add(0, type);

					return new RustTupleType(remainingTypes);
				}

				return type;
			}

			return RustUnitType.INSTANCE;
			
		case "[":
			Type arrayType = codeVisitor.visitTy_sum(ctx.ty_sum());
			
			if (ctx.expr() != null) {
				return new RustArrayType(arrayType, getConstantValue(ctx.expr()));
			}

		default: // TODO skipping other productions
			return Untyped.INSTANCE;
		}
	}
	
	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private Integer getConstantValue(ExprContext expr) {
		// TODO We are just parsing the simple literal integers, the rest for now is out of our scope
		if (isInteger(expr.getText()))
			return Integer.parseInt(expr.getText());
		
		return null;
	}

	@Override
	public Type visitTy_path(Ty_pathContext ctx) {
		// TODO: we skip for the moment for_lifetime?
		return visitTy_path_main(ctx.ty_path_main());
	}

	@Override
	public Type visitTy_path_main(Ty_path_mainContext ctx) {
		// TODO: we are currently handling just the production ty_path_tail
		return visitTy_path_tail(ctx.ty_path_tail());
	}

	@Override
	public Type visitTy_path_tail(Ty_path_tailContext ctx) {
		// TODO: we are currently handling just the production
		// ty_path_segment_no_super
		return visitTy_path_segment_no_super(ctx.ty_path_segment_no_super());
	}

	@Override
	public Type visitTy_path_segment_no_super(Ty_path_segment_no_superContext ctx) {
		// TODO skipping ty_args?
		return ctx.ident() == null ? Untyped.INSTANCE : visitIdent(ctx.ident());
	}

	@Override
	public Type visitIdent(IdentContext ctx) {
		// TODO skipping "auto", "default" and "union"
		switch (ctx.Ident().getText()) {
		case "i32":
			System.out.println("SONO QUA");
			return RustI32Type.INSTANCE;
		default:
			return Untyped.INSTANCE;
		}
	}

	@Override
	public List<Type> visitTy_sum_list(Ty_sum_listContext ctx) {
		List<Type> types = new LinkedList<>();
		for (Ty_sumContext tyCtx : ctx.ty_sum()) {
			types.add((Type) visitTy_sum(tyCtx));
		}
		return types;
	}
}
