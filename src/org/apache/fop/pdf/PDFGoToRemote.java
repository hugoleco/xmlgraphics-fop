/*
 * $Id$
 * Copyright (C) 2001-2003 The Apache Software Foundation. All rights reserved.
 * For details on use and redistribution please refer to the
 * LICENSE file included with these sources.
 */

package org.apache.fop.pdf;

/**
 * class representing a /GoToR object.
 */
public class PDFGoToRemote extends PDFAction {

    /**
     * the file specification
     */
    private PDFFileSpec pdfFileSpec;
    private int pageReference = 0;
    private String destination = null;

    /**
     * create an GoToR object.
     *
     * @param number the object's number
     * @param pdfFileSpec the fileSpec associated with the action
     */
    public PDFGoToRemote(int number, PDFFileSpec pdfFileSpec) {
        /* generic creation of object */
        super(number);

        this.pdfFileSpec = pdfFileSpec;
    }

    /**
     * create an GoToR object.
     *
     * @param number the object's number
     * @param pdfFileSpec the fileSpec associated with the action
     * @param page a page reference within the remote document
     */
    public PDFGoToRemote(int number, PDFFileSpec pdfFileSpec, int page) {
        /* generic creation of object */
        super(number);

        this.pdfFileSpec = pdfFileSpec;
        this.pageReference = page;
    }

    /**
     * create an GoToR object.
     *
     * @param number the object's number
     * @param pdfFileSpec the fileSpec associated with the action
     * @param dest a named destination within the remote document
     */
    public PDFGoToRemote(int number, PDFFileSpec pdfFileSpec, String dest) {
        /* generic creation of object */
        super(number);

        this.pdfFileSpec = pdfFileSpec;
        this.destination = dest;
    }

    /**
     * return the action string which will reference this object
     *
     * @return the action String
     */
    public String getAction() {
        return this.referencePDF();
    }

    /**
     * represent the object in PDF
     *
     * @return the PDF string
     */
    public byte[] toPDF() {
        String p = new String(this.number + " " + this.generation + " obj\n"
                              + "<<\n/S /GoToR\n" + "/F "
                              + pdfFileSpec.referencePDF() + "\n");

        if (destination != null) {
            p += "/D (" + this.destination + ")";
        } else {
            p += "/D [ " + this.pageReference + " /XYZ null null null ]";
        }

        p += " \n>>\nendobj\n";

        return p.getBytes();
    }


    /*
     * example
     * 28 0 obj
     * <<
     * /S /GoToR
     * /F 29 0 R
     * /D [ 0 /XYZ -6 797 null ]
     * >>
     * endobj
     */

    /**
     * Check if this equals another object.
     *
     * @param obj the object to compare
     * @return true if this equals other object
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof PDFGoToRemote)) {
            return false;
        }

        PDFGoToRemote remote = (PDFGoToRemote)obj;

        if (!remote.pdfFileSpec.referencePDF().equals(pdfFileSpec.referencePDF())) {
            return false;
        }

        if (destination != null) {
            if (!destination.equals(remote.destination)) {
                return false;
            }
        } else {
            if (pageReference != remote.pageReference) {
                return false;
            }
        }

        return true;
    }
}

