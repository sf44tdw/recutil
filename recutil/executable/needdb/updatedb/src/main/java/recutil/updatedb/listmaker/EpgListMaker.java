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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;

import java.nio.charset.Charset;
import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.listmaker.fileseeker.FileSeeker;

/**
 * 指定されたディレクトリからxmlファイルを捜索する。サブディレクトリは無視する。
 *
 * @author dosdiaopfhj
 */
public class EpgListMaker {

    private final FileSeeker seeker;

    private final Charset charset;

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    /**
     * EPG XMLファイルを読み込む際、文字コードを指定する。
     *
     * @param sourceDir ファイルを検索するディレクトリ
     * @param charset 読み込むファイルの文字コード。
     */
    public EpgListMaker(File sourceDir, Charset charset) {
        this.charset = charset;
        seeker = new FileSeeker(sourceDir, XmlSuffix.getFilter());
        seeker.setRecursive(false);
    }

    /**
     * EPG XMLファイルの検索を行い、見つかったファイルを読み込む。
     *
     * @return 見つかったファイルを変換したDocumentオブジェクトのリスト。変更不可。読み込みに失敗したファイルは無視する。
     */
    public synchronized List<Document> getEpgList() {
        List<Document> EPGs = Collections.synchronizedList(new ArrayList<Document>());
        List<File> FL = this.seeker.seek();
        Iterator<File> it_F = FL.iterator();
        while (it_F.hasNext()) {
            File F = it_F.next();
            Document d = new XmlLoader(charset).Load(F);
            if (d != null) {
                LOG.info("EPGファイルが読み込まれました。 EPG FILE={0}", F.toString());
                EPGs.add(d);
            } else {
                LOG.warn("EPGファイルが読み込まれませんでした。このファイルは無視されます。 EPG FILE={0}", F.toString());
            }
        }
        return Collections.unmodifiableList(EPGs);
    }
}
