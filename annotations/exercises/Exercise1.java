package annotations.exercises;

import annotations.database.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@DBTable(name = "worker")
public class Exercise1 {
    @SQLString(30) String firstName;
    @SQLString(50) String lastName;

    @SQLInteger
    Integer age;

    @SQLNumber
    Double salary;

    @SQLString(value = 30,
            constraints = @Constraints(primaryKey = true))
    String handle;

    static int memberCount;
    public String getHandle() { return handle; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String toString() { return handle; }
    public Integer getAge() { return age; }

    public Double getSalary() { return salary;}

    public static void main(String[] args) throws ClassNotFoundException {

        Class<?> cl = Exercise1.class;
        DBTable dbTable = cl.getAnnotation(DBTable.class);

        if(dbTable == null) {
            System.out.println(
                    "No DBTable annotations in class " + cl);
            return;
        }

        String tableName = dbTable.name();
        // If the name is empty, use the Class name:
        if(tableName.length() < 1)
            tableName = cl.getName().toUpperCase();

        List<String> columnDefs = new ArrayList<String>();

        for(Field field : cl.getDeclaredFields()) {
            String columnName = null;
            Annotation[] anns = field.getDeclaredAnnotations();
            if(anns.length < 1)
                continue; // Not a db table column
            if(anns[0] instanceof SQLInteger) {
                SQLInteger sInt = (SQLInteger) anns[0];
                // Use field name if name not specified
                if(sInt.name().length() < 1)
                    columnName = field.getName().toUpperCase();
                else
                    columnName = sInt.name();
                columnDefs.add(columnName + " INT" +
                        getConstraints(sInt.constraints()));
            }
            if(anns[0] instanceof SQLString) {
                SQLString sString = (SQLString) anns[0];
                // Use field name if name not specified.
                if(sString.name().length() < 1)
                    columnName = field.getName().toUpperCase();
                else
                    columnName = sString.name();
                columnDefs.add(columnName + " VARCHAR(" +
                        sString.value() + ")" +
                        getConstraints(sString.constraints()));
            }
            if(anns[0] instanceof SQLNumber) {
                SQLNumber numField = (SQLNumber) anns[0];
                // Use field name if name not specified.
                if(numField.name().length() < 1)
                    columnName = field.getName().toUpperCase();
                else
                    columnName = numField.name();
                columnDefs.add(columnName + " NUMBER(" +
                        numField.scale() + "," + numField.precision()+ ")" +
                        getConstraints(numField.constraints()));
            }
            StringBuilder createCommand = new StringBuilder(
                    "CREATE TABLE " + tableName + "(");
            for(String columnDef : columnDefs)
                createCommand.append("\n    " + columnDef + ",");
            // Remove trailing comma
            String tableCreate = createCommand.substring(
                    0, createCommand.length() - 1) + ");";
            System.out.println("Table Creation SQL for " +
                    cl + " is :\n" + tableCreate);
        }

    }

    private static String getConstraints(Constraints con) {
        String constraints = "";
        if(!con.allowNull())
            constraints += " NOT NULL";
        if(con.primaryKey())
            constraints += " PRIMARY KEY";
        if(con.unique())
            constraints += " UNIQUE";
        return constraints;
    }
}
