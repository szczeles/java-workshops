package pl.jeppesen.workshops.flights;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by mariusz.strzelecki on 01.11.16.
 */
public class StreamExamples {
    public static void main(String[] args) throws IOException {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> twoEvenSquares =
                numbers.stream()
                        .filter(n -> {
                            System.out.println("filtering " + n);
                            return n % 2 == 0;
                        })
                        .map(n -> {
                            System.out.println("mapping " + n);
                            return n * n;
                        })
                        .limit(2)
                        .collect(toList());
        System.out.println(twoEvenSquares);

        boolean small =
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8).stream()
                        .allMatch(t -> t < 10);
        System.out.println(small);

        IntStream.rangeClosed(10, 30)
                        .filter(n -> n % 2 == 1).forEach(System.out::println);

        System.out.println(Files.lines(Paths.get("data/flights.csv"), Charset.defaultCharset())
                .count());
    }
}
