Pre
Ask:
How much RAM memory is there on the simulated computer? (can be up to 4 billion, so use datatypes that do not overflow)
How many hard disks does the simulated compute have (enumerations start at 0).

Pres
Listens for user inputs (no confirmation). User inputs signal some system events. Possible inputs are:

A priority memory_size
'A' means new process has been creative. This process has a priority 'priority' and requires 'memory_size' bytes of memory. When choosing a PID for a new process start from 1 and go up. Do NOT reuse PIDs of the terminated processes.

t
Process currently using the CPU terminates. It leaves system immediately. Make sure you release te memory used by this process. When process is gone from sstem it is also gone from the page table. It can cause indees of some processes in page tbale to change. Must apply change of indexes to all affected processes in the CPU, ready-queue and I/O-queues.

d number file_name
THe process that that curretnly uses the CPU requrests the hard disk number 'number'. It wants to read or write the file 'file_name'.

D number Hard disk number 'number' has finished the work for one process.

S r
Shows a process currently using the CPU and processes waiting in the ready-queue. PID and priority show per process. Mark the process using the CPU.

S i
Shows what processes are currently using the hard disks and what processes are waiting to use them. For each busy hard disk show the process that uses it and show its I/O-queue.Make sure to display the filenames (from the d command) for each process. THe enumberation of hard disks start from -. Processes in I/)-queue should be displayed in the correct ueue order. Remember that our I/O-queues are FCFS.

S m
Shows the state of memory. SHow the range of memory addresses used by each process in the system.







Read up on race condition, mutexes, and semaphores.

Scenarios:
critical section, bounded-buffer, readers-writers, dining philosophers

potential problems:
race condition, deadlock







How do:

Premptive priority CPU-scheduling. Higher numbers = higher priority. The one with the highest priority among available processes will be given the CPU next. 

I/O on the other hand is just first come first serve. (Not preemptive scheduling)

Assigns memory blocks haiving consecutive addresses. Uses base and limit registers.
Physical Address = base register address + logical address/Virtual address.



DEC 5th

Memory Management
