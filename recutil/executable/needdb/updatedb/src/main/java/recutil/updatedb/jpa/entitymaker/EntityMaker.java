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
package recutil.updatedb.jpa.entitymaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.dataextractor.channel.ChannelData;
import recutil.updatedb.dataextractor.programme.ProgrammeData;

/**
 * データ保管クラスをDB登録用エンティティに変換する。
 *
 * @author normal
 */
public final class EntityMaker {

    private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();
    private final Set<ChannelData> channels;
    private final Set<ProgrammeData> programmes;

    private Map<String, recutil.dbaccessor.entity.Channel> channelEntities;

    private List<recutil.dbaccessor.entity.Programme> programmeEntities;

    public EntityMaker(List<ChannelData> channels, List<ProgrammeData> programmes) {
        //重複は排除する。
        this.channels = new HashSet<>(channels);
        this.programmes = new HashSet<>(programmes);
    }

    public synchronized void makeEntities() {
        String DEFAULT_LINE_SEPARATOR = recutil.commmonutil.Util.getDefaultLineSeparator();

        if (LOG.isDebugEnabled()) {
            LOG.debug("チャンネルエンティティ作成開始。");
        }
        Map<String, recutil.dbaccessor.entity.Channel> m = new HashMap<>();
        for (ChannelData ch : this.channels) {
            recutil.dbaccessor.entity.Channel chE = new recutil.dbaccessor.entity.Channel();
            chE.setChannelId(ch.getId());
            chE.setChannelNo(ch.getPhysicalChannelNumber());
            chE.setDisplayName(ch.getBroadcastingStationName());
            m.put(chE.getChannelId(), chE);
            if (LOG.isDebugEnabled()) {
                LOG.debug("チャンネルエンティティ作成。" + DEFAULT_LINE_SEPARATOR + "from:{}" + DEFAULT_LINE_SEPARATOR + "to:{}", ch, ReflectionToStringBuilder.toString(chE));
            }
        }
        this.channelEntities = Collections.unmodifiableMap(m);
        if (LOG.isDebugEnabled()) {
            LOG.debug("チャンネルエンティティ作成完了。 件数 = {}", m.size());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("番組エンティティ作成開始。");
        }
        List<recutil.dbaccessor.entity.Programme> l = new ArrayList<>();
        for (ProgrammeData pg : this.programmes) {
            recutil.dbaccessor.entity.Programme pgE = new recutil.dbaccessor.entity.Programme();
            pgE.setChannelId(this.channelEntities.get(pg.getId()));
            pgE.setEventId(pg.getEventId());
            pgE.setStartDatetime(pg.getStartDatetime());
            pgE.setStopDatetime(pg.getStopDatetime());
            pgE.setTitle(pg.getTitle());
            l.add(pgE);
            if (LOG.isDebugEnabled()) {
                LOG.debug("番組エンティティ生成" + DEFAULT_LINE_SEPARATOR + "from:{}" + DEFAULT_LINE_SEPARATOR + "to:{}", pg, ReflectionToStringBuilder.toString(pgE));
            }
        }
        this.programmeEntities = Collections.unmodifiableList(l);
        if (LOG.isDebugEnabled()) {
            LOG.debug("番組エンティティ作成完了。 件数 = {}", l.size());
        }
    }

    public List<recutil.dbaccessor.entity.Channel> getChannelEntities() {
        return new ArrayList<>(channelEntities.values());
    }

    public List<recutil.dbaccessor.entity.Programme> getProgrammeEntities() {
        return programmeEntities;
    }

}
