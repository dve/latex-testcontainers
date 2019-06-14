package net.vergien.ltc;

import java.nio.file.Path;
import java.time.Duration;

public class LatexContainer extends AbstractLatexContainer<LatexContainer> {

  public LatexContainer(Path workDir) {
    super(workDir);
  }

  public LatexContainer withTimeout(Duration timeout) {
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
