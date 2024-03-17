package tech.dl.sos;

import org.openjdk.jmh.runner.RunnerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunBenchmarks {

	public static void main(String[] args) {
		try {
			new IntegerSumBenchmark().runBenchmark();
			new DoubleCalculationBenchmark().runBenchmark();
			new GroupBenchmark().runBenchmark();
			new FilterSortDistinctBenchmark().runBenchmark();
		} catch (RunnerException e) {
			log.error("Benchmark run failed", e);
		}
	}

}
