package disk_scheduling;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;
import java.lang.*;
import java.io.*;

public class disk_scheduling {
    static final int MAX_CYLINDERS = 5000;
    static final int MAX_REQUESTS = 1000;

    public static void main(String[] args) {
        // Set up scanner
        Scanner input = new Scanner(System.in);
        Random rand = new Random();     // Set up random object
        boolean exitFlag = false;       // To set up when a user quits
        boolean errorFlag = false;      // Used to loop back when there's an error
        int reqOption = 1;                  // Used for user input on reading requests
        int algOption = 1;                  // User input on which algorithm to choose
        int[] requestsArr = new int[MAX_REQUESTS]; // Array for requests
        int requestArrSize = MAX_REQUESTS;          // Will change if a file is read instead
        int initialPos = 0;                         // Starting position
        // First user input
        do {
            errorFlag = false;      // Reset errorFlag

            // Let user choose which option
            System.out.println("Pick an option:");
            System.out.println("\t1: Generate Random Requests");
            System.out.println("\t2: Read Requests from File");

            try {
                reqOption = input.nextInt();
                input.nextLine();
                if(reqOption != 1 && reqOption != 2) {          // Need only 2 inputs
                    throw new Exception("ERROR: Not a valid option");   // Throw Exception if not 1 or 2
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());             // Print Error Message
                input.nextLine();                               // Clear the buffer
                errorFlag = true;                               // Signal an Error Loop
            }
        } while (errorFlag);

        // Create requests
        switch(reqOption) {
            case 1:         // If array was chosen
                // Fill in arrays with random values
                for(int i = 0; i < MAX_REQUESTS; i++) {
                    requestsArr[i] = rand.nextInt(MAX_CYLINDERS);
                }
                break;
            case 2:         // If files were chosen
                do {
                    errorFlag = false;      // Reset Error Flag
                    // Read values from a file
                    String fileName;
                    System.out.println("Enter the name of a file: ");
                    try {
                        fileName = input.nextLine();

                        // Need absolute path name.... for some reason?
                        File fileObj = new File("C:\\Users\\bruhe\\Documents\\os_hw\\CS4310_Project_Two\\disk_scheduling\\" + fileName);

                        Scanner fileReader = new Scanner(fileObj);
                        requestArrSize = 0;     // Reset requestArr Size
                        int idx = 0;

                        // Fill the array with requests from a file (Note: Max values read is 1000)
                        while(fileReader.hasNext()) {
                            requestsArr[idx] = Integer.parseInt(fileReader.nextLine());
                            idx++;
                            requestArrSize++;
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());         // Print Message
                        errorFlag = true;
                    }

                } while(errorFlag);
                break;
        }

        // Once initial values are read, ask user for initial position
        do {
            try {
                if(args.length == 0) {
                    errorFlag = false;      // Reset error flag
                    System.out.print("Enter initial position: ");
                    initialPos = input.nextInt();
                } else {
                    initialPos = Integer.parseInt(args[0]);         // Take from the command line
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                errorFlag = true;
            }
        } while(errorFlag);

        do {                    // Exit loop
            do {                // Error Loop
                try {
                    errorFlag = false;      // Reset error flag
                    // Display menu
                    // Which algorithm would you like to check
                    System.out.println("Choose an algorithm:");
                    System.out.println("\t1. First Come First Serve");
                    System.out.println("\t2. Shortest Seek Time First");
                    System.out.println("\t3. SCAN");
                    System.out.println("\t4. C-SCAN");
                    System.out.println("\t5. Quit");
                    algOption = input.nextInt();
                    input.nextLine();

                    switch(algOption) {
                        case 1:             // First Come First Serve
                            fcfs(initialPos, requestsArr, requestArrSize);
                            break;
                        case 2:             // Shortest Seek Time First
                            sstf(initialPos, requestsArr, requestArrSize);
                            break;
                        case 3:             // Scan
                            scan(initialPos, requestsArr, requestArrSize);
                            break;
                        case 4:             // Circular Scan
                            cscan(initialPos, requestsArr, requestArrSize);
                            break;
                        case 5:
                            exitFlag = true;
                            break;
                        default:
                            throw new Exception("ERROR: Not a valid option");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());         // Print Error Messages
                    errorFlag = true;                           // Signal Error loop
                }
            } while(errorFlag);
        } while(!exitFlag);
    }

    public static void fcfs(int startingPosition, int[] requests, int size) {
        int trackDist = 0;              // Keeps Track of distance
        int turnNum = 0;                // Keeps Track of Turns
        int currentDestination = 0;     // Lets us know where we need to go
        boolean leftMove;               // Keeps track of the current direction

        // Determine the initial movement based on the starting position and the first element
        if(startingPosition > requests[0]) {
            leftMove = true;            // If true, track is currently moving left
        } else {
            leftMove = false;           // If false, track is currently moving right
        }

        // Main algorithm loop
        for(int i = 0; i < size; i++) {
            currentDestination = requests[i];

            System.out.println(startingPosition + " ---> " + currentDestination);   // Showing user where code is moving to
            if(startingPosition > currentDestination) {         // If Starting position is greater, we must move left
                if(!leftMove) {                                 // If we were moving right, there is a turn change
//                    System.out.println("Turn occurred");
                    turnNum++;                                  // Increment Turn Number
                    leftMove = true;                            // Change the turn direction
                }
                for(int j = startingPosition; j > currentDestination; j--) {
                    trackDist++;                                // Count Track Distance from start to destination
                }
            } else if(startingPosition < currentDestination) {  // If Starting position is less, we must move right
                if(leftMove) {                                  // If we were moving left, there is a turn change
//                    System.out.println("Turn occurred");
                    turnNum++;                                  // Increment Turn Number
                    leftMove = false;                           // Change the turn direction
                }
                for(int j = startingPosition; j < currentDestination; j++) {
                    trackDist++;                                // Count track distance
                }
            }
            startingPosition = currentDestination;              // The destination becomes starting position
        }

        System.out.println("Total Distance: " + trackDist);     // After all Requests are processed display info
        System.out.println("Total Direction Changes: " + turnNum);
    }

    public static void sstf(int startingPosition, int[] requests, int size) {
        int trackDist = 0;                      // Keeps track of distance
        int turnNum = 0;                        // Keeps track of turns
        int currentDestination = 0;             // Tells us where to go next
        boolean leftMove = false;               // Tells us where we are turning
        int nextIdx = 0;                        // We need to know which index will be the next destination of the Temp array
        int[] tempArr = new int[size];          // Use a temp array to avoid modifying the real array

        System.arraycopy(requests, 0, tempArr, 0, size); // Copy request to the temp array


        int requestsTracker = 0;                // Keeps track of all the requests
        while(requestsTracker < size) {         // while the requests has not reached the size of the array
            int minDist = MAX_CYLINDERS;        // Maximum distance starts out as 0 - 4999 and will reset each iteration
            for(int i = 0; i < size; i++) {
                if(tempArr[i] != -1) {          // -1 symbolizes a request that was already read
                    // Determine the minimum distance
                    int currDist = Math.abs(startingPosition - tempArr[i]);
                    if(minDist > currDist) {
                        minDist = currDist;
                        nextIdx = i;            // Keep track of the index which has the closest distance
                    }
                }
            }
            tempArr[nextIdx] = -1;              // Once chosen, change value to -1 to symbolize request has been used
            currentDestination = requests[nextIdx]; // Make it the current Destination

            // Determines the initial movement direction
            if(requestsTracker == 0) {
                leftMove = startingPosition > currentDestination; //
            }

            // Display to user start to end movement
            System.out.println(startingPosition + " ---> " + currentDestination);

            // Determines if there was a direction change
            if(startingPosition > currentDestination) {     // If going left,
                if(!leftMove) {                             // But went right before
                    turnNum++;                              // A turn occurred
                    leftMove = true;                        // Switch directions
                }
                for(int i = startingPosition; i > currentDestination; i--) {
                    trackDist++;                                // Count distance
                }
            } else if(startingPosition < currentDestination) {  // If going right
                if(leftMove) {                                  // But went left before
                    turnNum++;                                  // A turn occurred
                    leftMove = false;                           // Switch directions
                }
                for(int i = startingPosition; i < currentDestination; i++) {
                    trackDist++;                                // Count dirrections
                }
            }

            startingPosition = currentDestination;              // Update starting position
            requestsTracker++;                                  // Complete request
        }

        System.out.println("Total Distance: " + trackDist);     // After display results
        System.out.println("Total Direction Changes: " + turnNum);
    }

    public static void scan(int startingPosition, int[] requests, int size) throws Exception {
        int trackDist = 0;                          // Keeps track of distance
        int turnNum = 0;                            // Keeps track of turns
        int currentDestination = 0;                 // Keeps track of destination
        boolean leftMove = false;                   // Keeps track of movement direction
        int nextIdx = 0;                            // Needed to know index of the next request
        int[] tempArr = new int[size];              // Needed to not accidentally change initial array
        int requestsTracker = 0;                    // Stops initial loop

        System.arraycopy(requests, 0, tempArr, 0, size); // Copy requests into tempArray

        // Ask for position before initial position
        System.out.println("What was the position before the starting position?");
        Scanner prevPosInput = new Scanner(System.in);

        // Used to determine starting direction
        try {
            int prevPos = prevPosInput.nextInt();
            prevPosInput.nextLine();
            if(prevPos > startingPosition) {
                leftMove = true;
            }
        } catch (Exception e) {
            throw new InputMismatchException();
        }

        // Runs mostly like SSTF
        while(requestsTracker < size) {
            int minDist = MAX_CYLINDERS;
            for(int i = 0; i < size; i++) {
                if(tempArr[i] != -1) {
                    // Only difference is that direction must remain consistent
                    if(leftMove) {                                                  // If moving left, only check lower numbers
                        int currDist = Math.abs(startingPosition - tempArr[i]);
                        if(tempArr[i] < startingPosition && minDist > currDist) {   // And check shortest distance from starting
                            minDist = currDist;
                            nextIdx = i;
                        }
                    } else {                                                        // If moving right, only check greater numbers
                        int currDist = Math.abs(startingPosition - tempArr[i]);     // And checking shortest distance from starting
                        if(tempArr[i] > startingPosition && minDist > currDist) {
                            minDist = currDist;
                            nextIdx = i;
                        }
                    }
                }
            }

            tempArr[nextIdx] = -1;      // Signal the number was used

            if(minDist == MAX_CYLINDERS) {  // If the minimum distance did not change, there are no more numbers to search
                if(leftMove) {
                    currentDestination = 0;     // If moving left, go to the beginning of the disk
                } else {                        // If moving right, go to the end of the disk
                    currentDestination = MAX_CYLINDERS - 1;
                }

                requestsTracker--;              // This was a request not in the initial array, so we must account for it
            } else {
                currentDestination = requests[nextIdx]; // If not, assign the next number as the destination
            }

            System.out.println(startingPosition + " ---> " + currentDestination);       // Display the path

            if(startingPosition > currentDestination) {     // Count distance
                for(int i = startingPosition; i > currentDestination; i--) {
                    trackDist++;
                }
            } else if(startingPosition < currentDestination) {
                for(int i = startingPosition; i < currentDestination; i++) {
                    trackDist++;
                }
            }

            // A turn will only occur at the beginning and end of the disk
            // So only need to check here
            startingPosition = currentDestination;
            if(startingPosition == 0) {
                turnNum++;
                leftMove = false;
            }

            if(startingPosition == MAX_CYLINDERS - 1) {
                turnNum++;
                leftMove = true;
            }
            requestsTracker++;
        }

        // Display results
        System.out.println("Total Distance: " + trackDist);
        System.out.println("Total Direction Changes: " + turnNum);
    }

    public static void cscan(int startingPosition, int[] requests, int size) {
        // Literally same variables as before, check the above functions for reference ^^
        int trackDist = 0;
        int turnNum = 0;
        int currentDestination = 0;
        boolean leftMove = false;
        boolean cycle = false;          // ONLY DIFFERENCE: NEEDED to know when to cycle
        int nextIdx = 0;
        int[] tempArr = new int[size];
        int requestsTracker = 0;

        System.arraycopy(requests, 0, tempArr, 0, size);

        // Ask for the position prior to initial position
        System.out.println("What was the position before the starting position?");
        Scanner prevPosInput = new Scanner(System.in);

        // This determines the initial direction
        try {
            int prevPos = prevPosInput.nextInt();
            prevPosInput.nextLine();
            if(prevPos > startingPosition) {
                leftMove = true;
            }
        } catch (Exception e) {
            throw new InputMismatchException();
        }

        // Algorithm matches SCAN with slight differences
        while(requestsTracker < size) {
            int minDist = MAX_CYLINDERS;
            // If the Starting position is 0 or 4999, and we currently are not in a cycle
            if(startingPosition == 0 && !cycle) {
                currentDestination = MAX_CYLINDERS - 1;         // Begin a cycle
                cycle = true;
                requestsTracker--;                              // Account for the extra request
            } else if(startingPosition == MAX_CYLINDERS - 1 && !cycle) {
                currentDestination = 0;                         // Same process but in the other direction
                cycle = true;
                requestsTracker--;                              // Account for the extra request
            } else {
                // If not, run the algorithm as normal
                // This block basically runs the same code as SCAN
                for(int i = 0; i < size; i++) {
                    if(tempArr[i] != -1) {
                        if(leftMove) {
                            int currDist = Math.abs(startingPosition - tempArr[i]);
                            if(tempArr[i] < startingPosition && minDist > currDist) {
                                minDist = currDist;
                                nextIdx = i;
                            }
                        } else {
                            int currDist = Math.abs(startingPosition - tempArr[i]);
                            if(tempArr[i] > startingPosition && minDist > currDist) {
                                minDist = currDist;
                                nextIdx = i;
                            }
                        }
                    }
                }

                // Runs th
                tempArr[nextIdx] = -1;

                if(minDist == MAX_CYLINDERS) {
                    if(leftMove) {
                        currentDestination = 0;
                    } else {
                        currentDestination = MAX_CYLINDERS - 1;
                    }
                    requestsTracker--;
                } else {
                    currentDestination = requests[nextIdx];
                }
            }

            System.out.println(startingPosition + " ---> " + currentDestination);

            if(startingPosition > currentDestination) {
                for(int i = startingPosition; i > currentDestination; i--) {
                    trackDist++;
                }
            } else if(startingPosition < currentDestination) {
                for(int i = startingPosition; i < currentDestination; i++) {
                    trackDist++;
                }
            }

            startingPosition = currentDestination;
            if(startingPosition == 0) {
                turnNum++;
                leftMove = false;
            } else if(startingPosition == MAX_CYLINDERS - 1) {
                turnNum++;
                leftMove = true;
            }
            requestsTracker++;
        }

        // Display results
        System.out.println("Total Distance: " + trackDist);
        System.out.println("Total Direction Changes: " + turnNum);
    }
}
