/*
 * Copyright (C) 2016 normal
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package recutil.updatedb.common;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author normal
 */
public final class Const {

    private Const() {
    }


    public static final Charset CHARCODE = StandardCharsets.UTF_8;

    /**
     * テストデータ置き場
     */
    private static final File TESTDATADIR = new File("./test");

    /**
     * テスト用EPGのxml置き場
     */
    private static final File XMLTESTDATADIR = new File(TESTDATADIR, "xml");

    /**
     * テスト用EPGのxml置き場
     */
    private static final File XMLTESTDATADIR_RECURSIVE = new File(XMLTESTDATADIR, "recursive");

    public static final String TESTDATADIR_S = TESTDATADIR.getAbsolutePath();

    /**
     * XMLファイル1
     */
    private static final File TESTDATA_XML_1 = new File(XMLTESTDATADIR, "24.xml");

    /**
     * XMLファイル2
     */
    private static final File TESTDATA_XML_2 = new File(XMLTESTDATADIR, "22.xml");

    /**
     * XMLファイル3
     */
    private static final File TESTDATA_XML_3 = new File(XMLTESTDATADIR_RECURSIVE, "23.xml");

    private static File copy(File f) {
        return new File(f.getAbsolutePath());
    }

    public static File getTESTDATADIR() {
        return copy(TESTDATADIR);
    }

    public static File getXMLTESTDATADIR() {
        return copy(XMLTESTDATADIR);
    }

    public static File getXMLTESTDATADIR_RECURSIVE() {
        return copy(XMLTESTDATADIR_RECURSIVE);
    }

    public static File getTESTDATA_XML_1() {
        return copy(TESTDATA_XML_1);
    }

    public static File getTESTDATA_XML_2() {
        return copy(TESTDATA_XML_2);
    }

    public static File getTESTDATA_XML_3() {
        return copy(TESTDATA_XML_3);
    }


}
