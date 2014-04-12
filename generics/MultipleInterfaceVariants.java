//: generics/MultipleInterfaceVariants.java
package generics; /* Added by Eclipse.py */
// {CompileTimeError} (Won't compile)

interface Payable<T> {}

class Employee implements Payable<Employee2> {}
class Hourly extends Employee2
  implements Payable<Hourly> {} ///:~
