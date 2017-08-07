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
package recutil.updatedb.listmaker;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 *
 * @author normal
 */
public final class XmlSuffix {

    private XmlSuffix() {
    }

    public static final IOFileFilter SUFFIX_1 = FileFilterUtils.suffixFileFilter("xml");
    public static final IOFileFilter SUFFIX_2 = FileFilterUtils.suffixFileFilter("Xml");
    public static final IOFileFilter SUFFIX_3 = FileFilterUtils.suffixFileFilter("xMl");
    public static final IOFileFilter SUFFIX_4 = FileFilterUtils.suffixFileFilter("xmL");
    public static final IOFileFilter SUFFIX_5 = FileFilterUtils.suffixFileFilter("XMl");
    public static final IOFileFilter SUFFIX_6 = FileFilterUtils.suffixFileFilter("xML");
    public static final IOFileFilter SUFFIX_7 = FileFilterUtils.suffixFileFilter("XmL");
    public static final IOFileFilter SUFFIX_8 = FileFilterUtils.suffixFileFilter("XML");

    public static IOFileFilter getFilter() {
        return FileFilterUtils.or(SUFFIX_1, SUFFIX_2, SUFFIX_3, SUFFIX_4, SUFFIX_5, SUFFIX_6, SUFFIX_7, SUFFIX_8);
    }
}
