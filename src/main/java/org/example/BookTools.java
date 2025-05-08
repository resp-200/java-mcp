package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BookTools {
    private final List<Book> bookList = new ArrayList<Book>();


    public BookTools() {
        var one = new Book("java怎么学", "https://www.baidu.com", 2025);
        var two = new Book("程序员怎么养生", "https://www.baidu.com", 2024);
        var three = new Book("ai的发展", "https://www.baidu.com", 2001);
        var four = new Book("时间简史", "https://www.baidu.com", 2008);
        this.bookList.addAll(List.of(one, two, three, four));
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public List<Book> getBookListByYear(int year) {
        return bookList.stream().filter(p -> p.year() == year).collect(Collectors.toList());
    }

    public List<Map<String, List<Object>>> getBookListAsMapList() {
        return bookList.stream().map(p -> {
            return Map.<String, List<Object>>of(
                    "title", List.of(p.title()),
                    "url", List.of(p.url()),
                    "year", List.of(p.year())
            );
        }).collect(Collectors.toList());
    }
}
