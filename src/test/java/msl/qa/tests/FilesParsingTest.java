package msl.qa.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import msl.qa.model.Glossary;
import msl.qa.model.GlossaryInner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class FilesParsingTest {

  private ClassLoader cl = FilesParsingTest.class.getClassLoader();
  private static final Gson gson = new Gson();

  @Test
  void pdfFileParsingTest() throws IOException {
    open("https://docs.junit.org/6.0.3/overview.html");
    File downloaded = $("[href='_exports/junit-user-guide-6.0.3.pdf#overview']").download();
    PDF pdf = new PDF(downloaded);
    boolean is = pdf.text.contains("What is JUnit?");
    System.out.println("### " + is + "  " + pdf.numberOfPages);
    Assertions.assertTrue(is);
  }

  @Test
  void xlsFileParsingTest() {
    open("https://excelvba.ru/programmes/Teachers?ysclid=lfcu77j9j9951587711");
    File downloaded = $("[href='https://ExcelVBA.ru/sites/default/files/teachers.xls']").download();
    XLS xls = new XLS(downloaded);
    String rawResult = xls.excel.getSheetAt(2).getRow(4).getCell(1).getStringCellValue();
    String result = new String(rawResult.getBytes(StandardCharsets.UTF_8));
    System.out.println("### " + result);
    Assertions.assertEquals("Белый Владимир Михайлович", result);
  }

  @Test
  void csvWithClassLoaderFileParsingTest() throws Exception {
    try (InputStream is = cl.getResourceAsStream("csv/example.csv");
         CSVReader reader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
      List<String[]> data = reader.readAll();
      System.out.println(Arrays.toString(data.get(0)));
      System.out.println(Arrays.toString(data.get(1)));
      Assertions.assertEquals(2, data.size());
      Assertions.assertArrayEquals(new String[]{"Selenide", "https://selenide.org"}, data.get(0));
      Assertions.assertArrayEquals(new String[]{"JUnit 5", "https://junit.org"}, data.get(1));
    }
  }

  @Test
  void csvWithoutClassLoaderFileParsingTest() throws Exception {
    try (InputStream is = new FileInputStream(("src/test/resources/csv/example.csv"));
         BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      List<String> data = new ArrayList<>();
      while (br.ready()) {
        data.add(br.readLine());
      }
      System.out.println(data);
      Assertions.assertEquals(2, data.size());
      Assertions.assertEquals("Selenide,https://selenide.org", data.get(0));
      Assertions.assertEquals("JUnit 5,https://junit.org", data.get(1));
    }
  }

  @Test
  void zipFileParsingTest() throws IOException {
    try (InputStream is = cl.getResourceAsStream("zip/sample.zip");
         ZipInputStream zis = new ZipInputStream(is)) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null){
        System.out.println(entry.getName());
      }
    }
  }

  @Test
  void jsonFileParsingTest() throws IOException {
    try (InputStream is = cl.getResourceAsStream("json/glossary.json");
         Reader reader = new InputStreamReader(is)) {
      Glossary actual = gson.fromJson(reader, Glossary.class);
      Assertions.assertEquals("example glossary", actual.title());
      Assertions.assertEquals(234234, actual.id());
      Assertions.assertEquals("example glossary", actual.title());
      Assertions.assertEquals("SGML", actual.glossary().sortAs());
      Assertions.assertEquals("Standard Generalized Markup Language", actual.glossary().glossTerm());
      Assertions.assertEquals("SGML", actual.glossary().acronym());
    }
  }

}
