package annotations.exercises;

import annotations.database.Constraints;
import annotations.database.DBTable;
import annotations.database.SQLInteger;
import annotations.database.SQLString;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Exercise1 {
    public static void main(String[] args) throws Exception {
        TableCreator1.main(new String[]{ Worker1.class.getCanonicalName() });
    }
}




class TableCreator1 {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("arguments: annotated classes");
            System.exit(0);
        }
        for (String className : args) {
            Class<?> cl = Class.forName(className);
            DBTable dbTable = cl.getAnnotation(DBTable.class);
            if (dbTable == null) {
                System.out.println(
                        "No DBTable annotations in class " + className);
                continue;
            }
            String tableName = dbTable.name();
            // If the name is empty, use the Class name:
            if (tableName.length() < 1)
                tableName = cl.getName().toUpperCase();
            List<String> columnDefs = new ArrayList<String>();
            for (Field field : cl.getDeclaredFields()) {
                String columnName = null;
                Annotation[] anns = field.getDeclaredAnnotations();
                if (anns.length < 1)
                    continue; // Not a db table column

                if (anns[0] instanceof SQLInteger) {
                    SQLInteger sInt = (SQLInteger) anns[0];
                    // Use field name if name not specified
                    if (sInt.name().length() < 1)
                        columnName = field.getName().toUpperCase();
                    else
                        columnName = sInt.name();
                    columnDefs.add(columnName + " INT" +
                            getConstraints(sInt.constraints()));
                }

                if (anns[0] instanceof SQLString) {
                    SQLString sString = (SQLString) anns[0];
                    // Use field name if name not specified.
                    if (sString.name().length() < 1)
                        columnName = field.getName().toUpperCase();
                    else
                        columnName = sString.name();
                    columnDefs.add(columnName + " VARCHAR(" +
                            sString.value() + ")" +
                            getConstraints(sString.constraints()));
                }

                if(anns[0] instanceof SQLNumber1) {
                    SQLNumber1 sNumber = (SQLNumber1) anns[0];

                    if (sNumber.name().isEmpty())
                        columnName = field.getName().toUpperCase();
                    else
                        columnName = sNumber.name();
                    columnDefs.add(String.format("%s NUMBER(%d, %d) %s",
                            columnName,
                            sNumber.scale(),
                            sNumber.precision(),
                            getConstraints(sNumber.constraints())
                    ));
                }
            }

            StringBuilder createCommand = new StringBuilder(
                    "CREATE TABLE " + tableName + "(");
            for (String columnDef : columnDefs)
                createCommand.append("\n    " + columnDef + ",");
            // Remove trailing comma
            String tableCreate = createCommand.substring(
                    0, createCommand.length() - 1) + ");";
            System.out.println("Table Creation SQL for " +
                    className + " is :\n" + tableCreate);
        }
    }

    private static String getConstraints(Constraints con) {
        String constraints = "";
        if (!con.allowNull())
            constraints += " NOT NULL";
        if (con.primaryKey())
            constraints += " PRIMARY KEY";
        if (con.unique())
            constraints += " UNIQUE";
        return constraints;
    }
}
