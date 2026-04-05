package msl.qa.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipParsingTest {
  private final ClassLoader cl = getClass().getClassLoader();

  @Test
  void zipIsNotEmptyParsingTest() throws Exception {
    try (InputStream is = cl.getResourceAsStream("zip/file3.zip");
         ZipInputStream zis = new ZipInputStream(is)) {
      ZipEntry entry;
      int count = 0;
      while ((entry = zis.getNextEntry()) != null) {
        System.out.println(entry.getName());
        count++;
      }
      System.out.println("Zip entry count: " + count);
      Assertions.assertTrue(count > 0, "Zip is empty");
    }
  }

  @Test
  void csvFromZipParsingTest() throws Exception {
    byte[] data = getBytesFromZip(".csv");
    try (CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(data), StandardCharsets.UTF_8))) {
      List<String[]> lines = reader.readAll();
      Assertions.assertEquals(3, lines.size());
      Assertions.assertArrayEquals(new String[]{"Иванов", "Иван"}, lines.get(0));
      Assertions.assertArrayEquals(new String[]{"Петров", "Петр"}, lines.get(1));
      Assertions.assertArrayEquals(new String[]{"Сидоров", "Сидор"}, lines.get(2));
    }
  }

  @Test
  void pdfFromZipParsingTest() throws Exception {
    byte[] data = getBytesFromZip(".pdf");
    PDF pdf = new PDF(data);
    boolean existText = pdf.text.contains("Руководство пользователя");
    Assertions.assertTrue(existText);
  }

  @Test
  void xlsxFromZipParsingTest() throws Exception {
    byte[] data = getBytesFromZip(".xlsx");
    XLS xls = new XLS(data);
    String result = xls.excel.getSheetAt(0).getRow(1).getCell(0).getStringCellValue();
    Assertions.assertEquals("№ Формы ФСН/ОСН", result);
  }

  private byte[] getBytesFromZip(String extensionFile) throws IOException {
    try (InputStream is = cl.getResourceAsStream("zip/file3.zip")) {
      if (is == null) {
        throw new IllegalArgumentException("### File zip/file3.zip not found in classpath");
      }
      try (ZipInputStream zis = new ZipInputStream(is)) {
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
          if (entry.getName().endsWith(extensionFile)) {
            return zis.readAllBytes();
          }
        }
      }
    }
    throw new IllegalArgumentException("### No file in zip archive with such extension" + extensionFile);
  }

}
