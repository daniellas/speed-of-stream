package tech.dl.sos.parallel;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

import tech.dl.sos.run.BenchmarkBase;

public class SequentialFilterSortDistinctBenchmark extends BenchmarkBase {

	// Minimal value used in filter
	static final Double MIN = 0.0;
	// Maximal value used in filter
	static final Double MAX = 10.0;

	public Collection<Double> operation(Params params) {
		return params.items.stream()
				.filter(n -> n > MIN && n < MAX)
				.distinct()
				.sorted()
				.collect(Collectors.toList());
	}

	@Threads(2)
	@Benchmark
	public Collection<Double> benchmarkA(Params params) {
		return operation(params);
	}

	@Threads(4)
	@Benchmark
	public Collection<Double> benchmarkB(Params params) {
		return operation(params);
	}

	@Threads(6)
	@Benchmark
	public Collection<Double> benchmarkC(Params params) {
		return operation(params);
	}

	@Threads(8)
	@Benchmark
	public Collection<Double> benchmarkD(Params params) {
		return operation(params);
	}

	@Threads(10)
	@Benchmark
	public Collection<Double> benchmarkE(Params params) {
		return operation(params);
	}

	@Threads(12)
	@Benchmark
	public Collection<Double> benchmarkF(Params params) {
		return operation(params);
	}

	@Threads(14)
	@Benchmark
	public Collection<Double> benchmarkG(Params params) {
		return operation(params);
	}

	@Threads(16)
	@Benchmark
	public Collection<Double> benchmarkH(Params params) {
		return operation(params);
	}

	// Benchmark parameters
	@State(Scope.Benchmark)
	public static class Params {
		@Param({ "1000", "100000" })
		public int size;

		public List<Double> items;

		@Setup
		public void setUp() {
			Random random = new Random();
			// Generate 500 random values from range MIN >= value < MAX + 5
			List<Double> values = random.doubles(500, MIN, MAX + 5)
					.mapToObj(i -> i)
					.collect(Collectors.toList());
			// Create list from random numbers within range
			items = random.ints(size, 0, values.size())
					.mapToObj(values::get)
					.collect(Collectors.toList());
		}
	}

	@Override
	protected String reportFile() {
		return "benchmark-threads-streams-filter-sort-distinct-sequential.json";
	}

}
