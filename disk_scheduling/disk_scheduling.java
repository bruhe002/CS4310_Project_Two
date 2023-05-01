package disk_scheduling;

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
                if(reqOption != 1 && reqOption != 2) {
                    throw new Exception("ERROR: Not a valid option");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                input.nextLine();
                errorFlag = true;
            }
        } while (errorFlag);

        // Create requests
        switch(reqOption) {
            case 1:
                // Fill in arrays with random values
                for(int i = 0; i < MAX_REQUESTS; i++) {
                    requestsArr[i] = rand.nextInt(MAX_CYLINDERS);
                }
                break;
            case 2:
                do {
                    errorFlag = false;      // Reset Error Flag
                    // Read values from a file
                    String fileName;
                    System.out.println("Enter the name of a file: ");
                    try {
                        fileName = input.nextLine();
                        File fileObj = new File("C:\\Users\\bruhe\\Documents\\os_hw\\CS4310_Project_Two\\disk_scheduling\\" + fileName);
                        Scanner fileReader = new Scanner(fileObj);
                        requestArrSize = 0;     // Reset requestArr Size
                        int idx = 0;
                        while(fileReader.hasNext()) {
                            requestsArr[idx] = Integer.parseInt(fileReader.nextLine());
                            idx++;
                            requestArrSize++;
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        errorFlag = true;
                    }

                } while(errorFlag);
                break;
        }

        do {
            try {
                errorFlag = false;      // Reset error flag
                System.out.print("Enter initial position: ");
                initialPos = input.nextInt();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                errorFlag = true;
            }
        } while(errorFlag);

        do {
            do {
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
                        case 1:
                            fcfs(initialPos, requestsArr, requestArrSize);
                            break;
                        case 2:
                            sstf(initialPos, requestsArr, requestArrSize);
                            break;
                        case 3:
                            scan(initialPos, requestsArr, requestArrSize);
                            break;
                        case 4:
                            cscan(initialPos, requestsArr, requestArrSize);
                            break;
                        case 5:
                            exitFlag = true;
                            break;
                        default:
                            throw new Exception("ERROR: Not a valid option");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    errorFlag = true;
                }
            } while(errorFlag);
        } while(!exitFlag);
    }

    public static void fcfs(int startingPosition, int[] requests, int size) {
        int trackDist = 0;
        int turnNum = 0;
        int currentDestination = 0;
        boolean leftMove;
        if(startingPosition > requests[0]) {
            leftMove = true;
        } else {
            leftMove = false;
        }
        for(int i = 0; i < size; i++) {
            currentDestination = requests[i];

            System.out.println(startingPosition + " ---> " + currentDestination);
            if(startingPosition > currentDestination) {
                if(!leftMove) {
//                    System.out.println("Turn occurred");
                    turnNum++;
                    leftMove = true;
                }
                for(int j = startingPosition; j > currentDestination; j--) {
                    trackDist++;
                }
            } else if(startingPosition < currentDestination) {
                if(leftMove) {
//                    System.out.println("Turn occurred");
                    turnNum++;
                    leftMove = false;
                }
                for(int j = startingPosition; j < currentDestination; j++) {
                    trackDist++;
                }
            }
            startingPosition = currentDestination;
        }

        System.out.println("Total Distance: " + trackDist);
        System.out.println("Total Direction Changes: " + turnNum);
    }

    public static void sstf(int startingPosition, int[] requests, int size) {
        int trackDist = 0;
        int turnNum = 0;
        int currentDestination = 0;
        boolean leftMove = false;
        int nextIdx = 0;
        int[] tempArr = new int[size];

        System.arraycopy(requests, 0, tempArr, 0, size);

        int requestsTracker = 0;
        while(requestsTracker < size) {
            int minDist = 1001;
            for(int i = 0; i < size; i++) {
                if(tempArr[i] != -1) {
                    int currDist = Math.abs(startingPosition - tempArr[i]);
                    if(minDist > currDist) {
                        minDist = currDist;
                        nextIdx = i;
                    }
                }
            }
            tempArr[nextIdx] = -1;
            currentDestination = requests[nextIdx];
            if(requestsTracker == 0) {
                leftMove = startingPosition > currentDestination;
            }

            System.out.println(startingPosition + " ---> " + currentDestination);

            if(startingPosition > currentDestination) {
                for(int i = startingPosition; i > currentDestination; i--) {
                    if(!leftMove) {
                        turnNum++;
                        leftMove = true;
                    }
                    trackDist++;
                }
            } else if(startingPosition < currentDestination) {
                for(int i = startingPosition; i < currentDestination; i++) {
                    if(leftMove) {
                        turnNum++;
                        leftMove = false;
                    }
                    trackDist++;
                }
            }

            startingPosition = currentDestination;
            requestsTracker++;
        }

        System.out.println("Total Distance: " + trackDist);
        System.out.println("Total Direction Changes: " + turnNum);
    }

    public static void scan(int startingPosition, int[] requests, int size) {
        int trackDist = 0;
        int turnNum = 0;
        int currentDestination = 0;
        boolean leftMove = true;
        int nextIdx = 0;
        int[] tempArr = new int[size];
        int requestsTracker = 0;

        System.arraycopy(requests, 0, tempArr, 0, size);

        while(requestsTracker < size) {
            int minDist = 1001;
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

            tempArr[nextIdx] = -1;

            if(minDist == 1001) {
                currentDestination = 0;
                requestsTracker--;
            } else {
                currentDestination = requests[nextIdx];
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
            }
            requestsTracker++;
        }

        System.out.println("Total Distance: " + trackDist);
        System.out.println("Total Direction Changes: " + turnNum);
    }

    public static void cscan(int startingPosition, int[] requests, int size) {
        int trackDist = 0;
        int turnNum = 0;
        int currentDestination = 0;
        boolean leftMove = false;
        boolean cycle = false;
        int nextIdx = 0;
        int[] tempArr = new int[size];
        int requestsTracker = 0;

        System.arraycopy(requests, 0, tempArr, 0, size);

        while(requestsTracker < size) {
            int minDist = 1001;
            if(startingPosition == 0 && !cycle && requestsTracker != 0) {
                currentDestination = 4999;
                cycle = true;
                requestsTracker--;
            } else if(startingPosition == 4999 && !cycle && requestsTracker != 0) {
                currentDestination = 0;
                cycle = true;
                requestsTracker--;
            } else {
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

                tempArr[nextIdx] = -1;

                if(minDist == 1001) {
                    if(leftMove) {
                        currentDestination = 0;
                    } else {
                        currentDestination = 4999;
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
            } else if(startingPosition == 4999) {
                turnNum++;
                leftMove = true;
            }
            requestsTracker++;
        }

        System.out.println("Total Distance: " + trackDist);
        System.out.println("Total Direction Changes: " + turnNum);
    }
}
