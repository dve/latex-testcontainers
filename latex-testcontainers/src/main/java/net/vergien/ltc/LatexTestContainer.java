package net.vergien.ltc;

import java.nio.file.Path;
import java.time.Duration;

public class LatexTestContainer extends AbstractLatexContainer<LatexTestContainer> {

  public LatexTestContainer(Path workDir) {
    super(workDir);
  }

  public LatexTestContainer withTimeout(Duration timeout) {
    // Only "startup" command is running
    return super.withStartupTimeout(timeout);
  }
@Override
  public Path pdflatex(String fileName) {
    withCommand("pdflatex " + fileName);
    start();
    return workDir;
  }

}
