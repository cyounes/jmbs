/*
 * Copyright (c) 2012. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright 
 * notice, this list of conditions and the following disclaimer in 
 * the documentation and/or other materials provided with the distribution.
 *
 * 3. The names of the authors may not be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
 * INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jmbs.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import jmbs.client.ClientRequests;
import jmbs.client.CurrentUser;
import jmbs.client.dataTreatment.HashPassword;
import jmbs.client.dataTreatment.ImageTreatment;
import jmbs.client.gui.images.ExtensionFileFilter;
import jmbs.client.gui.images.ImagePanel;
import jmbs.client.gui.images.ImagePreview;
import jmbs.client.gui.others.SayToUser;
import jmbs.common.User;

public class ProfilePanel extends JPanel {

	private static final long serialVersionUID = -398362580268810333L;
	private JTextField nameTextField, fnameTextField, emailTextField,
			profilePicturePathTextField;
	private JPasswordField passwordField, newpasswordField,
			confirmpasswordField;
	private JFileChooser fc;
	private ImagePanel profilePicturePanel;
	private JSeparator separator, separator_1, separator_2;
	private JLabel lblName, lblLastName, lblEmailAdress, lblConfirmPassword,
			lblPublicInformations, lblProfilePicture, lblOldPassword,
			lblChangePassword, lblNewPassword;
	private JButton btnBrowse, btnUpdate;

	/**
	 * Create the panel.
	 */
	public ProfilePanel(User currentUser) {
		initialize();
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fc == null) {
					fc = new JFileChooser(".");
					fc.setDialogType(JFileChooser.OPEN_DIALOG);
					ExtensionFileFilter imgFilter = new ExtensionFileFilter(
							"Images", new String[] { "jpg", "jpeg", "png",
									"gif" });
					fc.setFileFilter(imgFilter);
					fc.setAccessory(new ImagePreview(fc));
					fc.setFileHidingEnabled(true);
				}

				// Show it.
				int returnVal = fc.showOpenDialog(MainWindow.getFrame());

				// Process the results.
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					profilePicturePathTextField.setText(file.getPath());
				} else {
					profilePicturePathTextField.setText("");
				}

				// Reset the file chooser for the next time it's shown.
				fc.setSelectedFile(null);
			}
		});
		btnBrowse.setBounds(263, 480, 89, 29);

		setLayout(null);
		add(lblPublicInformations);
		add(lblChangePassword);
		add(lblOldPassword);
		add(newpasswordField);
		add(passwordField);
		add(lblNewPassword);
		add(lblProfilePicture);
		add(separator);
		add(lblName);
		add(lblLastName);
		add(lblEmailAdress);
		add(emailTextField);
		add(fnameTextField);
		add(nameTextField);
		add(profilePicturePanel);
		add(profilePicturePathTextField);
		add(btnBrowse);
		add(separator_1);
		add(lblConfirmPassword);
		add(confirmpasswordField);
		add(separator_2);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// This hashMap contains the values which we want to update
				HashMap<String, Boolean> valuesToEdit = new HashMap<String, Boolean>();
				// Put the values we want to edit in the HashMap with the false
				// as default
				if (passwordChanged()) {
					valuesToEdit.put("pass", false);
				}
				if (nameEdited()) {
					valuesToEdit.put("name", false);
				}
				if (fNameEdited()) {
					valuesToEdit.put("fname", false);
				}
				if (mailEdited()) {
					valuesToEdit.put("mail", false);
				}
				if (pictureEdited()) {
					valuesToEdit.put("pic", false);
				}

				// Coloring the textFileds
				if (valuesToEdit.containsKey("pass")) {
					if (newPassConfirmed()) {
						valuesToEdit.put("pass", true);
						newpasswordField.setBorder(BorderFactory
								.createLineBorder(Color.green));
						confirmpasswordField.setBorder(BorderFactory
								.createLineBorder(Color.green));
					} else {
						newpasswordField.setBorder(BorderFactory
								.createLineBorder(Color.red));
						confirmpasswordField.setBorder(BorderFactory
								.createLineBorder(Color.red));
					}
				}
				
				if(valuesToEdit.containsKey("pic")) {
					if (new File(profilePicturePathTextField.getText()).exists()) {
						valuesToEdit.put("pic", true);
						profilePicturePathTextField.setBorder(BorderFactory
								.createLineBorder(Color.green));
					}
					else {
						profilePicturePathTextField.setBorder(BorderFactory
								.createLineBorder(Color.red));
					}
				}

				if (valuesToEdit.containsKey("name")) {
					if (!nameTextField.getText().equals(CurrentUser.getName())
							&& nameTextField.getText().length() > 0) {
						valuesToEdit.put("name", true);
						nameTextField.setBorder(BorderFactory
								.createLineBorder(Color.green));
					} else {
						nameTextField.setBorder(BorderFactory
								.createLineBorder(Color.red));
					}
				}

				if (valuesToEdit.containsKey("fname")) {
					if (!fnameTextField.getText()
							.equals(CurrentUser.getFname())
							&& fnameTextField.getText().length() > 0) {
						valuesToEdit.put("fname", true);
						fnameTextField.setBorder(BorderFactory
								.createLineBorder(Color.green));
					} else {
						fnameTextField.setBorder(BorderFactory
								.createLineBorder(Color.red));
					}
				}

				if (valuesToEdit.containsKey("mail")) {
					if (!emailTextField.getText().equals(CurrentUser.getMail())
							&& Pattern
									.matches(
											"^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$",
											emailTextField.getText())
							&& passwordField.getPassword().length >= 4) {
						valuesToEdit.put("mail", true);
						emailTextField.setBorder(BorderFactory
								.createLineBorder(Color.green));
					} else {
						emailTextField.setBorder(BorderFactory
								.createLineBorder(Color.red));
						passwordField.setBorder(BorderFactory
								.createLineBorder(Color.red));
					}
				}

				HashMap<String, Boolean> editingResults = new HashMap<String, Boolean>();

				if (!valuesToEdit.containsValue(false)) {
					if (valuesToEdit.containsKey("name")) {
						editingResults.put("name", ClientRequests.changName(
								CurrentUser.getId(), nameTextField.getText()));
					}

					if (valuesToEdit.containsKey("fname")) {
						editingResults.put("fname", ClientRequests.changeFname(
								CurrentUser.getId(), fnameTextField.getText()));
					}

					if (valuesToEdit.containsKey("mail")) {
						editingResults.put("mail", ClientRequests.changeMail(
								CurrentUser.getId(), new HashPassword(
										passwordField.getPassword())
										.getHashed(), emailTextField.getText()));
					}

					if (valuesToEdit.containsKey("pass")) {
						editingResults.put("pass", ClientRequests
								.changePassword(
										CurrentUser.getId(),
										new HashPassword(passwordField
												.getPassword()).getHashed(),
										new HashPassword(newpasswordField
												.getPassword()).getHashed()));
					}
					if (valuesToEdit.containsKey("pic")) {
						ImageTreatment imt = new ImageTreatment();
						byte[] imInByteTmp = imt
								.preparedImage(profilePicturePathTextField
										.getText());
						if (imInByteTmp != null) {
							boolean pictureSetted = ClientRequests.setPicture(
									CurrentUser.getId(), imInByteTmp);
							CurrentUser.get().setPic(imInByteTmp);
							editingResults.put("pic", pictureSetted);
							if (pictureSetted)
								profilePicturePanel.setImage(
										profilePicturePathTextField.getText(), 70,
										70);
						} else {
							SayToUser
									.warning("Wrong type!",
											"Please choose a right image format , 'JPG' , 'JPEG'...");
						}
						
					}

					String updateSucess = "<b>Updates : </b><br />";
					String updateFailure = "<b>Failures : </b><br />";

					if (editingResults.containsKey("name")) {
						if (editingResults.get("name")) {
							updateSucess += "name<br />";
							CurrentUser.get().setName(nameTextField.getText());
						} else {
							updateFailure += "name<br />";
							nameTextField.setBorder(BorderFactory
									.createLineBorder(Color.red));
						}
					}
					if (editingResults.containsKey("fname")) {
						if (editingResults.get("fname")) {
							updateSucess += "forname<br />";
							CurrentUser.get()
									.setFname(fnameTextField.getText());
						} else {
							updateFailure += "forname<br />";
							fnameTextField.setBorder(BorderFactory
									.createLineBorder(Color.red));
						}
					}
					if (editingResults.containsKey("mail")) {
						if (editingResults.get("mail")) {
							updateSucess += "Email Adress<br />";
							CurrentUser.get().setMail(emailTextField.getText());
						} else {
							updateFailure += "Email Adress<br />";
							emailTextField.setBorder(BorderFactory
									.createLineBorder(Color.red));
							passwordField.setBorder(BorderFactory
									.createLineBorder(Color.red));
						}
					}
					if (editingResults.containsKey("pass")) {
						if (editingResults.get("pass")) {
							updateSucess += "Password<br />";
						} else {
							updateFailure += "Password<br />";
							passwordField.setBorder(BorderFactory
									.createLineBorder(Color.red));
							newpasswordField.setBorder(BorderFactory
									.createLineBorder(Color.red));
							confirmpasswordField.setBorder(BorderFactory
									.createLineBorder(Color.red));
						}
					}
					
					if (editingResults.containsKey("pic")) {
						if (editingResults.get("pic")) {
							updateSucess += "Profile Picture<br />";
						} else {
							updateFailure += "Profile Picture<br />";
							profilePicturePathTextField.setBorder(BorderFactory
									.createLineBorder(Color.red));
						}
					}
					// If editing successed of some fields
					if (!updateSucess.equals("<b>Updates : </b><br />")) {
						// If All right
						if (updateFailure.equals("<b>Failures : </b><br />")) {
							SayToUser.success("Update Successed", updateSucess);
						} else {
							SayToUser.warning("Update Warning", updateSucess
									+ updateFailure);
						}
					} else if (!updateFailure
							.equals("<b>Failures : </b><br />")) {
						SayToUser.error("Update Failure", updateFailure);
					} else {
						SayToUser.warning("", "No thing to update :)");
					}

					if (!mailEdited() && !passwordChanged()) {
						passwordField.setBorder(BorderFactory
								.createLineBorder(Color.black));
					}
				}

			}
		});
		btnUpdate.setBounds(235, 519, 117, 29);
		add(btnUpdate);

		JLabel lblAboutYou = new JLabel("About You:");
		lblAboutYou.setBounds(9, 154, 89, 16);
		add(lblAboutYou);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(9, 171, 330, 96);
		add(scrollPane);

		JTextArea aboutTextArea = new JTextArea();
		aboutTextArea.setLineWrap(true);
		aboutTextArea.setText("A new user of JMBS!");
		scrollPane.setViewportView(aboutTextArea);
	}

	public void resetAll(User currentUser) {
		nameTextField.setText(currentUser.getName());
		fnameTextField.setText(currentUser.getFname());
		emailTextField.setText(currentUser.getMail());
	}

	private boolean passwordChanged() {
		boolean retVal = false;
		if (newpasswordField.getPassword().length > 0
				|| confirmpasswordField.getPassword().length > 0) {
			retVal = true;
		} else {
			newpasswordField.setBorder(BorderFactory
					.createLineBorder(Color.black));
			confirmpasswordField.setBorder(BorderFactory
					.createLineBorder(Color.black));
		}
		return retVal;
	}

	private boolean nameEdited() {
		boolean retVal = false;
		if (!nameTextField.getText().equals(CurrentUser.getName())) {
			retVal = true;
		} else {
			nameTextField
					.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		return retVal;
	}
	
	private boolean pictureEdited() {
		return (!profilePicturePathTextField.getText().equals(""));
	}

	private boolean fNameEdited() {
		boolean retVal = false;
		if (!fnameTextField.getText().equals(CurrentUser.getFname())) {
			retVal = true;
		} else {
			fnameTextField.setBorder(BorderFactory
					.createLineBorder(Color.black));
		}
		return retVal;
	}

	private boolean mailEdited() {
		boolean retVal = false;
		if (!emailTextField.getText().equals(CurrentUser.getMail())) {
			retVal = true;
		} else {
			emailTextField.setBorder(BorderFactory
					.createLineBorder(Color.black));
		}
		return retVal;
	}

	private boolean newPassConfirmed() {

		boolean passConfirmed = newpasswordField.getPassword().length == confirmpasswordField
				.getPassword().length;
		if (!passConfirmed) {
			return false;
		}
		passConfirmed = newpasswordField.getPassword().length >= 4;
		if (passConfirmed) {
			for (int i = 0; i < newpasswordField.getPassword().length; i++) {
				if (newpasswordField.getPassword()[i] != confirmpasswordField
						.getPassword()[i]) {
					passConfirmed = false;
					break;
				}
			}
		}
		return passConfirmed;
	}

	private void initialize() {
		lblName = new JLabel("Name:");
		lblName.setBounds(90, 46, 40, 16);
		lblLastName = new JLabel("Last Name:");
		lblLastName.setBounds(60, 86, 70, 16);
		lblEmailAdress = new JLabel("Email Adress:");
		lblEmailAdress.setBounds(45, 126, 85, 16);
		separator = new JSeparator();
		separator.setBounds(36, 267, 267, 12);
		lblPublicInformations = new JLabel("Public Informations");
		lblPublicInformations.setBounds(84, 8, 162, 20);
		lblPublicInformations.setHorizontalAlignment(SwingConstants.CENTER);
		lblPublicInformations.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblChangePassword = new JLabel("Change Password");
		lblChangePassword.setBounds(95, 277, 163, 20);
		lblChangePassword.setForeground(Color.RED);
		lblChangePassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblChangePassword.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblOldPassword = new JLabel("Old Password:");
		lblOldPassword.setBounds(58, 309, 89, 16);
		lblNewPassword = new JLabel("New Password:");
		lblNewPassword.setBounds(52, 349, 94, 16);
		lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setBounds(29, 395, 118, 16);
		nameTextField = new JTextField(CurrentUser.getName());
		nameTextField.setBorder(BorderFactory.createLineBorder(null));
		nameTextField.setBounds(142, 40, 180, 28);
		nameTextField.setColumns(10);
		fnameTextField = new JTextField(CurrentUser.getFname());
		fnameTextField.setBounds(142, 80, 180, 28);
		fnameTextField.setColumns(10);
		fnameTextField.setBorder(BorderFactory.createLineBorder(null));
		emailTextField = new JTextField(CurrentUser.getMail());
		emailTextField.setBounds(142, 120, 180, 28);
		emailTextField.setColumns(10);
		emailTextField.setBorder(BorderFactory.createLineBorder(null));
		passwordField = new JPasswordField();
		passwordField.setBounds(159, 303, 180, 28);
		passwordField.setBorder(BorderFactory.createLineBorder(null));
		newpasswordField = new JPasswordField();
		newpasswordField.setBounds(159, 343, 180, 28);
		newpasswordField.setBorder(BorderFactory.createLineBorder(null));
		confirmpasswordField = new JPasswordField();
		confirmpasswordField.setBounds(159, 383, 180, 28);
		confirmpasswordField.setBorder(BorderFactory.createLineBorder(null));
		profilePicturePanel = new ImagePanel(ImageTreatment.convert(CurrentUser
				.getPic()), 70, 70);
		profilePicturePanel.setBounds(9, 437, 70, 70);
		profilePicturePanel.setBackground(Color.GRAY);
		separator_1 = new JSeparator();
		separator_1.setBounds(6, 423, 346, 12);
		lblProfilePicture = new JLabel("Profile Picture");
		lblProfilePicture.setBounds(93, 447, 115, 20);
		lblProfilePicture.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		btnBrowse = new JButton("Browse");
		profilePicturePathTextField = new JTextField();
		profilePicturePathTextField.setBounds(87, 479, 159, 28);
		profilePicturePathTextField.setColumns(10);
		profilePicturePathTextField.setEditable(false);
		separator_2 = new JSeparator();
		separator_2.setBounds(6, 511, 343, 12);
	}
}
