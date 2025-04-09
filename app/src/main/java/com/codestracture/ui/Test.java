package com.codestracture.ui;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Test {

    public void checkDirectory(Context context, String fileName, String fileType) {
        if (fileType == "EPUB") {
            File file = new File(context.getCacheDir().getAbsoluteFile() + "/" + fileName + "/EPUB");
            if (file.isDirectory()) {
                List<File> listOfFile = Arrays.asList(Objects.requireNonNull(file.listFiles()));
                List<File> xhtmlFileList = listOfFile.stream().filter(file1 -> file1.getAbsolutePath().contains(".xhtml")).collect(Collectors.toList());
                List<File> cssFileList = listOfFile.stream().filter(file1 -> file1.getAbsolutePath().contains(".css")).collect(Collectors.toList());

                File newHtml = new File(context.getCacheDir().getAbsoluteFile() + "/" + fileName + "/EPUB/book.html");
                if (!newHtml.exists()) {
                    try {
                        newHtml.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                String cssLink = "";

                for (int i = 0; i < cssFileList.size(); i++) {
                    cssLink += "<link rel=\"stylesheet\" type=\"text/css\" href=\"file://" + cssFileList.get(i).getAbsolutePath() + "\"> \n";
                }

                String bookStart = "<html>\n" +
                        "<head>\n" + cssLink +
                        "    <meta name=\"viewport\" content=\"height=device-height, user-scalable=no\"/>\n" +
                        "    <style>\n" +
                        "        * {\n" +
                        "            font-size: 1.0em;\n" +
                        "        }\n" +
                        "        .bookchapter {\n" +
                        "            margin-top: 50px;\n" +
                        "            margin-bottom: 50px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body dir=\"rtl\" id=\"content\">";

                StringBuilder chapters = new StringBuilder();
                String regex = ".*\\d+.*";
                int chapterNumber = 1;
                for (int i = 0; i < xhtmlFileList.size(); i++) {
                    File xhtml = xhtmlFileList.get(i);
                    if (xhtml.getAbsolutePath().contains("cover.xhtml")) {
                        chapters.append(getBodyContentFromFile(xhtml, "cover")).append("\n");
                    } else if (xhtml.getAbsolutePath().matches(regex)) {
                        if (!xhtml.getAbsolutePath().contains("toc.xhtml")) {
                            chapters.append(getBodyContentFromFile(xhtml, "Chapter-" + chapterNumber)).append("\n");
                            chapterNumber++;
                        }
                    } else if (xhtml.getAbsolutePath().contains("addindiebook.xhtml")) {
                        chapters.append(getBodyContentFromFile(xhtml, "addindiebook")).append("\n");
                    }
                }

                String bookEnd = "</body>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/jsface.min.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/jquery-3.4.1.min.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/rangy-core.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/rangy-highlighter.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/rangy-classapplier.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/rangy-serializer.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/Bridge.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/rangefix.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/readium-cfi.umd.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"file:///android_asset/js/Bridge-after.js\"></script>\n" +
                        "</html>";

                try {
                    String fileContent = bookStart + "\n" + chapters.toString() + "\n" + bookEnd;
                    FileWriter fw = new FileWriter(newHtml);
                    fw.write(fileContent);
                    fw.close();
                } catch (Exception e) {
                    Log.d("MyTag", "Exception::" + e.getMessage());
                }
            }
        } else if (fileType == "OEBPS") {
            File file = new File(context.getCacheDir().getAbsoluteFile() + "/" + fileName + "/OEBPS/Text");
            if (file.isDirectory()) {
                File newHtml = new File(context.getCacheDir().getAbsoluteFile() + "/" + fileName + "/OEBPS/Text/book.html");
                if (!newHtml.exists()) {
                    try {
                        newHtml.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {

        }
    }

    private String getBodyContentFromFile(File xhtml, String chapterName) {
        Pattern pattern = Pattern.compile("<body[^>]*>(.*?)</body>", Pattern.DOTALL);
        String fileContent = readFile(xhtml.getAbsolutePath());
        Matcher matcher = pattern.matcher(fileContent);
        String onlyBody = "";
        if (matcher.find()) {
            onlyBody = matcher.group(1);
        }
        return "<div class=\"cover bookchapter\" id='" + chapterName + "'>" + onlyBody + "</div>\n";
    }

    private String readFile(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
