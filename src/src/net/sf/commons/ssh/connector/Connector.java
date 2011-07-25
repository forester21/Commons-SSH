package net.sf.commons.ssh.connector;


import net.sf.commons.ssh.Feature;
import net.sf.commons.ssh.auth.AuthenticationOptions;
import net.sf.commons.ssh.common.Closable;
import net.sf.commons.ssh.connection.Connection;
import net.sf.commons.ssh.errors.ErrorHolder;
import net.sf.commons.ssh.event.EventProcessor;
import net.sf.commons.ssh.options.Configurable;

import java.util.Set;

public interface Connector extends Closable,Configurable,ErrorHolder,EventProcessor
{
	void start();
    Connection createConnection();
    Connection openConnection(String host,int port,AuthenticationOptions auth);
    Set<Feature> getSupportedFeatures();
}
