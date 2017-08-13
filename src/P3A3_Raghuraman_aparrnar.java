
/**
 *
 * Author: aparrnaa
 * Creation Date: 08/05/2017
 * Last Modified Date: 08/05/2017
 * Description: This class reads from a log file, parses it and stores the content in the form of an Arraylist of P3A3_Raghuraman_FileData_aparrnar objects.
 * Allows the user to analyze top 20% commits to files based on quarterly, half yearly and annual time chunks
 * User can either enter the specific year during which he wants to know the commits made or he/she can generate the data for all years present in the log file.
 *
 */
import com.opencsv.CSVWriter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class P3A3_Raghuraman_aparrnar {

    public static ArrayList<P3A3_Raghuraman_FileData_aparrnar> files = new ArrayList<P3A3_Raghuraman_FileData_aparrnar>();
    public static HashMap<String, ArrayList<Date>> authorsAndCommitDates = new HashMap<String, ArrayList<Date>>();

    /*
    * Checks if a file exists or not
    * @param fileName - the name of file
    * @return- true/false based on whether file exists and also checks if the file is a directory or not, since we are looking only at files
     */
    public static boolean checkIfFileExists(String fileName) {
        File f = new File(fileName);
        if (f.exists() && !f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    /*
    * Prints the hashmap
    * @param m - a hashmap consisting of Integer keys and ArrayList of String as the value
    * Uses an Entry object to iterate through HashMap and prints key/value pairs
     */
    public static void printMap(Map<Integer, ArrayList<String>> m) {
        System.out.println("Here");
        for (Map.Entry<Integer, ArrayList<String>> entry : m.entrySet()) {
            // System.out.println("Here!!!!");
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /*
    * Sort entries of a TreeMap by descending order of its values
    * @param h - a hashmap consisting of String keys and Integer as the value
    * @return t- a TreeMap consisting of Integer keys and ArrayList of String as values- sorted in descending order
    * Uses an Entry object to iterate through HashMap and prints key/value pairs
     */
    public static TreeMap<Integer, ArrayList<String>> orderEntriesByValue(TreeMap<String, Integer> h) {
        TreeMap<Integer, ArrayList<String>> t = new TreeMap<Integer, ArrayList<String>>(Collections.reverseOrder()); //descending order
        for (Map.Entry<String, Integer> map : h.entrySet()) {
            if (t.containsKey(map.getValue())) {
                ArrayList<String> str = t.get(map.getValue());
                str.add(map.getKey());
                t.put(map.getValue(), str);
            } else {
                ArrayList<String> str = new ArrayList<String>();
                str.add(map.getKey());
                t.put(map.getValue(), str);
            }
        }
        return t;
    }

    /*
    * Prints the analysis chosen by the user
    * The different analyses that can be performed are:
    * Annual - return the top 20% of users in all the years present in log file or allow the user to choose a year to display the top 20% user commits made
    * Half Yearly - return the top 20% of users in all the years present in log file or allow the user to choose a year to display the top 20% user commits made half-yearly
    * Quarterly- return the top 20% of users in all the years present in log file or allow the user to choose a year to display the top 20% user commits made quarterly
     */
    public static void printStatsToFile() {

        try {
            Scanner input = new Scanner(System.in);
            boolean doAgain = false;

            TreeMap<String, Integer> authorAndCommits = new TreeMap<String, Integer>(); //stores the name of authors and their number of commits made

            //Provide separate files for each category to avoid confusion
            BufferedWriter out = new BufferedWriter(new FileWriter("Result_Annual_AllYears.csv"));
            BufferedWriter out2 = new BufferedWriter(new FileWriter("Result_Annual_SelectedYear.csv"));
            BufferedWriter out3 = new BufferedWriter(new FileWriter("Result_HalfYearly_AllYears.csv"));
            BufferedWriter out4 = new BufferedWriter(new FileWriter("Result_HalfYearly_SelectedYear.csv"));
            BufferedWriter out5 = new BufferedWriter(new FileWriter("Result_Quarterly_AllYears.csv"));
            BufferedWriter out6 = new BufferedWriter(new FileWriter("Result_Quarterly_SelectedYears.csv"));
            CSVWriter writer = null;
            int userChoice;
            int flag = 0;
            while (true) {

                System.out.println("Enter the time period for which you want the data to be analyzed");
                System.out.println("1. Annually \n 2. Half-yearly \n 3.Quarterly \n 4. Exit");
                System.out.println("Enter your choice:");
                String userInput = input.next();
                Calendar date = Calendar.getInstance();
                Calendar fromDate = Calendar.getInstance();
                Calendar toDate = Calendar.getInstance();
                String heading, data;
                String[] splitvals = new String[100];
                String userNames = "";
                userChoice = Integer.parseInt(userInput);
                switch (userChoice) {
                    case 1:
                        do {
                            doAgain = false; //reset counter

                            System.out.println("Do you want to print the log for all the years or for specific year? Press 1 for all years and 2 for specific year");
                            String userVal = input.next();
                            int val = Integer.parseInt(userVal);
                            if (val == 1) { //all years in log
                                writer = new CSVWriter(out);
                                String[] years = new String[]{"2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015"};
                                //   heading = "2001,2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015";

                                //  writer.writeNext(years);
                                for (String year : years) {
                                    try {
                                        authorAndCommits.clear(); //reset hashmap
                                        userNames = ""; //reset variable

                                        userNames = year + ",";

                                        date.setTime(new SimpleDateFormat("yyyy").parse(year)); //check if year entered matches the format
                                        // splitvals[0] = year;
                                        //writer.writeNext(splitvals);
                                        fromDate.setTime(date.getTime());
                                        date.add(Calendar.YEAR, 1); //add a year to set the end date
                                        toDate.setTime(date.getTime());
                                        // System.out.println(fromDate.getTime() + " " + toDate.getTime());
                                        for (Map.Entry<String, ArrayList<Date>> mapEntry : authorsAndCommitDates.entrySet()) {
                                            ArrayList<Date> authorDates = mapEntry.getValue();
                                            for (Date d : authorDates) {
                                                if (d.after(fromDate.getTime()) && d.before(toDate.getTime())) { //check the commits made by the author within the time period
                                                    // System.out.println(d);
                                                    if (authorAndCommits.containsKey(mapEntry.getKey())) {
                                                        authorAndCommits.put(mapEntry.getKey(), authorAndCommits.get(mapEntry.getKey()) + 1);
                                                    } else {
                                                        authorAndCommits.put(mapEntry.getKey(), 1);
                                                    }
                                                }
                                            }
                                        }

                                        Map<Integer, ArrayList<String>> newMap = new TreeMap<Integer, ArrayList<String>>();
                                        newMap = orderEntriesByValue(authorAndCommits); //sort entries by descending order of values to get top 20% of user commits
                                        // System.out.println();
                                        //printMap(newMap);
                                        // System.out.println("treemap size: " + newMap.size());
                                        int boundIndex = (int) Math.ceil(authorAndCommits.size() * 0.2); //calculate the top 20% value
                                        ArrayList<Integer> commitValues = new ArrayList<Integer>(newMap.keySet());
                                        for (int it = 0; it < boundIndex; it++) {
                                            userNames += newMap.get(commitValues.get(it)) + ",";
                                            //System.out.println(newMap.get(commitValues.get(it)));

                                        }
                                        splitvals = userNames.split(",");
                                        writer.writeNext(splitvals); //write this to the file
                                        // System.out.println(authorAndCommits.firstEntry());
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            } else if (val == 2) { //user wants specific year
                                writer = new CSVWriter(out2);

                                System.out.println("Enter the year in yyyy format for which data is to be analyzed in yearly manner");

                                try {
                                    int flag1 = 1;
                                    while (flag1 == 1) {
                                        String year = input.next();
                                        date.setTime(new SimpleDateFormat("yyyy").parse(year)); //check if entered year matches the year format
                                        fromDate.setTime(date.getTime());
                                        date.add(Calendar.YEAR, 1); //add a year to the date to get the end date
                                        toDate.setTime(date.getTime());
                                        //System.out.println(fromDate.getTime() + " " + toDate.getTime());
                                        if (fromDate.get(Calendar.YEAR) < 2001 || fromDate.get(Calendar.YEAR) > 2016) {
                                            System.out.println("Date must be between 2001 and 2016!");

                                            //doAgain = true;
                                        } else {

                                            splitvals[0] = year;
                                            writer.writeNext(splitvals);

                                            flag1 = 0;
                                        }
                                    }

                                    for (Map.Entry<String, ArrayList<Date>> mapEntry : authorsAndCommitDates.entrySet()) {
                                        ArrayList<Date> authorDates = mapEntry.getValue();
                                        for (Date d : authorDates) {
                                            if (d.after(fromDate.getTime()) && d.before(toDate.getTime())) { //check the commits made by the author within the time period
                                                // System.out.println(d);
                                                if (authorAndCommits.containsKey(mapEntry.getKey())) {
                                                    authorAndCommits.put(mapEntry.getKey(), authorAndCommits.get(mapEntry.getKey()) + 1);
                                                } else {
                                                    authorAndCommits.put(mapEntry.getKey(), 1);
                                                }
                                            }
                                        }
                                    }

                                    Map<Integer, ArrayList<String>> newMap = new TreeMap <Integer, ArrayList<String>>();
                                    newMap = orderEntriesByValue(authorAndCommits); //sort entries by descending order of values to get top 20% of user commits
                                    int boundIndex = (int) Math.ceil(authorAndCommits.size() * 0.2); //calculate the value for top 20%
                                    ArrayList<Integer> commitValues = new ArrayList<Integer>(newMap.keySet());
                                    for (int it = 0; it < boundIndex; it++) {
                                        userNames += newMap.get(commitValues.get(it)) + ",";
                                        //   System.out.println(newMap.get(commitValues.get(it)));

                                    }
                                    splitvals = userNames.split(",");
                                    writer.writeNext(splitvals);
                                    // System.out.println(authorAndCommits.firstEntry());
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            } else {
                                System.out.println("Enter either 1 or 2 only");
                                doAgain = true;
                            }
                        } while (doAgain == true);
                        break;

                    case 2:
                        do {
                            doAgain = false; //reset counter
                            System.out.println("Do you want to print the log for all the years or for specific year? Press 1 for all years and 2 for specific year");
                            String userVal = input.next();
                            int val = Integer.parseInt(userVal);
                            if (val == 1) {
                                writer = new CSVWriter(out3);
                                String[] years = new String[]{"2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015"};
                                //   heading = "2001,2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015";

                                //  writer.writeNext(years);
                                for (String year : years) {
                                    for (int period = 0; period < 2; period++) {
                                        try {
                                            authorAndCommits.clear();
                                            userNames = "";
                                            // userNames = year + ",";
                                            date.setTime(new SimpleDateFormat("yyyy").parse(year));
                                            if (period == 0) {
                                                fromDate.setTime(date.getTime());
                                                date.add(Calendar.MONTH, 6);
                                                toDate.setTime(date.getTime());
                                                // System.out.println("1st half: " + fromDate.getTime() + "  " + toDate.getTime());
                                                userNames += "Jan 01 " + year + "- July 01 " + year + ",";
                                            } else {
                                                toDate.add(Calendar.DATE, 1);
                                                fromDate.setTime(toDate.getTime());
                                                toDate.add(Calendar.MONTH, 6);
                                                toDate.setTime(toDate.getTime());

                                                //System.out.println("2nd half: " + fromDate.getTime() + "  " + toDate.getTime());
                                                //  int year2 = Integer.parseInt(year) + 1;
                                                userNames += "Jul 02 " + year + "- Dec 31 " + year + ",";
                                            }
                                            //  System.out.println(fromDate.getTime() + " " + toDate.getTime());

                                            for (Map.Entry<String, ArrayList<Date>> mapEntry : authorsAndCommitDates.entrySet()) {
                                                ArrayList<Date> authorDates = mapEntry.getValue();
                                                for (Date d : authorDates) {
                                                    if (d.after(fromDate.getTime()) && d.before(toDate.getTime())) { //check the commits made by the author within the time period
                                                        //  System.out.println(d);
                                                        if (authorAndCommits.containsKey(mapEntry.getKey())) {
                                                            authorAndCommits.put(mapEntry.getKey(), authorAndCommits.get(mapEntry.getKey()) + 1);
                                                        } else {
                                                            authorAndCommits.put(mapEntry.getKey(), 1);
                                                        }
                                                    }
                                                }
                                            }

                                            Map<Integer, ArrayList<String>> newMap = new TreeMap<Integer, ArrayList<String>>();
                                            newMap = orderEntriesByValue(authorAndCommits); //sort entries by descending order of values to get top 20% of user commits
                                            // System.out.println();
                                            //printMap(newMap);
                                            //  System.out.println("treemap size: " + newMap.size());
                                            int boundIndex = (int) Math.ceil(authorAndCommits.size() * 0.2); //calculate the value for top 20%
                                            ArrayList<Integer> commitValues = new ArrayList<Integer>(newMap.keySet());
                                            for (int it = 0; it < boundIndex; it++) {
                                                userNames += newMap.get(commitValues.get(it)) + ",";
                                                //System.out.println(newMap.get(commitValues.get(it)));

                                            }
                                            splitvals = userNames.split(",");
                                            writer.writeNext(splitvals);
                                            // System.out.println(authorAndCommits.firstEntry());
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                }
                            } else if (val == 2) {
                                writer = new CSVWriter(out4);
                                System.out.println("Enter the year in yyyy format for which data is to be analyzed in half-yearly manner");
                                int flag1 = 1;
                                String year = "";
                                while (flag1 == 1) {
                                    year = input.next();
                                    date.setTime(new SimpleDateFormat("yyyy").parse(year));
                                    fromDate.setTime(date.getTime());
                                    date.add(Calendar.YEAR, 1);
                                    toDate.setTime(date.getTime());
                                    //System.out.println(fromDate.getTime() + " " + toDate.getTime());
                                    if (fromDate.get(Calendar.YEAR) < 2001 || fromDate.get(Calendar.YEAR) > 2016) {
                                        System.out.println("Date must be between 2001 and 2016!");

                                        //doAgain = true;
                                    } else {

                                        splitvals[0] = year;
                                        writer.writeNext(splitvals);

                                        flag1 = 0;
                                    }
                                }

                                for (int period = 0; period < 2; period++) {
                                    try {
                                        authorAndCommits.clear();
                                        userNames = "";
                                        date.setTime(new SimpleDateFormat("yyyy").parse(year));
                                        if (period == 0) {
                                            fromDate.setTime(date.getTime());
                                            date.add(Calendar.MONTH, 6);
                                            toDate.setTime(date.getTime());
                                            // System.out.println("1st half: " + fromDate.getTime() + "  " + toDate.getTime());
                                            userNames += "Jan 01 " + year + "- July 01 " + year + ",";
                                        } else {
                                            toDate.add(Calendar.DATE, 1);
                                            fromDate.setTime(toDate.getTime());
                                            toDate.add(Calendar.MONTH, 6);
                                            toDate.setTime(toDate.getTime());

                                            // System.out.println("2nd half: " + fromDate.getTime() + "  " + toDate.getTime());
                                            userNames += "Jul 02 " + year + "- Dec 31 " + year + ",";
                                        }
                                        //  System.out.println(fromDate.getTime() + " " + toDate.getTime());
                                        if (fromDate.get(Calendar.YEAR) < 2001 || fromDate.get(Calendar.YEAR) > 2016) {
                                            System.out.println("Date must be between 2001 and 2016! Please Enter Again!");
                                            doAgain = true;
                                        }

                                        for (Map.Entry<String, ArrayList<Date>> mapEntry : authorsAndCommitDates.entrySet()) {
                                            ArrayList<Date> authorDates = mapEntry.getValue();
                                            for (Date d : authorDates) {
                                                if (d.after(fromDate.getTime()) && d.before(toDate.getTime())) { //check the commits made by the author within the time period
                                                    //   System.out.println(d);
                                                    if (authorAndCommits.containsKey(mapEntry.getKey())) {
                                                        authorAndCommits.put(mapEntry.getKey(), authorAndCommits.get(mapEntry.getKey()) + 1);
                                                    } else {
                                                        authorAndCommits.put(mapEntry.getKey(), 1);
                                                    }
                                                }
                                            }
                                        }

                                        Map<Integer, ArrayList<String>> newMap = new TreeMap<Integer, ArrayList<String>>();
                                        newMap = orderEntriesByValue(authorAndCommits); //sort entries by descending order of values to get top 20% of user commits
                                        // System.out.println();
                                        //printMap(newMap);
                                        // System.out.println("treemap size: " + newMap.size());
                                        int boundIndex = (int) Math.ceil(authorAndCommits.size() * 0.2); //calculate the value for top 20%
                                        ArrayList<Integer> commitValues = new ArrayList<Integer>(newMap.keySet());
                                        for (int it = 0; it < boundIndex; it++) {
                                            userNames += newMap.get(commitValues.get(it)) + ",";
                                            // System.out.println(newMap.get(commitValues.get(it)));

                                        }
                                        splitvals = userNames.split(",");
                                        writer.writeNext(splitvals);
                                        // System.out.println(authorAndCommits.firstEntry());
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            } else {
                                System.out.println("Enter either 1 or 2 only");
                                doAgain = true;
                            }
                        } while (doAgain == true);
                        break;
                    case 3:
                        do {
                            doAgain = false; //reset counter
                            System.out.println("Do you want to print the log for all the years or for specific year? Press 1 for all years and 2 for specific year");
                            String userVal = input.next();
                            int val = Integer.parseInt(userVal);
                            if (val == 1) {
                                writer = new CSVWriter(out5);
                                String[] years = new String[]{"2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015"};

                                for (String year : years) {
                                    for (int period = 0; period < 4; period++) {
                                        try {
                                            authorAndCommits.clear();
                                            userNames = "";
                                            // userNames = year + ",";

                                            date.setTime(new SimpleDateFormat("yyyy").parse(year));
                                            //1 January – 31 March - Quarter 1
                                            if (period == 0) {
                                                fromDate.setTime(date.getTime());
                                                toDate.set(Integer.parseInt(year), 2, 31);
                                                //   System.out.println("1st half: " + fromDate.getTime() + "  " + toDate.getTime());
                                                userNames += "Jan 01 " + year + "- Mar 31 " + year + ",";
                                            } //Quarter 2- 1 April – 30 June
                                            else if (period == 1) {
                                                fromDate.set(Integer.parseInt(year), 3, 1);
                                                toDate.set(Integer.parseInt(year), 5, 30);
                                                // System.out.println("2nd half: " + fromDate.getTime() + "  " + toDate.getTime());
                                                userNames += "Apr 01 " + year + "- Jun 30 " + year + ",";
                                            } //Quarter 3- 1 July – 30 September
                                            else if (period == 2) {
                                                fromDate.set(Integer.parseInt(year), 6, 1);
                                                toDate.set(Integer.parseInt(year), 8, 30);
                                                //System.out.println("3rd half: " + fromDate.getTime() + "  " + toDate.getTime());
                                                userNames += "Jul 01 " + year + "- Sep 30 " + year + ",";
                                            } //Quarter 4- 1 October – 31 December
                                            else if (period == 3) {
                                                fromDate.set(Integer.parseInt(year), 9, 1);
                                                toDate.set(Integer.parseInt(year) + 1, 11, 31);
                                                //  System.out.println("3rd half: " + fromDate.getTime() + "  " + toDate.getTime());
                                                userNames += "Oct 01 " + year + "- Dec 31 " + year + ",";

                                            }
                                            //  System.out.println(fromDate.getTime() + " " + toDate.getTime());
                                            if (fromDate.get(Calendar.YEAR) < 2001 || fromDate.get(Calendar.YEAR) > 2015) {
                                                System.out.println("Date must be between 2001 and 2016! Please Enter Again!");
                                                doAgain = true;
                                            }

                                            for (Map.Entry<String, ArrayList<Date>> mapEntry : authorsAndCommitDates.entrySet()) {
                                                ArrayList<Date> authorDates = mapEntry.getValue();
                                                for (Date d : authorDates) {
                                                    if (d.after(fromDate.getTime()) && d.before(toDate.getTime())) { //check the commits made by the author within the time period
                                                        //  System.out.println(d);
                                                        if (authorAndCommits.containsKey(mapEntry.getKey())) {
                                                            authorAndCommits.put(mapEntry.getKey(), authorAndCommits.get(mapEntry.getKey()) + 1);
                                                        } else {
                                                            authorAndCommits.put(mapEntry.getKey(), 1);
                                                        }
                                                    }
                                                }
                                            }

                                            Map<Integer, ArrayList<String>> newMap = new TreeMap <Integer, ArrayList<String>>();
                                            newMap = orderEntriesByValue(authorAndCommits); //sort entries by descending order of values to get top 20% of user commits
                                            // System.out.println();
                                            //  printMap(newMap);
                                            //   System.out.println("treemap size: " + newMap.size());
                                            int boundIndex = (int) Math.ceil(authorAndCommits.size() * 0.2); //calculate the value for top 20%
                                            ArrayList<Integer> commitValues = new ArrayList<Integer>(newMap.keySet());
                                            for (int it = 0; it < boundIndex; it++) {
                                                userNames += newMap.get(commitValues.get(it)) + ",";
                                                // System.out.println(newMap.get(commitValues.get(it)));

                                            }
                                            splitvals = userNames.split(",");
                                            writer.writeNext(splitvals);
                                            // System.out.println(authorAndCommits.firstEntry());
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                }
                            } else if (val == 2) {
                                writer = new CSVWriter(out6);
                                System.out.println("Enter the year in yyyy format for which data is to be analyzed in quarterly manner");
                                String year = "";
                                int flag1 = 1;

                                while (flag1 == 1) {
                                    year = input.next();
                                    date.setTime(new SimpleDateFormat("yyyy").parse(year));
                                    fromDate.setTime(date.getTime());
                                    date.add(Calendar.YEAR, 1);
                                    toDate.setTime(date.getTime());
                                    //System.out.println(fromDate.getTime() + " " + toDate.getTime());
                                    if (fromDate.get(Calendar.YEAR) < 2001 || fromDate.get(Calendar.YEAR) > 2016) {
                                        System.out.println("Date must be between 2001 and 2016! Please Enter Again!");

                                        //doAgain = true;
                                    } else {

                                        splitvals[0] = year;
                                        writer.writeNext(splitvals);

                                        flag1 = 0;
                                    }
                                }

                                for (int period = 0; period < 4; period++) {
                                    try {
                                        authorAndCommits.clear();
                                        userNames = "";
                                        date.setTime(new SimpleDateFormat("yyyy").parse(year));
                                        if (period == 0) {
                                            fromDate.setTime(date.getTime());
                                            toDate.set(Integer.parseInt(year), 2, 31);
                                            //   System.out.println("1st half: " + fromDate.getTime() + "  " + toDate.getTime());
                                            userNames += "Jan 01 " + year + "- Mar 31 " + year + ",";
                                        } //Quarter 2- 1 April – 30 June
                                        else if (period == 1) {
                                            fromDate.set(Integer.parseInt(year), 3, 1);
                                            toDate.set(Integer.parseInt(year), 5, 30);

                                            userNames += "Apr 01 " + year + "- Jun 30 " + year + ",";
                                        } //Quarter 3- 1 July – 30 September
                                        else if (period == 2) {
                                            fromDate.set(Integer.parseInt(year), 6, 1);
                                            toDate.set(Integer.parseInt(year), 8, 30);
                                            userNames += "Jul 01 " + year + "- Sep 30 " + year + ",";
                                        } //Quarter 4- 1 October – 31 December
                                        else if (period == 3) {
                                            fromDate.set(Integer.parseInt(year), 9, 1);
                                            toDate.set(Integer.parseInt(year) + 1, 11, 31);

                                            userNames += "Oct 01 " + year + "- Dec 31 " + year + ",";

                                        }

                                        if (fromDate.get(Calendar.YEAR) < 2001 || fromDate.get(Calendar.YEAR) > 2016) {
                                            System.out.println("Date must be between 2001 and 2016!");
                                            doAgain = true;
                                        }

                                        for (Map.Entry<String, ArrayList<Date>> mapEntry : authorsAndCommitDates.entrySet()) {
                                            ArrayList<Date> authorDates = mapEntry.getValue();
                                            for (Date d : authorDates) {
                                                if (d.after(fromDate.getTime()) && d.before(toDate.getTime())) { //check the commits made by the author within the time period
                                                    //   System.out.println(d);
                                                    if (authorAndCommits.containsKey(mapEntry.getKey())) {
                                                        authorAndCommits.put(mapEntry.getKey(), authorAndCommits.get(mapEntry.getKey()) + 1);
                                                    } else {
                                                        authorAndCommits.put(mapEntry.getKey(), 1);
                                                    }
                                                }
                                            }
                                        }

                                        Map<Integer, ArrayList<String>> newMap = new TreeMap<Integer, ArrayList<String>>();
                                        newMap = orderEntriesByValue(authorAndCommits); //sort entries by descending order of values to get top 20% of user commits
                                        // System.out.println();
                                        //printMap(newMap);
                                        // System.out.println("treemap size: " + newMap.size());
                                        int boundIndex = (int) Math.ceil(authorAndCommits.size() * 0.2); //calculate the value for top 20%
                                        ArrayList<Integer> commitValues = new ArrayList<Integer>(newMap.keySet());
                                        for (int it = 0; it < boundIndex; it++) {
                                            userNames += newMap.get(commitValues.get(it)) + ",";
                                            // System.out.println(newMap.get(commitValues.get(it)));

                                        }
                                        splitvals = userNames.split(",");
                                        writer.writeNext(splitvals);
                                        // System.out.println(authorAndCommits.firstEntry());
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            } else {
                                System.out.println("Enter either 1 or 2 only");
                                doAgain = true;
                            }
                        } while (doAgain == true);
                        break;
                    case 4:
                        System.out.println("Exiting..");
                        flag = 0;
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Enter only options from 1-4");
                        flag = 0;
                       break;
                }
                writer.close(); //close writer after write completes
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    * read a log file to parse values
    * @param fileName - a file containing logs of commits and revisions made to several files
    * Uses buffered reader to read every line of the file and process it, based on a specific format
     */
    public static void parseFile(String fileName) {
        P3A3_Raghuraman_FileData_aparrnar obj = new P3A3_Raghuraman_FileData_aparrnar();
        String line;
        boolean setLastCommitFlag = false, setFirstCommitFlag = false;
        int objectCounter = 0, lineCounter = 0;
        String[] lineSplit;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
                //  System.out.println("Line "+(++lineCounter)+":"+line);
                if (line.startsWith("Working file")) { //update filename
                    lineSplit = line.split(":");
                    //  System.out.println("Array Size: "+files.size());
                    obj = new P3A3_Raghuraman_FileData_aparrnar();
                    setLastCommitFlag = false;
                    setFirstCommitFlag = false;
                    obj.setFileName(lineSplit[1]);
                    //System.out.println(lineSplit[1]);
                } else if (line.contains("total revisions")) { //update total revisions
                    lineSplit = line.split(";");
                    String[] values = lineSplit[0].split(":");
                    obj.setTotalRevisions(Integer.parseInt(values[1].trim()));
                    String[] values2 = lineSplit[1].split(":");
                    obj.setSelectedRevisions(Integer.parseInt(values2[1].trim()));
                    // System.out.println(values2[1]); 
                } else if (line.startsWith("revision")) { //update revision flags for first and last commit
                    lineSplit = line.split("\\s");
                    if (obj.selectedRevisions == 1) {
                        setLastCommitFlag = true;
                        setFirstCommitFlag = true;
                    }
                    if (lineSplit[1].endsWith(Integer.toString(obj.selectedRevisions))) {
                        setLastCommitFlag = true;
                    } else if (lineSplit[1].endsWith(Integer.toString(1))) {
                        setFirstCommitFlag = true;
                    }
                } else if (line.startsWith("date")) { //update revision time- earliest and last commit 
                    lineSplit = line.split("\\s");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d = null;
                    String date = lineSplit[1] + " " + lineSplit[2];
                    d = dateFormat.parse(date);

                    if (setFirstCommitFlag) {
                        obj.setFirstCommit(d);
                    }
                    if (setLastCommitFlag) {
                        obj.setLastCommit(d);
                    }

                    lineSplit = line.split(";");
                    String[] value = lineSplit[1].split(":");
                    String author = value[1];
                    if (obj.userCommits.containsKey(author)) { //update commits made by each author within a particular file
                        obj.userCommits.put(author, obj.userCommits.get(author) + 1);
                    } else {
                        obj.userCommits.put(author, 1);
                    }

                    if (authorsAndCommitDates.containsKey(author)) { //update the dates commited by each author
                        ArrayList<Date> values = authorsAndCommitDates.get(author);
                        values.add(d);
                        authorsAndCommitDates.put(author, values);
                    } else {
                        ArrayList<Date> value1 = new ArrayList<Date>();
                        value1.add(d);
                        authorsAndCommitDates.put(author, value1);
                    }
                } else if (line.contains("====")) { //end of file data
                    objectCounter++;
                    files.add(obj);
                }
            }

            printStatsToFile(); //print details to a file

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception er) {
            System.out.println(er.getMessage());
        }
    }

    /*
    * The main function takes a file name as an input, checks if the file exists and then calls the parseFile function to parse the file contents.
    * @param args[]- an array of strings
     */
    public static void main(String[] args) {
        boolean doesFileExist = false;
        boolean doAgain = false;
        Scanner input = new Scanner(System.in);
        System.out.println("----------------------------");
        System.out.println("Log File Parsing");
        System.out.println("----------------------------");
        do {
            System.out.println("Enter the filename to be parsed along with the extension");
            String fileName = input.nextLine();
            doesFileExist = checkIfFileExists(fileName);
            if (doesFileExist == false) {
                System.out.println("File does not exist! Please enter a valid file name! ");
                doAgain = true;
            } else {
                parseFile(fileName);
            }
        } while (doAgain == true);

    }
}
