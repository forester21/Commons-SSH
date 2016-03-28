package net.sf.commons.ssh.impl.ussh;

import net.sf.commons.ssh.Feature;
import net.sf.commons.ssh.common.Status;
import net.sf.commons.ssh.connection.Connection;
import net.sf.commons.ssh.connector.AbstractConnector;
import net.sf.commons.ssh.connector.SupportedFeatures;
import net.sf.commons.ssh.options.Properties;
import net.sf.commons.ssh.verification.VerificationRepository;

import java.io.IOException;

/**
 * Created by anku0315 on 28.03.2016.
 */
//TODO: Is ASYNCHRONOUS or SYNCHRONOUS?
@SupportedFeatures({Feature.AUTH_CREDENTIALS, Feature.SSH1, Feature.CONNECTION_TIMEOUT, Feature.CONNECT_WITHOUT_AUTHENTICATE, Feature.SESSION_SHELL, Feature.ASYNCHRONOUS, Feature.CONNECTION_TIMEOUT})
public class UnixSshConnector extends AbstractConnector {

    public UnixSshConnector(Properties properties) {
        super(properties);
        setContainerStatus(Status.CREATED);
    }

    @Override
    protected void closeImpl() throws IOException {

    }

    //TODO: merged
    @Override
    public Connection createConnection() {
        Connection connection = new UnixSshConnection(this);
        registerChild(connection);
        return connection;
    }



    @Override
    public boolean isClosed() {
        return getContainerStatus() == Status.CLOSED;
    }
}
