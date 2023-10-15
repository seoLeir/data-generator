package org.bitpioneers.data;

import lombok.Data;

/**
 * The Geo class is primarily used as a subcomponent within the DepartmentInfo class. It is employed to store precise
 * geographical coordinates representing the location of a bank department. These coordinates are essential for mapping
 * and location-based services within a banking application.
 *
 * @see DepartmentInfo
 * @since 1.0
 * @author Mirolim Mirzayev
*/
@Data
public class Geo {
    /**
    * The latitude coordinate of the department's location.
    */
    Double latitude;

    /**
     * The longitude coordinate of the department's location.
     */
    Double longitude;
}
