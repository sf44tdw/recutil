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
package recutil.updatedb.dataextractor.programme;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.dataextractor.AbstractEpgFileExtractor;
import recutil.updatedb.dateconverter.Converter;

/**
 * EPGから番組関係の情報を取得する。 番組ID :event_id チャンネルID :channel タイトル :title 開始時刻 :start
 * 終了時刻 :stop
 * 
 * タイトル内の文字化けの原因になりそうな文字、空白文字とクォートは別の文字に置き換える。
 *
 * @author dosdiaopfhj
 */
public class ProgrammeDataExtractor extends AbstractEpgFileExtractor<ProgrammeData> {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    /**
     * EPGの放送日データをDate型に変換するためのフォーマット
     */
    private static final String C_DATE_PATTERN = "yyyyMMddHHmmss ZZ";

    /**
     * EPG関連 番組要素の要素名
     */
    private static final String EPG_PROGRAMME = "programme";

    /**
     * EPG関連 番組要素の番組IDの要素名
     */
    private static final String EPG_PROGRAMME_EVENT_ID = "event_id";

    /**
     * EPG関連 番組要素のチャンネルIDの要素名
     */
    private static final String EPG_PROGRAMME_CHANNEL_ID = "channel";

    /**
     * EPG関連 番組要素の番組開始時刻の要素名
     */
    private static final String EPG_PROGRAMME_START_TIME = "start";

    /**
     * EPG関連 番組要素の番組終了時刻の要素名
     */
    private static final String EPG_PROGRAMME_STOP_TIME = "stop";

    /**
     * EPG関連 番組要素の番組名の要素名
     */
    private static final String EPG_PROGRAMME_NAME = "title";

    private static final MessageFormat DUMP_FORMAT = new MessageFormat("event_id:{0}_start:{1}_stop:{2}_channel_id:{3}_title:{4}");

    public ProgrammeDataExtractor(Document doc) {
        super(doc, ProgrammeDataExtractor.EPG_PROGRAMME);
    }

    @Override
    protected final synchronized ProgrammeData dump(Node N) throws IllegalArgumentException {
        String event_id_s;
        String start_time_s;
        String stop_time_s;
        String channel_id_s;
        String title_s = "";

        NamedNodeMap programme_attrs = N.getAttributes();  // NamedNodeMapの取得
        Node event_id = programme_attrs.getNamedItem(ProgrammeDataExtractor.EPG_PROGRAMME_EVENT_ID);
        Node start_time = programme_attrs.getNamedItem(ProgrammeDataExtractor.EPG_PROGRAMME_START_TIME);
        Node stop_time = programme_attrs.getNamedItem(ProgrammeDataExtractor.EPG_PROGRAMME_STOP_TIME);
        Node channel_id = programme_attrs.getNamedItem(ProgrammeDataExtractor.EPG_PROGRAMME_CHANNEL_ID);

        event_id_s = event_id.getNodeValue();
        start_time_s = start_time.getNodeValue();
        stop_time_s = stop_time.getNodeValue();
        channel_id_s = channel_id.getNodeValue();

        NodeList programmeChildren = N.getChildNodes();
        int Nodes = programmeChildren.getLength();
        for (int i = 0; i < Nodes; i++) {
            Node gchild = programmeChildren.item(i);
            if (gchild.getNodeName().equals(ProgrammeDataExtractor.EPG_PROGRAMME_NAME)) {
                title_s = gchild.getFirstChild().getNodeValue();
            }
        }
        
		//文字化け等の原因になりそうな文字を除去する。
		title_s = recutil.commmonutil.Util.quoteString(title_s);
		title_s = recutil.commmonutil.Util.replaceProhibitedCharacter(title_s);
        
        if (LOG.isDebugEnabled()) {
            Object[] message = {event_id_s, start_time_s, stop_time_s, channel_id_s, title_s};
            LOG.debug(ProgrammeDataExtractor.DUMP_FORMAT.format(message));
        }
        ProgrammeData p = new ProgrammeData(
                channel_id_s,
                Long.parseLong(event_id_s),
                title_s,
                Converter.stringToDate(start_time_s, ProgrammeDataExtractor.C_DATE_PATTERN),
                Converter.stringToDate(stop_time_s, ProgrammeDataExtractor.C_DATE_PATTERN)
        );
        return p;
    }

}
