package msl.qa.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SelenideFilesTest {

  @Test
  void downloadFileTest() throws IOException {
    open("https://github.com/junit-team/junit-framework/blob/main/README.md");
    File downloaded = $("[href*='raw/refs/heads/main/README.md']").download();

    try(InputStream is = new FileInputStream(downloaded))  {
      byte[] data = is.readAllBytes();
      String dataAsString = new String(data, StandardCharsets.UTF_8);
      Assertions.assertTrue(dataAsString
              .contains("Contributions to JUnit are both welcomed and appreciated"));
    }
  }

  @Test
  void uploadFileTest() throws IOException {
    open("https://the-internet.herokuapp.com/upload");
    $("#file-upload").uploadFromClasspath("img/1.png");//"input[type='file']"
    $("#file-submit").click();
    $("#uploaded-files").shouldHave(text("1.png"));
  }
}
