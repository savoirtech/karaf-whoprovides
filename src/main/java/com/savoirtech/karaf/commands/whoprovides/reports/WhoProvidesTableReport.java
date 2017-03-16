/*
 * Copyright (c) 2017 Savoir Technologies
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.savoirtech.karaf.commands.whoprovides.reports;

import com.savoirtech.karaf.commands.whoprovides.MatchingBundleInfo;
import com.savoirtech.karaf.commands.whoprovides.WhoProvidesReport;

import org.apache.karaf.shell.table.ShellTable;
import org.osgi.framework.Bundle;

/**
 * Created by art on 3/13/17.
 */
public class WhoProvidesTableReport implements WhoProvidesReport {

  private ShellTable shellTable;
  private boolean showExportOnly;

//========================================
// Setters and Getters
//----------------------------------------

  public boolean isShowExportOnly() {
    return showExportOnly;
  }

  public void setShowExportOnly(boolean showExportOnly) {
    this.showExportOnly = showExportOnly;
  }

//========================================
// Reporting Interface
//----------------------------------------

  @Override
  public void prepareReport() {
    this.shellTable = new ShellTable();

    this.shellTable.column("Package");
    this.shellTable.column("ID").alignRight();
    this.shellTable.column("Exports");
    this.shellTable.column("Name");
    this.shellTable.column("Version");
    this.shellTable.column("Location");
  }

  @Override
  public void addToReport(String packageName, MatchingBundleInfo matchingBundleInfo) {
    for (Bundle oneBundle : matchingBundleInfo.exporterBundles) {
      shellTable.addRow().addContent(
          packageName,
          oneBundle.getBundleId(),
          "Y",
          oneBundle.getSymbolicName(),
          oneBundle.getVersion(),
          oneBundle.getLocation()
      );
    }

    if (! this.showExportOnly) {
      for (Bundle oneBundle : matchingBundleInfo.containerBundles) {
        // Don't list this bundle again if it was already listed as an exporter.
        if (!matchingBundleInfo.exporterBundles.contains(oneBundle)) {
          shellTable.addRow().addContent(
              packageName,
              oneBundle.getBundleId(),
              "N",
              oneBundle.getSymbolicName(),
              oneBundle.getVersion(),
              oneBundle.getLocation()
          );
        }
      }
    }
  }

  @Override
  public void finalizeReport() {
    this.shellTable.print(System.out);
  }
}
