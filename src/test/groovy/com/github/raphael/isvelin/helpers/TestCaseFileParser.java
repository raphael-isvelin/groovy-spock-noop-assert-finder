package com.github.raphael.isvelin.helpers;

import com.github.raphael.isvelin.helpers.TestCase.Template;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCaseFileParser {


    public TestCase parseGroovyFile(String fileName) {
        try {
            URL resource = getClass().getClassLoader().getResource(fileName);
            if (resource == null) {
                throw new FileNotFoundException("Resource file not found: " + fileName);
            }

            String content = new String(Files.readAllBytes(Paths.get(resource.toURI())));

            String name = extractData(content, "#name=(.*)");
            String templateStr = extractData(content, "#template=(.*)");
            String code = extractData(content, "//<code>\\s*(.*?)\\s*//</code>", Pattern.DOTALL);
            String expected = extractData(content, "/\\*<expected>\\s*(.*?)\\s*</expected>\\*/",
                    Pattern.DOTALL);

            Template template = Template.valueOf(templateStr);

            return new TestCase(name, template, code, expected);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't parse test case: " + fileName, e);
        }
    }

    private String extractData(String content, String regex) {
        return extractData(content, regex, Pattern.MULTILINE);
    }

    private String extractData(String content, String regex, int flags) {
        Pattern pattern = Pattern.compile(regex, flags);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
}
