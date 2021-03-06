/* 
 * Copyright 2005 James House 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package org.quartz.examples.example11;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * This example will spawn a large number of jobs to run
 * 
 * @author James House, Bill Kratzer
 */
public class LoadExample {

    private int _numberOfJobs = 500;
    
    public LoadExample(int inNumberOfJobs) {
        _numberOfJobs = inNumberOfJobs;
    }
    
    public void run() throws Exception {
        Log log = LogFactory.getLog(LoadExample.class);

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        log.info("------- Initialization Complete -----------");

        String schedId = sched.getSchedulerInstanceId();

        // schedule 500 jobs to run
        for (int count=1; count <= _numberOfJobs; count++) {
            JobDetail job = new JobDetail("job" + count, "group1",
                    SimpleJob.class);
            // tell the job to delay some small amount... to simulate work...
            long timeDelay = (long) (java.lang.Math.random() * 2500);
            job.getJobDataMap().put(SimpleJob.DELAY_TIME, timeDelay);
            // ask scheduler to re-execute this job if it was in progress when
            // the scheduler went down...
            job.setRequestsRecovery(true);
            SimpleTrigger trigger = new SimpleTrigger("trigger_" + count, "group_1");
            trigger.setStartTime(new Date(System.currentTimeMillis() + 10000L
                    + (count * 100)));
            sched.scheduleJob(job, trigger);
            if (count % 25 == 0) {
                log.info("...scheduled " + count + " jobs");
            }
        }
        
        
        log.info("------- Starting Scheduler ----------------");

        // start the schedule 
        sched.start();

        log.info("------- Started Scheduler -----------------");

        log.info("------- Waiting five minutes... -----------");

        // wait five minutes to give our jobs a chance to run
        try {
            Thread.sleep(300L * 1000L); 
        } catch (Exception e) {
        }

        // shut down the scheduler
        log.info("------- Shutting Down ---------------------");
        sched.shutdown(true);
        log.info("------- Shutdown Complete -----------------");

        SchedulerMetaData metaData = sched.getMetaData();
        log.info("Executed " + metaData.numJobsExecuted() + " jobs.");
    }

    public static void main(String[] args) throws Exception {

        int numberOfJobs = 500;
        if (args.length == 1) {
            numberOfJobs = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            System.out.println(
                    "Usage: java " + 
                    LoadExample.class.getName() + 
                    "[# of jobs]");
            return;
        }
        LoadExample example = new LoadExample(numberOfJobs);
        example.run();
    }

}