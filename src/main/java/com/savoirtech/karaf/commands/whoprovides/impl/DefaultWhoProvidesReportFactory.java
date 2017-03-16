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

package com.savoirtech.karaf.commands.whoprovides.impl;

import com.savoirtech.karaf.commands.whoprovides.OutputFormat;
import com.savoirtech.karaf.commands.whoprovides.WhoProvidesReport;
import com.savoirtech.karaf.commands.whoprovides.WhoProvidesReportFactory;
import com.savoirtech.karaf.commands.whoprovides.reports.WhoProvidesListReport;
import com.savoirtech.karaf.commands.whoprovides.reports.WhoProvidesTableReport;

/**
 * Created by art on 3/15/17.
 */
public class DefaultWhoProvidesReportFactory implements WhoProvidesReportFactory {

  @Override
  public WhoProvidesReport newReport(OutputFormat outputFormat) {
    switch (outputFormat) {
      case TABLE:
        return new WhoProvidesTableReport();

      case LIST:
        return new WhoProvidesListReport();

      default:
        throw new RuntimeException(
            "internal error: unrecognized report output format \"" + outputFormat + "\"");
    }
  }
}
