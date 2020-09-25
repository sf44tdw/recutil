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

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.dataextractor.AbstractEpgFileExtractor;

/**
 * チャンネル関係の情報だけDBへ格納する
 *
 * 地上波の場合 EPGのチャンネルid(GR_*)? :id EPG取得時の物理チャンネル番号 :tp テレビ局名 :display-name
 *
 * BS/CSの場合 EPGのチャンネルid?(BS_物理チャンネル番号) :id EPG取得時の物理チャンネル番号(BS_*_*) :tp
 * テレビ局名:display-name
 *
 * を取得し、リストに格納する。
 *
 * テレビ局名内の文字化けの原因になりそうな文字、空白文字とクォートは別の文字に置き換える。
 * 
 * @author dosdiaopfhj
 */
public class ChannelDataExtractor extends AbstractEpgFileExtractor<ChannelData> {

	private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();

	/**
	 * EPG関連 チャンネル要素の要素名
	 */
	private static final String EPG_CHANNEL = "channel";
	/**
	 * EPG関連 チャンネル要素のチャンネルIDの要素名
	 */
	private static final String EPG_CHANNEL_ID = "id";

	/**
	 * EPG関連 チャンネル要素の物理チャンネル番号の要素名
	 */
	private static final String EPG_CHANNEL_TP = "tp";

	/**
	 * EPG関連 チャンネルIDが地上波の場合の接頭辞
	 */
	private static final String PREFIX_GR = "GR";

	/**
	 * EPG関連 チャンネルIDがBSの場合の接頭辞
	 */
	private static final String PREFIX_BS = "BS";

	/**
	 * EPG関連 チャンネルIDがCSの場合の接頭辞
	 */
	private static final String PREFIX_CS = "CS";
	/**
	 * EPG関連 チャンネル要素の局名の要素名
	 */
	private static final String EPG_CHANNEL_DISPLAY_NAME = "display-name";

	private static final MessageFormat DUMP_FORMAT = new MessageFormat("ch:{0}_TP:{1}_display-name:{2}");

	public ChannelDataExtractor(Document doc) {
		super(doc, ChannelDataExtractor.EPG_CHANNEL);
	}

	private synchronized long getChannelNumber(String ch_S, String tp_S) throws IllegalArgumentException {
		//チャンネルidの頭2文字を取り出す。
		long r;
		String pref = ch_S.substring(0, 2);
		if (LOG.isDebugEnabled()) {
			LOG.debug("チャンネルid:{} その頭2文字:{}", ch_S, pref);
		}
		switch (pref) {
		case ChannelDataExtractor.PREFIX_GR:
			//地上波の場合
			r = Integer.parseInt(tp_S);
			break;
		case ChannelDataExtractor.PREFIX_BS:
		case ChannelDataExtractor.PREFIX_CS:
			//BSかCSの場合(CSについては憶測)
			//チャンネルidの頭3文字以外を数字に変換して物理チャンネル番号とする。
			r = Long.parseLong(ch_S.substring(3));
			break;
		default:
			//想定外のケース。
			throw new IllegalArgumentException("物理チャンネル番号を取り出せません。ch_S = " + ch_S);
		}
		return r;
	}

	@Override
	protected final synchronized ChannelData dump(Node N) throws IllegalArgumentException {
		NamedNodeMap attrs_channel = N.getAttributes(); // NamedNodeMapの取得

		Node ch = attrs_channel.getNamedItem(ChannelDataExtractor.EPG_CHANNEL_ID);
		Node tp = attrs_channel.getNamedItem(ChannelDataExtractor.EPG_CHANNEL_TP);
		String ch_S = ch.getNodeValue();
		if (LOG.isTraceEnabled()) {
			LOG.trace("ch:{}", ch_S);
		}
		String tp_S = tp.getNodeValue();
		if (LOG.isTraceEnabled()) {
			LOG.trace("TP:{}", tp_S);
		}
		String display_name_S = "";
		NodeList channelChildren = N.getChildNodes();
		int Nodes = channelChildren.getLength();
		for (int i = 0; i < Nodes; i++) {
			Node gchild = channelChildren.item(i);

			//たまに放送局名が空欄になっていることがあるので、その場合は仮の名前を記入する。
			try {
				if (gchild.getNodeName().equals(ChannelDataExtractor.EPG_CHANNEL_DISPLAY_NAME)) {
					display_name_S = gchild.getFirstChild().getNodeValue();
				}
				if (LOG.isTraceEnabled()) {
					LOG.trace("放送局名有り。");
				}
			} catch (NullPointerException e) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("放送局名無し。");
				}
				display_name_S = "Unknown display-name";
			} finally {
				if (LOG.isTraceEnabled()) {
					LOG.trace("display-name:{}", display_name_S);
				}
			}
		}
		//文字化け等の原因になりそうな文字を除去する。
		display_name_S = recutil.commmonutil.Util.quoteString(display_name_S);
		display_name_S = recutil.commmonutil.Util.replaceProhibitedCharacter(display_name_S);
		if (LOG.isDebugEnabled()) {
			Object[] message = { ch_S, tp_S, display_name_S };
			LOG.debug(ChannelDataExtractor.DUMP_FORMAT.format(message));
		}
		try {
			return new ChannelData(ch_S, getChannelNumber(ch_S, tp_S), display_name_S);
		} catch (IllegalArgumentException ex) {
			Object[] message = { ch_S, tp_S, display_name_S };
			throw new IllegalArgumentException(ChannelDataExtractor.DUMP_FORMAT.format(message), ex);
		}
	}
}
