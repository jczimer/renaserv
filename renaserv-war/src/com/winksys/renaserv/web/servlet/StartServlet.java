package com.winksys.renaserv.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

@WebListener("/")
public class StartServlet implements ServletContextListener {
	
	private Logger LOG = Logger.getLogger(StartServlet.class); 

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		SchedulerFactory shedFact = new StdSchedulerFactory();
        try {
               Scheduler scheduler = shedFact.getScheduler();
               scheduler.start();
               JobDetail job = JobBuilder.newJob(ValidatorJob.class)
                             .withIdentity("validadorJOB", "grupo01")
                             .build();
               Trigger trigger = TriggerBuilder.newTrigger()
                             .withIdentity("validadorTRIGGER","grupo01")
                             .withSchedule(CronScheduleBuilder.cronSchedule("0 */10 * * * ?"))
                             .build();
               scheduler.scheduleJob(job, trigger);
               
               LOG.info("Quartz iniciado");
        } catch (SchedulerException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
        }
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	
	
}
