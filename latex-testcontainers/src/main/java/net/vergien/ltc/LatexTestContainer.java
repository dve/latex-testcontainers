package net.vergien.ltc;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import org.testcontainers.containers.GenericContainer;

public class LatexTestContainer extends GenericContainer<LatexTestContainer> {
  private final String fileName;
  private final Path workDir;
  private int pollIntervall = 50;

  public LatexTestContainer(Path workDir, String fileName) {
    this.fileName = fileName;
    this.workDir = workDir;
    setDockerImageName("blang/latex:ctanbasic");
    withFileSystemBind(workDir.toAbsolutePath().toString(), "/data");
    withCommand("pdflatex " + fileName);
  }

  public CompletableFuture<Path> buildPDF() {

    return CompletableFuture.supplyAsync(() -> {
      try {
        start();
        while (isRunning()) {
          try {
            Thread.sleep(pollIntervall);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      } catch (Exception e) {
        logger().warn("Failure running latex", e);
      }
      return workDir;
    });

  }

}
