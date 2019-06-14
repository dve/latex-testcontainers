package net.vergien.ltc;

import java.nio.file.Path;
import org.testcontainers.containers.GenericContainer;

public abstract class AbstractLatexContainer<SELF extends AbstractLatexContainer<SELF>>
    extends GenericContainer<SELF> {

  protected final Path workDir;

  public AbstractLatexContainer(Path workDir) {
    super();
    this.workDir = workDir;
    setDockerImageName("blang/latex:ctanbasic");
    withFileSystemBind(workDir.toAbsolutePath().toString(), "/data");
  }

  public abstract Path pdflatex(String fileName);
}
