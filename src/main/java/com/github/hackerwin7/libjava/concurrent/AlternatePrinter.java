package com.github.hackerwin7.libjava.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * thread 1 print A, thread 2 print B, thread 3 print C, thread 4 print D;
 * file-1 print ABCDABCDABCD...
 * file-2 print BCDABCDABCDA...
 * file-3 print CDABCDABCDAB...
 * file-4 print DABCDABCDABC...
 */
public class AlternatePrinter {
    private static final Logger LOG = LoggerFactory.getLogger(AlternatePrinter.class);
    private static final int LOOP_COUNT = 10;
    private static final List<Character> CHARACTERS = new ArrayList<Character>() {{
        add('A');
        add('B');
        add('C');
        add('D');
    }};

    public static void main(String[] args) throws Exception {
        AlternatePrinter ap = new AlternatePrinter();
        ap.execute();
    }

    public void execute() throws Exception {
        List<Semaphore> semaphores = new ArrayList<>();
        List<ThreadPrinter> threadPrinters = new ArrayList<>();
        for (int i = 0; i < CHARACTERS.size(); i++) {
            semaphores.add(new Semaphore(1));
        }
        for (int  i = 0; i < CHARACTERS.size(); i++) {
            threadPrinters.add(new ThreadPrinter(CHARACTERS.get(i), LOOP_COUNT,
                    semaphores.get(i), semaphores.get((i + 1) % CHARACTERS.size())));
        }
        // ABCDABCD...
        semaphores.get(1).acquire();
        semaphores.get(2).acquire();
        semaphores.get(3).acquire();
        startThreadsAndWaitComplete(threadPrinters);
        // BCDABCDA...
        semaphores.clear();
        for (int i = 0; i < CHARACTERS.size(); i++) {
            semaphores.add(new Semaphore(1));
        }
        threadPrinters.clear();
        for (int  i = 0; i < CHARACTERS.size(); i++) {
            threadPrinters.add(new ThreadPrinter(CHARACTERS.get(i), LOOP_COUNT,
                    semaphores.get(i), semaphores.get((i + 1) % CHARACTERS.size())));
        }
        semaphores.get(2).acquire();
        semaphores.get(3).acquire();
        semaphores.get(0).acquire();
        startThreadsAndWaitComplete(threadPrinters);
        // CDABCDAB...
        semaphores.clear();
        for (int i = 0; i < CHARACTERS.size(); i++) {
            semaphores.add(new Semaphore(1));
        }
        threadPrinters.clear();
        for (int  i = 0; i < CHARACTERS.size(); i++) {
            threadPrinters.add(new ThreadPrinter(CHARACTERS.get(i), LOOP_COUNT,
                    semaphores.get(i), semaphores.get((i + 1) % CHARACTERS.size())));
        }
        semaphores.get(0).acquire();
        semaphores.get(1).acquire();
        semaphores.get(3).acquire();
        startThreadsAndWaitComplete(threadPrinters);
        // DABCDABC...
        semaphores.clear();
        for (int i = 0; i < CHARACTERS.size(); i++) {
            semaphores.add(new Semaphore(1));
        }
        threadPrinters.clear();
        for (int  i = 0; i < CHARACTERS.size(); i++) {
            threadPrinters.add(new ThreadPrinter(CHARACTERS.get(i), LOOP_COUNT,
                    semaphores.get(i), semaphores.get((i + 1) % CHARACTERS.size())));
        }
        semaphores.get(0).acquire();
        semaphores.get(1).acquire();
        semaphores.get(2).acquire();
        startThreadsAndWaitComplete(threadPrinters);
    }

    public void startThreadsAndWaitComplete(List<? extends  Thread> threads) {
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        });
        System.out.println();
    }

    static class ThreadPrinter extends Thread {

        private final Character printChar;
        private final int loopCount;
        private final Semaphore begin;
        private final Semaphore end;

        public ThreadPrinter(Character ch, int loopCount, Semaphore begin, Semaphore end) {
            this.printChar = ch;
            this.loopCount = loopCount;
            this.begin = begin;
            this.end = end;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < loopCount; i++) {
                    begin.acquire();
                    System.out.print(printChar);
                    end.release();
                }
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
