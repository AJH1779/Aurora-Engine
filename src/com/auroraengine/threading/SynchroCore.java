/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.threading;

import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for thread looping.
 *
 * Tested 29/08/2014. This method works and does not require replacing. If the
 * functionality is not desired, alter it.
 *
 * @author AJH1779
 * @version InDev 0.1.0
 */
public abstract class SynchroCore implements Runnable {

	private static final Logger LOG = AuroraLogs.getLogger(SynchroCore.class);

	private final String name;
	private final SynchroCore master;
	private volatile boolean halted = false, running = false,
			looping = false, threading = false;
	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private Thread thread;

	/**
	 * Creates a new inmaster thread.
	 *
	 * @param name
	 */
	public SynchroCore(String name) {
		this(name, null);
	}

	/**
	 * Returns a new thread that is running so long as the provided thread is
	 * running.
	 *
	 * @param name
	 * @param master
	 */
	public SynchroCore(String name, SynchroCore master) {
		this.name = name;
		this.master = master;
		if (master != null) {
			LOG.log(Level.INFO,
					"New Dependent Synchro \"{0}\" Created, master on Synchro \"{1}\"",
					new Object[]{name, master});
		} else {
			LOG.log(Level.INFO, "New Inmaster Synchro \"{0}\" Created", name);
		}
	}
	
	/**
	 * 
	 * @return 
	 */
	public final SynchroCore getMaster() {
		return master;
	}

	/**
	 * Begins the thread with the default priority. The thread created for this
	 * task is returned.
	 *
	 * @return The Created Thread
	 */
	public final Thread start() {
		if (thread != null && thread.isAlive()) {
			return null;
		}
		halted = false;
		thread = new Thread(this, this.name);
		thread.start();
		LOG.log(Level.INFO, "Started New Synchro Thread \"{0}\"", this.name);
		return thread;
	}

	/**
	 * Begins the thread with the specified priority. The thread created for
	 * this task is returned.
	 *
	 * @param priority The specified priority.
	 * @return The Created Thread
	 */
	public final Thread start(int priority) {
		if (thread != null && thread.isAlive()) {
			return null;
		}
		halted = false;
		thread = new Thread(this, this.name);
		thread.setPriority(priority);
		thread.start();
		LOG.log(Level.INFO,
				"Started New Synchro Thread \"{0}\" with Priority {1}",
				new Object[]{this.name, priority});
		return thread;
	}

	public final Thread getThread() {
		return thread;
	}

	/**
	 * Creates a new thread with the specified name and priority, waiting this
	 * thread until the created thread is confirmed as created.
	 *
	 * @param synchro
	 * @param priority
	 */
	public final void waitForStart(SynchroCore synchro, int priority) {
		LOG.log(Level.INFO, "Synchro \"{0}\" is waiting for \"{1}\" to start.",
				new Object[]{this.name, synchro.name});
		lock.lock();
		try {
			if (synchro.master == this && !synchro.getAlive()) {
				synchro.start(priority);
				condition.await();
			}
		} catch (InterruptedException ex) {
			LOG.log(Level.SEVERE,
					"Synchro \"{0}\" interrupted whilst waiting for \"{1}\" to start. "
					+ "Synchro \"{1}\" may have not started! Exception: \"{2}\"",
					new Object[]{this.name, synchro.name, ex});
		} finally {
			lock.unlock();
		}
		LOG.log(Level.INFO, "Synchro \"{0}\" is now resuming.", this.name);
	}

	/**
	 * Holds this thread until the specified thread has stopped running.
	 *
	 * @param synchro
	 */
	public final void waitForStop(SynchroCore synchro) {
		LOG.log(Level.INFO, "Synchro \"{0}\" is waiting for \"{1}\" to stop.",
				new Object[]{this.name, synchro.name});
		lock.lock();
		try {
			if (synchro.master == this && synchro.getAlive()) {
				condition.await();
			}
		} catch (InterruptedException ex) {
			LOG.log(Level.SEVERE,
					"Synchro \"{0}\" interrupted whilst waiting for \"{1}\" to stop. "
					+ "Synchro \"{1}\" may have not stoppped! Exception: \"{2}\"",
					new Object[]{this.name, synchro.name, ex});
		} finally {
			lock.unlock();
		}
		LOG.log(Level.INFO, "Synchro \"{0}\" is now resuming.", this.name);
	}

	/**
	 * Safely closes the thread.
	 */
	public final void synchroClose() {
		lock.lock();
		try {
			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Terminates the update without an exception being thrown. This is useful
	 * for abruptly ending a update externally without relying on the specific
	 * mechanics of the update.
	 */
	public final void halt() {
		halted = true;
	}

	/**
	 * Returns true if the thread has been halted, false otherwise.
	 *
	 * @return If the thread has been halted.
	 */
	public final boolean getHalted() {
		return halted;
	}

	/**
	 * Returns true if the thread is still alive, false otherwise. This should
	 * be called in favour of <code>getThreading()</code> as it will react to
	 * uncaught exceptions.
	 *
	 * @return
	 */
	public final boolean getAlive() {
		return thread != null ? thread.isAlive() : false;
	}

	/**
	 * Returns true if the thread exists and is running.
	 *
	 * @return If this Synchro is running in a thread.
	 */
	public final boolean getThreading() {
		return threading;
	}

	/**
	 * Returns true if the program is in the initialisation or looping stage.
	 *
	 * @return If this thread is running.
	 */
	public final boolean getRunning() {
		return running;
	}

	/**
	 * Returns true if this thread is currently in the looping stage.
	 *
	 * @return If this thread is looping.
	 */
	public final boolean getLooping() {
		return looping;
	}

	/**
	 * The thread update.
	 */
	@Override
	public void run() {
		try {
			halted = false;
			threading = true;
			if (master != null) {
				master.synchroClose();
			}
			initialise();
			running = true;
			looping = true;
			while (!halted
					&& (master == null
					|| (master.getRunning() && master.getAlive()))
					&& isRunning()) {
				update();
			}
		} catch (AuroraException ex) {
			looping = false;
			running = false;
			processException(ex);
		} finally {
			looping = false;
			running = false;
			try {
				shutdown();
			} catch (Exception ex) {
				LOG.log(Level.SEVERE, "Failed to close cleanly due to Exception: {0}", ex);
			}
			threading = false;
			if (master != null) {
				master.synchroClose();
			}
		}
	}

	/**
	 * The method called at the beginning of thread creation. At this time,
	 * <code>getThreading()</code> returns true whilst <code>getRunning()</code>
	 * and <code>getLooping()</code> returns false.
	 *
	 * This thread is only called once.
	 *
	 * This method should not be called outside of its own thread.
	 *
	 * @throws AuroraException
	 */
	protected abstract void initialise() throws AuroraException;

	/**
	 * This method is called at the beginning of each thread update. At this
	 * time, <code>getThreading()</code>, <code>getRunning()</code> and
	 * <code>getLooping()</code> all return true.
	 *
	 * Returns false if the thread is ending gracefully.
	 *
	 * This method should not be called outside of its own thread.
	 *
	 * @return If the thread should continue to run.
	 * @throws AuroraException
	 */
	protected abstract boolean isRunning() throws AuroraException;

	/**
	 * This method is called to perform each thread update. At this time,
	 * <code>getThreading()</code>, <code>getRunning()</code> and
	 * <code>getLooping()</code> all return true.
	 *
	 * This method should not be called outside of its own thread.
	 *
	 * @throws AuroraException
	 */
	protected abstract void update() throws AuroraException;

	/**
	 * This method is called to perform fatal exception handling. At this time,
	 * <code>getThreading()</code> and <code>getRunning()</code> returns true
	 * whilst <code>getLooping()</code> returns false.
	 *
	 * This method is only called once.
	 *
	 * This method should not be called outside of its own thread.
	 *
	 * @param ex The Fatal Exception
	 */
	protected abstract void processException(AuroraException ex);

	/**
	 * This method is always called when the thread is closing. Releasing of
	 * resources is performed here and a best effort attempt must be made. At
	 * this time, <code>getThreading()</code> and <code>getRunning()</code>
	 * returns true whilst <code>getLooping()</code> returns false.
	 *
	 * This is called even when <code>halt()</code> is used to end the thread.
	 * This method must account for that possibility.
	 */
	protected abstract void shutdown();

	@Override
	public String toString() {
		return name;
	}
}
