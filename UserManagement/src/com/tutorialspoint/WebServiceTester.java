package com.tutorialspoint;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class WebServiceTester  {

   //private Client client;
   private String REST_SERVICE_URL = "http://localhost:8080/UserManagement/rest/UserService/users";
   private String REST_SERVICE_IMG_URL = "http://localhost:8080/UserManagement/rest/UserService/img";
   private static final String SUCCESS_RESULT="<result>success</result>";
   private static final String PASS = "pass";
   private static final String FAIL = "fail";

   public class LoadImageApp extends Component {
       
	    BufferedImage img;
	    
	    public void paint(Graphics g) {
	        g.drawImage(img, 0, 0, null);
	    }
	 
	    public LoadImageApp(File imageFile) {
	       try {
	           img = ImageIO.read(imageFile);
	       } catch (IOException e) {
	    	   e.printStackTrace();
	       }
	 
	    }
	 
	    public Dimension getPreferredSize() {
	        if (img == null) {
	             return new Dimension(100,100);
	        } else {
	           return new Dimension(img.getWidth(null), img.getHeight(null));
	       }
	    }
   }
   private void init(){
	   // since the code depends on different users for different levels
	   // make client per test
      //this.client = ClientBuilder.newClient();
   }

   public static void main(String[] args){
      WebServiceTester tester = new WebServiceTester();
      //initialize the tester
      tester.init();
      //test get all users Web Service Method
      tester.testGetAllUsers();
      //test get user Web Service Method 
      tester.testGetUser();
      //test update user Web Service Method
      tester.testUpdateUser();
      //test add user Web Service Method
      tester.testAddUser();
      //test delete user Web Service Method
      tester.testDeleteUser();
      tester.testImage();
   }
   private void testImage(){
	   Client client = ClientBuilder.newClient().register(new Authenticator("admin","password"));
      
       Builder builder = client
         .target(REST_SERVICE_IMG_URL)
         .request(MediaType.APPLICATION_OCTET_STREAM);
         
       File imageFile = builder.get(File.class);
       System.out.println(imageFile.getAbsolutePath());
      
      JFrame f = new JFrame("Image from server");
      f.addWindowListener(new WindowAdapter(){
          public void windowClosing(WindowEvent e) {
              System.exit(0);
          }
      });

      f.add(new LoadImageApp(imageFile));
      f.pack();
      f.setVisible(true);      
   }

   //Test: Get list of all users
   //Test: Check if list is not empty
   private void testGetAllUsers(){
	   Client client = ClientBuilder.newClient().register(new Authenticator("admin","password"));
      GenericType<List<User>> list = new GenericType<List<User>>() {};
      List<User> users = client
         .target(REST_SERVICE_URL)
         .request(MediaType.APPLICATION_XML)
         .get(list);
      String result = PASS;
      if(users.isEmpty()){
         result = FAIL;
      }
      System.out.println("Test case name: testGetAllUsers, Result: " + result );
   }
   //Test: Get User of id 1
   //Test: Check if user is same as sample user
   private void testGetUser(){
      User sampleUser = new User();
      sampleUser.setId(1);
      Client client = ClientBuilder.newClient().register(new Authenticator("will","password"));
      User user = client
         .target(REST_SERVICE_URL)
         .path("/{userid}")
         .resolveTemplate("userid", 1)
         .request(MediaType.APPLICATION_XML)
         .get(User.class);
      String result = FAIL;
      if(sampleUser != null && sampleUser.getId() == user.getId()){
         result = PASS;
      }
      System.out.println("Test case name: testGetUser, Result: " + result );
   }
   //Test: Update User of id 1
   //Test: Check if result is success XML.
   private void testUpdateUser(){
      Form form = new Form();
      form.param("id", "1");
      form.param("name", "suresh");
      form.param("profession", "clerk");
      Client client = ClientBuilder.newClient();
      String callResult = client
         .target(REST_SERVICE_URL)
         .request(MediaType.APPLICATION_XML)
         .put(Entity.entity(form,
            MediaType.APPLICATION_FORM_URLENCODED_TYPE),
            String.class);
      String result = PASS;
      if(!SUCCESS_RESULT.equals(callResult)){
         result = FAIL;
      }

      System.out.println("Test case name: testUpdateUser, Result: " + result );
   }
   //Test: Add User of id 2
   //Test: Check if result is success XML.
   private void testAddUser(){
      Form form = new Form();
      form.param("id", "2");
      form.param("name", "naresh");
      form.param("profession", "clerk");
      Client client = ClientBuilder.newClient();
      String callResult = client
         .target(REST_SERVICE_URL)
         .request(MediaType.APPLICATION_XML)
         .post(Entity.entity(form,
            MediaType.APPLICATION_FORM_URLENCODED_TYPE),
            String.class);
   
      String result = PASS;
      if(!SUCCESS_RESULT.equals(callResult)){
         result = FAIL;
      }

      System.out.println("Test case name: testAddUser, Result: " + result );
   }
   //Test: Delete User of id 2
   //Test: Check if result is success XML.
   private void testDeleteUser(){
	   Client client = ClientBuilder.newClient();
      String callResult = client
         .target(REST_SERVICE_URL)
         .path("/{userid}")
         .resolveTemplate("userid", 2)
         .request(MediaType.APPLICATION_XML)
         .delete(String.class);

      String result = PASS;
      if(!SUCCESS_RESULT.equals(callResult)){
         result = FAIL;
      }

      System.out.println("Test case name: testDeleteUser, Result: " + result );
   }
}