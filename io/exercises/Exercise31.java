package io.exercises;

import nu.xom.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2013-12-16
 * Time: 12:18 AM
 */


class Person {
    private String first, last, address;
    public Person(String first, String last, String address) {
        this.first = first;
        this.last = last;
        this.address = address;
    }
    // Produce an XML Element from this Person object:
    public Element getXML() {
        Element person = new Element("person");

        Element firstName = new Element("first");
        firstName.appendChild(first);

        Element lastName = new Element("last");
        lastName.appendChild(last);

        Element address = new Element("address");
        address.appendChild(this.address);

        person.appendChild(firstName);
        person.appendChild(lastName);
        person.appendChild(address);
        return person;
    }
    // Constructor to restore a Person from an XML Element:
    public Person(Element person) {
        first= person.getFirstChildElement("first").getValue();
        last = person.getFirstChildElement("last").getValue();
        address = person.getFirstChildElement("address").getValue();
    }
    public String toString() { return first + " " + last; }
    // Make it human-readable:
    public static void format(OutputStream os, Document doc) throws Exception {
        Serializer serializer= new Serializer(os,"ISO-8859-1");
        serializer.setIndent(4);
        serializer.setMaxLength(60);
        serializer.write(doc);
        serializer.flush();
    }
    public static void main(String[] args) throws Exception {
        List<Person> people = Arrays.asList(
                new Person("Dr. Bunsen", "Honeydew", "Red square"),
                new Person("Gonzo", "The Great", "Big Ben"),
                new Person("Phillip J.", "Fry", "Statue of liberty"));

        System.out.println(people);

        Element root = new Element("people");
        for(Person p : people)
            root.appendChild(p.getXML());

        Document doc = new Document(root);
        format(System.out, doc);
        format(new BufferedOutputStream(new FileOutputStream(
                "People.xml")), doc);
    }
} /* Output:
[Dr. Bunsen Honeydew, Gonzo The Great, Phillip J. Fry]
<?xml version="1.0" encoding="ISO-8859-1"?>
<people>
    <person>
        <first>Dr. Bunsen</first>
        <last>Honeydew</last>
    </person>
    <person>
        <first>Gonzo</first>
        <last>The Great</last>
    </person>
    <person>
        <first>Phillip J.</first>
        <last>Fry</last>
    </person>
</people>
*///:~

class People extends ArrayList<xml.Person> {
    public People(String fileName) throws Exception  {
        Document doc = new Builder().build(fileName);
        Elements elements =
                doc.getRootElement().getChildElements();
        for(int i = 0; i < elements.size(); i++)
            add(new xml.Person(elements.get(i)));
    }
    public static void main(String[] args) throws Exception {
        People p = new People("People.xml");
        System.out.println(p);
    }
} /* Output:
[Dr. Bunsen Honeydew, Gonzo The Great, Phillip J. Fry]
*///:~

public class Exercise31 {
    public static void main(String[] args) throws Exception {
        Person.main(args);
        People.main(args);
    }
}
