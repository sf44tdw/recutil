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
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Tableアノテーションのcatalog,schemaについては、MariaDBのCREATE
 * INDEX時にSCHEMA.INDEXNAMEや、SCHEMA.TABLENAMEのようにしてSQLを発行すると不具合が出るので指定しない。
 * @author normal
 */
@Entity
@Table( uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CHANNEL_ID", "CHANNEL_NO"})}, indexes = {
    @Index(name = "CHANNEL_NO_I", columnList = "CHANNEL_NO")})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Channel.findAll", query = "SELECT c FROM Channel c")
    ,
    @NamedQuery(name = "Channel.findByChannelId", query = "SELECT c FROM Channel c WHERE c.channelId = :channelId")
    ,
    @NamedQuery(name = "Channel.findByChannelNo", query = "SELECT c FROM Channel c WHERE c.channelNo = :channelNo")
    ,
    @NamedQuery(name = "Channel.findByAllParams", query = "SELECT c FROM Channel c WHERE c.channelId = :channelId AND c.channelNo = :channelNo AND c.displayName = :displayName")
    ,
    @NamedQuery(name = "Channel.deleteAll", query = "DELETE FROM Channel")
})
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CHANNEL_ID", nullable = false, length = 20)
    private String channelId;
    @Basic(optional = false)
    @Column(name = "CHANNEL_NO", nullable = false)
    private int channelNo;
    @Lob
    @Column(name = "DISPLAY_NAME", length = 65535)
    private String displayName;

    /**
     * 番組情報との紐付けを遅延モードで行うと例外が発生するので、しないでおく。
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channelId")
    private Collection<Programme> programmeCollection;

    public Channel() {
    }

    public Channel(String channelId) {
        this.channelId = channelId;
    }

    public Channel(String channelId, int channelNo) {
        this.channelId = channelId;
        this.channelNo = channelNo;
    }

    public int getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @XmlTransient
    public Collection<Programme> getProgrammeCollection() {
        return programmeCollection;
    }

    public void setProgrammeCollection(Collection<Programme> programmeCollection) {
        this.programmeCollection = programmeCollection;
    }

    @Version
    private int version;

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 43, this);
    }

    /**
     * 保持している値がすべて等しければtrue
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
