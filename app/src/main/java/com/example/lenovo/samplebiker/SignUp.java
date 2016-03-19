package com.example.lenovo.samplebiker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends Activity {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    private static final String PS = "\\w{4,12}";

    private static final String UN = "\\w{1,}";

    private static final String PN = "\\w{10}";


    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }

    public void onSignUpClick(View v)
    {
        if(v.getId()== R.id.Bsignupbutton)
        {
            EditText name = (EditText)findViewById(R.id.TFname);
            EditText email = (EditText)findViewById(R.id.TFemail);
            EditText uname = (EditText)findViewById(R.id.TFuname);
            EditText pass1 = (EditText)findViewById(R.id.TFpass1);
            EditText pass2 = (EditText)findViewById(R.id.TFpass2);
            EditText phnno = (EditText)findViewById(R.id.phnno);

            String namestr = name.getText().toString();
            String emailstr = email.getText().toString();
            String unamestr = uname.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();
            long phnnoln = Long.parseLong(phnno.getText().toString());

            if(!pass1str.equals(pass2str))
            {
                //popup msg
                Toast pass = Toast.makeText(SignUp.this , "Passwords don't match!" , Toast.LENGTH_SHORT);
                pass.show();
            }

            else if(!emailstr.matches(EMAIL_PATTERN))
            {
                Toast.makeText(SignUp.this , "Invalid Email ID" , Toast.LENGTH_SHORT).show();
            }

            else if(!pass1str.matches(PS))
            {
                Toast.makeText(SignUp.this , "Password should 4-12Character" , Toast.LENGTH_SHORT).show();
            }
            else if(!unamestr.matches(UN))
            {
                Toast.makeText(SignUp.this , "Please Fill the Username" , Toast.LENGTH_SHORT).show();
            }
            else if(!namestr.matches(UN))
            {
                Toast.makeText(SignUp.this , "Please Fill the Name" , Toast.LENGTH_SHORT).show();
            }
            else if(!phnno.getText().toString().matches(PN))
            {
                Toast.makeText(SignUp.this , "Invalid Phone number" , Toast.LENGTH_SHORT).show();
            }



            else
            {
                String mail = helper.searchEmail(emailstr);
                String unam = helper.searchUnam(unamestr);

                if (emailstr.equals(mail))
                {

                    Toast.makeText(SignUp.this , "Email id already exists!" , Toast.LENGTH_SHORT).show();

                }
                else if (unamestr.equals(unam))
                {

                    Toast.makeText(SignUp.this , "Username already exists!" , Toast.LENGTH_SHORT).show();

                }

                else {
                    //insert the detailes in database

                    User c = new User();
                    c.setName(namestr);
                    c.setEmail(emailstr);
                    c.setUname(unamestr);
                    c.setPass(pass1str);
                    c.setphnno(phnnoln);

                    helper.insertContact(c);
                    finish();

                }
            }

        }

    }

}
