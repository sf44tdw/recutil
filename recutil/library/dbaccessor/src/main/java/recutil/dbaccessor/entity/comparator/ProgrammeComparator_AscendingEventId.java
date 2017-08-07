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
package recutil.dbaccessor.entity.comparator;

import java.util.Comparator;
import recutil.dbaccessor.entity.Programme;

/**
 *
 * @author normal
 */
public class ProgrammeComparator_AscendingEventId implements Comparator<Programme> {

//    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    @Override
    public int compare(Programme o1, Programme o2) {
        return o1.getEventId()-o2.getEventId();
    }

}
