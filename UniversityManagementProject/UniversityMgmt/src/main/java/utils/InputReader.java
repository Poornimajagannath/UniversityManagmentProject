package utils;

import utils.parsers.ParserEnum;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InputReader {

	public static <T> List<T> read(ParserEnum parserType, String filPath) {
		Function<String, T> mapper = s -> parserType.parse(s);
		List<T> result = null;
		try {
				result = Files.lines(Paths.get(filPath)).map(mapper).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

}
