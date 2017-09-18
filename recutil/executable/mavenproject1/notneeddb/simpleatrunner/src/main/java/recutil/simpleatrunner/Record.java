/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.simpleatrunner;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author normal
 */
@CsvEntity
public class Record {

    @CsvColumn(name = "reserveDateTime")
    private String reserveDateTime;

    @CsvColumn(name = "command")
    private String command;

    public Record() {
    }


    public Record(String reserveDateTime, String command) {
        this.reserveDateTime = reserveDateTime;
        this.command = command;
    }

    public synchronized String getReserveDateTime() {
        return this.reserveDateTime;
    }

    public synchronized void setReserveDateTime(String reserveDateTime) {
        this.reserveDateTime = reserveDateTime;
    }

    public synchronized String getCommand() {
        return command;
    }

    public synchronized void setCommand(String command) {
        this.command = command;
    }

    @Override
    public synchronized int hashCode() {
        return HashCodeBuilder.reflectionHashCode(5, 59, this);
    }

    /**
     *
     * @return 保持している値がすべて等しければtrue
     */
    @Override
    public synchronized boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public synchronized String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
