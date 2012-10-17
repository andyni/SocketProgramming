import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ClientGUI extends JFrame{

	private Client client;
	
	// Login
	private JPanel panel1;
	private JTextField textField1;
	private JLabel label1;
	private JButton button1;
	private JTextArea textArea1 = new JTextArea();
	
	// Chat Window
	private JPanel panel2;
	private JList list1;
	private JLabel label2;
	private JTextField textField2;
	private JButton button2;
	private JButton button3;
	private JScrollPane pane1;
	
	private String[] users = new String[0];
	
	public ClientGUI(){
		initializeLogin();
		client = new Client(this);
		client.run();
	}
	
    private void initializeLogin() {
    	panel1 = new JPanel();
    	panel1.setLayout(new FlowLayout());
    	getContentPane().add(panel1);
    	
    	label1 = new JLabel("Username: ");
    	textField1 = new JTextField(20);
    	button1 = new JButton("Log In");
    	
    	button1.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) { 
    			client.registerUsername(textField1.getText());
    			initializeChatWindow();
    		}
    	});
    	
    	panel1.add(label1);
    	panel1.add(textField1);
    	panel1.add(button1);

    	pack();
    	setTitle("ECE356 Chat Client");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

    public void initializeChatWindow(){
    	getContentPane().remove(panel1);
    	panel2 = new JPanel();
    	panel2.setLayout(new BorderLayout());
    	getContentPane().add(panel2);
    	
    	pane1 = new JScrollPane();
        pane1.setPreferredSize(new Dimension(150, 300));
        panel2.add(pane1, BorderLayout.EAST);
        
        textArea1.setPreferredSize(new Dimension(50,20));
        textArea1.setEditable(false);
        JScrollPane pane2 = new JScrollPane(textArea1);
    	panel2.add(pane2, BorderLayout.CENTER);
    	
    	list1 = new JList(users);
    	pane1.getViewport().add(list1);
    	button3 = new JButton("Who's online?");
    	button3.setPreferredSize(new Dimension(120, 25));
    	button3.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) { 
    			client.whoIsOnline();
    		}
    	});
    	
    	Panel bottomPanel = new Panel();
    	textField2 = new JTextField(30);    	
    	bottomPanel.add(textField2);
    	button2 = new JButton("Send");
    	button2.setPreferredSize(new Dimension(100, 25));
    	button2.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) { 
    			if(list1.getSelectedValue() == null){
    				display("Please select a user to chat with on the right hand side.\n");
    				return;
    			}
    			
    			String recipient = (String) list1.getSelectedValue();
    			if(!textField2.getText().equals("")){
    				client.message(recipient + " " + textField2.getText() + "\n");
    				textField2.setText("");
    			}
    		}
    	});
    	
    	bottomPanel.add(button2);
    	bottomPanel.add(button3);
    	panel2.add(bottomPanel, BorderLayout.SOUTH);
    	
    	pack();
    	setTitle("ECE356 Chat Client");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void display(String message){
    	textArea1.append(message);
    	if(message.contains("Users Online:")){
    		message = message.replace("Users Online:", "");
    		users = message.split("\\s");
	    	list1 = new JList(users);
	    	pane1.getViewport().add(list1);
    	}
    }
    
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ClientGUI client = new ClientGUI();
                client.setVisible(true);
            }
        });
    }
}
