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
package com.github.megatronking.svg.generator.svg.parser.attribute;

import com.github.megatronking.svg.generator.svg.model.Rect;
import com.github.megatronking.svg.generator.svg.model.SvgConstants;
import com.github.megatronking.svg.generator.svg.parser.SvgNodeAbstractAttributeParser;

import org.dom4j.DocumentException;
import org.dom4j.Element;


/**
 * Build rect's field values from attributes of the svg element.
 *
 * @author Megatron King
 * @since 2016/11/22 19:00
 */

public class RectAttributeParser extends SvgNodeAbstractAttributeParser<Rect> {

    @Override
    public void parse(Element element, Rect rect) throws DocumentException {
        super.parse(element, rect);
        rect.x = parseFloat(element, SvgConstants.ATTR_X);
        rect.y = parseFloat(element, SvgConstants.ATTR_Y);
        rect.width = parseFloat(element, SvgConstants.ATTR_WIDTH);
        rect.height = parseFloat(element, SvgConstants.ATTR_HEIGHT);
        rect.toPath();
    }
}
