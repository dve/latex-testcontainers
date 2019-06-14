/**
 * Copyright © 2017 Sven Ruppert (sven.ruppert@gmail.com) 2018 Daniel Nordhoff-Vergien
 * dve@vergien.net
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.vergien.ltc.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.vaadin.alejandro.PdfBrowserViewer;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import net.vergien.ltc.LatexContainer;


@Route("")
public class VaadinApp extends Composite<Div> implements HasLogger {
  private final Button createPDFButton =
      new Button("Create PDF", VaadinIcon.FILE_TEXT.create(), this::onCreatePDF);
  private final TextField firstNameField = new TextField("First name");
  private final TextField lastNameField = new TextField("Last name");
  private final FormLayout formLayout = new FormLayout(firstNameField, lastNameField);
  private final VerticalLayout layout = new VerticalLayout(formLayout, createPDFButton);
  private final Binder<Address> binder;

  public VaadinApp() {
    binder = new Binder<>(Address.class);
    binder.forField(firstNameField).asRequired().bind("firstName");
    binder.forField(lastNameField).asRequired().bind("lastName");
    getContent().add(layout);
  }


  public void onCreatePDF(ClickEvent<Button> event) {
    Address address = new Address();

    if (binder.writeBeanIfValid(address)) {
      Notification.show("Create pdf for " + address.getFirstName() + ", " + address.getLastName());
      final UI ui = UI.getCurrent();
      CompletableFuture.runAsync(() -> {
        try {
          Path workDir = Files.createTempDirectory("pdfCration");
          logger().info("Workdir: {}", workDir);
          String fileName = "helloWorld.tex";
          String latexSource = "\\documentclass[12pt]{article}\n" + "\\begin{document}\n" + "Hello "
              + address.toString() + "!\n" + "$Hello world!$ %math mode \n" + "\\end{document}";
          Path latexSourcePath = workDir.resolve(fileName);
          Files.write(latexSourcePath, latexSource.getBytes());
          try (LatexContainer ltc = new LatexContainer(workDir)) {
            ltc.pdflatex(fileName);
            Path output = workDir.resolve("helloWorld.pdf");
            logger().info("output {}", output);
            if (Files.exists(output)) {
              ui.access(() -> {
                showPDF(output);
              });
            }
          }
        } catch (Exception e) {
          logger().severe("Failure creating pdf", e);
        }
      });
    }
  }


  private void showPDF(Path output) {
    try {
      @SuppressWarnings("resource") // stream is closed within vaadin framework
      FileInputStream fileInputStream = new FileInputStream(output.toFile());
      PdfBrowserViewer pdfBrowserViewer =
          new PdfBrowserViewer(new StreamResource("helloWorld.pdf", () -> fileInputStream));
      layout.add(pdfBrowserViewer);
    } catch (FileNotFoundException e) {
      logger().severe("Failure showing pdf", e);
    }
  }
}
