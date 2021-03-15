
import java.util.*;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import java.io.*;

public class IOProcessing {

    private BufferedReader reader;
    private BufferedWriter writer;
    private List<String> userNamesList;
    private List<Integer> userScoresList; 
    private Map<String, Integer> usersAndScores; 
    private int userScore;
    private String userName;
    
    private String filePath;
    
    public IOProcessing() {

        usersAndScores = new TreeMap<>();
        
    }
    
    public void readFile(String filePath) {
        
        //If there are no more lines to read, the file reading stops

        try {
            reader = new BufferedReader(new FileReader(filePath));
            
            if (filePath == null) {
                throw new IllegalArgumentException();
            }
            
            this.filePath = filePath;
            String line = reader.readLine();
            while (line != null) {
                try {
                    String[] lineContents = line.split(" ");
                    userName = lineContents[0];
                    userScore = Integer.parseInt(lineContents[1]); 
                    
                    //only update high score for existing user if new score is higher
                    if (usersAndScores.containsKey(userName)) {
                        if (usersAndScores.get(userName) < userScore) {
                            usersAndScores.put(userName, userScore);
                        }
                        
                        
                    } else {
                        usersAndScores.put(userName, userScore);
                    }
                    
    
                    line = reader.readLine();
                } catch (ArrayIndexOutOfBoundsException e) {
                    JOptionPane.showMessageDialog(null, "Input file not formatted correctly. "
                            + "Has to be: [name] [score] for each line.");
                }
            }
            
            //sorting lists
            Collection<Integer> scores = usersAndScores.values();
            userScoresList = new ArrayList<>(scores);
            Collections.sort(userScoresList, Collections.reverseOrder());
            userNamesList = new ArrayList<>();
            for (int individualScore : userScoresList) {
                for (Entry<String, Integer> e : usersAndScores.entrySet()) {
                    String user = e.getKey();
                    int correspondingScore = e.getValue();
                    
                    if (correspondingScore == individualScore) {
                        if (!userNamesList.contains(user)) {
                            userNamesList.add(user);
                            break;
                        }
                       
                    }
                    
                }
            }
            
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            JOptionPane.showMessageDialog(null, "File not found.");
            throw new IllegalArgumentException();
            
        } catch (IOException e) {
            
        }
    }
    
    public void writeFile(int currentScore, String currentUserName) {
        try {
            writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.write(currentUserName + " " + Integer.toString(currentScore) + "\n");
            
            writer.close();
        } catch (IOException e) {
           
        
        }

        
    }
    

    
    public List<Integer> getSortedScores() {
        return userScoresList;
    }
    
    public List<String> getSortedUsers() {
        return userNamesList;
    }
       
    
    
    
}
