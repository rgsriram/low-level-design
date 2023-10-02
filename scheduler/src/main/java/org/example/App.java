package org.example;

import org.example.service.ISchedulerService;
import org.example.service.SchedulerService;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ISchedulerService service = SchedulerService.getInstance();

        System.out.println("@t:"+ Calendar.getInstance().getTime());

        Runnable task1 = () -> System.out.println("Running task 1 @t:"+ Calendar.getInstance().getTime());
        Runnable task2 = () -> System.out.println("Running task 2 @t:"+ Calendar.getInstance().getTime());

        service.schedule(task1, 2, TimeUnit.SECONDS);
        service.schedule(task2, 1, TimeUnit.SECONDS);

        Runnable task3 = () -> System.out.println("Running task 3 @t:"+ Calendar.getInstance().getTime());
        service.scheduleAtFixedRate(task3, 1, 2, TimeUnit.SECONDS);

        //System.out.println( "Hello World!" );
    }
}
