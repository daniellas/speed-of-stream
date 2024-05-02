package tech.dl.sos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

public class RunBenchmarksTest {

	@Test
	public void shouldSelectIncludedOnly() throws ParseException {
		CommandLine cmd = RunBenchmarks.parseCommandLineArgs(new String[]{
				"-i",
				"IntegerSumBenchmark",
				"-i",
				"DoubleCalculationBenchmark"});
		List<Class<?>> selected = RunBenchmarks.selectBenchmarks(cmd).stream()
				.map(Object::getClass)
				.collect(Collectors.toList());

		assertThat(selected).containsExactly(IntegerSumBenchmark.class, DoubleCalculationBenchmark.class);
	}

	@Test
	public void shouldSelectAllExceptExcluded() throws ParseException {
		CommandLine cmd = RunBenchmarks.parseCommandLineArgs(new String[]{
				"-e",
				"IntegerSumBenchmark"});
		List<Class<?>> selected = RunBenchmarks.selectBenchmarks(cmd).stream()
				.map(Object::getClass)
				.collect(Collectors.toList());
		List<Class<?>> expected = RunBenchmarks.BENCHMARKS.stream()
				.map(BenchmarkBase::getClass)
				.collect(Collectors.toList());
		
		expected.remove(IntegerSumBenchmark.class);
		
		assertThat(selected).containsExactlyElementsOf(expected);
	}
}
