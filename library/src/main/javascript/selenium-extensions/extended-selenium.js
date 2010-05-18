/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
Selenium.prototype.getStyle = function(locator, property) {
	var element = this.browserbot.findElementOrNull(locator);
	
	if (element == null) {
			throw new SeleniumError("null property value");
	}
	
	var currentDocument = this.browserbot.getCurrentWindow().document;
	
	return jQuery(element, currentDocument).css(property);
}

/**
 * Aligns screen to top (resp. bottom) of element given by locator.
 * 
 * TODO should be reimplemented to use of JQuery.scrollTo
 * 
 * @param locator of element which should be screen aligned to
 * @param alignToTop should be top border of screen aligned to top border of element
 */
Selenium.prototype.scrollIntoView = function(locator, alignToTop) {
	var elem = this.browserbot.findElementOrNull(locator);
	
	if (elem == null) {
		throw new SeleniumError("null property value");
	}
	
	if (elem.scrollIntoView == undefined) {
		throw new SeleniumError("scrollIntoView isn't supported at this element");
	}
	
	elem.scrollIntoView(alignToTop);
}
 
/**
 * Simulates a user hovering a mouse over the specified element at specific
 * coordinates relative to element.
 * 
 * @param locator
 *            element's locator
 * @param coordString
 *            specifies the x,y position (i.e. - 10,20) of the mouse event
 *            relative to the element returned by the locator.
 */
Selenium.prototype.doMouseOverAt = function(locator, coordString) {
	var element = this.browserbot.findElement(locator);
	var clientXY = getClientXY(element, coordString)

	this.browserbot.triggerMouseEvent(element, 'mouseover', true, clientXY[0],
			clientXY[1]);
}

/**
 * Returns the count of elements for given jQuery selector
 * 
 * @param selector
 *            jQuery selector
 * @return count of elements matching given selector
 */
Selenium.prototype.getJQueryCount = function(selector) {
	var inDocument = this.browserbot.getDocument();
	var found = $(inDocument).find(selector);
	return found.length;
}

/**
 * Gets the text of an element. This works for any element that contains
 * text. This command uses either the textContent (Mozilla-like browsers) or
 * the innerText (IE-like browsers) of the element, which is the rendered
 * text shown to the user.
 * 
 * If no element with given locator is found, returns null.
 *
 * @param locator an element locator
 * @return string the text of the element or null if element's wasn't found
 */
Selenium.prototype.getTextOrNull = function(locator) {
	var element = this.browserbot.findElementOrNull(locator);
	if (element == null) {
		throw new SeleniumError("element is not found");
	}
	return getText(element).trim();
};

/**
 * Syntactic shortcut for accessing RichFacesSelenium on the page.
 * 
 * @return the RichFacesSelenium object defined in the current page.
 */
var getRFS = function() {
	return selenium.browserbot.getCurrentWindow().RichFacesSelenium;
}