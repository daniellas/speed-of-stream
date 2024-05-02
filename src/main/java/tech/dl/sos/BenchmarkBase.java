package tech.dl.sos;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public abstract class BenchmarkBase {

	protected void runBenchmark(String profile) throws RunnerException {
		String resultsDir = String.format("results/%s", profile);

		new File(resultsDir).mkdirs();

		Options options = new OptionsBuilder()
				// Add class with methods annotated with @Benchmark
				.include(this.getClass().getName())
				// Use only one fork, single benchmark will be executed at once
				.forks(1)
				// Measure number of operations
				.mode(Mode.Throughput)
				// Performed within one second
				.timeUnit(TimeUnit.SECONDS)
				// Run 3 warmup iteration
				.warmupIterations(3)
				// Every 5 seconds long
				.warmupTime(new TimeValue(5, TimeUnit.SECONDS))
				// Then run 10 measurement iterations
				.measurementIterations(10)
				// Every 5 seconds long
				.measurementTime(new TimeValue(5, TimeUnit.SECONDS))
				// Finally write results to file
				.result(String.format("%s/%s", resultsDir, reportFile()))
				// In JSON format
				.resultFormat(ResultFormatType.JSON)
				.build();

		new Runner(options).run();
	}

	protected abstract String reportFile();

}
