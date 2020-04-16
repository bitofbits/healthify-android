package com.example.healthify;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class JavaMailAPI extends AsyncTask<Void,Void,Void>{
    private static final String SENDER_EMAIL = "healthifyservices@gmail.com";
    private static final String SENDER_PASSWORD = "healthify123";
    private Context context;
    private Session session;
    private String recepient_email;
    private String subject;
    private String message;
    private ProgressDialog progressDialog;
    public JavaMailAPI(Context context, String recepient_email, String subject, String message){
        this.context = context;
        this.recepient_email = recepient_email;
        this.subject = subject;
        this.message = message;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Not doing anything for now. Add pre-execute tasks below, if any.
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        Toast.makeText(context,"Order Confirmed. Please Check your Mail",Toast.LENGTH_LONG).show();
    }
    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(SENDER_EMAIL, "Healthify Support"));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(recepient_email));
            mm.setSubject(subject);
            //mm.setText(message);
            mm.setContent(message, "text/html; charset=utf-8");
            Transport.send(mm);
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
