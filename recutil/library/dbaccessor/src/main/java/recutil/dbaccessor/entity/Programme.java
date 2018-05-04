/*
 * Copyright (C) 2017 normal
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
package recutil.dbaccessor.entity;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/*
複合主キーの取り扱いが複雑なので、代理キーを設定し、元の複合主キーには一意性制約を設定する。

Tableアノテーションのcatalog,schemaについては、MariaDBのCREATEINDEX時にSCHEMA.INDEXNAMEや、SCHEMA.TABLENAMEのようにしてSQLを発行すると不具合が出るので指定しない。
(@Indexでインデックスを設定してもテーブル生成時に無視される。)

紐付けを遅延モードで行うと例外が発生するので、しない。
 */
/**
 *
 * @author normal
 */
@Entity
@Table(name = "PROGRAMME", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CHANNEL_ID", "EVENT_ID"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programme.findAll", query = "SELECT p FROM Programme p")
    , @NamedQuery(name = "Programme.findById", query = "SELECT p FROM Programme p WHERE p.id = :id")
    , @NamedQuery(name = "Programme.findByEventId", query = "SELECT p FROM Programme p WHERE p.eventId = :eventId")
    , @NamedQuery(name = "Programme.findByStartDatetime", query = "SELECT p FROM Programme p WHERE p.startDatetime = :startDatetime")
    , @NamedQuery(name = "Programme.findByStopDatetime", query = "SELECT p FROM Programme p WHERE p.stopDatetime = :stopDatetime")
    , @NamedQuery(name = "Programme.findByAllParams", query = "SELECT p FROM Programme p WHERE p.eventId = :eventId AND p.title = :title AND p.startDatetime = :startDatetime AND p.stopDatetime = :stopDatetime AND p.channelId.channelId = :channelId")
    , @NamedQuery(name = "Programme.deleteAll", query = "DELETE FROM Programme")})
public class Programme implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "EVENT_ID", nullable = false)
    private long eventId;
    @Basic(optional = false)
    @Column(name = "START_DATETIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetime;
    @Basic(optional = false)
    @Column(name = "STOP_DATETIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date stopDatetime;
    @Basic(optional = false)
    @Lob
    @Column(nullable = false, length = 2147483647)
    private String title;
    @JoinColumn(name = "CHANNEL_ID", referencedColumnName = "CHANNEL_ID", nullable = false)
    @ManyToOne(optional = false)
    private Channel channelId;

    public Programme() {
    }

    public Programme(Long id) {
        this.id = id;
    }

    public Programme(Long id, long eventId, Date startDatetime, Date stopDatetime, String title) {
        this.id = id;
        this.eventId = eventId;
        this.startDatetime = getCopyDate(startDatetime);
        this.stopDatetime = getCopyDate(stopDatetime);
        this.title = title;
    }

    private Date getCopyDate(Date d) {
        if (d == null) {
            return null;
        }
        return new Date(d.getTime());
    }

    /**
     * 放送開始時刻が放送終了時刻以前になっていたら、例外を発生させる。<br>
     *
     * @throws IllegalArgumentException
     */
    @PrePersist
    @PreUpdate
    protected void preWriteCheck() {
        this.dateTimeCheck(this.getStartDatetime(), this.getStopDatetime());
    }

    /**
     * 放送開始時刻と放送終了時刻が両方設定されているとき、<br>
     * 放送開始時刻が放送終了時刻以前になっていたら、例外を発生させる。<br>
     * どちらかが設定されていない場合は何もしない。<br>
     *
     * @throws IllegalArgumentException
     */
    private void dateTimeCheck(Date startDateTime, Date stopDateTime) {
        if (startDateTime == null || stopDateTime == null) {
            return;
        }
        if (startDateTime.getTime() >= stopDateTime.getTime()) {
            MessageFormat msg = new MessageFormat("放送開始時刻 {0} => 放送終了時刻 {1}");
            Object[] parameters = {startDateTime, stopDateTime};
            throw new IllegalArgumentException(msg.format(parameters));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Date getStartDatetime() {
        return getCopyDate(this.startDatetime);
    }

    public void setStartDatetime(Date startDatetime) {
        this.dateTimeCheck(startDatetime, this.getStopDatetime());
        this.startDatetime = getCopyDate(startDatetime);
    }

    public Date getStopDatetime() {
        return getCopyDate(stopDatetime);
    }

    public void setStopDatetime(Date stopDatetime) {
        this.dateTimeCheck(this.getStartDatetime(), stopDatetime);
        this.stopDatetime = getCopyDate(stopDatetime);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Channel getChannelId() {
        return channelId;
    }

    public void setChannelId(Channel channelId) {
        this.channelId = channelId;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(17, 31, this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
