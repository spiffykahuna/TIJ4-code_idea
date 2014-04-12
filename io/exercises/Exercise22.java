package io.exercises;

import net.mindview.util.OSExecute;
import net.mindview.util.OSExecuteException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static net.mindview.util.PPrint.pformat;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-16
 * Time: 2:40 PM
 */
public class Exercise22 {
    public static List<String> command(String command) {
        List<String> commandResult = new LinkedList<String>();
        boolean err = false;
        try {
            Process process =
                    new ProcessBuilder(command.split(" ")).start();
            BufferedReader results = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String s;
            while((s = results.readLine())!= null)
                commandResult.add(s);
            BufferedReader errors = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            // Report errors and return nonzero value
            // to calling process if there are problems:
            while((s = errors.readLine())!= null) {
                System.err.println(s);
                err = true;
            }
        } catch(Exception e) {
            // Compensate for Windows 2000, which throws an
            // exception for the default command line:
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                if(!command.startsWith("CMD /C"))
                    command("CMD /C " + command);
                else
                    throw new RuntimeException(e);
            } else
                throw new RuntimeException(e);


        }
        if(err)
            throw new OSExecuteException("Errors executing " +
                    command);

        return commandResult;
    }

    public static void main(String[] args) {
        //System.out.println("Execution results:\n" + Exercise22.command("abrakadabra"));
        System.out.println("Execution results:\n" + pformat(Exercise22.command("ls -lash")));
    }
}
