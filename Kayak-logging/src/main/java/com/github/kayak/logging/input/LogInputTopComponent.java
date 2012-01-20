/**
 * 	This file is part of Kayak.
 *
 *	Kayak is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	Kayak is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU Lesser General Public License
 *	along with Kayak.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.github.kayak.logging.input;

import com.github.kayak.core.Bus;
import com.github.kayak.core.LogFile;
import com.github.kayak.core.SeekableLogFileReplay;
import com.github.kayak.core.TimeSource;
import com.github.kayak.ui.projects.Project;
import com.github.kayak.ui.projects.ProjectManager;
import java.awt.Color;
import java.awt.dnd.DropTarget;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;

/**
 *
 * @author Jan-Niklas Meier <dschanoeh@googlemail.com>
 */
@ConvertAsProperties(dtd = "-//com.github.kayak.logging.input//LogInput//EN",
autostore = false)
@TopComponent.Description(preferredID = "LogInputTopComponent",
iconBase="org/tango-project/tango-icon-theme/16x16/actions/go-previous.png",
persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
public final class LogInputTopComponent extends TopComponent implements BusDropTargetAdapter.BusDropReceiver {

    private static final Logger logger = Logger.getLogger(LogInputTopComponent.class.getCanonicalName());

    private LogFile logFile;
    private JLabel[] labels;
    private JTextField[] fields;
    private JButton[] buttons;
    private ArrayList<String> busses;
    private SeekableLogFileReplay replay;
    private TimeSource timeSource;
    private Thread positionUpdateThread;
    private boolean seeking=false;

    private boolean inSet;
    private boolean outSet;

    private Runnable positionUpdater = new Runnable() {

        @Override
        public void run() {
            ProgressHandle p = ProgressHandleFactory.createHandle("Creating log file index...");
            p.start();
            while(true) {
                long in = replay.getIn()/1000;
                long out = replay.getOut()/1000;
                long current = replay.getCurrentTime()/1000;

                if(!seeking)
                    jSlider1.setValue((int) current);

                jTextField1.setText(String.format("%.3f", in/1000f));
                jTextField2.setText(String.format("%.3f", out/1000f));
                jLabel2.setText(String.format("%.3f", current/1000f));

                if(replay.isIndexCreated()) {
                    jButton4.setEnabled(true);
                    jButton5.setEnabled(true);
                    jSlider1.setEnabled(true);
                    p.finish();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }
    };

    public LogInputTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(LogInputTopComponent.class, "CTL_LogInputTopComponent"));
        setToolTipText(NbBundle.getMessage(LogInputTopComponent.class, "HINT_LogInputTopComponent"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jPanel5 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jPanel1.border.title"))); // NOI18N
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel3.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        jPanel3.add(jButton1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel3.add(jButton2, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButton3, org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.1;
        jPanel3.add(jButton3, gridBagConstraints);

        jPanel1.add(jPanel3);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox2, org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jCheckBox2.text")); // NOI18N
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBox2);

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jLabel1.text")); // NOI18N
        jPanel6.add(jLabel1);

        jPanel4.add(jPanel6);

        jPanel1.add(jPanel4);

        jSlider1.setEnabled(false);
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jSlider1MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider1MouseReleased(evt);
            }
        });
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jPanel1.add(jSlider1);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jTextField1.setEditable(false);
        jTextField1.setText(org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jTextField1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        jPanel5.add(jTextField1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButton4, org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jButton4.text")); // NOI18N
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4, new java.awt.GridBagConstraints());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.2;
        jPanel5.add(jLabel2, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButton5, org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jButton5.text")); // NOI18N
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton5, new java.awt.GridBagConstraints());

        jTextField2.setEditable(false);
        jTextField2.setText(org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jTextField2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.1;
        jPanel5.add(jTextField2, gridBagConstraints);

        jPanel1.add(jPanel5);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(LogInputTopComponent.class, "LogInputTopComponent.jPanel2.border.title"))); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 338, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel7);

        jPanel2.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        timeSource.play();
        jPanel6.setBackground(Color.GREEN);
        jLabel1.setForeground(Color.BLACK);
        jLabel1.setText("PLAY");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        timeSource.pause();
        if(timeSource.getMode() == TimeSource.Mode.PAUSE) {
            jPanel6.setBackground(Color.YELLOW);
            jLabel1.setForeground(Color.BLACK);
            jLabel1.setText("PAUSE");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        timeSource.stop();
        jPanel6.setBackground(Color.RED);
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setText("STOPPED");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        replay.setInfiniteReplay(jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(inSet) {
            jButton4.setText("Set in");
            replay.setIn(0);
            inSet = false;
        } else {
            jButton4.setText("Restore in");
            replay.setIn(replay.getCurrentTime());
            inSet = true;
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if(outSet) {
            jButton5.setText("Set out");
            replay.setOut(replay.getLength());
            outSet = false;
        } else {
            jButton5.setText("Restore out");
            replay.setOut(replay.getCurrentTime());
            outSet = true;
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged

    }//GEN-LAST:event_jSlider1StateChanged

    private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseReleased
        JSlider source = (JSlider)evt.getSource();
        int newVal = (int) source.getValue();
        replay.seekTo(((long) newVal) * 1000);
        seeking = false;
    }//GEN-LAST:event_jSlider1MouseReleased

    private void jSlider1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseClicked
        seeking = true;
    }//GEN-LAST:event_jSlider1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentClosed() {
        TimeSource ts = new TimeSource();
        replay.setTimeSource(ts);
        ts.stop();

        positionUpdateThread.interrupt();
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");

        p.setProperty("filename", logFile.getFile().getAbsolutePath());


        for(String bus : busses) {
            Bus b = replay.getBus(bus);

            if(b != null) {
                p.setProperty(bus, b.getName());
            }
        }
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");

        String filename = p.getProperty("filename");

        File f = new File(filename);

        if(f.exists() && f.canRead()) {
            try {
                LogFile l = new LogFile(f);
                setLogFile(l);

                Project project = ProjectManager.getGlobalProjectManager().getOpenedProject();
                if(project != null) {

                    for(int i=0;i<busses.size();i++) {
                        String bus = busses.get(i);
                        String busName = p.getProperty(bus);
                        for(Bus projectBus : project.getBusses()) {
                            if(projectBus.getName().equals(busName)) {
                                receive(projectBus, i);
                            }
                        }

                    }
                }

            } catch (Exception ex) {
                logger.log(Level.WARNING, "Could not restore persisted log input", ex);
                close();
            }
        } else {
            close();
        }
    }

    public void setLogFile(LogFile file) {
        this.logFile = file;
        this.setName(NbBundle.getMessage(LogInputTopComponent.class, "CTL_LogInputTopComponent") + " - " + logFile.getDescription());
        this.setToolTipText(NbBundle.getMessage(LogInputTopComponent.class, "CTL_LogInputTopComponent") + " - " + logFile.getDescription());

        busses = new ArrayList<String>();
        busses.addAll(logFile.getBusses());
        java.awt.GridLayout layout = new java.awt.GridLayout();
        layout.setColumns(3);
        layout.setRows(busses.size());
        jPanel7.setLayout(layout);

        labels = new JLabel[busses.size()];
        fields = new JTextField[busses.size()];
        buttons = new JButton[busses.size()];

        for (int i = 0; i < busses.size(); i++) {
            labels[i] = new JLabel();
            fields[i] = new JTextField();
            buttons[i] = new JButton();

            buttons[i].setMaximumSize(buttons[i].getPreferredSize());

            String text = busses.get(i) + " (" + logFile.getAlias(busses.get(i)) + ") -->";
            labels[i].setText(text);
            fields[i].setText("< Drag bus here >");
            buttons[i].setText("Remove bus");
            buttons[i].setEnabled(false);

            buttons[i].addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    removeBus(evt);
                }
            });

            fields[i].setEditable(false);

            DropTarget dt = new DropTarget(fields[i], new BusDropTargetAdapter(this, i));
            fields[i].setDropTarget(dt);

        }

        for (int i = 0; i < busses.size(); i++) {
            jPanel7.add(labels[i]);
            jPanel7.add(fields[i]);
            jPanel7.add(buttons[i]);
        }

        if(!logFile.getCompressed()) {
            try {
                replay = new SeekableLogFileReplay(logFile);
            } catch(Exception ex) {
                close();
            }
        } else {
            close();
        }

        timeSource = new TimeSource();
        replay.setTimeSource(timeSource);

        jSlider1.setMinimum(0);
        jSlider1.setMaximum((int) (replay.getLength()/1000));

        positionUpdateThread = new Thread(positionUpdater);
        positionUpdateThread.setName("PositionUpdateThread");
        positionUpdateThread.start();
    }

    @Override
    public void receive(Bus b, int number) {
        fields[number].setText(b.toString());
        buttons[number].setEnabled(true);
        replay.setBus(busses.get(number), b);
    }

    private void removeBus(java.awt.event.ActionEvent evt) {
        for(int i=0;i<buttons.length;i++) {
            if(buttons[i].equals(evt.getSource())) {
                fields[i].setText("< Drag bus here >");
                buttons[i].setEnabled(false);
                replay.setBus(busses.get(i), null);
            }
        }
    }
}
