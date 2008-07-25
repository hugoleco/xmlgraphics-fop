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

package org.apache.fop.pdf;

/**
 * Class representing a Root (/Catalog) object.
 */
public class PDFRoot extends PDFDictionary {

    /**
     * Use no page mode setting, default
     */
    public static final int PAGEMODE_USENONE = 0;

    /**
     * Use outlines page mode to show bookmarks
     */
    public static final int PAGEMODE_USEOUTLINES = 1;

    /**
     * Use thumbs page mode to show thumbnail images
     */
    public static final int PAGEMODE_USETHUMBS = 2;

    /**
     * Full screen page mode
     */
    public static final int PAGEMODE_FULLSCREEN = 3;

    private static final PDFName[] PAGEMODE_NAMES = new PDFName[] {
        new PDFName("UseNone"),
        new PDFName("UseOutlines"),
        new PDFName("UseThumbs"),
        new PDFName("FullScreen"),
    };

    /**
     * create a Root (/Catalog) object. NOTE: The PDFRoot
     * object must be created before the PDF document is
     * generated, but it is not assigned an object ID until
     * it is about to be written (immediately before the xref
     * table as part of the trsailer). (mark-fop@inomial.com)
     *
     * @param objnum the object's number
     * @param pages the PDFPages object
     */
    public PDFRoot(int objnum, PDFPages pages) {
        super();
        setObjectNumber(objnum);
        put("Type", new PDFName("Catalog"));
        setRootPages(pages);
    }

    /**
     * Set the page mode for the PDF document.
     *
     * @param mode the page mode (one of PAGEMODE_*)
     */
    public void setPageMode(int mode) {
        put("PageMode", PAGEMODE_NAMES[mode]);
    }

    /**
     * Returns the currently active /PageMode.
     * @return the /PageMode (one of PAGEMODE_*)
     */
    public int getPageMode() {
        PDFName mode = (PDFName)get("PageMode");
        if (mode != null) {
            for (int i = 0; i < PAGEMODE_NAMES.length; i++) {
                if (PAGEMODE_NAMES[i].equals(mode)) {
                    return i;
                }
            }
            throw new IllegalStateException("Unknown /PageMode encountered: " + mode);
        } else {
            return PAGEMODE_USENONE;
        }
    }

    /**
     * add a /Page object to the root /Pages object
     *
     * @param page the /Page object to add
     */
    public void addPage(PDFPage page) {
        PDFPages pages = getRootPages();
        pages.addPage(page);
    }

    /**
     * set the root /Pages object
     *
     * @param pages the /Pages object to set as root
     */
    public void setRootPages(PDFPages pages) {
        put("Pages", pages.makeReference());
    }

    /**
     * Returns the /PageLabels object.
     * @return the /PageLabels object if set, null otherwise.
     * @since PDF 1.3
     */
    public PDFPages getRootPages() {
        PDFReference ref = (PDFReference)get("Pages");
        return (ref != null ? (PDFPages)ref.getObject() : null);
    }

    /**
     * Sets the /PageLabels object.
     * @param pageLabels the /PageLabels object
     */
    public void setPageLabels(PDFPageLabels pageLabels) {
        put("PageLabels", pageLabels.makeReference());
    }

    /**
     * Returns the /PageLabels object.
     * @return the /PageLabels object if set, null otherwise.
     * @since PDF 1.3
     */
    public PDFPageLabels getPageLabels() {
        PDFReference ref = (PDFReference)get("PageLabels");
        return (ref != null ? (PDFPageLabels)ref.getObject() : null);
    }

    /**
     * Set the root outline for the PDF document.
     *
     * @param out the root PDF Outline
     */
    public void setRootOutline(PDFOutline out) {
        put("Outlines", out.makeReference());

        //Set /PageMode to /UseOutlines by default if no other mode has been set
        PDFName mode = (PDFName)get("PageMode");
        if (mode == null) {
            setPageMode(PAGEMODE_USEOUTLINES);
        }
    }

    /**
     * Get the root PDF outline for the document.
     *
     * @return the root PDF Outline
     */
    public PDFOutline getRootOutline() {
        PDFReference ref = (PDFReference)get("Outlines");
        return (ref != null ? (PDFOutline)ref.getObject() : null);
    }

    /**
     * Set the /Names object.
     * @param names the Names object
     * @since PDF 1.2
     */
    public void setNames(PDFNames names) {
        put("Names", names.makeReference());
    }

    /**
     * Returns the /Names object.
     * @return the Names object if set, null otherwise.
     * @since PDF 1.2
     */
    public PDFNames getNames() {
        PDFReference ref = (PDFReference)get("Names");
        return (ref != null ? (PDFNames)ref.getObject() : null);
    }

    /**
     * Set the optional Metadata object.
     * @param meta the Metadata object
     * @since PDF 1.4
     */
    public void setMetadata(PDFMetadata meta) {
        if (getDocumentSafely().getPDFVersion() >= PDFDocument.PDF_VERSION_1_4) {
            put("Metadata", meta.makeReference());
        }
    }

    /**
     * Returns the /Metadata object
     * @return the /Metadata object if set, null otherwise.
     * @since PDF 1.4
     */
    public PDFMetadata getMetadata() {
        PDFReference ref = (PDFReference)get("Metadata");
        return (ref != null ? (PDFMetadata)ref.getObject() : null);
    }

    /**
     * Returns the /OutputIntents array.
     * @return the /OutputIntents array or null if it doesn't exist
     * @since PDF 1.4
     */
    public PDFArray getOutputIntents() {
        return (PDFArray)get("OutputIntents");
    }

    /**
     * Adds an OutputIntent to the PDF
     * @param outputIntent the OutputIntent dictionary
     * @since PDF 1.4
     */
    public void addOutputIntent(PDFOutputIntent outputIntent) {
        if (getDocumentSafely().getPDFVersion() >= PDFDocument.PDF_VERSION_1_4) {
            PDFArray outputIntents = getOutputIntents();
            if (outputIntents == null) {
                outputIntents = new PDFArray(this);
                put("OutputIntents", outputIntents);
            }
            outputIntents.add(outputIntent);
        }
    }

    /**
     * Returns the language identifier of the document.
     * @return the language identifier of the document (or null if not set or undefined)
     * @since PDF 1.4
     */
    public String getLanguage() {
        return (String)get("Lang");
    }

    /**
     * Sets the language identifier of the document.
     * @param lang the language identifier of the document.
     */
    public void setLanguage(String lang) {
        if (lang == null) {
            throw new NullPointerException("lang must not be null");
        }
        put("Lang", lang);
    }

}
