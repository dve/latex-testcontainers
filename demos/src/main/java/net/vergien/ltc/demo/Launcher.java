/**
 * Copyright © 2017 Sven Ruppert (sven.ruppert@gmail.com) 2018 Daniel Nordhoff-Vergien
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

import org.rapidpm.vaadin.nano.CoreUIService;

/**
 *
 */
public class Launcher {
  private Launcher() {
    // NOPO
  }

  public static void main(String[] args) {
    if (args.length >= 2) {
      System.setProperty(CoreUIService.CORE_UI_SERVER_PORT, args[1]);
    }
    new CoreUIService().startup();
  }
}
