/*
* Copyright 2011 BetaSteward_at_googlemail.com. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:
*
*    1. Redistributions of source code must retain the above copyright notice, this list of
*       conditions and the following disclaimer.
*
*    2. Redistributions in binary form must reproduce the above copyright notice, this list
*       of conditions and the following disclaimer in the documentation and/or other materials
*       provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
* FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* The views and conclusions contained in the software and documentation are those of the
* authors and should not be interpreted as representing official policies, either expressed
* or implied, of BetaSteward_at_googlemail.com.
*/

/*
 * ConsoleFrame.java
 *
 * Created on May 13, 2011, 2:39:10 PM
 */

package mage.server.console;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import mage.interfaces.MageClient;
import mage.interfaces.callback.ClientCallback;
import mage.remote.Connection;
import mage.remote.Session;
import mage.remote.SessionImpl;
import mage.utils.MageVersion;
import org.apache.log4j.Logger;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public class ConsoleFrame extends javax.swing.JFrame implements MageClient {

    private static final Logger logger = Logger.getLogger(ConsoleFrame.class);

    private static Session session;
    private ConnectDialog connectDialog;
    private static final Preferences prefs = Preferences.userNodeForPackage(ConsoleFrame.class);
    private static final MageVersion version = new MageVersion(1, 3, 0, MageVersion.MAGE_VERSION_INFO);
    
    private static final ScheduledExecutorService pingTaskExecutor = Executors.newSingleThreadScheduledExecutor();
    /**
     * @return the session
     */
    public static Session getSession() {
        return session;
    }

    public static Preferences getPreferences() {
        return prefs;
    }

    @Override
    public MageVersion getVersion() {
        return version;
    }

    /** Creates new form ConsoleFrame */
    public ConsoleFrame() {

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApp();
            }
        });

        initComponents();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            session = new SessionImpl(this);
            connectDialog = new ConnectDialog();
        } catch (Exception ex) {
            logger.fatal("", ex);
        }
        
        pingTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                session.ping();
            }
        }, 60, 60, TimeUnit.SECONDS);        
    }

   public boolean connect(Connection connection) {
        if (session.connect(connection)) {
            this.consolePanel1.start();
            return true;
        }
        return false;
    }

    public void setStatusText(String status) {
        this.lblStatus.setText(status);
    }

    public void enableButtons() {
        btnConnect.setEnabled(true);
        btnConnect.setText("Disconnect & Close");
        btnSendMessage.setEnabled(true);
    }

    public void disableButtons() {
        btnConnect.setEnabled(true);
        btnConnect.setText("Connect");
        btnSendMessage.setEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        btnConnect = new javax.swing.JButton();
        btnSendMessage = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        consolePanel1 = new mage.server.console.ConsolePanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnConnect.setText("Connect");
        btnConnect.setFocusable(false);
        btnConnect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConnect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });
        jToolBar1.add(btnConnect);

        btnSendMessage.setActionCommand("SendMessage");
        btnSendMessage.setEnabled(false);
        btnSendMessage.setFocusable(false);
        btnSendMessage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSendMessage.setLabel("Send Message");
        btnSendMessage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSendMessage);

        lblStatus.setText("Not Connected");
        jToolBar1.add(Box.createHorizontalGlue());
        jToolBar1.add(lblStatus);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
            .addComponent(consolePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(consolePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        if (session.isConnected()) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to disconnect?", "Confirm disconnect", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.consolePanel1.stop();
                session.disconnect(false);
            }
        } else {
            connectDialog.showDialog(this);
        }
    }//GEN-LAST:event_btnConnectActionPerformed

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
        String message = JOptionPane.showInputDialog(null, "Type message to send", "Broadcast message", JOptionPane.INFORMATION_MESSAGE);
        if (message != null) {
            session.sendBroadcastMessage(message);
        }
    }//GEN-LAST:event_btnSendMessageActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        logger.info("Starting MAGE server console version " + version);
        logger.info("Logging level: " + logger.getEffectiveLevel());

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConsoleFrame().setVisible(true);
                logger.info("Started MAGE server console");
            }
        });
    }

    private ConsoleFrame getFrame() {
        return this;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnSendMessage;
    private mage.server.console.ConsolePanel consolePanel1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables

    @Override
    public void connected(final String message) {
        if (SwingUtilities.isEventDispatchThread()) {
            setStatusText(message);
            enableButtons();            
        }
        else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setStatusText(message);
                    enableButtons();
                }
            });
        }
    }

    @Override
    public void disconnected(boolean errorCall) {
        if (SwingUtilities.isEventDispatchThread()) {
            consolePanel1.stop();
            setStatusText("Not connected");
            disableButtons();
        }
        else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    consolePanel1.stop();
                    setStatusText("Not connected");
                    disableButtons();
                }
            });
        }
    }

    @Override
    public void showMessage(final String message) {
        if (SwingUtilities.isEventDispatchThread()) {
            JOptionPane.showMessageDialog(this, message);
        }
        else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(getFrame(), message);
                }
            });
        }
    }

    @Override
    public void showError(final String message) {
        if (SwingUtilities.isEventDispatchThread()) {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(getFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    @Override
    public void processCallback(ClientCallback callback) {
    }

    public void exitApp() {
        if (session.isConnected()) {
            if (JOptionPane.showConfirmDialog(this, "You are currently connected.  Are you sure you want to disconnect?", "Confirm disconnect", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            session.disconnect(false);
        } else {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm exit", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
        }
        dispose();
        System.exit(0);
    }
}
