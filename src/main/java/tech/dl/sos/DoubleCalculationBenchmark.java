package tech.dl.sos;

import static tech.dl.sos.Operations.calculate;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import tech.dl.sos.run.BenchmarkBase;

public class DoubleCalculationBenchmark extends BenchmarkBase {
	// Benchmark parameters
	@State(Scope.Benchmark)
	public static class Params {
		@Param({"1000", "10000", "100000", "1000000"})
		public int size;

		public List<Double> items;
		public Double[] itemsArray;

		// We generate pseudo random doubles
		@Setup
		public void setUp() {
			Random random = new Random();
			items = random.doubles(size).mapToObj(i -> i)
					.collect(Collectors.toList());
			itemsArray = itemsAsArray();
		}
		public Double[] itemsAsArray() {
			return items.toArray(Double[]::new);
		}
	}
	// Counting loop implementation over array
	@Benchmark
	public Double forCountLoop(Params params) {
		Double res = 0d;

		for (int i = 0; i < params.size; i++) {
			res += calculate(params.itemsArray[i]);
		}

		return res;
	}
	// Counting loop implementation over array with conversion
	@Benchmark
	public Double forCountLoopWithConversion(Params params) {
		Double[] itemsArray = params.itemsAsArray();
		Double res = 0d;

		for (int i = 0; i < params.size; i++) {
			res += calculate(itemsArray[i]);
		}

		return res;
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
				.map(Operations::calculate)
				.collect(Collectors.summingDouble(i -> i));
	}
	// Using collect with summing collector on parallel stream
	@Benchmark
	public Double collectPar(Params params) {
		return params.items.parallelStream()
				.map(Operations::calculate)
				.collect(Collectors.summingDouble(i -> i));
	}

	// Using reduce
	@Benchmark
	public Double reduce(Params params) {
		return params.items.stream()
				.map(Operations::calculate)
				.reduce(0d, Double::sum);
	}
	// Using reduce on parallel stream
	@Benchmark
	public Double reducePar(Params params) {
		return params.items.parallelStream()
				.map(Operations::calculate)
				.reduce(0d, Double::sum);
	}

	@Override
	protected String reportFile() {
		return "benchmark-streams-sum-double-calculation.json";
	}

}
