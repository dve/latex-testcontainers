package net.vergien.ltc;

import java.io.IOException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy;

public class LatexContainerBG extends AbstractLatexContainer<LatexContainerBG> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LatexContainerBG.class);

  public LatexContainerBG(Path workDir) {
    super(workDir);
    withCommand(new String[] {"/bin/sh", "-c", "/bin/sleep infinity"});
    withStartupCheckStrategy(new IsRunningStartupCheckStrategy());
  }

  @Override
  public Path pdflatex(String fileName) {
    start();
    try {
      Container.ExecResult result = execInContainer("pdflatex", fileName);
      LOGGER.info("result {}", result);
    } catch (UnsupportedOperationException | IOException | InterruptedException e) {
      logger().error("Failure executing pdflatex", e);
    }
    return workDir;
  }
}
