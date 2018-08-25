/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.manager.SelectedPersistenceName;

/**
 * 除外チャンネル登録テーブル。番組情報更新時にここの内容を除外チャンネルテーブルに転記する。
 *
 * @author normal
 */
@Entity
@Table(name = "TEMP_EXCLUDECHANNEL", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CHANNEL_ID"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TempExcludechannel.findAll", query = "SELECT t FROM TempExcludechannel t")
    , @NamedQuery(name = "TempExcludechannel.findByChannelId", query = "SELECT t FROM TempExcludechannel t WHERE t.channelId = :channelId")
    , @NamedQuery(name = "TempExcludechannel.deleteAll", query = "DELETE FROM TempExcludechannel")})
public class TempExcludechannel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CHANNEL_ID", nullable = false, length = 20)
    private String channelId;

    public TempExcludechannel() {
    }

    public TempExcludechannel(String channelId) {
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
