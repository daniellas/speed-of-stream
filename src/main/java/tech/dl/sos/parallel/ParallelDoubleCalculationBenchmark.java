package tech.dl.sos.parallel;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

import tech.dl.sos.BenchmarkBase;
import tech.dl.sos.Operations;

public class ParallelDoubleCalculationBenchmark extends BenchmarkBase {


	// Benchmark parameters
	@State(Scope.Benchmark)
	public static class Params {
		@Param({"1000", "100000"})
		public int size;

		public List<Double> items;

		@Setup
		public void setUp() {
			Random random = new Random();

			items = random.doubles(size).mapToObj(i -> i)
					.collect(Collectors.toList());
		}
	}

	// Benchmarks for other threads counts are omitted for brevity
	@Threads(2)
	@Benchmark
	public Double benchmarkA(Params params) {
		return benchmark(params);
	}

	@Threads(4)
	@Benchmark
	public Double benchmarkB(Params params) {
		return benchmark(params);
	}

	@Threads(6)
	@Benchmark
	public Double benchmarkC(Params params) {
		return benchmark(params);
	}

	@Threads(8)
	@Benchmark
	public Double benchmarkD(Params params) {
		return benchmark(params);
	}

	@Threads(10)
	@Benchmark
	public Double benchmarkE(Params params) {
		return benchmark(params);
	}

	@Threads(12)
	@Benchmark
	public Double benchmarkF(Params params) {
		return benchmark(params);
	}

	@Threads(14)
	@Benchmark
	public Double benchmarkG(Params params) {
		return benchmark(params);
	}

	@Threads(16)
	@Benchmark
	public Double benchmarkH(Params params) {
		return benchmark(params);
	}

	private Double benchmark(Params params) {
		return params.items.parallelStream()
				.map(Operations::calculate)
				.reduce(0d, Double::sum);
	}

	@Override
	protected String reportFile() {
		return "benchmark-threads-streams-sum-double-calculation-parallel.json";
	}

}
