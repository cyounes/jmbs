/*
 * JMBS: Java Micro Blogging System
 *
 * Copyright (C) 2012  
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmbs.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import jmbs.client.ClientRequests;
import jmbs.client.CurrentUser;
import jmbs.client.ServerConnection;
import jmbs.client.cache.CacheRequests;
import jmbs.client.dataTreatment.AutoRefresh;
import jmbs.client.dataTreatment.FramesConf;
import jmbs.client.gui.messages.MsgPanel;
import jmbs.client.gui.messages.NewMessagePanel;
import jmbs.client.gui.messages.TimeLinePanel;
import jmbs.client.gui.others.AboutFrame;
import jmbs.client.gui.others.Preferences;
import jmbs.client.gui.projects.PrjectTabbedPane;
import jmbs.client.gui.users.UsersMngmntPanel;
import jmbs.common.Message;

/**
 * The main window
 *
 * @author <a target="_blank" href="http://cyounes.com/">Younes CHEIKH</a>
 * @author Benjamin Babic
 */
public class MainWindow {

    private static JFrame frmJmbsClient = null;
    /**
     * TimeLine Panel
     */
    public static TimeLinePanel timelinepanel;
    private static ProfilePanel ppanel;
    private static AboutFrame about;
    private static ArrayList<Message> msgListTL;
    private UsersMngmntPanel usersMngmntPanel;
    private MainMenuBar menuBar;
    private JPanel projectsPanel;
    private PrjectTabbedPane prjctTabbedPanel;
    private Preferences prfrm;
    private JPanel mainPanel;
    private JButton btnTimeline;
    private JButton btnProjects;
    private JButton btnProfile;
    private JButton btnUsers;
    private JScrollPane tlscrollPane;
    private JPanel profpanel;
    private JButton btnLogout;
    private JToolBar toolBar;
    private JSeparator separator_1;
    private JButton btnRefresh;
    private NewMessagePanel newMsgPanel;
    private JPanel tlpan;
    private CacheRequests cache;

    /**
     *
     * @return preferences frame
     */
    public Preferences getPreferencesFrame() {
        return this.prfrm;
    }

    /**
     * if the main Frame is null , initialize it.
     */
    public MainWindow() {
        if (frmJmbsClient == null) {
            initialize();
        }
    }

    /**
     * 
     * @return the Main Frame
     */
    public static JFrame getFrame() {
        return frmJmbsClient;
    }

    /**
     * initialize the main Frame
     */
    public static void initFrame() {
        frmJmbsClient = null;
    }

    /**
     * Initialize the contents of the frame.
     *
     * @wbp.parser.entryPoint
     */
    private void initialize() {
        msgListTL = new ArrayList<Message>();
        frmJmbsClient = new JFrame();
        frmJmbsClient.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                frmJmbsClient.dispose();
                ClientRequests.close();
                System.exit(0);
            }
        });
        cache = new CacheRequests();
        ppanel = new ProfilePanel(CurrentUser.get());
        about = new AboutFrame();
        prfrm = new Preferences();
        usersMngmntPanel = new UsersMngmntPanel(this);
        final ButtonGroup sideBarBtns = new ButtonGroup();
        frmJmbsClient.setTitle("JMBS Client : " + CurrentUser.getFullName());
        frmJmbsClient.setSize(520, 640);
        frmJmbsClient.setMinimumSize(new Dimension(480, 600));
        frmJmbsClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FramesConf.centerThisFrame(frmJmbsClient);

        projectsPanel = new JPanel();
        prjctTabbedPanel = new PrjectTabbedPane(projectsPanel, this);

        profpanel = new JPanel();
        profpanel.setLayout(new BorderLayout(0, 0));

        JScrollPane profilescrollPane = new JScrollPane();
        profilescrollPane.setViewportBorder(UIManager.getBorder(
                "List.evenRowBackgroundPainter"));
        profilescrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        profilescrollPane.setViewportView(ppanel);

        profpanel.add(profilescrollPane);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.removeAll();
        mainPanel.updateUI();
        frmJmbsClient.getContentPane().setLayout(new BorderLayout(0, 0));
        frmJmbsClient.getContentPane().add(mainPanel);

        tlpan = new JPanel();
        mainPanel.add(tlpan, BorderLayout.CENTER);
        tlpan.setLayout(new BorderLayout(0, 0));
        timelinepanel = new TimeLinePanel();
        tlscrollPane = new JScrollPane();
        tlscrollPane.getVerticalScrollBar().addAdjustmentListener(
                new GetMessages());
        tlpan.add(tlscrollPane);
        tlscrollPane.getVerticalScrollBar().setUnitIncrement(30);
        tlscrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tlscrollPane.setViewportView(timelinepanel);

        newMsgPanel = new NewMessagePanel(timelinepanel);

        tlpan.add(newMsgPanel, BorderLayout.SOUTH);

        toolBar = new JToolBar();
        frmJmbsClient.getContentPane().add(toolBar, BorderLayout.WEST);
        toolBar.setBackground(Color.DARK_GRAY);
        toolBar.setRollover(true);
        toolBar.setOrientation(SwingConstants.VERTICAL);

        btnTimeline = new JButton("");
        btnTimeline.setToolTipText("TimeLine");
        toolBar.add(btnTimeline);
        btnTimeline.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btnTimeline.setIcon(new ImageIcon(getClass().getResource(
                        "/img/timeline.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnTimeline.setIcon(new ImageIcon(getClass().getResource(
                        "/img/timeline_off.png")));
            }
        });
        btnTimeline.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateMainPanel(1);
            }
        });
        btnTimeline.setBorderPainted(false);
        btnTimeline.setIcon(new ImageIcon(getClass().getResource(
                "/img/timeline_off.png")));
        btnTimeline.setSelectedIcon(new ImageIcon(getClass().getResource(
                "/img/timeline.png")));

        sideBarBtns.add(btnTimeline);
        btnTimeline.setSelected(true);
        btnProjects = new JButton("");
        toolBar.add(btnProjects);
        btnProjects.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btnProjects.setIcon(new ImageIcon(getClass().getResource(
                        "/img/projects.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnProjects.setIcon(new ImageIcon(getClass().getResource(
                        "/img/projects_off.png")));
            }
        });

        btnProjects.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateMainPanel(2);
            }
        });
        btnProjects.setToolTipText("Projects");
        btnProjects.setBorderPainted(false);
        btnProjects.setIcon(new ImageIcon(getClass().getResource(
                "/img/projects_off.png")));
        btnProjects.setSelectedIcon(new ImageIcon(getClass().getResource(
                "/img/projects.png")));
        sideBarBtns.add(btnProjects);
        btnProfile = new JButton("");
        toolBar.add(btnProfile);
        btnProfile.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btnProfile.setIcon(new ImageIcon(getClass().getResource(
                        "/img/profile_edit.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnProfile.setIcon(new ImageIcon(getClass().getResource(
                        "/img/profile_edit_off.png")));
            }
        });

        btnProfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateMainPanel(3);
            }
        });

        btnProfile.setToolTipText("Profile");
        btnProfile.setBorderPainted(false);
        btnProfile.setIcon(new ImageIcon(getClass().getResource(
                "/img/profile_edit_off.png")));
        btnProfile.setSelectedIcon(new ImageIcon(getClass().getResource(
                "/img/profile_edit.png")));
        sideBarBtns.add(btnProfile);

        btnUsers = new JButton("");
        toolBar.add(btnUsers);
        btnUsers.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btnUsers.setIcon(new ImageIcon(getClass().getResource(
                        "/img/users.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnUsers.setIcon(new ImageIcon(getClass().getResource(
                        "/img/users_off.png")));
            }
        });
        btnUsers.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // uFrame.setVisible(true);
                updateMainPanel(4);
            }
        });
        btnUsers.setIcon(new ImageIcon(getClass().getResource(
                "/img/users_off.png")));
        btnUsers.setSelectedIcon(new ImageIcon(getClass().getResource(
                "/img/users.png")));
        btnUsers.setToolTipText("Users");
        btnUsers.setBorderPainted(false);

        JSeparator separator = new JSeparator();
        toolBar.add(separator);

        final JButton btnPreferences = new JButton("");
        toolBar.add(btnPreferences);
        btnPreferences.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btnPreferences.setIcon(new ImageIcon(getClass().getResource(
                        "/img/pref.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnPreferences.setIcon(new ImageIcon(getClass().getResource(
                        "/img/pref_off.png")));
            }
        });
        btnPreferences.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                prfrm.setVisible(true);
            }
        });
        btnPreferences.setToolTipText("Preferences");
        btnPreferences.setIcon(new ImageIcon(getClass().getResource(
                "/img/pref_off.png")));
        btnPreferences.setBorderPainted(false);

        separator_1 = new JSeparator();
        toolBar.add(separator_1);

        btnRefresh = new JButton("");
        btnRefresh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                checkNewMessages(timelinepanel.getLastIdMsg());
            }
        });

        btnRefresh.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btnRefresh.setIcon(new ImageIcon(getClass().getResource(
                        "/img/refreshmsgs.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnRefresh.setIcon(new ImageIcon(getClass().getResource(
                        "/img/refreshmsgs_off.png")));
            }
        });
        btnRefresh.setToolTipText("Refresh");
        btnRefresh.setBorderPainted(false);
        btnRefresh.setIcon(new ImageIcon(getClass().getResource(
                "/img/refreshmsgs_off.png")));
        toolBar.add(btnRefresh);

        final JButton btnAdd = new JButton("");
        btnAdd.setToolTipText("Add New Message");
        toolBar.add(btnAdd);
        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateNewMsgPane();
            }
        });
        newMsgPanel.setVisible(false);
        btnAdd.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btnAdd.setIcon(new ImageIcon(getClass().getResource(
                        "/img/add.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnAdd.setIcon(new ImageIcon(getClass().getResource(
                        "/img/add_off.png")));
            }
        });
        btnAdd.setBorderPainted(false);
        btnAdd.setIcon(new ImageIcon(getClass().getResource("/img/add_off.png")));

        btnLogout = new JButton("");
        btnLogout.setToolTipText("Logout");
        toolBar.add(btnLogout);
        btnLogout.setIcon(new ImageIcon(getClass().getResource(
                "/img/logout_off.png")));
        btnLogout.setBorderPainted(false);
        btnLogout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MainMenuBar.disconnect();
            }
        });
        btnLogout.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setIcon(new ImageIcon(getClass().getResource(
                        "/img/logout.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setIcon(new ImageIcon(getClass().getResource(
                        "/img/logout_off.png")));
            }
        });

        AutoRefresh autoRefresh = new AutoRefresh(this);
        autoRefresh.timeLineRefresh(10);
    }

    /**
     * used to update the timelinepanel from the other windows as
     * NewMessageFrame
     *
     * @return the default timeline Panel
     */
    public TimeLinePanel getTLPanel() {
        return MainWindow.timelinepanel;
    }

    /**
     * set color theme for the main window 
     * @param name 
     */
    public void setColors(String name) {
        ColorStyle cs = new ColorStyle(name);
        ArrayList<Color> defaultColors = new ArrayList<Color>();
        defaultColors.add(frmJmbsClient.getBackground());
        defaultColors.add(frmJmbsClient.getContentPane().getBackground());
        defaultColors.add(frmJmbsClient.getForeground());
        defaultColors.add(frmJmbsClient.getContentPane().getForeground());
        cs.setDefaultColors(defaultColors);
        frmJmbsClient.setVisible(false);
        frmJmbsClient.setBackground(cs.getWindowBackground()); // color 0
        frmJmbsClient.setForeground(cs.getWindowBackground());
        frmJmbsClient.getContentPane().setBackground(cs.getWindowBackground()); // Color 0
        frmJmbsClient.getContentPane().setForeground(cs.getWindowBackground()); // Color
        timelinepanel.setBackground(cs.getTimeLineBackground()); // Color 1

        for (Component c : timelinepanel.getComponents()) {
            ((MsgPanel) c).setColors(name);
        }
        //tlpan.add(newMsgPanel, BorderLayout.SOUTH);
        frmJmbsClient.setVisible(true);
    }

    /**
     * 
     * @return panel contains text area allows the user to create new message
     */
    public NewMessagePanel getNmPanel() {
        return newMsgPanel;
    }

    /**
     * 
     * @return About frame
     */
    public AboutFrame getAbout() {
        return about;
    }

    /**
     * this list used to know which messages are visible on the timeline panel
     * @return arraylist
     */
    public ArrayList<Message> getMsgListTL() {
        return msgListTL;
    }

    /**
     * clear the message list
     */
    public void initMsgListTL() {
        msgListTL.clear();
    }

    /**
     * 
     * @return the main menu bar
     */
    public MainMenuBar getMenuBar() {
        return menuBar;
    }

    public void resetProfilePanel() {
        ppanel.resetAll(CurrentUser.get());
    }

    public void setMenuBar() {
        menuBar = new MainMenuBar(this);
        frmJmbsClient.setJMenuBar(menuBar);
    }

    public JPanel getProjectsPanel() {
        return projectsPanel;
    }

    public void checkCacheMsgs() {
        msgListTL = cache.getMessages();
        timelinepanel.putList(msgListTL);
        int max = 0;
        for (Message m : msgListTL) {
            if (m.getId() > max) {
                max = m.getId();
            }
        }
        timelinepanel.setLastIdMsg(max);
        System.out.println(timelinepanel.getLastIdMsg());
    }

    public void checkNewMessages(int idLastMsg) {
        ArrayList<Message> listTmp = new ArrayList<Message>();
        listTmp = ClientRequests.getLatestTL(CurrentUser.getId(), idLastMsg,
                ServerConnection.maxReceivedMsgs);
        for (Message m : listTmp) {
            boolean found = false;
            for (Message m2 : msgListTL) {
                if (m2.getId() == m.getId()) {
                    found = true;
                }
            }
            if (!found) {
                msgListTL.add(m);
                cache.addMessage(m);
            }
        }
        timelinepanel.putList(listTmp);
    }

    public void updateMainPanel(int sideBarItem) {
        mainPanel.removeAll();
        if (sideBarItem == 1) {
            mainPanel.add(tlpan, BorderLayout.CENTER);
        } else if (sideBarItem == 2) {
            mainPanel.add(prjctTabbedPanel, BorderLayout.CENTER);
        } else if (sideBarItem == 3) {
            mainPanel.add(profpanel, BorderLayout.CENTER);
        } else if (sideBarItem == 4) {
            mainPanel.add(usersMngmntPanel, BorderLayout.CENTER);
        }
        btnTimeline.setSelected(sideBarItem == 1);
        btnProjects.setSelected(sideBarItem == 2);
        btnProfile.setSelected(sideBarItem == 3);
        btnUsers.setSelected(sideBarItem == 4);
        mainPanel.updateUI();
    }

    private void showNewMsgPan() {
        newMsgPanel.setVisible(true);
    }

    private void hideNewMsgPan() {
        newMsgPanel.setVisible(false);
    }

    public void updateNewMsgPane() {
        if (newMsgPanel.isVisible()) {
            hideNewMsgPan();
        } else {
            showNewMsgPan();
        }
    }

    public class GetMessages implements AdjustmentListener {

        public void adjustmentValueChanged(AdjustmentEvent ae) {
            if (ae.getValue() + tlscrollPane.getVerticalScrollBar().getHeight()
                    == tlscrollPane.getVerticalScrollBar().getMaximum()) {
                // TODO: Replace the lastidmessage by firstidmessage 0 >
                ClientRequests.getOldestTL(CurrentUser.getId(), 0,
                        ServerConnection.maxReceivedMsgs);
            } else if (ae.getValue() == 0) {
                checkNewMessages(timelinepanel.getLastIdMsg());
            }

        }
    }
}
