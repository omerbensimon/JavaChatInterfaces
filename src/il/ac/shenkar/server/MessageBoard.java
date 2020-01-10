package il.ac.shenkar.server;

import il.ac.shenkar.common.ConnectionProxy;
import il.ac.shenkar.common.StringConsumer;
import il.ac.shenkar.common.StringProducer;

import java.util.LinkedList;

public class MessageBoard implements StringConsumer, StringProducer {
    LinkedList<ConnectionProxy> _consumers;

    public MessageBoard() {
        _consumers = new LinkedList<ConnectionProxy>();
    }


    @Override
    public void consume(String message) {
        ConnectionProxy tempCon;
        for (int i = 0; i < _consumers.size(); i++) {
            tempCon = _consumers.get(i);
            if (tempCon.isClosed()) {
                _consumers.remove(tempCon);
                i--;
                continue;
            }
            tempCon.consume(message);
        }
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        if (sc instanceof ConnectionProxy) {
            _consumers.add((ConnectionProxy) sc);
        }
    }

    @Override
    public void removeConsumer(StringConsumer sc) {

    }
}
