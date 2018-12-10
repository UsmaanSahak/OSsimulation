import java.math.BigInteger;
public class pcb {
  int pid;
  int priority;
  block ram;
  public pcb(int newPid, int newPriority, block newRam) {
    pid = newPid;
    priority = newPriority;
    ram = newRam;
  }
}

class block {
  block(BigInteger newBase, BigInteger newLimit, pcb newPcb) {
    base = newBase;
    limit = newLimit;
    usedBy = newPcb;
  }
  block(BigInteger newBase, BigInteger newLimit) {
    base = newBase;
    limit = newLimit;
    usedBy = null; //Should be set later manually.
  }
  BigInteger base;
  BigInteger limit;
  pcb usedBy;
}

class file {
  file(pcb newPcb, String newName) {
    process = newPcb;
    name = newName;
  }
  pcb process;
  String name;
} 
