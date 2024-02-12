package com.example.walletwise;

public class IsCheck {
    private String str;


    public IsCheck(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String checkUserNameShor(){
        String msg = "";
        if(this.str.length()<3){
            msg+=" שם משתמש חייב להיות 3 תוים";

        }
        boolean ok =true;
        for(int i = 0 ; i <this.str.length();i++){
            if(!((this.str.charAt(i)>='a' && this.str.charAt(i)<='z')||(this.str.charAt(i)>='A' && this.str.charAt(i)<='א')||(this.str.charAt(i)>='A' && this.str.charAt(i)<='ת'))) {
                msg += "השם משתמש לא תקין";
                ok =false;

            }

            if (ok==false)
                i=this.str.length();
        }

        return msg;
    }
    public boolean checkUserNameShort(){
        if(this.str.length()<3){
            return false;
        }
        return true;
    }
    public boolean checkUserNameValid(){
        for(int i = 0 ; i <this.str.length();i++){
            if(!((this.str.charAt(i)>='a' && this.str.charAt(i)<='z')||(this.str.charAt(i)>='A' && this.str.charAt(i)<='Z'))) {
             return false;
            }
        }
        return true;
    }


    public boolean IsEqual(String str1){
        return this.str.equals(str1);
    }

    public boolean ValidWage(String num){
        double num1=Double.parseDouble(num);
        return num1>0;
    }

}
