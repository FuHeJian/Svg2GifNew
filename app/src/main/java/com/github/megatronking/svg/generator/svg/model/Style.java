/*
 * Copyright (C) 2017, Megatron King
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.megatronking.svg.generator.svg.model;

import com.github.megatronking.svg.generator.svg.utils.StyleUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The style SVG element allows style sheets to be embedded directly within SVG content.
 *
 * @author Megatron King
 * @since 2017/1/4 10:26
 */

public class Style extends SvgNode {

    public String cssStyle;

    @Override
    public void toPath() {
        // nothing to do
    }

    @Override
    public void applyStyles(Map<String, String> inheritStyles, Map<String, Map<String, String>> defineStyles) {
        // Nothing to do
    }

    public Map<String, Map<String, String>> toStyle() {
        Map<String, Map<String, String>> styleMapsWithClass = new HashMap<>();
        StyleUtils.fill2Map(cssStyle, styleMapsWithClass);
        return styleMapsWithClass;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
