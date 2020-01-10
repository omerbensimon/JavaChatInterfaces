package il.ac.shenkar.common;

public interface StringProducer {
    public void addConsumer(StringConsumer sc);
    public void removeConsumer(StringConsumer sc);
}