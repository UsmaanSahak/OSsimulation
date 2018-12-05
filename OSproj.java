//USMAAN SAHAK OS PROJECT
import java.util.Scanner;
import java.math.BigInteger;
import java.util.ArrayList;

public class OSproj {
  static int PIDCOUNT = 0;
  static ArrayList<pcb> processTable = new ArrayList<pcb>();
  static ArrayList<Integer> readyQueue = new ArrayList<Integer>(); //Stores indices in order of priority
  static ArrayList<Integer> waitQueue = new ArrayList<Integer>(); //Between ready and wait
  //static ArrayList<int> ioQueue = new ArrayList<int>(); //First come first serve. diff per dev?
  static int cpuProcess = -1; //Store index of currently running process.
  static BigInteger ramMemory = new BigInteger("4000000000");
  static BigInteger availableMemory = new BigInteger("4000000000");
  static int numDisks = 0;
  static Scanner s = new Scanner(System.in);
  public static void main(String[] args) {
    initialize();
    while (true) {
      getInput();  
    }
  }


  public static void initialize() {
    System.out.println("How much RAM memory is there on the simulated computer?");
    String input = s.next();
    ramMemory = new BigInteger(input);
    availableMemory = ramMemory;
    System.out.println("How many hard disks does the simulated computer have? (Enumeration of the hard disks start with 0.)");
    input = s.next();
    numDisks = Integer.parseInt(input);
    System.out.println();
    System.out.println();
  }
  public static void getInput() {
    String input = s.next();
    if (input.equals("A")) {
      int priority = Integer.parseInt(s.next());
      BigInteger memory_size = new BigInteger(s.next());
      /*Create new Process. Calculated a PID for this PCB etc
        Recalculate available RAM memory.*/
       if ((availableMemory.subtract(memory_size)).compareTo(new BigInteger("0")) != -1) {
        availableMemory = availableMemory.subtract(memory_size);
        processTable.add(new pcb(PIDCOUNT++,priority));
        if (cpuProcess == -1) {
          cpuProcess = processTable.size() - 1; //Get index of just added pcb.
          System.out.println("RUNNING PROCESS AT INDEX " + cpuProcess);
        } else {
          addToReady(processTable.size());
        }
      } else {
        System.out.println("No memory available!");
      }
    } else if (input.equals("t")) { 
      /*Terminate the process currently using the CPU.
      pop from ReadyQueue. Make cpuProcess = that index.*/
      
    } else if (input.equals("d")) { 
      int hardDisk = Integer.parseInt(s.next());
      String file_name = s.next();
      /*Currently cpu running process reads/writes file, file_name, into hard disk #<hardDisk>*/
    } else if (input.equals("D")) {
      int hardDisk = Integer.parseInt(s.next());
      /* hard disk #<hardDisk> has finished the work for one process.*/  
    } else if (input.equals("S")) { 
      input = s.next();
      if (input.equals("r")) {
        /*Show CPU process and ready queue processes. PID and priority.*/
        displayPT();
        System.out.println("Displaying cpuProcess:");
        System.out.println(cpuProcess);
        displayRQ();
      } else if (input.equals("i")) {
        /*Show processes curr using hardDisks and what processes waiting for them. filenamesetc*/
      } else if (input.equals("m")) {
        /*Show state of memory. Range of memory addresses used by each process in system.*/
      } 
    } else if (input.equals("exit")) {
      System.out.println("Exiting Gracefully..");
      System.exit(0);
    }
  }
  private static void displayPT() {
    System.out.println("Displaying all processes...");
    for (int i=0; i < processTable.size(); i++) {
      System.out.println("PT pid : " + processTable.get(i).pid); 
    }
  }
  private static void displayRQ() {
    System.out.println("Displaying Ready Queue...");
    for (int i=0; i < readyQueue.size(); i++) {
      System.out.println("RQ pid : " + processTable.get(readyQueue.get(i)).pid); 
    }
  }
  private static void addToReady(int pti) {
    //Compare processTable[pti]'s priority 
    Boolean replaced = false;
    for (int i=0; i < readyQueue.size(); i++) {
      if (processTable.get(pti).priority < processTable.get(readyQueue.get(i)).priority) {
        //Place this one here. push everything else back.
        readyQueue.add(readyQueue.get(readyQueue.size()));//Add element at end repeat.
        for (int j=i+1; j < readyQueue.size(); j++) {
          readyQueue.set(j,readyQueue.get(j-1));
        }
        readyQueue.set(i,pti);
        replaced = true;
        break;
      }
    }
    if (!replaced) {
      readyQueue.add(pti-1);//If empty or new process is lowest priority, add to end.
    }
  }
  
} 


/*
Simulates some aspects of operating system.
Based off of text inputs from user.

Scheduling: implement preemptive priority CPU-scheduling. Higher numbers = higher priority.

I/O queues are first come first serve (FCFS).

Memory: your program should simulate contiguous memory management with 'best fit' approach.

Each program should be represented by own PCB. Don't move around PCBs, store all PCBs in process table and move only the indices of processes in that table. (A possibility is to do just a vector of PCBs but you can do whatever)

*/

/*
Add process? Make PCB, add to processTable.
Place get the priority of process and go down ready queue until reach priority lower than new.
Insert process there in the ready queue.

*/
