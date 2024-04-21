package tech.dl.sos;

import java.util.Arrays;
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
import tech.dl.sos.parallel.SequentialDoubleCalculationBenchmark;

@Slf4j
public class RunBenchmarks {

	private static final Option PROFILE_OPT = Option.builder("p").hasArg().required(false).build();
	private static final Option INCLUDE_OPT = Option.builder("i").hasArgs().build();
	private static final Option EXCLUDE_OPT = Option.builder("e").hasArgs().build();

	public static void main(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption(EXCLUDE_OPT).addOption(INCLUDE_OPT).addOption(PROFILE_OPT);

		DefaultParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);
			String profile = profile(cmd);

			if (!isIncluded(cmd, IntegerSumBenchmark.class)) {
				new IntegerSumBenchmark().runBenchmark(profile);
			}
			if (!isIncluded(cmd, DoubleCalculationBenchmark.class)) {
				new DoubleCalculationBenchmark().runBenchmark(profile);
			}
			if (!isIncluded(cmd, GroupBenchmark.class)) {
				new GroupBenchmark().runBenchmark(profile);
			}
			if (!isIncluded(cmd, FilterSortDistinctBenchmark.class)) {
				new FilterSortDistinctBenchmark().runBenchmark(profile);
			}

			if (!isIncluded(cmd, SequentialDoubleCalculationBenchmark.class)) {
				new SequentialDoubleCalculationBenchmark().runBenchmark(profile);
			}
			if (!isIncluded(cmd, ParallelDoubleCalculationBenchmark.class)) {
				new ParallelDoubleCalculationBenchmark().runBenchmark(profile);
			}
		} catch (RunnerException e) {
			log.error("Benchmark run failed", e);
		}
	}

	private static boolean isIncluded(CommandLine cmd, Class<? extends BenchmarkBase> benchmarkCls) {
		String benchmark = benchmarkCls.getSimpleName().replace("Benchmark", "");
		
		Set<String> excludes = Optional.ofNullable(cmd.getOptionValues(EXCLUDE_OPT))
				.map(Arrays::stream)
				.orElse(Stream.empty())
				.collect(Collectors.toSet());
		Set<String> includes = Optional.ofNullable(cmd.getOptionValues(INCLUDE_OPT))
				.map(Arrays::stream)
				.orElse(Stream.empty())
				.collect(Collectors.toSet());

		return !excludes.contains(benchmark) || (!includes.isEmpty() && includes.contains(benchmark));
	}

	private static String profile(CommandLine cmd) {
		return Optional.ofNullable(cmd.getOptionValue(PROFILE_OPT)).orElse("default");
	}

}
