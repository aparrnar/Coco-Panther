
/**
 *
 * Author: aparrnaa
 * Creation Date: 08/04/2017
 * Last Modified Date: 08/05/2017
 * Description:
 * This class takes a log file as input, parses the content and writes specific details of the log file into a .csv file
 */
import com.opencsv.CSVWriter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class P3A2_Raghuraman_aparrnar {

    public static ArrayList<P3A2_Raghuraman_FileData_aparrnar> files = new ArrayList<P3A2_Raghuraman_FileData_aparrnar>();
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
    * prints the filename, total number of revisions, first commit date and last commit date to a file.
    * Uses an OpenCV writer to write to a .csv file
     */
    public static void printStatsToFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("result.csv"));
            CSVWriter writer = new CSVWriter(out);
            String head = "Filename,Number of Commits, First Commit,Last Commit";
            String[] headings = head.split(",");
            writer.writeNext(headings);
            String[] values;
            for (int objectCounter = 0; objectCounter < files.size(); objectCounter++) {
                P3A2_Raghuraman_FileData_aparrnar obj1 = files.get(objectCounter);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String data = "";
                data += obj1.getFileName() + "," + obj1.getTotalRevisions() + "," + dateFormat.format(obj1.getFirstCommit()) + "," + dateFormat.format(obj1.getLastCommit());
                String[] value = data.split(",");
                writer.writeNext(value);
            }
            writer.close();

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
        P3A2_Raghuraman_FileData_aparrnar obj = new P3A2_Raghuraman_FileData_aparrnar();
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
                    obj = new P3A2_Raghuraman_FileData_aparrnar();
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
                } else if (line.contains("====")) { //end of file data
                    objectCounter++;
                    files.add(obj);
                }
            }

            printStatsToFile(); //print details to file

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
