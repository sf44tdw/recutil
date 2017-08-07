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


/*
 * 指定されたディレクトリ下のファイルをリストアップする。
 */
package recutil.updatedb.listmaker.fileseeker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 *
 */
public class FileSeeker {

    private final File SourceDir;

    //検索対象とする条件の一覧
    private final IOFileFilter seekType;

    private IOFileFilter dirf = TrueFileFilter.INSTANCE;

    public FileSeeker(File SourceDir, IOFileFilter seekType) {
        this.SourceDir = new File(SourceDir.getAbsolutePath());
        this.seekType = seekType;
    }

    public synchronized boolean isRecursive() {
        boolean Ret = false;
        if (this.dirf instanceof TrueFileFilter) {
            Ret = true;
        } else if (this.dirf instanceof FalseFileFilter) {
            Ret = false;
        }
        return Ret;
    }

    /**
     * サブディレクトリも検索するか。trueをセットすると検索するようになる。デフォルトはtrue
     *
     * @param recursive セット対象
     */
    public synchronized void setRecursive(boolean recursive) {
        if (recursive == false) {
            this.dirf = FalseFileFilter.INSTANCE;
        } else {
            this.dirf = TrueFileFilter.INSTANCE;
        }
    }

    /**
     * 検索を行い、その結果を返す。
     *
     * @return 見つかったファイルを示すファイルオブジェクトのリスト 検索先がディレクトリではなかった場合はnull
     */
    public synchronized List<File> seek() {
        if (this.SourceDir.isDirectory()) {
            List<File> list = Collections.synchronizedList(new ArrayList<File>());
            Collection<File> files = FileUtils.listFiles(this.SourceDir, this.seekType, this.dirf);
            list.addAll(files);
            return Collections.unmodifiableList(list);
        } else {
            return null;
        }
    }
}
