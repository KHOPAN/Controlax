package com.khopan.controlax.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.Controlax.NetworkEntry;
import com.khopan.controlax.action.action.ResponseAction;
import com.khopan.controlax.ui.chat.ChatWindow;
import com.khopan.controlax.ui.color.ColorPanel;
import com.khopan.controlax.ui.command.CommandPanel;
import com.khopan.controlax.ui.controlling.ControllingPanel;
import com.khopan.controlax.ui.execute.ExecutePanel;
import com.khopan.controlax.ui.image.ScreenshotPanel;
import com.khopan.controlax.ui.image.StreamRenderer;
import com.khopan.controlax.ui.message.MessagePanel;
import com.khopan.lazel.client.Client;
import com.khopan.lazel.config.BinaryConfig;
import com.khopan.lazel.config.BinaryConfigObject;

public class ControlWindow {
	public static final File USER_HOME = new File(System.getProperty("user.home"));
	public static final File CONTROLAX_FILE = new File(ControlWindow.USER_HOME, "controlax.bcfg");

	public final int panelBorder;
	public final int labelBorder;
	public final JTextPane statusPane;
	public final ChatWindow chatWindow;
	public final JFrame frame;
	public final JPanel firstSubPanel;
	public final StreamRenderer streamRenderer;
	public final ColorPanel colorPanel;
	public final CommandPanel commandPanel;
	public final MessagePanel messagePanel;
	public final ScreenshotPanel screenshotPanel;
	public final ControllingPanel controllingPanel;
	public final ExecutePanel executePanel;
	public final ControlMenuBar menuBar;

	private BinaryConfigObject config;

	public ControlWindow() {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		double totalSize = (((double) size.width) + ((double) size.height));
		this.panelBorder = (int) Math.round(totalSize * 0.00937207123d);
		this.labelBorder = (int) Math.round(totalSize * 0.00468603561d);
		this.statusPane = new JTextPane();
		this.chatWindow = new ChatWindow();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable Errors) {
				ControlWindow.this.printException(Errors);
			}
		});

		if(ControlWindow.CONTROLAX_FILE.exists()) {
			this.config = (BinaryConfigObject) BinaryConfig.readFile(ControlWindow.CONTROLAX_FILE);
		} else {
			if(!ControlWindow.USER_HOME.exists()) {
				if(!ControlWindow.USER_HOME.mkdirs()) {
					this.printException(new InternalError("Cannot create a directory for storing cache file"));
				}
			}

			this.config = new BinaryConfigObject();
			BinaryConfig.writeFile(this.config, ControlWindow.CONTROLAX_FILE);
		}

		this.frame = new JFrame();
		this.frame.setTitle("Control Panel");
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.setBorder(new EmptyBorder(this.panelBorder, this.panelBorder, this.panelBorder, this.panelBorder));
		this.firstSubPanel = new JPanel();
		this.firstSubPanel.setLayout(new GridLayout(2, 1));
		JPanel ipListStatusPanel = new JPanel();
		ipListStatusPanel.setLayout(new GridLayout(1, 2));
		JPanel ipListPanel = new JPanel();
		ipListPanel.setBorder(new TitledBorder("IP List"));
		ipListPanel.setLayout(new BorderLayout());
		JTable table = new JTable() {
			private static final long serialVersionUID = -6580976632339751762L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};

		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new Object[] {"Host Address", "Custom Name"});
		List<Client> clientList = new ArrayList<>();

		new Thread(() -> {
			List<NetworkEntry> list = Controlax.getLANIPAddress();

			for(int i = 0; i < list.size(); i++) {
				NetworkEntry entry = list.get(i);

				new Thread(() -> {
					Client client = new Client();
					client.port().set(2553);
					client.host().set(entry.hostAddress);
					client.connectionListener().set(() -> {
						model.addRow(new Object[] {entry.hostAddress, this.getName(entry.hostAddress)});
						clientList.add(client);
					});

					client.start();
				}).start();
			}
		}).start();

		model.addTableModelListener(Event -> {
			int index = Event.getFirstRow();
			this.putName((String) model.getValueAt(index, 0), (String) model.getValueAt(index, 1));
		});

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(model);
		table.setFocusable(false);
		table.getSelectionModel().addListSelectionListener(Event -> {
			int index = Event.getFirstIndex();

			for(int i = 0; i < clientList.size(); i++) {
				Client client = clientList.get(i);

				if(i == index) {
					Controlax.INSTANCE.client = client;
					client.packetListener().set(packet -> {
						Controlax.INSTANCE.processPacket(packet);
					});
				} else {
					client.packetListener().set(packet -> {});
				}
			}
		});

		table.putClientProperty("terminateEditOnFocusLost", true);
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(table);
		ipListPanel.add(pane);
		ipListStatusPanel.add(ipListPanel);
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new TitledBorder("Output"));
		statusPanel.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(this.statusPane);
		statusPanel.add(scrollPane, BorderLayout.CENTER);
		ipListStatusPanel.add(statusPanel);
		this.firstSubPanel.add(ipListStatusPanel);
		this.streamRenderer = new StreamRenderer();
		this.firstSubPanel.add(this.streamRenderer);
		panel.add(this.firstSubPanel);
		JPanel secondSubPanel = new JPanel();
		secondSubPanel.setLayout(new GridLayout(3, 1));
		JPanel colorCommandPanel = new JPanel();
		colorCommandPanel.setLayout(new GridLayout(1, 2));
		this.colorPanel = new ColorPanel(this.labelBorder);
		colorCommandPanel.add(this.colorPanel);
		this.commandPanel = new CommandPanel(this.labelBorder);
		colorCommandPanel.add(this.commandPanel);
		secondSubPanel.add(colorCommandPanel);
		JPanel messageScreenshotPanel = new JPanel();
		messageScreenshotPanel.setLayout(new GridLayout(1, 2));
		this.messagePanel = new MessagePanel(this.labelBorder);
		messageScreenshotPanel.add(this.messagePanel);
		this.screenshotPanel = new ScreenshotPanel();
		messageScreenshotPanel.add(this.screenshotPanel);
		secondSubPanel.add(messageScreenshotPanel);
		JPanel controllingExecutePanel = new JPanel();
		controllingExecutePanel.setLayout(new GridLayout(1, 2));
		this.controllingPanel = new ControllingPanel();
		controllingExecutePanel.add(this.controllingPanel);
		this.executePanel = new ExecutePanel(this.labelBorder);
		controllingExecutePanel.add(this.executePanel);
		secondSubPanel.add(controllingExecutePanel);
		panel.add(secondSubPanel);
		this.frame.add(panel, BorderLayout.CENTER);
		this.menuBar = new ControlMenuBar();
		this.frame.setJMenuBar(this.menuBar);
		this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.frame.setVisible(true);
	}

	private void printException(Throwable Errors) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		Errors.printStackTrace(printWriter);
		this.logError(stringWriter.toString());
	}

	public void logNormal(String message) {
		if(this.menuBar.normalToggle.isSelected()) {
			this.append(message);
		}
	}

	public void logError(String message) {
		if(this.menuBar.errorToggle.isSelected()) {			
			this.append(message);
		}
	}

	public void logDebug(String message) {
		if(this.menuBar.debugToggle.isSelected()) {
			this.append(message);
		}
	}

	private void append(String message) {
		this.statusPane.setText(this.statusPane.getText() + message + "\n");
	}

	private String getName(String address) {
		if(this.config.map().containsKey(address)) {
			return this.config.getString(address);
		}

		return "";
	}

	private void putName(String address, String name) {
		this.config.putString(address, name);
		BinaryConfig.writeFile(this.config, ControlWindow.CONTROLAX_FILE);
	}

	public void response(ResponseAction action) {
		this.logNormal(action.getResponse());
	}
}
