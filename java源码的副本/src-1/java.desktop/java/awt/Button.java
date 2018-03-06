/*
 * Copyright (c) 1995, 2015, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.awt;

import java.awt.peer.ButtonPeer;
import java.beans.BeanProperty;
import java.util.EventListener;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import javax.accessibility.*;

/**
 * This class creates a labeled button. The application can cause
 * some action to happen when the button is pushed. This image
 * depicts three views of a "{@code Quit}" button as it appears
 * under the Solaris operating system:
 * <p>
 * <img src="doc-files/Button-1.gif" alt="The following context describes the graphic"
 * style="float:center; margin: 7px 10px;">
 * <p>
 * The first view shows the button as it appears normally.
 * The second view shows the button
 * when it has input focus. Its outline is darkened to let the
 * user know that it is an active object. The third view shows the
 * button when the user clicks the mouse over the button, and thus
 * requests that an action be performed.
 * <p>
 * The gesture of clicking on a button with the mouse
 * is associated with one instance of {@code ActionEvent},
 * which is sent out when the mouse is both pressed and released
 * over the button. If an application is interested in knowing
 * when the button has been pressed but not released, as a separate
 * gesture, it can specialize {@code processMouseEvent},
 * or it can register itself as a listener for mouse events by
 * calling {@code addMouseListener}. Both of these methods are
 * defined by {@code Component}, the abstract superclass of
 * all components.
 * <p>
 * When a button is pressed and released, AWT sends an instance
 * of {@code ActionEvent} to the button, by calling
 * {@code processEvent} on the button. The button's
 * {@code processEvent} method receives all events
 * for the button; it passes an action event along by
 * calling its own {@code processActionEvent} method.
 * The latter method passes the action event on to any action
 * listeners that have registered an interest in action
 * events generated by this button.
 * <p>
 * If an application wants to perform some action based on
 * a button being pressed and released, it should implement
 * {@code ActionListener} and register the new listener
 * to receive events from this button, by calling the button's
 * {@code addActionListener} method. The application can
 * make use of the button's action command as a messaging protocol.
 *
 * @author      Sami Shaio
 * @see         java.awt.event.ActionEvent
 * @see         java.awt.event.ActionListener
 * @see         java.awt.Component#processMouseEvent
 * @see         java.awt.Component#addMouseListener
 * @since       1.0
 */
public class Button extends Component implements Accessible {

    /**
     * The button's label.  This value may be null.
     * @serial
     * @see #getLabel()
     * @see #setLabel(String)
     */
    String label;

    /**
     * The action to be performed once a button has been
     * pressed.  This value may be null.
     * @serial
     * @see #getActionCommand()
     * @see #setActionCommand(String)
     */
    String actionCommand;

    transient ActionListener actionListener;

    private static final String base = "button";
    private static int nameCounter = 0;

    /*
     * JDK 1.1 serialVersionUID
     */
    private static final long serialVersionUID = -8774683716313001058L;


    static {
        /* ensure that the necessary native libraries are loaded */
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    /**
     * Initialize JNI field and method IDs for fields that may be
     * accessed from C.
     */
    private static native void initIDs();

    /**
     * Constructs a button with an empty string for its label.
     *
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public Button() throws HeadlessException {
        this("");
    }

    /**
     * Constructs a button with the specified label.
     *
     * @param label  a string label for the button, or
     *               {@code null} for no label
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public Button(String label) throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        this.label = label;
    }

    /**
     * Construct a name for this component.  Called by getName() when the
     * name is null.
     */
    String constructComponentName() {
        synchronized (Button.class) {
            return base + nameCounter++;
        }
    }

    /**
     * Creates the peer of the button.  The button's peer allows the
     * application to change the look of the button without changing
     * its functionality.
     *
     * @see     java.awt.Component#getToolkit()
     */
    public void addNotify() {
        synchronized(getTreeLock()) {
            if (peer == null)
                peer = getComponentFactory().createButton(this);
            super.addNotify();
        }
    }

    /**
     * Gets the label of this button.
     *
     * @return    the button's label, or {@code null}
     *                if the button has no label.
     * @see       java.awt.Button#setLabel
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the button's label to be the specified string.
     *
     * @param     label   the new label, or {@code null}
     *                if the button has no label.
     * @see       java.awt.Button#getLabel
     */
    public void setLabel(String label) {
        boolean testvalid = false;

        synchronized (this) {
            if (label != this.label && (this.label == null ||
                                        !this.label.equals(label))) {
                this.label = label;
                ButtonPeer peer = (ButtonPeer)this.peer;
                if (peer != null) {
                    peer.setLabel(label);
                }
                testvalid = true;
            }
        }

        // This could change the preferred size of the Component.
        if (testvalid) {
            invalidateIfValid();
        }
    }

    /**
     * Sets the command name for the action event fired
     * by this button. By default this action command is
     * set to match the label of the button.
     *
     * @param     command  a string used to set the button's
     *                  action command.
     *            If the string is {@code null} then the action command
     *            is set to match the label of the button.
     * @see       java.awt.event.ActionEvent
     * @since     1.1
     */
    public void setActionCommand(String command) {
        actionCommand = command;
    }

    /**
     * Returns the command name of the action event fired by this button.
     * If the command name is {@code null} (default) then this method
     * returns the label of the button.
     *
     * @return the action command name (or label) for this button
     */
    public String getActionCommand() {
        return (actionCommand == null? label : actionCommand);
    }

    /**
     * Adds the specified action listener to receive action events from
     * this button. Action events occur when a user presses or releases
     * the mouse over this button.
     * If l is null, no exception is thrown and no action is performed.
     * <p>Refer to <a href="doc-files/AWTThreadIssues.html#ListenersThreads"
     * >AWT Threading Issues</a> for details on AWT's threading model.
     *
     * @param         l the action listener
     * @see           #removeActionListener
     * @see           #getActionListeners
     * @see           java.awt.event.ActionListener
     * @since         1.1
     */
    public synchronized void addActionListener(ActionListener l) {
        if (l == null) {
            return;
        }
        actionListener = AWTEventMulticaster.add(actionListener, l);
        newEventsOnly = true;
    }

    /**
     * Removes the specified action listener so that it no longer
     * receives action events from this button. Action events occur
     * when a user presses or releases the mouse over this button.
     * If l is null, no exception is thrown and no action is performed.
     * <p>Refer to <a href="doc-files/AWTThreadIssues.html#ListenersThreads"
     * >AWT Threading Issues</a> for details on AWT's threading model.
     *
     * @param           l     the action listener
     * @see             #addActionListener
     * @see             #getActionListeners
     * @see             java.awt.event.ActionListener
     * @since           1.1
     */
    public synchronized void removeActionListener(ActionListener l) {
        if (l == null) {
            return;
        }
        actionListener = AWTEventMulticaster.remove(actionListener, l);
    }

    /**
     * Returns an array of all the action listeners
     * registered on this button.
     *
     * @return all of this button's {@code ActionListener}s
     *         or an empty array if no action
     *         listeners are currently registered
     *
     * @see             #addActionListener
     * @see             #removeActionListener
     * @see             java.awt.event.ActionListener
     * @since 1.4
     */
    public synchronized ActionListener[] getActionListeners() {
        return getListeners(ActionListener.class);
    }

    /**
     * Returns an array of all the objects currently registered
     * as <code><em>Foo</em>Listener</code>s
     * upon this {@code Button}.
     * <code><em>Foo</em>Listener</code>s are registered using the
     * <code>add<em>Foo</em>Listener</code> method.
     *
     * <p>
     * You can specify the {@code listenerType} argument
     * with a class literal, such as
     * <code><em>Foo</em>Listener.class</code>.
     * For example, you can query a
     * {@code Button b}
     * for its action listeners with the following code:
     *
     * <pre>ActionListener[] als = (ActionListener[])(b.getListeners(ActionListener.class));</pre>
     *
     * If no such listeners exist, this method returns an empty array.
     *
     * @param listenerType the type of listeners requested; this parameter
     *          should specify an interface that descends from
     *          {@code java.util.EventListener}
     * @return an array of all objects registered as
     *          <code><em>Foo</em>Listener</code>s on this button,
     *          or an empty array if no such
     *          listeners have been added
     * @exception ClassCastException if {@code listenerType}
     *          doesn't specify a class or interface that implements
     *          {@code java.util.EventListener}
     *
     * @see #getActionListeners
     * @since 1.3
     */
    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        EventListener l = null;
        if  (listenerType == ActionListener.class) {
            l = actionListener;
        } else {
            return super.getListeners(listenerType);
        }
        return AWTEventMulticaster.getListeners(l, listenerType);
    }

    // REMIND: remove when filtering is done at lower level
    boolean eventEnabled(AWTEvent e) {
        if (e.id == ActionEvent.ACTION_PERFORMED) {
            if ((eventMask & AWTEvent.ACTION_EVENT_MASK) != 0 ||
                actionListener != null) {
                return true;
            }
            return false;
        }
        return super.eventEnabled(e);
    }

    /**
     * Processes events on this button. If an event is
     * an instance of {@code ActionEvent}, this method invokes
     * the {@code processActionEvent} method. Otherwise,
     * it invokes {@code processEvent} on the superclass.
     * <p>Note that if the event parameter is {@code null}
     * the behavior is unspecified and may result in an
     * exception.
     *
     * @param        e the event
     * @see          java.awt.event.ActionEvent
     * @see          java.awt.Button#processActionEvent
     * @since        1.1
     */
    protected void processEvent(AWTEvent e) {
        if (e instanceof ActionEvent) {
            processActionEvent((ActionEvent)e);
            return;
        }
        super.processEvent(e);
    }

    /**
     * Processes action events occurring on this button
     * by dispatching them to any registered
     * {@code ActionListener} objects.
     * <p>
     * This method is not called unless action events are
     * enabled for this button. Action events are enabled
     * when one of the following occurs:
     * <ul>
     * <li>An {@code ActionListener} object is registered
     * via {@code addActionListener}.
     * <li>Action events are enabled via {@code enableEvents}.
     * </ul>
     * <p>Note that if the event parameter is {@code null}
     * the behavior is unspecified and may result in an
     * exception.
     *
     * @param       e the action event
     * @see         java.awt.event.ActionListener
     * @see         java.awt.Button#addActionListener
     * @see         java.awt.Component#enableEvents
     * @since       1.1
     */
    protected void processActionEvent(ActionEvent e) {
        ActionListener listener = actionListener;
        if (listener != null) {
            listener.actionPerformed(e);
        }
    }

    /**
     * Returns a string representing the state of this {@code Button}.
     * This method is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not be
     * {@code null}.
     *
     * @return     the parameter string of this button
     */
    protected String paramString() {
        return super.paramString() + ",label=" + label;
    }


    /* Serialization support.
     */

    /*
     * Button Serial Data Version.
     * @serial
     */
    private int buttonSerializedDataVersion = 1;

    /**
     * Writes default serializable fields to stream.  Writes
     * a list of serializable {@code ActionListeners}
     * as optional data.  The non-serializable
     * {@code ActionListeners} are detected and
     * no attempt is made to serialize them.
     *
     * @serialData {@code null} terminated sequence of 0 or
     *   more pairs: the pair consists of a {@code String}
     *   and an {@code Object}; the {@code String}
     *   indicates the type of object and is one of the following:
     *   {@code actionListenerK} indicating an
     *     {@code ActionListener} object
     *
     * @param s the {@code ObjectOutputStream} to write
     * @see AWTEventMulticaster#save(ObjectOutputStream, String, EventListener)
     * @see java.awt.Component#actionListenerK
     * @see #readObject(ObjectInputStream)
     */
    private void writeObject(ObjectOutputStream s)
      throws IOException
    {
      s.defaultWriteObject();

      AWTEventMulticaster.save(s, actionListenerK, actionListener);
      s.writeObject(null);
    }

    /**
     * Reads the {@code ObjectInputStream} and if
     * it isn't {@code null} adds a listener to
     * receive action events fired by the button.
     * Unrecognized keys or values will be ignored.
     *
     * @param s the {@code ObjectInputStream} to read
     * @exception HeadlessException if
     *   {@code GraphicsEnvironment.isHeadless} returns
     *   {@code true}
     * @serial
     * @see #removeActionListener(ActionListener)
     * @see #addActionListener(ActionListener)
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #writeObject(ObjectOutputStream)
     */
    private void readObject(ObjectInputStream s)
      throws ClassNotFoundException, IOException, HeadlessException
    {
      GraphicsEnvironment.checkHeadless();
      s.defaultReadObject();

      Object keyOrNull;
      while(null != (keyOrNull = s.readObject())) {
        String key = ((String)keyOrNull).intern();

        if (actionListenerK == key)
          addActionListener((ActionListener)(s.readObject()));

        else // skip value for unrecognized key
          s.readObject();
      }
    }


/////////////////
// Accessibility support
////////////////

    /**
     * Gets the {@code AccessibleContext} associated with
     * this {@code Button}. For buttons, the
     * {@code AccessibleContext} takes the form of an
     * {@code AccessibleAWTButton}.
     * A new {@code AccessibleAWTButton} instance is
     * created if necessary.
     *
     * @return an {@code AccessibleAWTButton} that serves as the
     *         {@code AccessibleContext} of this {@code Button}
     * @since 1.3
     */
    @BeanProperty(expert = true, description
            = "The AccessibleContext associated with this Button.")
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleAWTButton();
        }
        return accessibleContext;
    }

    /**
     * This class implements accessibility support for the
     * {@code Button} class.  It provides an implementation of the
     * Java Accessibility API appropriate to button user-interface elements.
     * @since 1.3
     */
    protected class AccessibleAWTButton extends AccessibleAWTComponent
        implements AccessibleAction, AccessibleValue
    {
        /*
         * JDK 1.3 serialVersionUID
         */
        private static final long serialVersionUID = -5932203980244017102L;

        /**
         * Get the accessible name of this object.
         *
         * @return the localized name of the object -- can be null if this
         * object does not have a name
         */
        public String getAccessibleName() {
            if (accessibleName != null) {
                return accessibleName;
            } else {
                if (getLabel() == null) {
                    return super.getAccessibleName();
                } else {
                    return getLabel();
                }
            }
        }

        /**
         * Get the AccessibleAction associated with this object.  In the
         * implementation of the Java Accessibility API for this class,
         * return this object, which is responsible for implementing the
         * AccessibleAction interface on behalf of itself.
         *
         * @return this object
         */
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        /**
         * Get the AccessibleValue associated with this object.  In the
         * implementation of the Java Accessibility API for this class,
         * return this object, which is responsible for implementing the
         * AccessibleValue interface on behalf of itself.
         *
         * @return this object
         */
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        /**
         * Returns the number of Actions available in this object.  The
         * default behavior of a button is to have one action - toggle
         * the button.
         *
         * @return 1, the number of Actions in this object
         */
        public int getAccessibleActionCount() {
            return 1;
        }

        /**
         * Return a description of the specified action of the object.
         *
         * @param i zero-based index of the actions
         */
        public String getAccessibleActionDescription(int i) {
            if (i == 0) {
                // [[[PENDING:  WDW -- need to provide a localized string]]]
                return "click";
            } else {
                return null;
            }
        }

        /**
         * Perform the specified Action on the object
         *
         * @param i zero-based index of actions
         * @return true if the action was performed; else false.
         */
        public boolean doAccessibleAction(int i) {
            if (i == 0) {
                // Simulate a button click
                Toolkit.getEventQueue().postEvent(
                        new ActionEvent(Button.this,
                                        ActionEvent.ACTION_PERFORMED,
                                        Button.this.getActionCommand()));
                return true;
            } else {
                return false;
            }
        }

        /**
         * Get the value of this object as a Number.
         *
         * @return An Integer of 0 if this isn't selected or an Integer of 1 if
         * this is selected.
         * @see javax.swing.AbstractButton#isSelected()
         */
        public Number getCurrentAccessibleValue() {
            return Integer.valueOf(0);
        }

        /**
         * Set the value of this object as a Number.
         *
         * @return True if the value was set.
         */
        public boolean setCurrentAccessibleValue(Number n) {
            return false;
        }

        /**
         * Get the minimum value of this object as a Number.
         *
         * @return An Integer of 0.
         */
        public Number getMinimumAccessibleValue() {
            return Integer.valueOf(0);
        }

        /**
         * Get the maximum value of this object as a Number.
         *
         * @return An Integer of 0.
         */
        public Number getMaximumAccessibleValue() {
            return Integer.valueOf(0);
        }

        /**
         * Get the role of this object.
         *
         * @return an instance of AccessibleRole describing the role of the
         * object
         * @see AccessibleRole
         */
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PUSH_BUTTON;
        }
    } // inner class AccessibleAWTButton

}
