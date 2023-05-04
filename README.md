# CS4310_Project_Two
##This Project covers three tasks which simulate the following: 

- ### Calculating page number of a virtual address
- ### Simulating disk scheduling algorithms
- ### Locating the logical block number of a hard disk. 


## Virtual Address

Like every other process, we must compile the code.

To compile run the following command in the terminal:

`javac virtual_address_pg_num.java`

Once compilation finishes without errors, run the code by entering the following command:

`java virtual_address_pg_num.java`

**Note**: Make sure you are in the correct directory when running: */virtual_address/*

Once The code is running, you will be asked *Enter Page Size (KB)*

Enter a number, the code will throw an error if the number is not a perfect power of 2.

Next you will be asked *Enter Virtual Address*

An error will occur if a number is greater than a 32-bit integer.

Once that is entered, the terminal will display the page number and the offset number.

## Disk Scheduling

To compile:

`javac disk_scheduling.java`

To run:

`java disk_scheduling.java`

To run with Command Line Parameter

`java disk_scheduling.java [initial_number]`

**Note**: Make sure you are in the correct directory when running: */disk_scheduling/*

Once running, you will be prompted for 2 inputs:
1) Generate Random Requests
2) Read Requests from File

Choosing OPTION ONE will generate an array of random numbers that will act as our requests

Choosing OPTION TWO will take request numbers from a file, which the user will input. 
Make sure that the file path is correct.

**NOTE**: If a Command Line Parameter was not passed, the user will be asked for an initial
position.

From there, the user will be displayed a menu of algorithms to run requests with 5 options
1) First Come First Serve
2) Shortest Seek Time First
3) SCAN
4) C-SCAN
5) Quit

Input an option to see the results of the algorithm, along with the path between each number.

If a user chooses OPTION THREE or OPTION FOUR, they will be prompted with a message:

`What was the position before the starting position?`

The number will determine the starting direction of the scan.
## Logical Block Number

To compile:

`javac hard_disk_logic_block.java`

To run:

`java hard_disk_logic_block.java`

User will be asked 
`Enter a logic block number`

After they will be asked the following questions:

`Enter HD number of cylinders`

`Enter HD number of tracks`

`Enter HD number of sectors`

Once the value are given, the address is given in the following format:
`<Cylinder #, Track #, Sector # >`
