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

import org.osgi.framework.Bundle;

import java.util.TreeSet;

/**
 * Bundles matched during search.
 *
 * Created by art on 3/12/17.
 */
public class MatchingBundleInfo {
    /**
     * Matching bundles that contain a package, or other resource.
     */
    public TreeSet<Bundle> containerBundles = new TreeSet<>();

    /**
     * Matching bundles that export a package, or other resource.
     */
    public TreeSet<Bundle> exporterBundles = new TreeSet<>();
}