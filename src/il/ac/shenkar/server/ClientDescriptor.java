package il.ac.shenkar.server;

import il.ac.shenkar.common.StringConsumer;
import il.ac.shenkar.common.StringProducer;

public class ClientDescriptor implements StringConsumer, StringProducer
{
    private MessageBoard _msgBoard;


    @Override
    public void consume(String str) {
        _msgBoard.consume(str);
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        if (_msgBoard != null){
            // ERROR
            System.out.println("There is no message");
            return;
        }
        if (sc instanceof MessageBoard){
            _msgBoard = (MessageBoard) sc;
        }
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        _msgBoard = null;
    }
}