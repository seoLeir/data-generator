package org.bitpioneers.data;

import lombok.Data;

import java.util.List;

/**
* The BranchesInfo class represents a data structure used to encapsulate a list of DepartmentInfo objects.
 * It serves as a container for department information, making it easier to serialize and deserialize JSON data.
 * This class is typically utilized when dealing with a collection of department details in the context of a
 * VTB banking application.
* @since 1.0
 * @author Mirolim Mirzayev
* */
@Data
public class BranchesInfo {
    List<DepartmentInfo> branches;
}
