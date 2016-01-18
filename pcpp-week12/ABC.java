import java.util.Random;
import java.io.*;
import akka.actor.*;

// -- MESSAGES --------------------------------------------------
class StartTransferMessage implements Serializable {
	public final ActorRef bank, from, to;
	public StartTransferMessage(ActorRef bank, ActorRef from, ActorRef to) {
		this.bank = bank;
		this.from = from;
		this.to = to;
	}
}

class TransferMessage implements Serializable {
	public final ActorRef from, to;
	public final int amount;
	public TransferMessage(ActorRef from, ActorRef to, int amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}
	
}

class DepositMessage implements Serializable {
	public final int amount;
	public DepositMessage(int amount) {
		this.amount = amount;
	}
}

class PrintBalanceMessage implements Serializable {
	public PrintBalanceMessage() {
	}
}

// -- ACTORS --------------------------------------------------
class AccountActor extends UntypedActor {
	public int balance;

	public void onReceive(Object message) throws Exception {
		if (message instanceof DepositMessage) {
			DepositMessage msg = (DepositMessage) message;
			balance += msg.amount;
		}
		else if (message instanceof PrintBalanceMessage) {
			PrintBalanceMessage msg = (PrintBalanceMessage) message;
			System.out.println("Balance: " + balance);
		}
	}
}

class BankActor extends UntypedActor {
	public void onReceive(Object message) throws Exception {
		if (message instanceof TransferMessage) {
			TransferMessage msg = (TransferMessage) message;
			msg.from.tell(new DepositMessage(-msg.amount), ActorRef.noSender());
			msg.to.tell(new DepositMessage(msg.amount), ActorRef.noSender());
		}
	}
}

class ClerkActor extends UntypedActor {
	private void ntransfers(int n, ActorRef bank, ActorRef from, ActorRef to) {
		Random rnd = new Random();
		for (; n > 0 ; n--) {
			bank.tell(new TransferMessage(from, to, rnd.nextInt(10)), ActorRef.noSender());
		}
	}

	Random rnd = new Random();
	public void onReceive(Object message) throws Exception {
		if (message instanceof StartTransferMessage) {
			StartTransferMessage msg = (StartTransferMessage) message;
			ntransfers(100, msg.bank, msg.from, msg.to);
		}
	}	
}

// -- MAIN --------------------------------------------------
public class ABC { // Demo showing how things work:
	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("ABCSystem");

		final ActorRef a1 = system.actorOf(Props.create(AccountActor.class), "a1");
		final ActorRef a2 = system.actorOf(Props.create(AccountActor.class), "a2");
		final ActorRef b1 = system.actorOf(Props.create(BankActor.class), "b1");
		final ActorRef b2 = system.actorOf(Props.create(BankActor.class), "b2");
		final ActorRef c1 = system.actorOf(Props.create(ClerkActor.class), "c1");
		final ActorRef c2 = system.actorOf(Props.create(ClerkActor.class), "c2");

		c1.tell(new StartTransferMessage(b1, a1, a2), ActorRef.noSender());
		c2.tell(new StartTransferMessage(b2, a2, a1), ActorRef.noSender());
		
		try {
			System.out.println("Press return to inspect...");
			System.in.read();

			/* TODO (INSPECT FINAL BALANCES) */
			a1.tell(new PrintBalanceMessage(), ActorRef.noSender());
			a2.tell(new PrintBalanceMessage(), ActorRef.noSender());

			System.out.println("Press return to terminate...");
			System.in.read();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			system.shutdown();
		}
	}
} 