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
     * -> If there's no message, it will stay in this while loop. We'll just make this an empty block.
     * -> Once the message is successfully retrieved, this code will set hasMessage to false.
     * -> Finally, the method returns the message to the consumer, who's waiting for it.
     */
    public synchronized String read() {
        while(!hasMessage){
            // added the wait in side loop to check the condition to satisfied, as notifyAll() awakens all threads
            // at a time. If we remove this wait() the code will get hanged.
            try {
                wait();  // don't have message then waits after releasing lock
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        hasMessage = false; // indicates message is read and no new message available.
        notifyAll();
        return message;
    }

    /**
     * The write method will also be synchronized, and it takes a String as an argument. If there's already
     * a message in the message repository, it will hang out and wait, presumably until the Consumer has a
     * chance to read the message, and set this flag to false.
     */
    public synchronized void write(String message) {
        while (hasMessage){
            try {
                wait();     // Has message to read, then waits till message is read to produce new message.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        hasMessage = true; // indicates new message available to read.
        notifyAll();
        this.message = message;
    }
}

// Producer class to implement deadlock situation demo
class MessageWriterWithDeadLock implements Runnable {
    private MessageRepository outgoingMessage;
    private final String text = """
            Humpty Dumpty sat on a wall, Humpty Dumpty had a great fall,
            All the king's horses and so on.
            Couldn't put Humpty together again.
            """;

    public MessageWriterWithDeadLock(MessageRepository outgoingMessage) {
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

// consumer class to implement deadlock situation.
class MessageReaderWithDeadLock implements Runnable {
    private MessageRepository incomingMessage;

    public MessageReaderWithDeadLock(MessageRepository incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    @Override
    public void run() {

        Random random = new Random();
        String latestMessage = "";
        do {
            try {
                // put the thread to sleep first. This gives the writer a bit of time to get its message out there,
                // before this code attempts to read it.
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latestMessage = this.incomingMessage.read();
            System.out.println("latest Message........."+"\n"+latestMessage);
        } while (!latestMessage.equals("Finished"));

    }
}
/*
 Continuation from SynchronizationFour...
 wait and notify methods that are defined on the Object class and used when the thread acquire the monitor lock.
 If we run the main, it may hang, we need to manually stop the program. This is due to deadlock, live lock, starvation.

 DeadLock:
    A deadlock usually occurs, when you have two or more threads accessing multiple shared resources.
    Thread A is our Consumer, the MesssageReaderWithDeadLock. It can usually get in, to run the read method,
    because the hasMessage flag is usually true. So when that flag is true, it won't go into the while loop.
    But If for some reason, the flag is false, it will execute it's while loop, and that's where the problem lies.
    It waits for that hasMessage flag to change value, to exit the loop.
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
        Thread reader = new Thread(new MessageReaderWithDeadLock(messageRepository), "Thread A");
        Thread writer = new Thread(new MessageWriterWithDeadLock(messageRepository), "Thread B");

        reader.start();
        writer.start();
    }
}

/*
 The Object class's wait(), notify() and notifyAll() methods: ---------------------------
    -> The wait(), notify() and notifyAll() methods are used to manage some monitor lock situations to prevent threads
       from blocking indefinitely.
    -> Because these methods are in Object class, any instance of any class can execute these methods from within a
       synchronized method or statement.
    -> The wait() method in java is an instance of java.long.Object class that is used for inter-thread communication.
       It is typically called inside a synchronized block or method and causes the current thread to pause until
       another thread calls notify() or notifyAll() method on the same object.

    What wait() does ?---------------
    -> Puts the current thread into a waiting state on the current object monitor.
    -> The thread releases the lock hold on that object, so that the other thread can enter the synchronized block of
       the code after notify() from the other thread.
    -> The thread resumes only when:
        -> Another thread calls notify() or notifyAll() method on the same Object, or
        -> The waiting time(if a timeout given) expires or
        -> It is interrupted(throws InterruptedException).
     -> wait() has three overloaded versions
        public final void wait() throws InterruptedException
        public final void wait(long timeout) throws InterruptedException
        public final void wait(long timeout, int nanos) trows InterruptedException

        Typical usage pattern:
        Because of possible spurious awakened (threads waking up without notify()), wait() is usually
        placed inside a loop:

        synchronized (lock) {
            while (condition == false) {
                lock.wait(1000);   // or wait(), wait(1000, 500000)
            }
            // now condition is true, proceed
        }

        On the notifying side:---------

        synchronized (lock) {
            condition = true;
            lock.notify();   // or notifyAll()
        }

        notify() method : ---------------------------------------------------

        public final void notify() throws InterruptedException

        It wakes up a single thread that is waiting on this object's monitor. If any thread is waiting on this object,
        One of them would be chosen to be awakened. The choice is arbitrary and occurs at the discretion of the implementation.
        A thread waits on an Object's monitor by calling one of the wait() method.

        The awakened thread will not be able to proceed until the current thread relinquishes the lock on this object.
        Awakened thread will compete in the usual manner with any other threads that might be actively competing to
        synchronize on this object. For Example: The awakened thread enjoys no reliable privilege or disadvantage in
        being the next thread to lock this object.

        The notify() method should be called only by the owner of the object's monitor. A thread becomes the owner of
        the object's monitor in one of the three ways:
            1. By executing a synchronized instance method of that object.
            2. By executing the body of the synchronized statement that synchronizes on that object.
            3. For object of type class, by executing the synchronized static method of that class.
            Only one thread at a time can own the object monitor.



 */

