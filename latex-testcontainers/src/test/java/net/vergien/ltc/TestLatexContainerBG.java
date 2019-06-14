package net.vergien.ltc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.rapidpm.dependencies.core.logger.HasLogger;

class TestLatexContainerBG implements HasLogger {

  @Test
  void test() throws IOException, InterruptedException, ExecutionException {
    Path workDir = Files.createTempDirectory("latex-textcontainers");

    try (LatexContainerBG ltcUT = new LatexContainerBG(workDir);) {      
      logger().info("Workdir: {}", workDir);
      Files.copy(getClass().getResourceAsStream("/helloworld.tex"),
          workDir.resolve("helloworld.tex"));
      Path resultDir = ltcUT.pdflatex("helloworld.tex");
      assertThat(Files.exists(resultDir.resolve("helloworld.pdf")), is(true));
    }
  }

  @Test
  void test_withError() throws IOException, InterruptedException, ExecutionException {
    Path workDir = Files.createTempDirectory("latex-textcontainers");

    try (LatexContainerBG ltcUT = new LatexContainerBG(workDir);) {
      logger().info("Workdir: {}", workDir);

      Files.copy(getClass().getResourceAsStream("/helloworld_witherrors.tex"),
          workDir.resolve("helloworld.tex"));
      Path resultDir = ltcUT.pdflatex("helloworld.tex");
      assertThat(Files.exists(resultDir.resolve("helloworld.pdf")), is(false));
    }
  }
}
