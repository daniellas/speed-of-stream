package tech.dl.sos;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openjdk.jmh.runner.RunnerException;

import lombok.extern.slf4j.Slf4j;
import tech.dl.sos.parallel.ParallelDoubleCalculationBenchmark;
import tech.dl.sos.parallel.ParallelFilterSortDistinctBenchmark;
import tech.dl.sos.parallel.ParallelGroupBenchmark;
import tech.dl.sos.parallel.ParallelGroupConcurrentBenchmark;
import tech.dl.sos.parallel.SequentialDoubleCalculationBenchmark;
import tech.dl.sos.parallel.SequentialFilterSortDistinctBenchmark;
import tech.dl.sos.parallel.SequentialGroupBenchmark;

@Slf4j
public class RunBenchmarks {

	private static final Option PROFILE_OPT = Option.builder("p").hasArg().required(false).build();
	private static final Option INCLUDE_OPT = Option.builder("i").hasArgs().build();
	private static final Option EXCLUDE_OPT = Option.builder("e").hasArgs().build();

	private static final Options OPTIONS = new Options();

	static List<BenchmarkBase> BENCHMARKS = Arrays.asList(
			new IntegerSumBenchmark(),
			new DoubleCalculationBenchmark(),
			new GroupBenchmark(),
			new FilterSortDistinctBenchmark(),
			new SequentialDoubleCalculationBenchmark(),
			new ParallelDoubleCalculationBenchmark(),
			new SequentialGroupBenchmark(),
			new ParallelGroupBenchmark(),
			new ParallelGroupConcurrentBenchmark(),
			new SequentialFilterSortDistinctBenchmark(),
			new ParallelFilterSortDistinctBenchmark());

	static {
		OPTIONS.addOption(PROFILE_OPT)
				.addOption(EXCLUDE_OPT)
				.addOption(INCLUDE_OPT);

	}
	public static void main(String[] args) throws ParseException {
		try {
			CommandLine cmd = parseCommandLineArgs(args);
			String profile = profile(cmd);

			selectBenchmarks(cmd)
					.forEach(benchmark -> {
						try {
							benchmark.runBenchmark(profile);
						} catch (RunnerException be) {
							throw new RuntimeException(be);
						}
					});
		} catch (Exception e) {
			log.error("Benchmark run failed", e);
		}
	}

	static CommandLine parseCommandLineArgs(String[] args) throws ParseException {
		return new DefaultParser().parse(OPTIONS, args);
	}

	static List<BenchmarkBase> selectBenchmarks(CommandLine cmd) {
		return BENCHMARKS.stream()
				.filter(benchmark -> isIncluded(cmd, benchmark))
				.collect(Collectors.toList());
	}

	private static boolean isIncluded(CommandLine cmd, BenchmarkBase benchmark) {
		String benchmarkName = benchmark.getClass().getSimpleName();
		Set<String> exclusions = Optional.ofNullable(cmd.getOptionValues(EXCLUDE_OPT))
				.map(Arrays::stream)
				.orElse(Stream.empty())
				.collect(Collectors.toSet());
		Set<String> inclusions = Optional.ofNullable(cmd.getOptionValues(INCLUDE_OPT))
				.map(Arrays::stream)
				.orElse(Stream.empty())
				.collect(Collectors.toSet());

		if (exclusions.contains(benchmarkName)) {
			return false;
		}
		if (!inclusions.isEmpty()) {
			return inclusions.contains(benchmarkName);
		}

		return true;
	}

	private static String profile(CommandLine cmd) {
		return Optional.ofNullable(cmd.getOptionValue(PROFILE_OPT)).orElse("default");
	}

}
