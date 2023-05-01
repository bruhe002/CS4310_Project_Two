package logic_block;

import java.util.Scanner;
import java.lang.*;

public class hard_disk_logic_block {
    // Use a greedy algorithm
    public static void main(String[] args) {
        boolean errorFlag = false;
        // New Scanner
        Scanner input = new Scanner(System.in);

        // Display Prompt
        do {
            try {
                errorFlag = false;
                System.out.print("Enter a logical block number: ");
                int logicBlockNum = input.nextInt();
                input.nextLine();

                System.out.print("Enter HD number of cylinders: ");
                int cylinderNum = input.nextInt();
                input.nextLine();

                System.out.print("Enter HD number of tracks: ");
                int trackNum = input.nextInt();
                input.nextLine();

                System.out.print("Enter HD number of sectors: ");
                int sectorNum = input.nextInt();
                input.nextLine();


                int secPerCyl = sectorNum * trackNum;
                int remainingBlocks = logicBlockNum;

                int outputCyl = 0;
                int outputTrack = 0;
                int outputSect = 0;

                while (remainingBlocks > 0) {
                    if(remainingBlocks > secPerCyl) {
                        remainingBlocks -= secPerCyl;
                        outputCyl++;
                    } else if(remainingBlocks > sectorNum) {
                        remainingBlocks -= sectorNum;
                        outputTrack++;
                    } else {
                        outputSect = remainingBlocks;
                        remainingBlocks = 0;
                    }
                }

                if(outputCyl > cylinderNum) {
                    throw new Exception("Not enough disk space for logic block number!");
                }

                System.out.println("The logical block number " + logicBlockNum + " is located as < " + outputCyl + " , " +
                        outputTrack + " , " + outputSect + " >");
            } catch(Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                errorFlag = true;
            }

        } while(errorFlag);
    }


}
