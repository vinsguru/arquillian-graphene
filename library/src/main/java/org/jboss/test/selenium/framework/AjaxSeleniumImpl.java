/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.selenium.framework;

import java.net.URL;

import org.jboss.test.selenium.browser.Browser;
import org.jboss.test.selenium.framework.internal.PageExtensions;
import org.jboss.test.selenium.framework.internal.SeleniumExtensions;
import org.jboss.test.selenium.interception.InterceptionProxy;

import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.HttpCommandProcessor;

/**
 * <p>
 * Implementation of {@link TypedSelenium} extended by methods in {@link ExtendedTypedSeleniumImpl}.
 * </p>
 * 
 * <p>
 * Internally using {@link AjaxAwareInterceptor} and {@link InterceptionProxy}.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AjaxSeleniumImpl extends ExtendedTypedSeleniumImpl implements AjaxSelenium {

    /** The reference. */
    private static final ThreadLocal<AjaxSeleniumImpl> REFERENCE = new ThreadLocal<AjaxSeleniumImpl>();

    /** The JavaScript Extensions to tested page */
    PageExtensions pageExtensions;

    /** The JavaScript Extension to Selenium */
    SeleniumExtensions seleniumExtensions;

    /**
     * The command interception proxy
     */
    InterceptionProxy interceptionProxy;

    /**
     * Instantiates a new ajax selenium.
     */
    private AjaxSeleniumImpl() {
    }

    /**
     * Instantiates a new ajax selenium.
     * 
     * @param serverHost
     *            the server host
     * @param serverPort
     *            the server port
     * @param browser
     *            the browser
     * @param contextPathURL
     *            the context path url
     */
    public AjaxSeleniumImpl(String serverHost, int serverPort, Browser browser, URL contextPathURL) {
        CommandProcessor commandProcessor =
            new HttpCommandProcessor(serverHost, serverPort, browser.getAsString(), contextPathURL.toString());
        interceptionProxy = new InterceptionProxy(commandProcessor);
        selenium = new ExtendedSelenium(interceptionProxy.getCommandProcessorProxy());
        pageExtensions = new PageExtensions(this);
        seleniumExtensions = new SeleniumExtensions(this);
        setCurrentSelenium(this);
    }

    /**
     * <p>
     * Sets the current context.
     * </p>
     * 
     * <p>
     * <b>FIXME</b> not safe for multi-instance environment
     * </p>
     * 
     * @param selenium
     *            the new current context
     */
    public static void setCurrentSelenium(AjaxSeleniumImpl selenium) {
        REFERENCE.set(selenium);
    }

    /**
     * Gets the current context from Contextual objects.
     * 
     * @param inContext
     *            the in context
     * @return the current context
     */
    static AjaxSeleniumImpl getCurrentSelenium() {
        return REFERENCE.get();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.framework.AjaxSelenium#getPageExtensions()
     */
    public PageExtensions getPageExtensions() {
        return pageExtensions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.framework.AjaxSelenium#getSeleniumExtensions()
     */
    public SeleniumExtensions getSeleniumExtensions() {
        return seleniumExtensions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.framework.AjaxSelenium#getInterceptionProxy()
     */
    public InterceptionProxy getInterceptionProxy() {
        return interceptionProxy;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public AjaxSelenium clone() throws CloneNotSupportedException {
        AjaxSeleniumImpl copy = new AjaxSeleniumImpl();
        copy.pageExtensions = new PageExtensions(copy);
        copy.seleniumExtensions = new SeleniumExtensions(copy);
        copy.interceptionProxy = this.interceptionProxy.immutableCopy();
        copy.selenium = new ExtendedSelenium(copy.interceptionProxy.getCommandProcessorProxy());
        return copy;
    }
}