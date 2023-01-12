package it.unipr.frontend.analysis;

import static it.unive.lisa.LiSAFactory.getDefaultFor;

import java.util.Arrays;

import it.unive.lisa.LiSAFactory;
import it.unive.lisa.LiSAFactory.ConfigurableComponent;
import it.unipr.frontend.RustLiSATestExecutor;
import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAFactory;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.heap.pointbased.PointBasedHeap;
import it.unive.lisa.analysis.nonrelational.value.TypeEnvironment;
import it.unive.lisa.analysis.nonrelational.value.ValueEnvironment;
import it.unive.lisa.analysis.numeric.Interval;
import it.unive.lisa.analysis.types.InferredTypes;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class RustTuple extends RustLiSATestExecutor {

	@Test
	public void testTuple() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration();
		
		for (ConfigurableComponent component : LiSAFactory.configurableComponents()) {
			String result = component.getComponent().toString();
			if (component.getDefaultInstance() != null) {
				result += " (defaults to: '" + component.getDefaultInstance().getName() + "'";
				if (component.getDefaultParameters() != null && component.getDefaultParameters().length != 0) {
					String[] paramNames = Arrays.stream(component.getDefaultParameters()).map(c -> c.getName()).toArray(String[]::new);
					result += " with parameters [" + StringUtils.join(paramNames, ", ") + "]";
				}
				result += ")";
			}
			String[] alternatives = component.getAlternatives().entrySet().stream()
					.map(e -> e.getKey().getName() + (e.getValue().length == 0 ? ""
							: " (with default parameters: " + StringUtils.join(e.getValue(), ", ") + ")"))
					.toArray(String[]::new);
			result += " possible implementations: " + StringUtils.join(alternatives, ", ");
			System.out.println(result);
		}
		
		System.out.println(SimpleAbstractState.class.getName());
		
		
		conf.abstractState = new SimpleAbstractState<>(
				getDefaultFor(HeapDomain.class),
				new ValueEnvironment<>(new Interval()),
				new TypeEnvironment<>(new InferredTypes()));
		conf.serializeResults = true;
		conf.jsonOutput = true;

		perform("analysis/tuple", "tuple.rs", conf);
	}
}
