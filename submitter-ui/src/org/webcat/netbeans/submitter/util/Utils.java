package org.webcat.netbeans.submitter.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//--------------------------------------------------------------------------
/**
 * Minor functions used throughout the program.
 *
 * @author Tony Allevato, Robert Poole, Rusty Todd, Stephan McCarn
 */
public class Utils
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Converts an xml read string into a list to be used by the plugin.
     *
     * @param str the string
     * @param storeName the description of each string in the list
     * @return the list of strings
     */
    public static List<String> readStringFormat(String str, String storeName)
    {
        List<String> rtn = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(str, "\b&" + storeName
                                                             + "=\b");
        int numTokens = tokenizer.countTokens();
        String[] strA = splitString(str, "&" + storeName + "=");
        for (int i = 1; i < strA.length; i++)
        {
            rtn.add(strA[i]);
        }
        return rtn;
    }


    // ----------------------------------------------------------
    /**
     * String split on multicharacter delimiter.
     *
     * @param stringToSplit
     * @param delimiter
     * @return
     */
    public static final String[] splitString(String stringToSplit,
                                             String delimiter)
    {
        String[] aRet;
        int iLast;
        int iFrom;
        int iFound;
        int iRecords;

        // return Blank Array if stringToSplit == "")
        if (stringToSplit.equals(""))
        {
            return new String[0];
        }

        // count Field Entries
        iFrom = 0;
        iRecords = 0;
        while (true)
        {
            iFound = stringToSplit.indexOf(delimiter, iFrom);
            if (iFound == -1)
            {
                break;
            }
            iRecords++;
            iFrom = iFound + delimiter.length();
        }
        iRecords = iRecords + 1;

        // populate aRet[]
        aRet = new String[iRecords];
        if (iRecords == 1)
        {
            aRet[0] = stringToSplit;
        }
        else
        {
            iLast = 0;
            iFrom = 0;
            iFound = 0;
            for (int i = 0; i < iRecords; i++)
            {
                iFound = stringToSplit.indexOf(delimiter, iFrom);
                if (iFound == -1)
                { // at End
                    aRet[i] = stringToSplit.substring(
                            iLast + delimiter.length(), stringToSplit.length());
                }
                else if (iFound == 0)
                { // at Beginning
                    aRet[i] = "";
                }
                else
                { // somewhere in middle
                    aRet[i] = stringToSplit.substring(iFrom, iFound);
                }
                iLast = iFound;
                iFrom = iFound + delimiter.length();
            }
        }
        return aRet;
    }


    // ----------------------------------------------------------
    /**
     * Converts a list into a string to be stored in an xml file.
     *
     * @param list the list of strings
     * @param storeName the description of each string in the list
     * @return the xml ready string
     */
    public static String storeStringFormat(List<String> list, String storeName)
    {
        StringBuilder sb = new StringBuilder("");
        for (String s : list)
        {
            sb.append("&").append(storeName).append("=").append(s);
        }
        return sb.toString();

    }


    // ----------------------------------------------------------
    public static URI writeSubmissionResults(String content)
    {
        try
        {
            File tempFile = File.createTempFile(
                    "submission-results", ".html");
            tempFile.deleteOnExit();

            writeToFile(tempFile, content); // fill it with content

            return tempFile.toURI();
        }
        catch (IOException e)
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    public static void writeToFile(File file, String toWrite)
    {
        try
        {
            // Create file
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(toWrite);
            // Close the output stream
            out.close();
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
