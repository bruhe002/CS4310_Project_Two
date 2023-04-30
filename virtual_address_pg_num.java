import java.util.Scanner;
import java.lang.*;

public class virtual_address_pg_num {
    private static final int INITIAL_PAGE_SIZE = 10;         // 2^10 = 1 KB
    private static final int MAX_BIT_SIZE = 32;              // 32 bit addresses

    public static void main(String[] args) {
        boolean errorFlag;
        Scanner input = new Scanner(System.in);
        do {
            errorFlag = false;      // Reset errorFlag
            // Try block used to catch input errors
            try {
                // Acquire the input numbers
                System.out.print("Enter Page Size (KB): "); // Acquire Page size
                int pageSize = input.nextInt();
                input.nextLine();

                // For simplicity, use only numbers that are of the power of 2
                if(power_of_two(pageSize)) {
                    System.out.print("Enter Virtual Address: ");    // Acquire Virtual Address
                    int virtual_address = input.nextInt();
                    input.nextLine();

                    String binaryNum = get_bin_num(virtual_address, MAX_BIT_SIZE); // Convert the virtual address to binary
                    int offsetSize = log2(pageSize) + INITIAL_PAGE_SIZE; // Acquire the exponent and add to the initial page size

                    // Create Substrings of the binary number based on the offset size
                    String substr1 = binaryNum.substring(0, MAX_BIT_SIZE - offsetSize); // This will be the Page Number
                    String substr2 = binaryNum.substring(MAX_BIT_SIZE - offsetSize); // This will be the offset number

                    // Convert the substrings to decimals
                    int pageNumber = bin_to_dec(substr1, MAX_BIT_SIZE - offsetSize);
                    int offsetNum = bin_to_dec(substr2, offsetSize);

                    // Print the results
                    System.out.println("The address " + virtual_address + " contains: page number = " + pageNumber +
                            "  and offset = " + offsetNum);
                } else {
                    // Exception if number is not a power of two
                    throw new Exception("ERROR: Make sure your page size is a power of two - Press ANY KEY to restart.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage()); // Display Error Message
                input.nextLine(); // Clean up the buffer
                errorFlag = true; // Set error flag to continue the loop
            }
        } while(errorFlag);

    }

    // Calculates if a number is a power of two
    public static boolean power_of_two(int n) {
        int temp = n;

        // Keep dividing by two as long as the resulting quotient is even
        while (temp % 2 == 0) {
            temp /= 2;
        }

        // If temp reaches 1, return true
        return temp == 1;
    }

    // Calculates the log (base 2) of a number
    public static int log2(int n) {
        return (int)(Math.log(n) / Math.log(2));
    }

    // Returns a string binary number of a decimal number
    public static String get_bin_num(int n, int size) {
        int temp = n;
        StringBuilder result = new StringBuilder();
        while(temp > 0) {
            if(temp % 2 == 0) {
                result.insert(0, "0"); // If the resulting temp is even, add a zero to string
            } else {
                result.insert(0, "1"); // If the resulting temp is odd, add a one to the string
            }

            temp /= 2;
        }

        // Pad out necessary zeroes
        if(result.length() < size) {
            while (result.length() < size) {
                result.insert(0, "0");
            }
        }
        return result.toString();
    }

    // Converts a binary string to a decimal number
    public static int bin_to_dec(String str, int size) {
        int result = 0;
        for(int i = size; i > 0 ; i--) {
            // If we encounter a one, add the binary value of that one to the result
            if(str.charAt(i - 1) == '1') {
                result += (int)Math.pow(2, size - i);
            }
        }

        return result;
    }
}
