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
package recutil.updatedb.dataextractor.channel;

import java.util.List;
import org.w3c.dom.Document;
import recutil.updatedb.dataextractor.AbstractAllEpgFileExtractor;

/**
 *
 * @author dosdiaopfhj
 */
public class AllChannelDataExtractor extends AbstractAllEpgFileExtractor<ChannelData, ChannelDataExtractor> {

    public AllChannelDataExtractor(List<Document> EPGXMLs) {
        super(EPGXMLs);
    }

    @Override
    protected synchronized ChannelDataExtractor getExtractor(Document doc) {
        return new ChannelDataExtractor(doc);
    }

    /**
     * チャンネルID昇順ソート
     * @inheritDoc
     */
    @Override
    protected void sortRersult(List<ChannelData> src) {
        src.sort((a, b) -> a.getId().compareTo(b.getId()));
    }

}
