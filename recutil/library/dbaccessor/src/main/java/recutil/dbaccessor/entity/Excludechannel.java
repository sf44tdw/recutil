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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import recutil.dbaccessor.manager.EntityManagerMaker;

/**
 * Tableアノテーションのcatalog,schemaについては、MariaDBのCREATE
 * INDEX時にSCHEMA.INDEXNAMEや、SCHEMA.TABLENAMEのようにしてSQLを発行すると不具合が出るので指定しない。
 *
 * @author normal
 */
@Entity
@Table()
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Excludechannel.findAll", query = "SELECT e FROM Excludechannel e")
    ,
    @NamedQuery(name = "Excludechannel.deleteAll", query = "DELETE FROM Excludechannel")})
public class Excludechannel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CHANNEL_ID", nullable = false, length = 20, unique = true)
    private String channelId;

    /**
     * チャンネルテーブルにないチャンネルIDの追加、更新を禁止する。
     * このテーブルへの登録後に、チャンネルテーブルからチャンネルIDが削除されるケースは関知しない。
     * レコードの追加、更新の際に、チャンネルテーブルにないチャンネルIDが送られてきたら、例外を発生させる。
     *
     * @throws IllegalArgumentException
     */
    @PrePersist
    @PreUpdate
    protected void isExistChannelId() {
        try (EntityManagerMaker mk = new EntityManagerMaker()) {
            if (this.channelId == null) {
                return;
            }
            EntityManager man = mk.getEntityManager();
            final TypedQuery<Channel> ql = man.createNamedQuery("Channel.findByChannelId", Channel.class);
            ql.setParameter("channelId", channelId);
            List<Channel> res = ql.getResultList();
            if (res.isEmpty()) {
                MessageFormat msg = new MessageFormat("チャンネルテーブルに登録のないチャンネルID {0}");
                Object[] parameters = {this.channelId};
                throw new IllegalArgumentException(msg.format(parameters));
            }
        }
    }

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
