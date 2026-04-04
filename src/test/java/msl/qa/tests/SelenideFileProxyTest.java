package msl.qa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SelenideFileProxyTest {

  @BeforeEach
  public void setUp() {
    Configuration.fileDownload = FileDownloadMode.PROXY;
    Configuration.proxyEnabled = true;
  }

  @Test
  void downloadFileProxyTest() throws InterruptedException, IOException {
    open("https://github.com/junit-team/junit-framework/blob/main/README.md");
    $("svg.octicon.octicon-download").click();
    Thread.sleep(4000);

    File downloadsDir = new File("build/downloads");
    File[] sessionDirs = downloadsDir.listFiles();
    File sessionDir = sessionDirs[0];
    System.out.println(Arrays.toString(sessionDirs));
    File[] filesInSessionDir = sessionDir.listFiles();
    System.out.println(Arrays.toString(filesInSessionDir));

    File downloadedFile = Arrays.stream(filesInSessionDir)
            .filter(file -> file.getName().equals("README.md"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("File README.md not found"));

    String dataFile = FileUtils.readFileToString(downloadedFile, StandardCharsets.UTF_8);
    Assertions.assertTrue(dataFile.contains("General Availability"));

    boolean fileDeleted = downloadedFile.delete();
    System.out.println(fileDeleted);
  }
}
