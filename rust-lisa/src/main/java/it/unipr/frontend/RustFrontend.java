package it.unipr.frontend;

import static it.unipr.frontend.RustFrontendUtilities.locationOf;

import it.unipr.cfg.program.unit.RustEnumUnit;
import it.unipr.cfg.program.unit.RustTraitUnit;
import it.unipr.cfg.type.RustBooleanType;
import it.unipr.cfg.type.RustCharType;
import it.unipr.cfg.type.RustPointerType;
import it.unipr.cfg.type.RustStrType;
import it.unipr.cfg.type.RustUnitType;
import it.unipr.cfg.type.composite.RustArrayType;
import it.unipr.cfg.type.composite.RustStructType;
import it.unipr.cfg.type.composite.RustTraitType;
import it.unipr.cfg.type.composite.RustTupleType;
import it.unipr.cfg.type.composite.enums.RustEnumType;
import it.unipr.cfg.type.composite.enums.RustEnumVariant;
import it.unipr.cfg.type.numeric.floating.RustF32Type;
import it.unipr.cfg.type.numeric.floating.RustF64Type;
import it.unipr.cfg.type.numeric.signed.RustI128Type;
import it.unipr.cfg.type.numeric.signed.RustI16Type;
import it.unipr.cfg.type.numeric.signed.RustI32Type;
import it.unipr.cfg.type.numeric.signed.RustI64Type;
import it.unipr.cfg.type.numeric.signed.RustI8Type;
import it.unipr.cfg.type.numeric.signed.RustIsizeType;
import it.unipr.cfg.type.numeric.unsigned.RustU128Type;
import it.unipr.cfg.type.numeric.unsigned.RustU16Type;
import it.unipr.cfg.type.numeric.unsigned.RustU32Type;
import it.unipr.cfg.type.numeric.unsigned.RustU64Type;
import it.unipr.cfg.type.numeric.unsigned.RustU8Type;
import it.unipr.cfg.type.numeric.unsigned.RustUsizeType;
import it.unipr.rust.antlr.RustBaseVisitor;
import it.unipr.rust.antlr.RustLexer;
import it.unipr.rust.antlr.RustParser;
import it.unipr.rust.antlr.RustParser.CrateContext;
import it.unipr.rust.antlr.RustParser.Enum_declContext;
import it.unipr.rust.antlr.RustParser.ItemContext;
import it.unipr.rust.antlr.RustParser.Mod_bodyContext;
import it.unipr.rust.antlr.RustParser.Pub_itemContext;
import it.unipr.rust.antlr.RustParser.Struct_declContext;
import it.unipr.rust.antlr.RustParser.Trait_declContext;
import it.unipr.rust.antlr.RustParser.Trait_itemContext;
import it.unive.lisa.program.ClassUnit;
import it.unive.lisa.program.CompilationUnit;
import it.unive.lisa.program.Global;
import it.unive.lisa.program.Program;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeMember;
import it.unive.lisa.program.cfg.statement.evaluation.EvaluationOrder;
import it.unive.lisa.program.cfg.statement.evaluation.LeftToRightEvaluation;
import it.unive.lisa.type.Type;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The Rust front-end for LiSA.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustFrontend extends RustBaseVisitor<Object> {

	/**
	 * The parameter evaluation order strategy.
	 */
	public static final EvaluationOrder EVALUATION_ORDER = LeftToRightEvaluation.INSTANCE;

	/**
	 * Reference to the parser
	 */
	private static RustParser parser;

	/**
	 * File path of the Rust program to be analyzed
	 */
	private final String filePath;

	/**
	 * LiSA program corresponding to the Rust program located at
	 * {@code filePath}
	 */
	private final Program program;

	/**
	 * Reference to the current unit
	 */
	private CompilationUnit currentUnit;

	private RustFrontend(String filePath) {
		this.filePath = filePath;
		this.program = new Program(new RustFeatures(), new RustTypeSystem());
	}

	private void registerTypes() {
		program.getTypes().registerType(RustF32Type.getInstance());
		program.getTypes().registerType(RustF64Type.getInstance());
		program.getTypes().registerType(RustI8Type.getInstance());
		program.getTypes().registerType(RustI16Type.getInstance());
		program.getTypes().registerType(RustI32Type.getInstance());
		program.getTypes().registerType(RustI64Type.getInstance());
		program.getTypes().registerType(RustI128Type.getInstance());
		program.getTypes().registerType(RustIsizeType.getInstance());
		program.getTypes().registerType(RustU8Type.getInstance());
		program.getTypes().registerType(RustU16Type.getInstance());
		program.getTypes().registerType(RustU32Type.getInstance());
		program.getTypes().registerType(RustU64Type.getInstance());
		program.getTypes().registerType(RustU128Type.getInstance());
		program.getTypes().registerType(RustUsizeType.getInstance());
		program.getTypes().registerType(RustBooleanType.getInstance());
		program.getTypes().registerType(RustCharType.getInstance());
		program.getTypes().registerType(RustStrType.getInstance());
		program.getTypes().registerType(RustUnitType.getInstance());
		RustPointerType.all().forEach(program.getTypes()::registerType);
		RustStructType.all().forEach(program.getTypes()::registerType);
		RustEnumType.all().forEach(program.getTypes()::registerType);
		RustArrayType.all().forEach(program.getTypes()::registerType);
		RustTupleType.all().forEach(program.getTypes()::registerType);
		RustTraitType.all().forEach(program.getTypes()::registerType);
	}

	/**
	 * Yields the {@link Program} corresponding to the Rust program located at
	 * {@code filePath}.
	 * 
	 * @param filePath the file path where the Rust program to be analyzed
	 * 
	 * @return the {@link Program} corresponding to the Rust program located at
	 *             {@code filePath}
	 * 
	 * @throws IOException if anything goes wrong during reading the file
	 */
	public static Program processFile(String filePath) throws IOException {
		return new RustFrontend(filePath).toLiSAProgram();
	}

	/**
	 * Yields the instance of {@link RustParser}.
	 * 
	 * @return the reference to the parser
	 */
	public static RustParser getParser() {
		return parser;
	}

	/**
	 * Yields the {@link Program} corresponding to the Rust program located at
	 * {@code filePath}.
	 * 
	 * @return the {@link Program} corresponding to the Rust program located at
	 *             {@code filePath}
	 * 
	 * @throws IOException if anything goes wrong during reading the file
	 */
	private Program toLiSAProgram() throws IOException {

		InputStream is = new FileInputStream(filePath);
		RustLexer lexer = new RustLexer(CharStreams.fromStream(is, StandardCharsets.UTF_8));
		RustParser parser = new RustParser(new CommonTokenStream(lexer));
		RustFrontend.parser = parser;

		ParseTree tree = parser.crate();
		visitCrate((CrateContext) tree);
		return program;
	}

	@Override
	public Object visitCrate(CrateContext ctx) {
		ClassUnit mainUnit = new ClassUnit(new SourceCodeLocation(filePath, 0, 0), program, filePath, false);
		currentUnit = mainUnit;
		program.addUnit(mainUnit);
		return visitMod_body(ctx.mod_body());
	}

	@Override
	public Void visitMod_body(Mod_bodyContext ctx) {
		// TODO: skipping for the moment inner_attr
		for (ItemContext i : ctx.item())
			visitItem(i);

		registerTypes();
		return null;
	}

	@Override
	public Void visitItem(ItemContext ctx) {
		if (ctx.pub_item() != null)
			visitPub_item(ctx.pub_item());

		for (Type t : RustStructType.all())
			program.addUnit(((RustStructType) t).getUnit());

		for (Type t : RustEnumType.all())
			program.addUnit(((RustEnumType) t).getUnit());

		if (ctx.impl_block() != null) {
			String[] structAndTraitName = ctx.impl_block().impl_what().getText().split("for");
			String structName;
			if (structAndTraitName.length == 1)
				structName = structAndTraitName[0];
			else
				structName = structAndTraitName[1];

			RustStructType struct = RustStructType.get(structName);
			Pair<Type, Type> structAndTrait = new RustTypeVisitor(filePath, struct.getUnit(), program)
					.visitImpl_what(ctx.impl_block().impl_what());

			CompilationUnit structUnit = struct.getUnit();
			if (structAndTrait.getRight() != null) {
				RustTraitType trait = RustTraitType.get(structAndTrait.getRight().toString());
				structUnit.addAncestor(trait.getUnit());
			}

			List<CFG> implCfg = new RustCodeMemberVisitor(filePath, program, structUnit)
					.visitImpl_block(ctx.impl_block());

			for (CFG cfg : implCfg)
				structUnit.addInstanceCodeMember(cfg);
		}

		if (ctx.item_macro_use() != null)
			// Both macro definitions and calls are here
			// TODO parsing only calls for now
			new RustCodeMemberVisitor(filePath, program, currentUnit).visitItem_macro_use(ctx.item_macro_use());

		return null;
	}

	@Override
	public Void visitPub_item(Pub_itemContext ctx) {
		if (ctx.struct_decl() != null)
			visitStruct_decl(ctx.struct_decl());

		if (ctx.enum_decl() != null)
			visitEnum_decl(ctx.enum_decl());

		if (ctx.trait_decl() != null)
			visitTrait_decl(ctx.trait_decl());

		if (ctx.fn_decl() != null)
			currentUnit.addCodeMember(new RustCodeMemberVisitor(filePath, program, currentUnit)
					.visitFn_decl(ctx.fn_decl()));

		return null;
	}

	@Override
	public Void visitStruct_decl(Struct_declContext ctx) {
		// TODO skipping ty_params? production
		String name = ctx.ident().getText();
		ClassUnit structUnit = new ClassUnit(locationOf(ctx, filePath), program, name, true);

		RustStructType.lookup(name, structUnit);

		List<Global> fields = new RustTypeVisitor(filePath, currentUnit, program).visitStruct_tail(ctx.struct_tail());

		for (Global f : fields)
			structUnit.addInstanceGlobal(f);

		return null;
	}

	@Override
	public Void visitEnum_decl(Enum_declContext ctx) {
		// TODO skipping ty_params? and where_clause?
		String name = ctx.ident().getText();
		RustEnumUnit enumUnit = new RustEnumUnit(locationOf(ctx, filePath), program, name, true);

		List<RustEnumVariant> enumVariants = new RustTypeVisitor(filePath, currentUnit, program)
				.visitEnum_variant_list(ctx.enum_variant_list());

		for (RustEnumVariant variant : enumVariants)
			enumUnit.addVariant(variant);

		RustEnumType.lookup(name, enumUnit);

		return null;
	}

	@Override
	public Void visitTrait_decl(Trait_declContext ctx) {
		// TODO skipping auto?, ty_params?, colon_bound? and where_clause?
		String name = ctx.ident().getText();
		RustTraitUnit traitUnit = new RustTraitUnit(locationOf(ctx, filePath), program, name,
				ctx.getChild(0).getText().equals("unsafe"));

		RustTraitType.lookup(name, traitUnit);

		List<CodeMember> fnDefinitions = new ArrayList<>();
		for (Trait_itemContext traitItemCtx : ctx.trait_item())
			fnDefinitions.add(new RustCodeMemberVisitor(filePath, program, traitUnit).visitTrait_item(traitItemCtx));

		for (CodeMember definition : fnDefinitions)
			traitUnit.addInstanceCodeMember(definition);

		return null;
	}

}
