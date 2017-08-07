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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import recutil.updatedb.dataextractor.channel.ChannelData;
import recutil.updatedb.dataextractor.programme.ProgrammeData;
import recutil.updatedb.dateconverter.Converter;

/**
 *
 * @author normal
 */
public final class TestData {

    private TestData() {
    }

    public static final String PATTERN = "yyyy-MM-dd HH:mm";

    public static List<ChannelData> get24Chs() {
        List<ChannelData> ch24 = new ArrayList<>();
        final int ch = 24;
        final String disp = "テレビ朝日";
        ch24.add(new ChannelData("GR_1064", ch, disp));
        ch24.add(new ChannelData("GR_1065", ch, disp));
        ch24.add(new ChannelData("GR_1066", ch, disp));
        return Collections.unmodifiableList(ch24);
    }

    public static List<ProgrammeData> get24chProgramme() {
        final String pattern = TestData.PATTERN;
        List<ProgrammeData> result = new ArrayList<>();
        result.add(new ProgrammeData("GR_1064", 7431, "ワイド！スクランブル　年末スペシャル【字】", Converter.stringToDate("2014-12-29 12:00", pattern), Converter.stringToDate("2014-12-29 15:00", pattern)));
        result.add(new ProgrammeData("GR_1064", 7433, "年末相棒セレクション・スペシャル版１月１日夜９時は相棒スペシャル！【字】", Converter.stringToDate("2014-12-29 15:00", pattern), Converter.stringToDate("2014-12-29 17:25", pattern)));
        result.add(new ProgrammeData("GR_1064", 7434, "大晦日はクイズサバイバー【字】", Converter.stringToDate("2014-12-29 17:25", pattern), Converter.stringToDate("2014-12-29 17:30", pattern)));
        return Collections.unmodifiableList(result);
    }

    public static List<ChannelData> get22Chs() {
        List<ChannelData> ch22 = new ArrayList<>();
        final int ch = 22;
        final String disp = "ＴＢＳ１";
        ch22.add(new ChannelData("GR_1048", ch, disp));
        return Collections.unmodifiableList(ch22);
    }

    public static List<ProgrammeData> get22chProgramme() {
        final String pattern = TestData.PATTERN;
        List<ProgrammeData> result = new ArrayList<>();
        result.add(new ProgrammeData("GR_1048", 43874, "ごごネタ！脳活！ＴＶ【字】", Converter.stringToDate("2014-12-29 13:50", pattern), Converter.stringToDate("2014-12-29 13:55", pattern)));
        result.add(new ProgrammeData("GR_1048", 44729, "トコトン掘り下げ隊！生き物にサンキュー！！【ネコ第三弾】【再】【字】", Converter.stringToDate("2014-12-29 13:55", pattern), Converter.stringToDate("2014-12-29 14:54", pattern)));
        result.add(new ProgrammeData("GR_1048", 43876, "ＴＢＳニュース", Converter.stringToDate("2014-12-29 14:54", pattern), Converter.stringToDate("2014-12-29 15:00", pattern)));
        return Collections.unmodifiableList(result);
    }
}
