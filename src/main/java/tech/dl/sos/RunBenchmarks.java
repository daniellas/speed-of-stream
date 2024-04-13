package tech.dl.sos;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openjdk.jmh.runner.RunnerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunBenchmarks {

	public static void main(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption(Option.builder("e").hasArgs().build());
		options.addOption(Option.builder("p").hasArg().required(false).build());

		DefaultParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);
			String profile = profile(cmd);

			if (!isExcluded(cmd, "IntegerSum")) {
				new IntegerSumBenchmark().runBenchmark(profile);
			}
			if (!isExcluded(cmd, "DoubleCalculation")) {
				new DoubleCalculationBenchmark().runBenchmark(profile);
			}
			if (!isExcluded(cmd, "Group")) {
				new GroupBenchmark().runBenchmark(profile);
			}
			if (!isExcluded(cmd, "FilterSortDistinct")) {
				new FilterSortDistinctBenchmark().runBenchmark(profile);
			}
		} catch (RunnerException e) {
			log.error("Benchmark run failed", e);
		}
	}

	private static boolean isExcluded(CommandLine cmd, String benchmark) {
		return Optional.ofNullable(cmd.getOptionValues("e"))
				.map(Arrays::stream)
				.orElse(Stream.empty())
				.anyMatch(benchmark::equals);
	}

	private static String profile(CommandLine cmd) {
		return Optional.ofNullable(cmd.getOptionValue("p")).orElse("default");
	}

}
