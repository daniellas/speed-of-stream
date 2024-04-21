package tech.dl.sos.parallel;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

import tech.dl.sos.BenchmarkBase;

public class ParallelGroupBenchmark extends BenchmarkBase {

	// This is grouping divisor value
	static final double DIVISOR = 100.0;

	// Benchmark parameters
	@State(Scope.Benchmark)
	public static class Params {
		@Param({"1000", "100000"})
		public int size;
		public List<Double> items;
		@Setup
		public void setUp() {
			Random random = new Random();
			items = random.doubles(size)
					.mapToObj(i -> i)
					.collect(Collectors.toList());
		}
	}
	// This is actual benchmarked operation with parallel stream
	private Map<Long, List<Double>> operation(List<Double> items) {
		return items.parallelStream()
				.collect(Collectors.groupingBy(n -> Math.round(n / DIVISOR)));
	}

	@Threads(2)
	@Benchmark
	public Map<Long, List<Double>> benchmarkA(Params params) {
		return operation(params.items);
	}

	@Threads(4)
	@Benchmark
	public Map<Long, List<Double>> benchmarkB(Params params) {
		return operation(params.items);
	}

	@Threads(6)
	@Benchmark
	public Map<Long, List<Double>> benchmarkC(Params params) {
		return operation(params.items);
	}

	@Threads(8)
	@Benchmark
	public Map<Long, List<Double>> benchmarkD(Params params) {
		return operation(params.items);
	}

	@Threads(10)
	@Benchmark
	public Map<Long, List<Double>> benchmarkE(Params params) {
		return operation(params.items);
	}

	@Threads(12)
	@Benchmark
	public Map<Long, List<Double>> benchmarkF(Params params) {
		return operation(params.items);
	}

	@Threads(14)
	@Benchmark
	public Map<Long, List<Double>> benchmarkG(Params params) {
		return operation(params.items);
	}

	@Threads(16)
	@Benchmark
	public Map<Long, List<Double>> benchmarkH(Params params) {
		return operation(params.items);
	}


	@Override
	protected String reportFile() {
		return "benchmark-threads-streams-group-parallel.json";
	}

}
