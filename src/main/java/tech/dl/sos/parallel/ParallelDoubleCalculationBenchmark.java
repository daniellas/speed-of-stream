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
	// This is actual benchmarked operation running calculation against every
	// item in the list in parallel
	private static Double operation(List<Double> items) {
		return items.parallelStream()
				.map(Operations::calculate)
				.reduce(0d, Double::sum);
	}
	// Benchmarks for other threads counts are omitted for brevity
	@Threads(2)
	@Benchmark
	public Double benchmarkA(Params params) {
		return operation(params.items);
	}

	@Threads(4)
	@Benchmark
	public Double benchmarkB(Params params) {
		return operation(params.items);
	}

	@Threads(6)
	@Benchmark
	public Double benchmarkC(Params params) {
		return operation(params.items);
	}

	@Threads(8)
	@Benchmark
	public Double benchmarkD(Params params) {
		return operation(params.items);
	}

	@Threads(10)
	@Benchmark
	public Double benchmarkE(Params params) {
		return operation(params.items);
	}

	@Threads(12)
	@Benchmark
	public Double benchmarkF(Params params) {
		return operation(params.items);
	}

	@Threads(14)
	@Benchmark
	public Double benchmarkG(Params params) {
		return operation(params.items);
	}

	@Threads(16)
	@Benchmark
	public Double benchmarkH(Params params) {
		return operation(params.items);
	}

	@Override
	protected String reportFile() {
		return "benchmark-threads-streams-sum-double-calculation-parallel.json";
	}

}
