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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/*
Tableアノテーションのcatalog,schemaについては、MariaDBのCREATEINDEX時にSCHEMA.INDEXNAMEや、SCHEMA.TABLENAMEのようにしてSQLを発行すると不具合が出るので指定しない。
(@Indexでインデックスを設定してもテーブル生成時に無視される。)

紐付けを遅延モードで行うと例外が発生するので、しない。
 */
/**
 *
 * @author normal
 */
@Entity
@Table(name = "EXCLUDECHANNEL", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CHANNEL_ID"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Excludechannel.findAll", query = "SELECT e FROM Excludechannel e")
    , @NamedQuery(name = "Excludechannel.findByChannelId", query = "SELECT e FROM Excludechannel e WHERE e.channelId = :channelId")
    , @NamedQuery(name = "Excludechannel.deleteAll", query = "DELETE FROM Excludechannel")})
public class Excludechannel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CHANNEL_ID", nullable = false, length = 20)
    private String channelId;
    @JoinColumn(name = "CHANNEL_ID", referencedColumnName = "CHANNEL_ID", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Channel channel;

    public Excludechannel() {
    }

    public Excludechannel(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(29, 149, this);
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
