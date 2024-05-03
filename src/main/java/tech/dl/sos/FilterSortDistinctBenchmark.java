package tech.dl.sos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import tech.dl.sos.run.BenchmarkBase;

public class FilterSortDistinctBenchmark extends BenchmarkBase {

	// Minimal value used in filter
	static final double MIN = 0.0;
	// Maximal value used in filter
	static final double MAX = 10.0;

	// Benchmark parameters
	@State(Scope.Benchmark)
	public static class Params {
		@Param({"1000", "10000", "100000", "1000000"})
		public int size;
		public List<Double> items;
		public Double[] itemsArray;
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
			itemsArray = itemsAsArray();
		}
		public Double[] itemsAsArray() {
			return items.toArray(Double[]::new);
		}
	}

	// Counting loop implementation over array
	@Benchmark
	public Collection<Double> forCountLoop(Params params) {
		Set<Double> set = new HashSet<>();
		for (int i = 0; i < params.size; i++) {
			Double item = params.itemsArray[i];
			if (item > MIN && item < MAX) {
				set.add(item);
			}
		}
		List<Double> res = new ArrayList<>(set);
		Collections.sort(res);
		return res;
	}
	// Counting loop implementation over array with conversion
	@Benchmark
	public Collection<Double> forCountLoopWithConversion(Params params) {
		Double[] itemsArray = params.itemsAsArray();
		Set<Double> set = new HashSet<>();
		for (int i = 0; i < params.size; i++) {
			Double item = itemsArray[i];
			if (item > MIN && item < MAX) {
				set.add(item);
			}
		}
		List<Double> res = new ArrayList<>(set);
		Collections.sort(res);
		return res;
	}
	// Using forEach
	@Benchmark
	public Collection<Double> forEach(Params params) {
		Set<Double> set = new HashSet<>();
		for (Double item : params.items) {
			if (item > MIN && item < MAX) {
				set.add(item);
			}
		}
		List<Double> res = new ArrayList<>(set);
		Collections.sort(res);
		return res;
	}
	// Using forEach and TreeSet
	@Benchmark
	public Collection<Double> forEachTreeSet(Params params) {
		Set<Double> set = new TreeSet<>();
		for (Double item : params.items) {
			if (item > MIN && item < MAX) {
				set.add(item);
			}
		}
		return set;
	}
	// Using collect
	@Benchmark
	public Collection<Double> collect(Params params) {
		return params.items.stream()
				.filter(n -> n > MIN && n < MAX)
				.distinct()
				.sorted()
				.collect(Collectors.toList());
	}
	// Using collect on parallel stream
	@Benchmark
	public Collection<Double> collectPar(Params params) {
		return params.items.parallelStream()
				.filter(n -> n > MIN && n < MAX)
				.distinct()
				.sorted()
				.collect(Collectors.toList());
	}

	@Override
	protected String reportFile() {
		return "benchmark-streams-filter-sort-distinct.json";
	}

}
