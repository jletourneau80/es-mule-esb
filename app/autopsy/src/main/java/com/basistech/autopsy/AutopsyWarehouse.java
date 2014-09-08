package com.basistech.autopsy;

import org.apache.abdera.Abdera;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: jletourneau
 * Date: Jan 26, 2010
 * Time: 8:45:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class AutopsyWarehouse {

     private static final Log log = LogFactory.getLog(AutopsyWarehouse.class);
     private static AutopsyWarehouse instance = new AutopsyWarehouse();

     private final Map<URI, Long> cache = Collections.synchronizedMap(
            new LinkedHashMap<URI, Long>());  //uri is key and time added is value

    private Timer timerThread;
    private final Object lock = new Object();
    private static Abdera abdera = new Abdera();


    public AutopsyWarehouse(){};

    public static AutopsyWarehouse getInstance() {
        return instance;
    }

    public void addAutopsyInstance(URI uri){

         cache.put(uri, System.currentTimeMillis());

         startService();
    }

    public  Map<URI, Long> getCache(){
        return cache;
    }

    public Abdera getAbdera(){
        return abdera;
    }
    

    private void startService() {
           synchronized (lock) {

               if (timerThread == null) {

                   try {
                       timerThread = new Timer(true);
                       timerThread.schedule(new TimerTask() {
                           public void run() {
                               update();
                           }
                       }, 0, 30000L);   //30 seconds
                   } catch (IllegalStateException e) {
                       
                       timerThread = null;
                   }
               }
           }
       }


    private void update() {
        log.info("ZZZ: Manager Update");
        synchronized (lock) {
           
            if (cache.size() == 0) {
                log.info("ZZZ: empty cache. stopping monitor thread");
                timerThread.cancel();
                timerThread = null;
                return;
            }
        }

        //log.info("ZZZ: Stanag Manager Update: 2");

        long startTime = System.currentTimeMillis();
        int count = 0;

        for (Iterator it = cache.values().iterator(); it.hasNext(); ) {
            Long entry = (Long)it.next();
            if (startTime - entry > 300000L) {   //hasn't checked in in more than 5 minutes minute
                log.info("Removing Autopsy instance");
                it.remove();
                continue;
            }

        }
    }





}
