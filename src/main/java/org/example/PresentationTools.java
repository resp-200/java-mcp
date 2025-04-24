package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PresentationTools {
    private List<Presentation> presentations = new ArrayList<Presentation>();


    public PresentationTools() {
        var one = new Presentation("java怎么学", "https://hao123.com", 2025);
        var two = new Presentation("程序员怎么养生", "https://hao123.com", 2024);
        var three = new Presentation("ai的发展", "https://hao123.com", 2001);
        var four = new Presentation("时间简史", "https://hao123.com", 2008);
        this.presentations.addAll(List.of(one, two, three, four));
    }

    public List<Presentation> getPresentations() {
        return presentations;
    }

    public List<Presentation> getPresentationByYear(int year) {
        return presentations.stream().filter(p -> p.year() == year).collect(Collectors.toList());
    }

    public List<Map<String, List<Object>>> getPresentationsAsMapList() {
        return presentations.stream().map(p -> {
            return Map.<String, List<Object>>of(
                    "title", List.of(p.title()),
                    "url", List.of(p.url()),
                    "year", List.of(p.year())
            );
        }).collect(Collectors.toList());
    }
}
