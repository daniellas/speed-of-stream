package tech.dl.sos;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class DoubleCalculationBenchmark extends BenchmarkBase {
	// Benchmark parameters
	@State(Scope.Benchmark)
	public static class Params {
		@Param({"1000", "10000", "100000", "1000000"})
		public int size;

		public List<Double> items;

		// We generate pseudo random doubles
		@Setup
		public void setUp() {
			Random random = new Random();

			items = random.doubles(size).mapToObj(i -> i).collect(Collectors.toList());
		}
	}

	// This is our calculation, takes Double type number, calculates logarithm,
	// then sine and then square root
	private static double calculate(double value) {
		return Math.sqrt(Math.sin(Math.log(value)));
	}

	// Using forEach
	@Benchmark
	public Double forEach(Params params) {
		Double res = 0d;

		for (Double item : params.items) {
			res += calculate(item);
		}

		return res;
	}
	// Using collect with summing collector
	@Benchmark
	public Double collect(Params params) {
		return params.items.stream()
				.map(DoubleCalculationBenchmark::calculate)
				.collect(Collectors.summingDouble(i -> i));
	}
	// Using collect with summing collector on parallel stream
	@Benchmark
	public Double collectPar(Params params) {
		return params.items.parallelStream()
				.map(DoubleCalculationBenchmark::calculate)
				.collect(Collectors.summingDouble(i -> i));
	}

	// Using reduce
	@Benchmark
	public Double reduce(Params params) {
		return params.items.stream()
				.map(DoubleCalculationBenchmark::calculate)
				.reduce(0d, Double::sum);
	}
	// Using reduce on parallel stream
	@Benchmark
	public Double reducePar(Params params) {
		return params.items.parallelStream()
				.map(DoubleCalculationBenchmark::calculate)
				.reduce(0d, Double::sum);
	}

	@Override
	protected String reportFile() {
		return "benchmark-streams-sum-double-calculation.json";
	}

}
