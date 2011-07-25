/**
 * 
 */
package net.sf.commons.ssh.errors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.sf.commons.ssh.common.AbstractClosable;
import net.sf.commons.ssh.options.Properties;

/**
 * @author fob
 * @date 24.07.2011
 * @since 2.0
 */
public abstract class AbstractErrorHolder extends AbstractClosable implements ErrorHolder
{
	/**
	 * @param properties
	 */
	public AbstractErrorHolder(Properties properties)
	{
		super(properties);
	}

	protected List<Error> errorsContainer = new LinkedList<Error>();
	protected final Object errorLock=new Object();

	/*
	 * (non-Javadoc) return max Error level from this and children
	 * 
	 * @see net.sf.commons.ssh.errors.ErrorHolder#getStatus()
	 */
	@Override
	public ErrorLevel getStatus()
	{
		ErrorLevel result = ErrorLevel.SUCCESSFUL;
		synchronized (errorLock)
		{
			if (!errorsContainer.isEmpty())
				result = Collections.max(errorsContainer, new Comparator<Error>()
					{

						@Override
						public int compare(Error o1, Error o2)
						{
							return o1.getLevel().compareTo(o2.getLevel());
						}

					}).getLevel();
		}
		Collection<ErrorHolder> children = getChildrenHolders();

		ErrorLevel childrenStatus = ErrorLevel.SUCCESSFUL;
		if (!children.isEmpty())
			childrenStatus = Collections.max(children, new Comparator<ErrorHolder>()
				{

					@Override
					public int compare(ErrorHolder o1, ErrorHolder o2)
					{
						return o1.getStatus().compareTo(o2.getStatus());
					}
				}).getStatus();
		if (result.compareTo(childrenStatus) < 0)
			return childrenStatus;
		else
			return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.commons.ssh.errors.ErrorHolder#getAllErrors()
	 */
	@Override
	public Collection<Error> getAllErrors()
	{
		List<Error> result;
		synchronized (errorLock)
		{
			result = new ArrayList<Error>(errorsContainer);
		}
		for (ErrorHolder holder : getChildrenHolders())
			result.addAll(holder.getSelfErrors());

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.commons.ssh.errors.ErrorHolder#getSelfErrors()
	 */
	@Override
	public Collection<Error> getSelfErrors()
	{
		synchronized (errorLock)
		{
			return Collections.unmodifiableList(errorsContainer);
		}
	}

	protected void pushError(Error error)
	{
		synchronized (errorLock)
		{
			errorsContainer.add(error);
		}
	}

}
