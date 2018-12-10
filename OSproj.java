//USMAAN SAHAK OS PROJECT
import java.util.Scanner;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;

public class OSproj {
  static int PIDCOUNT = 0;
  static LinkedList<pcb> processTable = new LinkedList<pcb>();
  static LinkedList<pcb> readyQueue = new LinkedList<pcb>(); //Stores indices in order of priority
  //static LinkedList<pcb> waitQueue = new LinkedList<pcb>(); //Between ready and wait
  static LinkedList<block> mainMemory = new LinkedList<block>();
  static ArrayList<LinkedList<file>> IOQueue = new ArrayList<LinkedList<file>>();
  static pcb cpuProcess = null; 
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
    for (int i=0; i<numDisks; i++) {
      IOQueue.add(new LinkedList<file>());
    }
  }
  public static void getInput() {
    //System.out.println("Getting INPUT");
    String input = s.next();

    if (input.equals("A")) {
      block newBlock = null;
      int priority = Integer.parseInt(s.next());
      BigInteger memory_size = new BigInteger(s.next());
      /*Create new Process. Calculated a PID for this PCB etc
        Recalculate available RAM memory.*/
       if ((availableMemory.subtract(memory_size)).compareTo(new BigInteger("0")) == -1) {
        System.out.println("No memory available!");
        return;
       }
       //Look for a gap, best fit.
       Boolean succAdd = false;
       if (mainMemory.size() == 0) {
        newBlock = new block(new BigInteger("0"),memory_size);
        mainMemory.add(newBlock); 
        succAdd=true;
       }
       else {
        BigInteger base = new BigInteger("-1");
        BigInteger limit = new BigInteger("-1");
        BigInteger gap = ramMemory;
        BigInteger newGap = new BigInteger("0");
        int gapIndex = -1;
        for (int i=0; i < mainMemory.size()-1; i++) {
          newGap = mainMemory.get(i+1).base.subtract(mainMemory.get(i).limit);
          if ((newGap.compareTo(memory_size) > -1) && (newGap.compareTo(gap) == -1)) {
           gapIndex = i; 
          }
        }
        if (gapIndex == -1) { //None was chosen. Check if room at back?
          if (((ramMemory.subtract(mainMemory.getLast().limit)).compareTo(memory_size)) > -1) {
            base = mainMemory.getLast().limit;
            limit = base.add(memory_size);
            newBlock = new block(base,limit);
            mainMemory.add(newBlock);
            succAdd = true;
          } else {
            System.out.println("No place to fit it in RAM!");
            return;
          }
        } else {
          newBlock = new block(mainMemory.get(gapIndex).limit,mainMemory.get(gapIndex).limit.add(memory_size));
          mainMemory.add(gapIndex+1,newBlock);
          succAdd = true;
        }
      }
      if (succAdd == true) {
        //Update availableMemory
        availableMemory = availableMemory.subtract(memory_size);
        pcb newPcb = new pcb(PIDCOUNT++,priority,newBlock);
        newBlock.usedBy = newPcb;
        processTable.add(newPcb);
        if (cpuProcess == null) {
          cpuProcess = newPcb; //Get index of just added pcb.
          System.out.println("RUNNING PROCESS AT pid " + cpuProcess.pid);
        } else {
          /*Preemptive. If higher priority, stop current cpuprocess and put this one.*/
          if (cpuProcess.priority < priority) {
            //System.out.println("ddingrdy1         " + cpuProcess);
            readyQueue.add(cpuProcess);
            cpuProcess = newPcb;
            System.out.println("RUNNING PROCESS AT pid " + cpuProcess.pid);
          }
          else {
            //System.out.println("ddingrdy2         " + (processTable.size() - 1));
            readyQueue.add(newPcb);
          }
        }
      }
    } else if (input.equals("t")) { 
      /*Terminate the process currently using the CPU.
      pop from ReadyQueue. Make cpuProcess = that index.*/
      mainMemory.remove(cpuProcess.ram);
      processTable.remove(cpuProcess);
      updateCpu();
    } else if (input.equals("d")) { 
      /*Currently cpu running process reads/writes file, file_name, into hard disk #<hardDisk>*/
      int diskNum = Integer.parseInt(s.next());
      String file_name = s.next();
      System.out.println("Writing " + file_name + " to hard disk # " + diskNum);
      //Put cpuProcess into IOQueue with fileName.
      IOQueue.get(diskNum).add(new file(cpuProcess,file_name)); 
      updateCpu();   
    } else if (input.equals("D")) {
      /* hard disk #<hardDisk> has finished the work for one process.*/  
      int diskNum = Integer.parseInt(s.next());
      System.out.println("Written file onto hard disk # " + diskNum);
      file writtenFile = IOQueue.get(diskNum).pollFirst();
      if (cpuProcess == null) {
        cpuProcess = writtenFile.process; //Get index of just added pcb.
        System.out.println("RUNNING PROCESS AT pid " + cpuProcess.pid);
      } else {
        /*Preemptive. If higher priority, stop current cpuprocess and put this one.*/
        if (cpuProcess.priority < writtenFile.process.priority) {
          //System.out.println("ddingrdy1         " + cpuProcess);
          readyQueue.add(cpuProcess);
          cpuProcess = writtenFile.process;
          //System.out.println("RUNNING PROCESS AT pid " + cpuProcess.pid);
        }
        else {
          //System.out.println("ddingrdy2         " + (processTable.size() - 1));
          readyQueue.add(writtenFile.process);
        }
      }
    } else if (input.equals("S")) { 
      input = s.next();
      if (input.equals("r")) {
        /*Show CPU process and ready queue processes. PID and priority.*/
        displayPT();
        System.out.println("Displaying cpuProcess:");
        System.out.println(cpuProcess.pid);
        displayRQ();
        displayMem();
      } else if (input.equals("i")) {
        /*Show processes curr using hardDisks and what processes waiting for them. filenamesetc*/
        //Iterate through IOQueue
        for (int i=0; i<IOQueue.size();i++) {
          System.out.println("Showing operations on hardDisk " + i);
          for (int j=0; j<IOQueue.get(i).size();j++) {
            System.out.println("PID : " + IOQueue.get(i).get(j).process.pid + " |" + IOQueue.get(i).get(j).name);
          }
        }
      } else if (input.equals("m")) {
        /*Show state of memory. Range of memory addresses used by each process in system.*/
        displayMem();
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
    System.out.println("readyQueue.size(): " + readyQueue.size());
    for (int i=0; i < readyQueue.size(); i++) {
      System.out.println("RQ pid : " + readyQueue.get(i).pid); 
    }
  }
  private static void displayMem() {
    System.out.println("Displaying Main Memory...");
    for (int i=0; i < mainMemory.size(); i++) {
      System.out.println("MEM : " + mainMemory.get(i).base.toString() + " - " + mainMemory.get(i).limit.toString() + " |  PID : " + mainMemory.get(i).usedBy.pid);
    } 
  }
  static void updateCpu() {
    pcb highest = null;
    for (int i=0; i<readyQueue.size(); i++) {
      if (highest == null) {
        highest = readyQueue.get(i);
      } else {
        if (highest.priority < readyQueue.get(i).priority) {
          highest = readyQueue.get(i);
        } 
      }
      readyQueue.remove(highest);
      cpuProcess = highest;
      //System.out.println("RUNNING PROCESS AT pid " + cpuProcess.pid);
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





//Each hard drive that exists must have its own I/O queue.
//Have array of queues, IOQueue, that hold linked lists?
  


