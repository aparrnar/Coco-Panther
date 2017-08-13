/**
 *
 * @author aparrnaa
 * Creation Date: 08/05/2017
 * Last Modified Date: 08/05/2017
 * Description:
 * 
 */
import java.text.SimpleDateFormat;
import java.util.*;

public class P3A2_Raghuraman_FileData_aparrnar {

    public static int numberOfFiles;
    public String fileName;
    public int totalRevisions;
    public int selectedRevisions;
    public HashMap<String, Integer> userCommits = new HashMap<String, Integer>();
    public Date firstCommit;
    public Date lastCommit;

    P3A2_Raghuraman_FileData_aparrnar() {
        fileName = null;
        totalRevisions = 0;
        selectedRevisions = 0;
        userCommits = new HashMap<String, Integer>();
        firstCommit = null;
        lastCommit = null;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the totalRevisions
     */
    public int getTotalRevisions() {
        return totalRevisions;
    }

    /**
     * @param totalRevisions the totalRevisions to set
     */
    public void setTotalRevisions(int totalRevisions) {
        this.totalRevisions = totalRevisions;
    }

    /**
     * @return the selectedRevisions
     */
    public int getSelectedRevisions() {
        return selectedRevisions;
    }

    /**
     * @param selectedRevisions the selectedRevisions to set
     */
    public void setSelectedRevisions(int selectedRevisions) {
        this.selectedRevisions = selectedRevisions;
    }

    /**
     * @return the firstCommit
     */
    public Date getFirstCommit() {
        return firstCommit;
    }

    /**
     * @param firstCommit the firstCommit to set
     */
    public void setFirstCommit(Date firstCommit) {
        this.firstCommit = firstCommit;
    }

    /**
     * @return the lastCommit
     */
    public Date getLastCommit() {
        return lastCommit;
    }

    /**
     * @param lastCommit the lastCommit to set
     */
    public void setLastCommit(Date lastCommit) {
        this.lastCommit = lastCommit;
    }

    public void clearValues() {
        fileName = null;
        totalRevisions = 0;
        selectedRevisions = 0;
        userCommits = null;
        firstCommit = null;
        lastCommit = null;
    }

}
