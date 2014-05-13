package annotations.exercises;

import annotations.database.Constraints;
import annotations.database.DBTable;
import annotations.database.SQLInteger;
import annotations.database.SQLString;

@DBTable(name = "WORKER")
public class Worker1 {
    @SQLString(30) String firstName;
    @SQLString(50) String lastName;
    @SQLInteger
    Integer age;

    @SQLString(value = 30,
            constraints = @Constraints(primaryKey = true))
    String handle;

    @SQLNumber1(scale = 5, precision = 2, constraints = @Constraints(allowNull = false))
    Double salary;
    static int memberCount;
    public String getHandle() { return handle; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String toString() { return handle; }
    public Integer getAge() { return age; }

    public Double getSalary() {
        return salary;
    }
} ///:~