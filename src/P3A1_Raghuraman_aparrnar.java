
/**
 *
 *  Author: aparrnaa
 * Creation Date: 08/03/2017
 * Last Modified Date: 08/05/2017
 * Description: This class takes a log file as input, parses the content and displays specific aspects of the log file to the console for the end user to view
 */
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class P3A1_Raghuraman_aparrnar {

    public static ArrayList<P3A1_Raghuraman_FileData_aparrnar> files = new ArrayList<P3A1_Raghuraman_FileData_aparrnar>();
    public static HashMap<String, Integer> authorsInLog = new HashMap<String, Integer>();

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
    * Finds the number of files with maximum number of revisions in the whole log file
    * Processes the Arraylist of P3A1_Raghuraman_FileData_aparrnar objects and stores them in a TreeMap such that they are sorted and the maximum value can be retrieved.
     */
    public static void getFilenamesWithMaximumRevisions() {
        int maxRevisions = -1;
        
        try {
            @SuppressWarnings("unchecked")
            TreeMap<Integer, ArrayList<String>> filenamesWithMaxRevisions = new TreeMap();
            for (int objectCounter = 0; objectCounter < files.size(); objectCounter++) {
                P3A1_Raghuraman_FileData_aparrnar object1 = files.get(objectCounter);

                if (filenamesWithMaxRevisions.containsKey(object1.getTotalRevisions())) {
                    ArrayList<String> getValues = filenamesWithMaxRevisions.get(object1.getTotalRevisions());
                    getValues.add(object1.fileName);
                    filenamesWithMaxRevisions.put(object1.getTotalRevisions(), getValues);
                } else {
                    ArrayList<String> values = new ArrayList<String>();
                    values.add(object1.getFileName());
                    filenamesWithMaxRevisions.put(object1.getTotalRevisions(), values);
                }
            }
        
        System.out.println("Files with most number of revisions: " + filenamesWithMaxRevisions.lastEntry().getValue().toString().substring(1, filenamesWithMaxRevisions.lastEntry().getValue().toString().length() - 1));
        System.out.println("Maximum number of revisions: " + filenamesWithMaxRevisions.lastKey());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    * Prints the arraylist
    * @param files - a list consisting of objects of P3A1_Raghuraman_FileData_aparrnar
    * Uses a for loop to get every object of the list and display its contents.
     */
    public static void printList(ArrayList<P3A1_Raghuraman_FileData_aparrnar> files) {
        System.out.println("Here");
        for (int i = 0; i < files.size(); i++) {
            P3A1_Raghuraman_FileData_aparrnar object1 = files.get(i);
            System.out.println("Size: " + object1.userCommits.size());
            System.out.println(object1.getFileName());
            for (Map.Entry<String, Integer> entry : object1.userCommits.entrySet()) {
                System.out.println("Here!!!!");
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }

    }

    /*
    * Finds the authors with maximum number of commits in the whole log file
    * Based on the authorsInLog HashMap, find the key with the maximum commit value
     */
    public static void findAuthorsWithMostCommits() {
       
        try {
                @SuppressWarnings("unchecked")

         HashMap<String, Integer> maxCommitsperAuthor = new HashMap<String, Integer>();
        Integer maxCommitValue = Collections.max(authorsInLog.values());
        String maxCommitAuthorInLog = null;
            for (Map.Entry<String, Integer> entry : authorsInLog.entrySet()) {
                if (entry.getValue().compareTo(maxCommitValue) >= 0) {
                    maxCommitsperAuthor.put(entry.getKey(), entry.getValue());
                }
            }
            maxCommitAuthorInLog = maxCommitsperAuthor.keySet().toString().substring(1, maxCommitsperAuthor.keySet().toString().length() - 1);
            System.out.println("Author Name:" + maxCommitAuthorInLog);
            System.out.println("Number of Commits: " + maxCommitValue);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    * Finds the files with maximum number of author commits in the whole log file
    * Based on the userCommits HashMap, insert them in a TreeMap to find the files with maximum number of authors commiting to it
     */
    public static void findFilesWithMostAuthorCommits() {
        
        try {
               @SuppressWarnings("unchecked")
            TreeMap<Integer, ArrayList<String>> filesAndUserCommits = new TreeMap();
            for (int objectCounter = 0; objectCounter < files.size(); objectCounter++) {
                P3A1_Raghuraman_FileData_aparrnar object1 = files.get(objectCounter);

                if (filesAndUserCommits.containsKey(object1.userCommits.size())) {
                    ArrayList<String> getValues = filesAndUserCommits.get(object1.userCommits.size());
                    getValues.add(object1.getFileName());
                    filesAndUserCommits.put(object1.userCommits.size(), getValues);
                } else {
                    ArrayList<String> values = new ArrayList<String> ();
                    values.add(object1.getFileName());
                    filesAndUserCommits.put(object1.userCommits.size(), values);
                }
            }
            System.out.println("Files with most number of user commits: " + filesAndUserCommits.lastEntry().getValue().toString().substring(1, filesAndUserCommits.lastEntry().getValue().toString().length() - 1));
            System.out.println("Maximum number of author commits in the files: " + filesAndUserCommits.lastKey());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    * Finds the files with earliest commit dates in one or more of their revisions in the whole log file
    * Based on the earliestCommit for each file, store the files ordered by Date in a TreeMap
     */
    public static void findFilesWithEarliestCommits() {
        
        try
        {
                @SuppressWarnings("unchecked")

            TreeMap<Date, ArrayList<String>> filesAndCommitDates = new TreeMap();
        for (int objectCounter = 0; objectCounter < files.size(); objectCounter++) {
            P3A1_Raghuraman_FileData_aparrnar object1 = files.get(objectCounter);
            if (filesAndCommitDates.containsKey(object1.getFirstCommit())) {
                ArrayList<String> getValues = filesAndCommitDates.get(object1.getFirstCommit());
                getValues.add(object1.getFileName());
                filesAndCommitDates.put(object1.getFirstCommit(), getValues);
            } else {
                ArrayList<String> values = new ArrayList<String> ();
                values.add(object1.getFileName());
                filesAndCommitDates.put(object1.getFirstCommit(), values);
            }
        }
        System.out.println("Files with earliest commits: " + filesAndCommitDates.firstEntry().getValue().toString().substring(1, filesAndCommitDates.firstEntry().getValue().toString().length() - 1));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Earliest commit date to a file: " + dateFormat.format(filesAndCommitDates.firstKey()));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /*
    * Prints the number of files in log, files with most users committing to it, earliest commit to a file, author with most commits in the log
    * Also prints the file name, earliest commit date, the name of author who made the maximum commits to the file, and the number of commits made by him 
     */
    public static void printStatistics() {
        try {
            System.out.println("--------------------------------------");
            System.out.println("Statistics in Log File");
            System.out.println("--------------------------------------");
            System.out.println("Number of files in log: " + files.size());
            System.out.println("--------------------------------------");
            getFilenamesWithMaximumRevisions();
            System.out.println("--------------------------------------");
            System.out.println("Filename with most number of users committing to it: ");
            findFilesWithMostAuthorCommits();
            System.out.println("--------------------------------------");
            System.out.println("Earliest commit to a file: ");
            findFilesWithEarliestCommits();
            System.out.println("--------------------------------------");
            System.out.println("Author with most commits in the log:");
            findAuthorsWithMostCommits();
            System.out.println("--------------------------------------");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // System.out.println("Earliest commit date: "+dateFormat.format(earliestCommitDate));
            FileWriter out = new FileWriter("data.txt");
            PrintWriter p = new PrintWriter(out);
            /*p.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
		p.printf("|%120s|%20s|%20s|%20s|%10s|%10s", "Filename", "Number of Revisions", "Last Commit", "First Commit", "Author with most commits", "Number of commits");
		p.println();
		p.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");*/

            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("|%-90s|%-20s|%-20s|%-3s|", "Filename", "First Commit", "Author with most commits", "Number of commits");
            System.out.println();
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (int i = 0; i < files.size(); i++) {
                HashMap<String, Integer> maxEntry2 = new HashMap<String, Integer>();
                String maxCommitsAuthors = null;
                P3A1_Raghuraman_FileData_aparrnar object1 = files.get(i);
                Integer maxValue = Collections.max(object1.userCommits.values());
                for (Map.Entry<String, Integer> entry : object1.userCommits.entrySet()) {
                    if (entry.getValue().compareTo(maxValue) >= 0) {
                        maxEntry2.put(entry.getKey(), entry.getValue());
                    }
                    maxCommitsAuthors = maxEntry2.keySet().toString().substring(1, maxEntry2.keySet().toString().length() - 1);
                }

                /* p.format("|%120s|%20d|%20s|%20s|%10s|%10d", object1.getFileName(), object1.getTotalRevisions(),object1.getLastCommit(),object1.getFirstCommit(), maxCommitsAuthors, maxValue);
	    	 p.println(); 
			 p.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
                 */
                System.out.format("|%-90s|%-20s|%-20s|%-3d|", object1.getFileName(), dateFormat.format(object1.getFirstCommit()), maxCommitsAuthors, maxValue);
                System.out.println();
                System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
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
        P3A1_Raghuraman_FileData_aparrnar obj = new P3A1_Raghuraman_FileData_aparrnar();
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
                    obj = new P3A1_Raghuraman_FileData_aparrnar();
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
                    // System.out.println(date);
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

                    if (authorsInLog.containsKey(author)) { //maintain overall commits made by author throughout the log
                        authorsInLog.put(author, authorsInLog.get(author) + 1);
                    } else {
                        authorsInLog.put(author, 1);
                    }
                } else if (line.contains("====")) { //end of a file data
                    objectCounter++;
                    files.add(obj);
                }
            }

            printStatistics(); //print details of the file

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
