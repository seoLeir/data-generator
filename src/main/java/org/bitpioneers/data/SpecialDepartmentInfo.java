package org.bitpioneers.data;

import lombok.Data;


/**
* The SpecialDepartmentInfo class is employed to represent special attributes or features associated with a bank
 * department. These attributes indicate specific characteristics of the department, such as whether it serves prime
 * customers, juridical entities, or individuals, and whether it offers features like ramps or VIP services.
*  @see DepartmentInfo
 *  @since 1.0
 *  @author Mirolim Mirzayev
*/
@Data
public class SpecialDepartmentInfo {
    /**
    * Indicates whether the department serves prime customers
    */
    boolean prime;

    /**
    * Indicates whether the department caters to juridical entities.
    */
    boolean juridical;

    /**
    *  Indicates whether the department provides services to individuals.
    */
    boolean person;

    /**
    * Indicates whether the department is equipped with ramps for accessibility.
    */
    boolean ramp;

    /**
    * Indicates whether the department offers VIP office services.
    */
    boolean vipOffice;

    /**
    *  Indicates whether the department features a VIP zone.
    */
    boolean vipZone;
}
