import java.math.BigInteger;
public class pcb {
  int pid;
  int priority;
  public pcb(int newPid, int newPriority) {
    pid = newPid;
    priority = newPriority;
  }
}

class block {
  block(BigInteger newBase, BigInteger newLimit) {
    base = newBase;
    limit = newLimit;
  }
  BigInteger base;
  BigInteger limit;
}
