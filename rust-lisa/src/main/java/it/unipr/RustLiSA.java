package it.unipr;

import it.unipr.frontend.RustFrontend;
import it.unipr.frontend.RustReturnTopPolicy;
import it.unive.lisa.AnalysisException;
import it.unive.lisa.LiSA;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAConfiguration.GraphType;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.pointbased.PointBasedHeap;
import it.unive.lisa.analysis.nonrelational.value.TypeEnvironment;
import it.unive.lisa.analysis.nonrelational.value.ValueEnvironment;
import it.unive.lisa.analysis.numeric.Interval;
import it.unive.lisa.analysis.types.InferredTypes;
import it.unive.lisa.interprocedural.ModularWorstCaseAnalysis;
import it.unive.lisa.interprocedural.callgraph.RTACallGraph;
import it.unive.lisa.program.Program;
import java.io.IOException;

/**
 * RustLiSA static analyzer build upon LiSA.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustLiSA {

	/**
	 * RustLiSA entry point.
	 * 
	 * @param args arguments
	 * 
	 * @throws AnalysisException if anything goes wrong during the analysis
	 * @throws IOException       if anything goes wrong during reading the file
	 */
	public static void main(String[] args) throws AnalysisException, IOException {
		Program program = RustFrontend.processFile(args[0]);

		LiSAConfiguration conf = new LiSAConfiguration();
		conf.abstractState = new SimpleAbstractState<>(
				new PointBasedHeap(),
				new ValueEnvironment<>(new Interval()),
				new TypeEnvironment<>(new InferredTypes()));
		conf.serializeResults = true;
		conf.analysisGraphs = GraphType.HTML;
		conf.callGraph = new RTACallGraph();
		conf.interproceduralAnalysis = new ModularWorstCaseAnalysis<>();
		conf.workdir = "output";
		conf.jsonOutput = true;
		conf.openCallPolicy = RustReturnTopPolicy.INSTANCE;

		LiSA lisa = new LiSA(conf);
		lisa.run(program);
	}
}
