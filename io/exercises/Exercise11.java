package io.exercises;

import innerclasses.GreenhouseControls;
import innerclasses.controller.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-15
 * Time: 5:12 PM
 */
public class Exercise11 {
    public static void main(String[] args) {
        GreenhouseControls gc = new GreenhouseControls();
        // Instead of hard-wiring, you could parse
        // configuration information from a text file here:
//        gc.addEvent(gc.new Bell(900));
//        Event[] eventList = {
//                gc.new ThermostatNight(0),
//                gc.new LightOn(200),
//                gc.new LightOff(400),
//                gc.new WaterOn(600),
//                gc.new WaterOff(800),
//                gc.new ThermostatDay(1400)
//        };
//        gc.addEvent(gc.new Restart(2000, eventList));
        Map<String, Long> greenHouseConfig = readGreenHouseConfig("./io/exercises/greenhouse.config");

        Event[] eventList = new Event[greenHouseConfig.size()];
        int i = 0;
        // Make events from map and add to Event array:
        for(Map.Entry<String,Long> me : greenHouseConfig.entrySet()) {
            eventList[i++] = makeEvent(gc, me);
        }
        gc.addEvent(gc.new Restart(2000, eventList));

        if(args.length != 1) {
            System.out.println("Usage: enter integer terminate time");
            System.exit(0);
        }

        if(args.length == 1)
            gc.addEvent(
                    new GreenhouseControls.Terminate(
                            new Integer(args[0])));
        gc.run();
    }

    private static Map<String, Long> readGreenHouseConfig(String filePath) {
        Map<String, Long> result = new LinkedHashMap<String, Long>();
        Properties greenHouseConfig = new Properties();
        try {
            greenHouseConfig.load(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Reading configuration...");
        for(Map.Entry<Object, Object> property: greenHouseConfig.entrySet()) {
            String key = null;
            Long value = null;

            Object strKey = property.getKey();
            if (strKey != null && (strKey instanceof String)) {
                key = (String) strKey;
            }

            Object strValue = property.getValue();
            if (strValue != null && (strValue instanceof String)) {
                value =  Long.decode((String) strValue);
            }

            if (key != null && value != null) {
                result.put(key, value);
                System.out.println(property.getKey() + " : " + property.getValue());
            } else {
                System.out.println("Wrong configuration: \n"
                        + property.getKey() + " : " + property.getValue());
            }

        }
        return result;
    }

    // To build Event objects from Map.Entry objects:
    private static Event makeEvent(GreenhouseControls gc, Map.Entry<String,Long> me) {
        String key = me.getKey();
        Long value = me.getValue();
        if(key.equals("Bell")) return gc.new Bell(value);
        if(key.equals("LightOn")) return gc.new LightOn(value);
        if(key.equals("LightOff")) return gc.new LightOff(value);
        if(key.equals("WaterOn")) return gc.new WaterOn(value);
        if(key.equals("WaterOff")) return gc.new WaterOff(value);
        if(key.equals("ThermostatDay")) return gc.new ThermostatDay(value);
        if(key.equals("ThermostatNight")) return gc.new ThermostatNight(value);
        return null;
    }
}
