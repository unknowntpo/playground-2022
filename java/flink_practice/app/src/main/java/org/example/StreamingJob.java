package org.example;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Imports for FileSource
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>
 * For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the
 * <a href="https://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>
 * To package your application into a JAR file for execution, run
 * 'gradle shadowJar' on the command line.
 */
public class StreamingJob {
	private static final Logger LOG = LoggerFactory.getLogger(StreamingJob.class);

	public static void main(String[] args) throws Exception {
		// Set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// Define the data directory
		String inputPath = "/opt/flink/data/input.txt";
		if (args.length > 0) {
			inputPath = args[0];
		}

		LOG.info("Reading from file: {}", inputPath);
		System.out.println("Reading from file: " + inputPath);

		// Create a FileSource using TextLineInputFormat instead of readTextFile
		FileSource<String> source = FileSource
				.forRecordStreamFormat(new TextLineInputFormat(), new Path(inputPath))
				.build();

		// Create a DataStream from the FileSource
		DataStream<String> text = env.fromSource(
				source,
				org.apache.flink.api.common.eventtime.WatermarkStrategy.noWatermarks(),
				"TextFileSource");

		// Parse the data and count the words without windowing
		DataStream<Tuple2<String, Integer>> wordCounts = text
				.flatMap(new Tokenizer())
				.keyBy(value -> value.f0)
				.sum(1);

		// Use a separate transform to log the results
		wordCounts.map(new LoggingMapper()).print();

		// Execute program
		env.execute("Flink File Streaming Word Count Example");
	}

	/**
	 * Implements the string tokenizer that splits sentences into words as a
	 * FlatMapFunction.
	 * The output is a tuple of (word, 1) for each word occurrence.
	 */
	public static class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {
		private static final Logger LOG = LoggerFactory.getLogger(Tokenizer.class);

		@Override
		public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
			// Log the received line
			LOG.info("Processing input: {}", value);

			// Normalize and split the line
			String[] words = value.toLowerCase().split("\\W+");

			// Emit the pairs of word and count
			for (String word : words) {
				if (word.length() > 0) {
					LOG.info("Emitting word: {}", word);
					out.collect(new Tuple2<>(word, 1));
				}
			}
		}
	}

	/**
	 * A MapFunction that logs each word count
	 */
	public static class LoggingMapper implements MapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>> {
		private static final Logger LOG = LoggerFactory.getLogger(LoggingMapper.class);

		@Override
		public Tuple2<String, Integer> map(Tuple2<String, Integer> value) {
			LOG.info("WORD COUNT: {} = {}", value.f0, value.f1);
			// Log to standard output as well for debugging
			System.out.println("WORD COUNT: " + value.f0 + " = " + value.f1);
			return value;
		}
	}
}