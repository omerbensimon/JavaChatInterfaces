package il.ac.shenkar.server;

import il.ac.shenkar.common.ConnectionProxy;
import il.ac.shenkar.common.StringConsumer;
import il.ac.shenkar.common.StringProducer;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class MessageBoard implements StringConsumer, StringProducer {
    LinkedList<ConnectionProxy> _consumers;

    public MessageBoard() {
        _consumers = new LinkedList<ConnectionProxy>();
    }


    @Override
    public void consume(String message) {
        for (ConnectionProxy tempCon : _consumers) {
            tempCon.consume(message);
        }
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        if (sc instanceof ConnectionProxy) {
            _consumers.add((ConnectionProxy) sc);
        }
        //TODO: err
    }

    @Override
    public void removeConsumer(StringConsumer sc) {

    }
}
