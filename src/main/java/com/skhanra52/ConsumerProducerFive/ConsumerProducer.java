package com.skhanra52.ConsumerProducerFive;


import java.util.Random;

class MessageRepository {
    // Both producer and consumer will interact with the message.
    private String message;
    /*
     It will indicate to both threads, whether there are work for them to do. When hasMessage is false,
     the Producer can populate the shared message. When hasMessage is true, the Consumer can read it.
     */
    private boolean hasMessage = false;

    /**
     * Methods that read the message, as well as write to it, or populate it. Because the message is a shared resource,
     * we'll synchronize the read method. When the consumer class calls this method, it will wait until there's a message
     * to read. We'll set this up with a while loop on the Message flag.
     * -> If there's no message, it will stay in this while loop. I'll just make this an empty block.
     * -> Once the message is successfully retrieved, this code will set hasMessage to false.
     * -> Finally, the method returns the message to the consumer, who's waiting for it.
     */
    public synchronized String read() {
        while(!hasMessage){

        }
        hasMessage = false;
        return message;
    }

    /**
     * The write method will also be synchronized, and it takes a String as an argument. If there's already
     * a message in the message repository, it will hang out and wait, presumably until the Consumer has a
     * chance to read the message, and set this flag to false.
     */
    public synchronized void write(String message) {
        while (hasMessage){

        }
        hasMessage = true;
        this.message = message;
    }
}

// Producer class
class MessageWriter implements Runnable {
    private MessageRepository outgoingMessage;
    private final String text = """
            Humpty Dumpty sat on a wall,
            Humpty Dumpty had a great fall,
            All the king's horses and so on.
            Couldn't put Humpty together again.
            """;

    public MessageWriter(MessageRepository outgoingMessage) {
        this.outgoingMessage = outgoingMessage;
    }

    @Override
    public void run() {
        // Used random, to randomize the time that the thread sleeps.
        //  We can get the lines of text, by splitting the text block by the new line character.
        Random random = new Random();
        String[] lines = text.split("\n");

        for (int i=0; i<lines.length; i++){
            outgoingMessage.write(lines[i]);
            try {
                Thread.sleep(random.nextInt(500, 2000));
            }catch (InterruptedException e){
               throw new RuntimeException();
            }
        }
        outgoingMessage.write("Finished");
    }
}

// consumer class
class MessageReader implements Runnable {
    private MessageRepository incomingMessage;

    public MessageReader(MessageRepository incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    @Override
    public void run() {

        Random random = new Random();
        String latestMessage = "";
        do {
            try {
                // put the thread to sleep first. This gives the writer  a bit of time to get its message out there,
                // before this code attempts to read it.
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latestMessage = this.incomingMessage.read();
            System.out.println(latestMessage);
        } while (!latestMessage.equals("Finished"));

    }
}
/*
 Continuation from SynchronizationFour...
 wait and notify methods that are defined on the object class and used when the thread acquire the monitor lock.
 If we run the main, it may hang, we need to manually stop the program. This is due to deadlock, live lock, starvation.

 DeadLock:
    A deadlock usually occurs, when you have two or more threads accessing multiple shared resources.
    Thread A is our Consumer, the MesssageReader. It can usually get in, to run the read method, because the hasMessage
    flag is usually true. So when that flag is true, it won't go into the while loop.
    But If for some reason, the flag is false, it will execute it's while loop, and that's where the problem lies.
    It's waiting on that hasMessage flag to change value, to exit the loop.
    But because of the way this code is currently written, That flag is never going to change its value.
    Thread A has acquired a lock on the shared resource, in this case the Message Repository,
    and Thread B can't get that lock, so it's blocked. Because Thread B is blocked, it can't change the flag,
    that would set the condition to let Thread A exit it's while loop, and release the lock. The threads are stuck,
    one spinning indefinitely, the other blocked from doing anything. This is a classic deadlock situation,
    and the resolution isn't always pretty. In this case, I have to shut down the application, or kill it manually.
    This situation could be equally true in reverse. This means the Producer could be in its while loop, waiting on
    the Consumer to flip the flag, but the Consumer can't get the lock to do it. So what can we do?

 */

public class ConsumerProducer {

    public static void main(String[] args) {
        MessageRepository messageRepository = new MessageRepository();
        Thread reader = new Thread(new MessageReader(messageRepository), "Thread A");
        Thread writer = new Thread(new MessageWriter(messageRepository), "Thread B");

        reader.start();
        writer.start();
    }
}

