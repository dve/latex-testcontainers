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
import java.nio.file.Files;
import java.nio.file.Path;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.vaadin.alejandro.PdfBrowserViewer;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Composite;
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
import net.vergien.ltc.LatexTestContainer;


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
      try {
        Path workDir = Files.createTempDirectory("pdfCration");
        String fileName = "helloWorld.tex";
        String latexSource = "\\documentclass[12pt]{article}\n" + "\\begin{document}\n" + "Hello "
            + address.toString() + "!\n" + "$Hello world!$ %math mode \n" + "\\end{document}";
        Path latexSourcePath = workDir.resolve(fileName);
        Files.write(latexSourcePath, latexSource.getBytes());
        try (LatexTestContainer ltc = new LatexTestContainer(workDir, fileName)) {
          Notification.show(ltc.buildPDF().get().toString());
          Path output = workDir.resolve("helloWorld.pdf");
          if (Files.exists(output)) {
            FileInputStream fileInputStream = new FileInputStream(output.toFile());
            StreamResource sr = new StreamResource("helloWorld.pdf", () -> fileInputStream);

            PdfBrowserViewer pdfBrowserViewer = new PdfBrowserViewer(sr);
            layout.add(pdfBrowserViewer);
          }
        }
      } catch (Exception e) {
      }

    }
  }
}
