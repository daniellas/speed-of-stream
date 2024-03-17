package tech.dl.sos;

import java.util.Arrays;

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

		options.addOption(Option.builder("e").hasArg(false).hasArgs().build());

		DefaultParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			if (!isExcluded(cmd, "IntegerSum")) {
				new IntegerSumBenchmark().runBenchmark();
			}
			if (!isExcluded(cmd, "DoubleCalculation")) {
				new DoubleCalculationBenchmark().runBenchmark();
			}
			if (!isExcluded(cmd, "Group")) {
				new GroupBenchmark().runBenchmark();
			}
			if (!isExcluded(cmd, "FilterSortDistinct")) {
				new FilterSortDistinctBenchmark().runBenchmark();
			}
		} catch (RunnerException e) {
			log.error("Benchmark run failed", e);
		}
	}

	private static boolean isExcluded(CommandLine cmd, String benchmark) {
		return Arrays.stream(cmd.getOptionValues("e")).anyMatch(benchmark::equals);
	}

}
