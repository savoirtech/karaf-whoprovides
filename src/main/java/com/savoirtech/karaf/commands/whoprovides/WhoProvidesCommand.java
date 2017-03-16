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

package com.savoirtech.karaf.commands.whoprovides;

import com.savoirtech.karaf.commands.whoprovides.impl.DefaultWhoProvidesReportFactory;

import java.util.List;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.karaf.shell.console.AbstractAction;
import org.apache.karaf.shell.table.ShellTable;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleWiring;

@Command(scope = "bundle", name = "find-package", description = "Karaf WhoProvides Command for Lookup of bundles providing packages")
public class WhoProvidesCommand extends AbstractAction {

  public static final OutputFormat DEFAULT_OUTPUT_FORMAT = OutputFormat.TABLE;

  @Argument(name = "package-name", required = false, multiValued = true, description = "name of package(s) for which to locate providing bundles")
  private List<String> packageNames;

  @Option(name = "--format", description = "output format; use format \"help\" to see supported formats")
  private String outputFormatName = DEFAULT_OUTPUT_FORMAT.toString();

  @Option(name="-e", aliases = { "--export-only" }, description = "only show bundles exporting the named packages")
  private boolean showExportOnly;

  private OutputFormat outputFormat = DEFAULT_OUTPUT_FORMAT;
  private ShellTableFactory shellTableFactory = ShellTable::new;
  private WhoProvidesReportFactory whoProvidesReportFactory = new DefaultWhoProvidesReportFactory();

//========================================
// Getters and Setters
//----------------------------------------

  public ShellTableFactory getShellTableFactory() {
    return shellTableFactory;
  }

  public void setShellTableFactory(ShellTableFactory shellTableFactory) {
    this.shellTableFactory = shellTableFactory;
  }

  public WhoProvidesReportFactory getWhoProvidesReportFactory() {
    return whoProvidesReportFactory;
  }

  public void setWhoProvidesReportFactory(
      WhoProvidesReportFactory whoProvidesReportFactory) {
    this.whoProvidesReportFactory = whoProvidesReportFactory;
  }
//========================================
// COMMAND EXECUTION
//----------------------------------------

  /**
   * Execute the command.
   *
   * @return null - always.
   * @throws Exception
   */
  protected Object doExecute() throws Exception {
    if (this.processArguments()) {

      BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
      WhoProvidesReport reportGenerator;

      if ((packageNames != null) && (! packageNames.isEmpty())) {
        reportGenerator = this.whoProvidesReportFactory.newReport(this.outputFormat);
        reportGenerator.prepareReport();

        for (String name : packageNames) {
          // Find the matching bundles.
          MatchingBundleInfo matchingBundleInfo = this.findPackageProviders(bundleContext, name);

          // Add the results for this package to the report.
          reportGenerator.addToReport(name, matchingBundleInfo);
        }

        // Finalize the report.
        reportGenerator.finalizeReport();
      } else {
        System.out.println("At least one package name is required");
      }
    }

    return null;
  }

//========================================
// Internal Methods
//----------------------------------------

  /**
   * Decode the command arguments.
   *
   * @return true => continue processing; false => aborted processing due to argument processing
   * failure.
   */
  private boolean processArguments() {
    if (! this.decodeOutputFormat()) {
      return false;
    }

    return true;
  }

  /**
   * Decode the output format specified by the user.
   *
   * @return true => continue processing; false => abort processing due to invalid output format
   * name given.
   */
  private boolean decodeOutputFormat() {
    if (this.outputFormatName.equals("help")) {
      for (OutputFormat oneFormat : OutputFormat.values()) {
        System.out.println("    " + oneFormat.toString().toLowerCase());
      }

      return false;
    }

    this.outputFormat = OutputFormat.valueOf(outputFormatName.toUpperCase());

    if (this.outputFormat == null) {
      System.out.println("Unrecognized output format " + outputFormatName);
      return false;
    }

    return true;
  }

  /**
   * Locate all of the providers, in the given bundle context, of the named package.
   *
   * @param bundleContext
   * @param packageName
   */
  private MatchingBundleInfo findPackageProviders(BundleContext bundleContext, String packageName) {
    MatchingBundleInfo result = new MatchingBundleInfo();
    String packagePath = packageName.replace(".", "/");

    for (Bundle oneBundle : bundleContext.getBundles()) {

      if (this.bundleHasPackage(oneBundle, packagePath)) {
        result.containerBundles.add(oneBundle);
      }

      if (this.bundleExportsPackage(oneBundle, packageName)) {
        result.exporterBundles.add(oneBundle);
      }
    }

    return result;
  }

  /**
   * Determine whether the given bundle contains the named package path.
   *
   * @param bundle bundle to test.
   * @param packagePath path to the package to check within the bundle.
   * @return true => the bundle contains the package path; false => the bundle does not contain the
   * package path.
   */
  private boolean bundleHasPackage(Bundle bundle, String packagePath) {
    return (bundle.getEntry(packagePath) != null);
  }

  /**
   * Determine whether the bundle exports the named package.
   *
   * @param bundle bundle to test.
   * @param packageName name of the package to check for export by the bundle.
   * @return true => the bundle exports the named package; false => the bundle does not export the
   * named package.
   */
  private boolean bundleExportsPackage(Bundle bundle, String packageName) {
    BundleWiring wiring = bundle.adapt(BundleWiring.class);

    List<BundleCapability> capabilities = wiring.getCapabilities("osgi.wiring.package");

    for (BundleCapability oneCapability : capabilities) {
      if (packageName.equals(oneCapability.getAttributes().get("osgi.wiring.package"))) {
        return true;
      }
    }

    return false;
  }
}
