package interfaces;
import enumerations.PaymentMethod;
public interface Payable {
double calculateTotal(); 
void pay(PaymentMethod method);
}
