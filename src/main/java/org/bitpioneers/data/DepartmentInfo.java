package org.bitpioneers.data;

import lombok.Data;

@Data
public class DepartmentInfo {
    Long Biskvit_id;
    String address;
    String city;
    Geo coordinates;
    Long id;
    String scheduleFl;
    String scheduleJurL;
    String shortName;
    SpecialDepartmentInfo special;
}
