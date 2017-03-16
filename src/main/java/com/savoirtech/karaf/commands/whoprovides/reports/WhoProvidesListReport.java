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

import org.osgi.framework.Bundle;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * List the results in a simple one-line-per-match format with '|' delimited fields.
 *
 * Created by art on 3/13/17.
 */
public class WhoProvidesListReport implements WhoProvidesReport {

  private List<String> lines = new LinkedList<>();
  private boolean showExportOnly;
  private PrintStream printStream = System.out;

//========================================
// Setters and Getters
//----------------------------------------

  public boolean isShowExportOnly() {
    return showExportOnly;
  }

  public void setShowExportOnly(boolean showExportOnly) {
    this.showExportOnly = showExportOnly;
  }

  public PrintStream getPrintStream() {
    return printStream;
  }

  public void setPrintStream(PrintStream printStream) {
    this.printStream = printStream;
  }
//========================================
// Reporting Interface
//----------------------------------------

  @Override
  public void prepareReport() {
  }

  @Override
  public void addToReport(String packageName, MatchingBundleInfo matchingBundleInfo) {
    for (Bundle oneBundle : matchingBundleInfo.exporterBundles) {
      String line = this.formatBundleInfoList(packageName, oneBundle, true);
      this.lines.add(line);
    }

    if (! this.showExportOnly) {
      for (Bundle oneBundle : matchingBundleInfo.containerBundles) {
        // Don't report the bundle twice.
        if (!matchingBundleInfo.exporterBundles.contains(oneBundle)) {
          String line = this.formatBundleInfoList(packageName, oneBundle, false);
          this.lines.add(line);
        }
      }
    }
  }

  @Override
  public void finalizeReport() {
    for (String oneLine : this.lines) {
      this.printStream.println(oneLine);
    }
  }

  /**
   * Format the detail information for the given bundle in the format used by the LIST output.
   *
   * @param bundle source of the information to format.
   * @param isExporter true => the bundle exports the matching package; false => the bundle contains,
   *                   but does not export, the matching package.
   * @return String with the bundle information in LIST format.
   */
  private String formatBundleInfoList(String packageName, Bundle bundle, boolean isExporter) {
    return packageName
           + "|"
           + bundle.getBundleId()
           + "|"
           + (isExporter ? "Y" : "N")
           + "|"
           + bundle.getSymbolicName()
           + "|"
           + bundle.getVersion()
           + "|"
           + bundle.getLocation();
  }
}
