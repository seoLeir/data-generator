package org.bitpioneers.data;

import lombok.Data;


/**
* The DepartmentInfo class is used to encapsulate information related to a bank's branch or department.
 * It contains various fields that describe the characteristics of a specific branch, such as its location,
 * schedule, and special features. This class is an integral part of the data model when working with bank department data.
*  @since 1.0
 * @author Mirolim Mirzayev
*/
@Data
public class DepartmentInfo {
    /**
     * A unique identifier associated with the department.
    */
    Long Biskvit_id;

    /**
    * The physical address of the department.
    */
    String address;

    /**
    * The city in which the department is located.
    */
    String city;
    /**
     * An instance of the Geo class, containing latitude and longitude coordinates of the department's location.
     * @see Geo
     */
    Geo coordinates;

    /**
     * A unique identifier for the department.
     */
    Long id;

    /**
     * The schedule for physical individuals.
     */
    String scheduleFl;

    /**
     * The schedule for juridical entities.
     */
    String scheduleJurL;

    /**
     *  A shortened name or alias for the department.
     */
    String shortName;

    /**
     * An instance of the SpecialDepartmentInfo class, representing special features or attributes of the department.
     * @see SpecialDepartmentInfo
     */
    SpecialDepartmentInfo special;
}
