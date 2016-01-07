import java.util.Random; 
import java.io.*; 
import akka.actor.*;

// -- MESSAGES --------------------------------------------------
class StartTransferMessage implements Serializable { 
	public final ActorRef bank, from, to;
	public StartTransferMessage(ActorRef bank, ActorRef from, ActorRef to){
		this.bank = bank;
		this.from = from;
		this.to = to;
	}
}

class TransferMessage implements Serializable {
	public final int amount;
	public final ActorRef from, to;
	
	public TransferMessage(int amount, ActorRef from, ActorRef to){
		this.amount = amount;
		this.from = from;
		this.to = to;
	}
}

class DepositMessage implements Serializable {
	public final int amount;
	public DepositMessage(int amount){
		this.amount = amount;
	}
}

class PrintBalanceMessage implements Serializable {
	public PrintBalanceMessage(){
	}
}

// -- ACTORS --------------------------------------------------
class AccountActor extends UntypedActor {
	public int balance;

	public void onReceive(Object o) throws Exception {
		if (o instanceof DepositMessage){
			DepositMessage msg = (DepositMessage) o;
			balance += msg.amount;
		}
		else if(o instanceof PrintBalanceMessage){
			System.out.printf("Balance = %d%n", balance);
		}
	}
}
class BankActor extends UntypedActor {

	public void onReceive(Object o) throws Exception {
		if (o instanceof TransferMessage){
			TransferMessage msg = (TransferMessage) o;
			msg.from.tell(new DepositMessage(-msg.amount), ActorRef.noSender());
			msg.to.tell(new DepositMessage(msg.amount), ActorRef.noSender());
		}
	}
}
class ClerkActor extends UntypedActor {

	private void ntransfers(int N, ActorRef bank, ActorRef from, ActorRef to){
		if(N == 0){ return; }
		else{
			Random R = new Random();
			bank.tell(new TransferMessage(R.nextInt(10), from, to), ActorRef.noSender());
			ntransfers(N-1, bank, from, to);
		}
	}
	
	public void onReceive(Object o) throws Exception {
		if (o instanceof StartTransferMessage){
			StartTransferMessage msg = (StartTransferMessage) o;
			ntransfers(100, msg.bank, msg.from, msg.to);
		}
	}
}

// -- MAIN --------------------------------------------------
public class ABC { // Demo showing how things work:
	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("ABCSystem");
		final ActorRef A1 = system.actorOf(Props.create(AccountActor.class), "Account1");
		final ActorRef A2 = system.actorOf(Props.create(AccountActor.class), "Account2");
		final ActorRef B1 = system.actorOf(Props.create(BankActor.class), "Bank1");
		final ActorRef B2 = system.actorOf(Props.create(BankActor.class), "Bank2");
		final ActorRef C1 = system.actorOf(Props.create(ClerkActor.class), "Clerk1");
		final ActorRef C2 = system.actorOf(Props.create(ClerkActor.class), "Clerk2");

		C1.tell(new StartTransferMessage(B1, A1, A2), ActorRef.noSender());
		C2.tell(new StartTransferMessage(B2, A2, A1), ActorRef.noSender());

		try {
			System.out.println("Press return to inspect...");
			System.in.read();
			A1.tell(new PrintBalanceMessage(), ActorRef.noSender());
			A2.tell(new PrintBalanceMessage(), ActorRef.noSender());
			System.out.println("Press return to terminate...");
			System.in.read();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			system.shutdown();
		}
	}	
} 