package in.vasista.vsales.payment;

import java.util.Date;

public class Payment {
	String id;
	Date date;
	String type,status,from,to;
	double amount_paid,amount_balance;

	public Payment(String id, Date date, String type, String status, String from, String to, double amount_paid, double amount_balance) {
		this.id = id;
		this.date = date;
		this.type = type;
		this.status = status;
		this.from = from;
		this.to = to;
		this.amount_paid = amount_paid;
		this.amount_balance = amount_balance;
	}

	public String getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public String getStatus() {
		return status;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public double getAmount_paid() {
		return amount_paid;
	}

	public double getAmount_balance() {
		return amount_balance;
	}

	@Override
	public String toString() {
		return "Payment{" +
				"id='" + id + '\'' +
				", date=" + date +
				", type='" + type + '\'' +
				", status='" + status + '\'' +
				", from='" + from + '\'' +
				", to='" + to + '\'' +
				", amount_paid=" + amount_paid +
				", amount_balance=" + amount_balance +
				'}';
	}

}
