/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.afp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.modca.DataStream;

public abstract class AbstractAFPPainter {

    /** Static logging instance */
    protected static Log log = LogFactory.getLog("org.apache.xmlgraphics.afp");

    protected final DataStream dataStream;
    protected final AFPPaintingState state;

    /**
     * Main constructor
     *
     * @param state the afp state
     * @param dataStream the afp datastream
     */
    public AbstractAFPPainter(AFPPaintingState state, DataStream dataStream) {
        this.state = state;
        this.dataStream = dataStream;
    }

    /**
     * Paints the painting item
     *
     * @param paintInfo the painting information
     */
    public abstract void paint(PaintInfo paintInfo);
}