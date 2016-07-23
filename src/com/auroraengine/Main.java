/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine;

import com.auroraengine.client.ClientCore;
import com.auroraengine.client.Session;
import com.auroraengine.data.ProgramProperties;
import static com.auroraengine.data.ProgramProperties.AURORA_CORE_VERSION;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.server.ServerCore;
import com.auroraengine.server.ServerException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the class from which the program is launched for at least all test
 * cases and in all liklihood the final product as well. It cannot be
 * implemented or extended and serves solely as a carrier of the static methods
 * relating to the initialisation of the program.
 *
 * @author A.J.H.
 * @version 0.0.1 Development
 */
public final class Main {

	private static final Logger LOG = AuroraLogs.getLogger(Main.class);

	/**
	 * Creates a test environment using the specified arguments. The meaning of
	 * the arguments are defined in the javadoc for <code>start()</code>, as
	 * well as the output for <code>help()</code>.
	 *
	 * @param args The arguments to run the program with.
	 * @see Main#start(java.lang.String[], java.util.function.Supplier,
	 * java.util.function.Supplier, java.util.function.Function)
	 */
	public static void main(String[] args) {
		ProgramProperties properties = new ProgramProperties("test", AURORA_CORE_VERSION);
		start(args,
				// The client creation function:
				() -> {
					try {
						return new ClientCore(properties, new Session("test_user", properties));
					} catch (AuroraException ex) {
						LOG.log(Level.SEVERE, "Exception in formation of Properties or Client: {0}", ex);
						return null;
					}
				},
				// The server creation function
				() -> {
					try {
						return new ServerCore("Test Server", properties);
					} catch (ServerException ex) {
						LOG.log(Level.SEVERE, "Exception in formation of Properties or Server: {0}", ex);
						return null;
					}
				},
				// The server creation function using an existing client.
				(client) -> {
					try {
						return new ServerCore("Test Server on Client",
								client == null ? new ProgramProperties("test", AURORA_CORE_VERSION) : client.getProperties(), client);
					} catch (ServerException ex) {
						LOG.log(Level.SEVERE, "Exception in formation of Server: {0}", ex);
						return null;
					}
				});
	}

	/**
	 * Called when the program is starting, using the provided suppliers and
	 * function to create the client and/or server for this program instance.
	 * Input Arguments:
	 *
	 * -c | --client | Starts a client.
	 *
	 * -s | --server | Starts a server.
	 *
	 * -h | --help | Cancels loading of the program and outputs the help.
	 *
	 * @param args The arguments
	 * @param client The supplier of the client core
	 * @param server The supplier of the independent server core
	 * @param dependent The supplier of the dependent server core
	 */
	public static void start(String[] args, Supplier<ClientCore> client,
			Supplier<ServerCore> server, Function<ClientCore, ServerCore> dependent) {
		boolean fatal_error = false, runclient = false, runserver = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--")) {
				// This argument is a complete one
				switch (args[i]) {
					case "--client":
						runclient = true;
						break;
					case "--server":
						runserver = true;
						break;
					case "--help":
						help();
						return;
					default:
						LOG.log(Level.SEVERE,
								"Provided argument \"{0}\" not recognised!", args[i]);
						fatal_error = true;
				}
			}
			for (int j = 1; j < args[i].length(); j++) {
				switch (args[i].charAt(j)) {
					case 'c':
						runclient = true;
						break;
					case 's':
						runserver = true;
						break;
					case 'h':
						help();
						return;
				}
			}
		}
		if (fatal_error) {
			LOG.log(Level.SEVERE,
					"Check the raised errors to correctly start this program.");
		} else if (runclient && runserver) {
			ClientCore c = client.get();
			c.start();
			ServerCore s = dependent.apply(c);
			s.start();
		} else if (runserver) {
			server.get().start();
		} else {
			client.get().start();
		}
	}

	/**
	 * Outputs the commands from the javadoc of start().
	 *
	 * @see Main#start(java.lang.String[], java.util.function.Supplier,
	 * java.util.function.Supplier, java.util.function.Function)
	 */
	public static void help() {
		// TODO: Print the javadoc for the start command
		System.out.println(
				"-c | --client | Starts a client.\n"
				+ "-s | --server | Starts a server.\n"
				+ "-h | --help   | Cancels loading of the program and outputs the help."
		);
	}

	private Main() {
	}
}
