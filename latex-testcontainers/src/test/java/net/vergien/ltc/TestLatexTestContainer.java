package net.vergien.ltc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.rapidpm.dependencies.core.logger.HasLogger;

class TestLatexTestContainer implements HasLogger {

  @Test
  void test() throws IOException, InterruptedException, ExecutionException {
    Path workDir = Files.createTempDirectory("latex-textcontainers");
    logger().info("Workdir: {}", workDir);
    Files.copy(getClass().getResourceAsStream("/helloworld.tex"),
        workDir.resolve("helloworld.tex"));
    LatexTestContainer ltcUT = new LatexTestContainer(workDir, "helloworld.tex");
    CompletableFuture<Path> result = ltcUT.buildPDF();

    Path resultDir = result.get();
    assertThat(Files.exists(resultDir.resolve("helloworld.pdf")), is(true));
  }

  @Test
  void test_withError() throws IOException, InterruptedException, ExecutionException {
    Path workDir = Files.createTempDirectory("latex-textcontainers");
    logger().info("Workdir: {}", workDir);
    Files.copy(getClass().getResourceAsStream("/helloworld_witherrors.tex"),
        workDir.resolve("helloworld.tex"));
    LatexTestContainer ltcUT = new LatexTestContainer(workDir, "helloworld.tex");
    CompletableFuture<Path> result = ltcUT.buildPDF();

    Path resultDir = result.get();
    assertThat(Files.exists(resultDir.resolve("helloworld.pdf")), is(false));
  }
}
